package com.example.greendao2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greendao2.database.Note;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<Note> noteList;
    private LayoutInflater inflater;
    Context context;
    private NoteClickListener clickListener;

    public  interface NoteClickListener{
        void onClick(int position);
    }
    public NoteAdapter(Context context, NoteClickListener clickListener) {
        inflater=LayoutInflater.from(context);
        this.context=context;
        this.clickListener=clickListener;
    }
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.recycle_list_item,parent,false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        if(noteList!=null) {
            Note current=noteList.get(position);
            Log.d("Word",current.getData());
            holder.textView.setText(current.getData());
        }
        else {
            holder.textView.setText("No Words :)");
        }
    }


    @Override
    public int getItemCount() {
        if (noteList != null)
            return noteList.size();
        else return 0;
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList=noteList;
    }

    public Note getNote(int position){
        return noteList.get(position);
    }
    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.textView);

            //Set onLongClick
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setMessage(R.string.alert_message)
                            .setTitle("Delete")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    clickListener.onClick(getAdapterPosition());
                                }
                            })
                            .setNegativeButton("Cancel", null);
                    builder.create().show();
                    return true;
                }
            });
        }
    }

}
