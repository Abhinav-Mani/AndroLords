package com.androlord.prayaas.Support;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.androlord.prayaas.DataClass.EbookDetails;
import com.androlord.prayaas.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicMarkableReference;


public class ViewPdf extends AppCompatActivity {
    private TextView textView;
    private TextToSpeech textToSpeech;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);







        Intent intent=getIntent();
        String path="";
        if(intent.hasExtra("Data"))
        {
            EbookDetails ebookDetails=new EbookDetails();
            ebookDetails=(EbookDetails) intent.getSerializableExtra("Data");
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ebookDetails.url));
            startActivity(browserIntent);

        }
        else
        {
            Log.d("ak47","EMpty Intent");
        }
    }
}
