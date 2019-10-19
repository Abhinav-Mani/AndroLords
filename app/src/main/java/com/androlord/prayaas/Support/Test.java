package com.androlord.prayaas.Support;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androlord.prayaas.R;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class Test extends AppCompatActivity {
    private TextView textView;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        textView=findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());



        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override

            public void onInit(int status) {



                textToSpeech.setLanguage(Locale.US);
            }
        });
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void readButtonOnClick(View view){


        File file=new File("/sdcard/NOC.pdf");
        String stringParser;
        try {

            PdfReader pdfReader=new PdfReader(file.getPath());
            stringParser= PdfTextExtractor.getTextFromPage(pdfReader,1).trim();
            pdfReader.close();
            textView.setText(stringParser);
            textToSpeech.speak(stringParser,TextToSpeech.QUEUE_FLUSH,null,null);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    public void data(View  view)
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"SELECT PDF FILE"),1);

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String Fpath = data.getDataString();
        //TODO handle your request here
        Toast.makeText(this,Fpath,Toast.LENGTH_LONG).show();
        File file=new File(Fpath);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
