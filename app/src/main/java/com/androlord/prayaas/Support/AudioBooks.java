package com.androlord.prayaas.Support;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.androlord.prayaas.R;

import java.io.File;
import java.io.FileReader;

public class AudioBooks extends AppCompatActivity {
    RadioButton radioButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_books);
        radioButton=findViewById(R.id.addAudiobooks);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDF();
            }
        });
    }
    private void selectPDF() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivityForResult(intent, 1);

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String Fpath = data.getDataString();
        Toast.makeText(this,Fpath,Toast.LENGTH_LONG).show();
        //TODO handle your request here
        super.onActivityResult(requestCode, resultCode, data);
    }



}
