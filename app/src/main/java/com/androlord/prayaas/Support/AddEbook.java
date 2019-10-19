package com.androlord.prayaas.Support;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.androlord.prayaas.DataClass.uploadPDF;
import com.androlord.prayaas.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddEbook extends AppCompatActivity {
    EditText name,author;
    ImageButton upload;
    StorageReference storageReference;

    DatabaseReference databaseReference;
    long Key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Key=System.currentTimeMillis();

        setContentView(R.layout.activity_add_ebook);
        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("Ebooks");
        name=findViewById(R.id.edittext_name_ebook);
        author=findViewById(R.id.edittext_author_ebook);
        upload=findViewById(R.id.upload_ebook);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDF();
            }
        });

    }
    private void selectPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"SELECT PDF FILE"),1);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode== RESULT_OK && data!=null &&data.getData()!=null){
            if(TextUtils.isEmpty( name.getText().toString().trim()))
            {
                Toast.makeText(AddEbook.this,"Enter Titile",Toast.LENGTH_LONG).show();
            }
            else if(TextUtils.isEmpty(author.getText().toString().trim()))
            {
                Toast.makeText(AddEbook.this,"Enter Author",Toast.LENGTH_LONG).show();
            }
            else
            {
                uploadPDFFiles(data.getData());
            }

        }


    }
    private void uploadPDFFiles(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(AddEbook.this);
        progressDialog.setTitle("Uploading......");
        progressDialog.show();


        StorageReference reference= storageReference.child("uploads/"+Key+".pdf");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uri =taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                        Uri url =uri.getResult();


                        uploadPDF uploadPDF=new uploadPDF(name.getText().toString().trim(),author.getText().toString().trim(),url.toString(),String.valueOf(Key));
                        databaseReference.child(databaseReference.push().getKey()).setValue(uploadPDF);
                        Toast.makeText(AddEbook.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        finish();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress =(100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded: "+(int)progress+"%");
            }
        });


    }
}
