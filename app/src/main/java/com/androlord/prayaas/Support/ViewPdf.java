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
        WebView webView=findViewById(R.id.pdfViwerWebView);
        webView.getSettings().setJavaScriptEnabled(true);
        Intent intent=getIntent();
        if(intent.hasExtra("URI"))
        {
            Toast.makeText(ViewPdf.this,intent.getStringExtra("URI"),Toast.LENGTH_LONG).show();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(intent.getStringExtra("URI")));
            startActivity(browserIntent);


        }
        else
        {
            Log.d("ak47","EMpty Intent");
        }
    }
}
