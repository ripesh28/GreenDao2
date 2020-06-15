package com.example.greendao2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewNoteActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY =
            "com.example.android.greendao2.REPLY";
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        editText=findViewById(R.id.edit_note);
        final Button button=findViewById(R.id.button_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reply=new Intent();
                if(TextUtils.isEmpty(editText.getText())) {
                    setResult(RESULT_CANCELED, reply);
                }
                else{
                    String word=editText.getText().toString();
                    reply.putExtra(EXTRA_REPLY,word);
                    setResult(RESULT_OK,reply);
                }
                finish();
            }
        });
    }
}
