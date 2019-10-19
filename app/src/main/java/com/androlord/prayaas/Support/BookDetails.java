package com.androlord.prayaas.Support;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androlord.prayaas.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class BookDetails extends AppCompatActivity {
    HashMap<String,String> info;
    ImageView imageView;
    TextView name,author,pref,publish,exam;
    RatingBar ratingBar;
    Button button,report,Ok,Remove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.androlord.prayaas.R.layout.activity_book_details);
        imageView=findViewById(R.id.bookdetailsimage);
        name=findViewById(R.id.bookdetailsname);
        author=findViewById(R.id.bookdetailsauthor);
        pref=findViewById(R.id.bookdetailspref);
        button=findViewById(R.id.contactperson);
        publish=findViewById(R.id.publish);
        exam=findViewById(R.id.exam);
        ratingBar=findViewById(R.id.rate);
        report=findViewById(R.id.reportBook);
        Ok=findViewById(R.id.OK);
        Remove=findViewById(R.id.Remove);
        if(getIntent().hasExtra("Admin"))
        {
            report.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
        }
        else
        {
            Ok.setVisibility(View.GONE);
            Remove.setVisibility(View.GONE);
        }




        if(getIntent().hasExtra("bookdetails"))
        {
            info=(HashMap<String,String>)getIntent().getSerializableExtra("bookdetails");
            Log.e("ak47", "onCreate: "+info.get("Title") );
            Ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase.getInstance().getReference().child("Reports").child(info.get("City")).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(BookDetails.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
            Remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase.getInstance().getReference().child(info.get("City")).child(info.get("Key")).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            FirebaseDatabase.getInstance().getReference().child("Reports").child(info.get("City")).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(BookDetails.this,"REMOVED",Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(BookDetails.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }
            });

//            Uri uri=Uri.parse(info.get("Cover"));
//            String Title=info.get("Title");
//            String Author=info.get("Author");
//            String pref=info.get("Pref");
            Log.e("ak47",name+" "+author+" "+pref);
            Glide.with(BookDetails.this).asBitmap().load(info.get("Cover")).transform(new RoundedCorners(4)).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(imageView);
            name.setText(info.get("Title"));
            author.setText(info.get("Author"));
            pref.setText(info.get("Pref"));
            String currentUser= FirebaseAuth.getInstance().getCurrentUser().getEmail();
            String user2=info.get("Email");
            ratingBar.setRating(Float.parseFloat(info.get("Rating"))/2);
            publish.setText(info.get("DateOFPublish"));
            exam.setText(info.get("Exam"));


            if(user2.equalsIgnoreCase(currentUser))
                button.setVisibility(View.GONE);
        }
        else
        {
            Toast.makeText(BookDetails.this,"Wromg",Toast.LENGTH_LONG).show();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(BookDetails.this,"Contact",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(BookDetails.this,ChatBox.class);

                HashMap<String,String> data = new HashMap<String,String >();
                ;
                data.put("Title",info.get("Title"));
                data.put("Cover",info.get("Cover"));
                data.put("Source","0");
                data.put("User",info.get("Email").substring(0,info.get("Email").indexOf('@')));
                data.put("DateOFPublish",info.get("DateOFPublish"));
                data.put("Exam",info.get("Exam"));
                data.put("Rating",info.get("Rating"));

                intent.putExtra("Data",data);
                startActivity(intent);
            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               FirebaseDatabase.getInstance().getReference("Reports").child(info.get("City")).setValue(info.get("Key")).addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {
                       Toast.makeText(BookDetails.this,"Reported",Toast.LENGTH_LONG);
                       finish();
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(BookDetails.this,"Something Went Wrong",Toast.LENGTH_LONG);
                   }
               });

            }
        });


    }
}
