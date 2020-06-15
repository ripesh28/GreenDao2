package com.example.greendao2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.greendao2.database.DaoMaster;
import com.example.greendao2.database.DaoSession;
import com.example.greendao2.database.Note;
import com.example.greendao2.database.NoteDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.Query;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Serializable{

    private static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    private NoteDao noteDao;
    private Query<Note> notesQuery;
    private NoteAdapter adapter;


    private DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,NewNoteActivity.class);
                startActivityForResult(intent,NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NoteAdapter(this,noteClickListener);
        recyclerView.setAdapter(adapter);

        //get noteDao
        daoSession=((App)getApplication()).getDaoSession();
        daoSession.clear();
        noteDao = daoSession.getNoteDao();


        // query all notes, sorted a-z by their text
        notesQuery = noteDao.queryBuilder().orderAsc(NoteDao.Properties.Data).build();
        adapter.setNoteList(notesQuery.list());
    }

    private void updateNotes(List<Note> list) {
        adapter.setNoteList(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int req_code, int res_code, Intent intent) {

        super.onActivityResult(req_code, res_code, intent);
        if(req_code==NEW_WORD_ACTIVITY_REQUEST_CODE && res_code==RESULT_OK){
            Note note=new Note();
            String data=intent.getStringExtra(new NewNoteActivity().EXTRA_REPLY);
            note.setData(data);
            noteDao.insert(note);
            updateNotes(notesQuery.list());
        }
        else{
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    NoteAdapter.NoteClickListener noteClickListener= new NoteAdapter.NoteClickListener() {
        @Override
        public void onClick(int position) {
            Note note=adapter.getNote(position);
            Long id=note.getNoteId();

            noteDao.deleteByKey(id);
            Toast.makeText(
                    getApplicationContext(),
                    "Deleted "+note.getData(),
                    Toast.LENGTH_LONG).show();
            updateNotes(notesQuery.list());
        }
    };

}
