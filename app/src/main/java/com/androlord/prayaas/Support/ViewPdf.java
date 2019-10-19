package com.androlord.prayaas.Support;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
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

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicMarkableReference;


public class ViewPdf extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);



        Intent intent=getIntent();
        String path="";
        if(intent.hasExtra("Data"))
        {
            EbookDetails ebookDetails=new EbookDetails();
            ebookDetails=(EbookDetails)intent.getSerializableExtra("Data");
            Log.d("ak47", "onCreate: "+ebookDetails.url);
            FirebaseStorage storage=FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            path="uploads/"+ebookDetails.url+".pdf";
            StorageReference islandRef = storageRef.child(path);

            final long ONE_MEGABYTE = 30*1024*1024;
            final String finalPath = path;
            islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Toast.makeText(ViewPdf.this,"hurray",Toast.LENGTH_LONG).show();
                    // Data for "images/island.jpg" is returns, use this as needed
                    
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Toast.makeText(ViewPdf.this,"m"+exception+ finalPath,Toast.LENGTH_LONG).show();
                }
            });
        }
        else
        {
            Log.d("ak47","EMpty Intent");
        }
    }
}
