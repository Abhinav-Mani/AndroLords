package com.androlord.prayaas.Support;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.androlord.prayaas.R;


public class ViewPdf extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);

        Intent intent=getIntent();
        if(intent.hasExtra("URI"))
        {
            Toast.makeText(ViewPdf.this,intent.getStringExtra("URI"),Toast.LENGTH_LONG).show();
            WebView webview = (WebView) findViewById(R.id.pdfView);
            webview.getSettings().setJavaScriptEnabled(true);
            String pdf = intent.getStringExtra("URI");
            webview.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);
        }
        else
        {
            Log.d("ak47","EMpty Intent");
        }
    }
}
