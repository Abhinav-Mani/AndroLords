package com.androlord.prayaas.Support;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androlord.prayaas.NavigationFragments.ExpandableTextView;
import com.androlord.prayaas.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddBooks extends AppCompatActivity {
    Spinner exam_type,rating,subject,state,city;
    ArrayAdapter exam,rate,sub,stat,cit;
    ImageView book_photo,img1,img2,img3;
    TextView publish,exam_error,sub_error,reuse_error,radio_error,book_error,date_error,state_error,city_error;
    TextInputEditText name,author;
    TextInputLayout name_layout,author_layout;
    RadioButton donate,sell;
    CheckBox privacy;
    private int flag=0,dater=0;
    String key;
    private Camera mCamera;
    ExpandableTextView expandableTextView,expandableTextView2,expandableTextView3;
    boolean valid=true;
    DatePickerDialog.OnDateSetListener dateSetListener;


    //upload@abhinavmani

    private StorageReference mStorageRef;
    private FirebaseDatabase database ;
    private DatabaseReference myRef,databaseReference;




    void upload()
    {
        String bookName="",authorName="",PublishDate="",subject="",exam="",city=this.city.getSelectedItem().toString(),
                resusabilityRating="",pref="sell";
        bookName=name.getText().toString();
        authorName=author.getText().toString().trim();
        exam=exam_type.getSelectedItem().toString();
        subject=this.subject.getSelectedItem().toString();
        resusabilityRating=rating.getSelectedItem().toString();
        PublishDate=publish.getText().toString().trim();

        if(donate.isChecked())
            pref="donate";
        Bitmap mainImage=((BitmapDrawable)book_photo.getDrawable()).getBitmap();
        String emailid= FirebaseAuth.getInstance().getCurrentUser().getEmail();


        Log.e("ak47", "upload: " );
        final Map<String,Object> bookdata=new HashMap<>();
        bookdata.put("Email",emailid);
        bookdata.put("Title",bookName);
        bookdata.put("Author",authorName);
        bookdata.put("DateOFPublish",PublishDate);
        bookdata.put("Subject",subject);
        bookdata.put("Exam",exam);
        bookdata.put("Rating",resusabilityRating);
        bookdata.put("Pref",pref);

        final Uri[] imguri = new Uri[4];

        mStorageRef= FirebaseStorage.getInstance().getReference().child(key);
        database = FirebaseDatabase.getInstance();
        databaseReference=database.getReference();
        myRef = databaseReference.child(city).child(key);


        databaseReference.child(emailid.substring(0,emailid.indexOf('@'))).child(key).setValue(city);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mainImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask mainImageUpload=mStorageRef.child("Cover").putBytes(data);
        mainImageUpload.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.e("ak47", "onSuccess: upload sucess" );
                mStorageRef.child("Cover").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imguri[0] =uri;
                        bookdata.put("Cover",String.valueOf(imguri[0]));
                        Log.e("ak47", "onSuccess: "+String.valueOf(imguri[0]) );
                        myRef.updateChildren(bookdata).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AddBooks.this,"Upload Sucess",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddBooks.this,"UploadFailed",Toast.LENGTH_LONG).show();
                                Log.e("ak47", "onFailure: upload" );
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddBooks.this,"Couldnot Fetch uri",Toast.LENGTH_LONG).show();
                                Log.e("ak47", "onFailure: uri" );
                            }
                        });
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddBooks.this,"Aww! Snap !!",Toast.LENGTH_LONG).show();
            }
        });






    }








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.androlord.prayaas.R.layout.activity_add_books);


        init();
        setCity();
        expand();
        spinnerExamAndRating();
        setSubjects();
        dateChange();

    }



    public void init()
    {

        key=String.valueOf(System.currentTimeMillis());
        exam_type=findViewById(R.id.exam_type);
        book_photo=findViewById(R.id.book_photo);
        rating=findViewById(R.id.reuse_rating);
        subject=findViewById(R.id.subjectNames);
        publish=findViewById(R.id.publish_date);

        expandableTextView=findViewById(R.id.reuse);

        expandableTextView3=findViewById(R.id.privacy);
        name=findViewById(R.id.book_name);
        author=findViewById(R.id.author_name);
        name_layout=findViewById(R.id.book_name_layout);
        author_layout=findViewById((R.id.author_name_layout));
        exam_error=findViewById(R.id.exam_error);
        sub_error=findViewById(R.id.sub_error);
        date_error=findViewById(R.id.date_error);
        reuse_error=findViewById(R.id.reuse_error);
        donate=findViewById(R.id.donate);
        sell=findViewById(R.id.sell);
        radio_error=findViewById(R.id.radio_error);
        privacy=findViewById(R.id.privacy_policy);
        book_error=findViewById(R.id.book_error);
        state=findViewById(R.id.state);
        city=findViewById(R.id.city);
        state_error=findViewById(R.id.state_error);
        city_error=findViewById(R.id.city_error);
    }
    public void spinnerExamAndRating()
    {
        exam=ArrayAdapter.createFromResource(this,R.array.exam_type,android.R.layout.simple_spinner_item);
        exam.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        exam_type.setAdapter(exam);
        stat=ArrayAdapter.createFromResource(this,R.array.state_list,android.R.layout.simple_spinner_item);
        stat.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        state.setAdapter(stat);
        rate=ArrayAdapter.createFromResource(this,R.array.rating,android.R.layout.simple_spinner_item);
        rate.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        rating.setAdapter(rate);
    }
    public void setCity()
    {
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                { case 0:
                    cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.selectCity,android.R.layout.simple_spinner_item);
                    cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    city.setAdapter(cit);
                    break;

                    case 1:
                        cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.andhra_pradesh_cities,android.R.layout.simple_spinner_item);
                        cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        city.setAdapter(cit);
                        break;


                    case 2:
                        cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.arunachal_pradesh_cities,android.R.layout.simple_spinner_item);
                        cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        city.setAdapter(cit);
                        break;

                    case 3:
                        cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.assam_cities,android.R.layout.simple_spinner_item);
                        cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        city.setAdapter(cit);
                        break;

                    case 4:
                        cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.Bihar,android.R.layout.simple_spinner_item);
                        cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        city.setAdapter(cit);
                        break;

                    case 5:
                        cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.Chhatisgarh,android.R.layout.simple_spinner_item);
                        cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        city.setAdapter(cit);
                        break;

                    case 6: cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.goa_cities,android.R.layout.simple_spinner_item);
                        cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        city.setAdapter(cit);
                        break;

                    case 7: cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.gujarat_cities,android.R.layout.simple_spinner_item);
                        cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        city.setAdapter(cit);
                        break;

                    case 8: cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.haryana_cities,android.R.layout.simple_spinner_item);
                        cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        city.setAdapter(cit);
                        break;

                    case 9: cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.himachal_pradesh_cities,android.R.layout.simple_spinner_item);
                        cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        city.setAdapter(cit);
                        break;

                    case 10: cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.jammu_and_kashmir_cities,android.R.layout.simple_spinner_item);
                        cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        city.setAdapter(cit);
                        break;

                    case 11: cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.jharkhand_cities,android.R.layout.simple_spinner_item);
                        cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        city.setAdapter(cit);
                        break;

                    case 12: cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.karnataka_cities,android.R.layout.simple_spinner_item);
                        cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        city.setAdapter(cit);
                        break;

                    case 13: cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.kerala_cities,android.R.layout.simple_spinner_item);
                        cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        city.setAdapter(cit);
                        break;

                    case 14: cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.madhya_pradesh_cities,android.R.layout.simple_spinner_item);
                        cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        city.setAdapter(cit);
                        break;
                    case 15: cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.maharashtra_cities,android.R.layout.simple_spinner_item);
                        cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        city.setAdapter(cit);
                        break;
                    case 16: cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.manipur_cities,android.R.layout.simple_spinner_item);
                        cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        city.setAdapter(cit);
                        break;
                    case 17: cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.meghalaya_cities,android.R.layout.simple_spinner_item);
                        cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        city.setAdapter(cit);
                        break;
                    case 18: cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.mizoram_cities,android.R.layout.simple_spinner_item);
                        cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        city.setAdapter(cit);
                        break;
                    case 19: cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.nagaland_cities,android.R.layout.simple_spinner_item);
                        cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        city.setAdapter(cit);
                        break;
                    case 20: cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.odisha_cities,android.R.layout.simple_spinner_item);
                        cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        city.setAdapter(cit);
                        break;
                    case 21: cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.punjab_cities,android.R.layout.simple_spinner_item);
                        cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        city.setAdapter(cit);
                        break;
                    case 22: cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.rajasthan_cities,android.R.layout.simple_spinner_item);
                        cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        city.setAdapter(cit);
                        break;
                    case 23: cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.sikkim_cities,android.R.layout.simple_spinner_item);
                        cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        city.setAdapter(cit);
                        break;
                    case 24: cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.tamil_nadu_cities,android.R.layout.simple_spinner_item);
                        cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        city.setAdapter(cit);
                        break;
                    case 25:cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.telangana_cities,android.R.layout.simple_spinner_item);
                        cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        city.setAdapter(cit);
                        break;
                    case 26:cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.tripura_cities,android.R.layout.simple_spinner_item);
                        cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        city.setAdapter(cit);
                        break;
                    case 27:cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.uttar_pradesh_cities,android.R.layout.simple_spinner_item);
                        cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        city.setAdapter(cit);
                        break;
                    case 28:cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.uttrakhand_cities,android.R.layout.simple_spinner_item);
                        cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        city.setAdapter(cit);
                        break;
                    case 29:cit=ArrayAdapter.createFromResource(AddBooks.this,R.array.west_bengal_cities,android.R.layout.simple_spinner_item);
                        cit.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        city.setAdapter(cit);
                        break;




                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void setSubjects()
    {
        exam_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                { case 0:
                    sub=ArrayAdapter.createFromResource(AddBooks.this,R.array.select,android.R.layout.simple_spinner_item);
                    sub.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    subject.setAdapter(sub);
                    break;

                    case 1:
                        sub=ArrayAdapter.createFromResource(AddBooks.this,R.array.jee_mains_subjects,android.R.layout.simple_spinner_item);
                        sub.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        subject.setAdapter(sub);
                        break;
                    case 2:
                        sub=ArrayAdapter.createFromResource(AddBooks.this,R.array.jee_advanced_subjects,android.R.layout.simple_spinner_item);
                        sub.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        subject.setAdapter(sub);
                        break;
                    case 3:
                        sub=ArrayAdapter.createFromResource(AddBooks.this,R.array.gate_subjects,android.R.layout.simple_spinner_item);
                        sub.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        subject.setAdapter(sub);
                        break;
                    case 4:
                        sub=ArrayAdapter.createFromResource(AddBooks.this,R.array.neet_subjects,android.R.layout.simple_spinner_item);
                        sub.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        subject.setAdapter(sub);
                        break;
                    case 5:
                        sub=ArrayAdapter.createFromResource(AddBooks.this,R.array.aiims_subjects,android.R.layout.simple_spinner_item);
                        sub.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        subject.setAdapter(sub);
                        break;
                    case 6: sub=ArrayAdapter.createFromResource(AddBooks.this,R.array.icse_subjects,android.R.layout.simple_spinner_item);
                        sub.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        subject.setAdapter(sub);
                        break;
                    case 7:sub=ArrayAdapter.createFromResource(AddBooks.this,R.array.isc_subjects,android.R.layout.simple_spinner_item);
                        sub.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        subject.setAdapter(sub);
                        break;
                    case 8:sub=ArrayAdapter.createFromResource(AddBooks.this,R.array.cbse_10_subjects,android.R.layout.simple_spinner_item);
                        sub.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        subject.setAdapter(sub);
                        break;
                    case 9:sub=ArrayAdapter.createFromResource(AddBooks.this,R.array.cbse_12_subjects,android.R.layout.simple_spinner_item);
                        sub.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        subject.setAdapter(sub);
                        break;
                    case 10:sub=ArrayAdapter.createFromResource(AddBooks.this,R.array.cat_subjects,android.R.layout.simple_spinner_item);
                        sub.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        subject.setAdapter(sub);
                        break;
                    case 11:sub=ArrayAdapter.createFromResource(AddBooks.this,R.array.xat_subjects,android.R.layout.simple_spinner_item);
                        sub.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        subject.setAdapter(sub);
                        break;
                    case 12:sub=ArrayAdapter.createFromResource(AddBooks.this,R.array.upsc_subjects,android.R.layout.simple_spinner_item);
                        sub.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        subject.setAdapter(sub);
                        break;
                    case 13:sub=ArrayAdapter.createFromResource(AddBooks.this,R.array.nda_subjects,android.R.layout.simple_spinner_item);
                        sub.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        subject.setAdapter(sub);
                        break;
                    case 14:sub=ArrayAdapter.createFromResource(AddBooks.this,R.array.empty,android.R.layout.simple_spinner_item);
                        sub.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        subject.setAdapter(sub);
                        break;


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void expand()
    {
        // reusee="Tap to know more:The reusability scale is the measure of state of book.Rate your product on a scale of 1-10 sighting its present condition.It should be filled with honesty.A new book has a rating of 10 while a book more than 10 years old has a scale of 1";
        expandableTextView.setText(getResources().getString(R.string.reusea));
        //  String pic="Tap to know more:Please add three photos of different pages of the book.These photos will be accessible by the customers.";
      //  expandableTextView2.setText(getResources().getString(R.string.pic));
        String priv="We have access to your email,your location,your search history.This is in order to provide you a better user experience and more features.We do not share these critical informations specifically with any third party without your permissions but we use these data for a better app experience.You allow us to have access to these data.";
        expandableTextView3.setText(getResources().getString(R.string.policy));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.language,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.English:
                setLocale("en");

                break;
            case R.id.Hindi:
                setLocale("hi");

        }
        return super.onOptionsItemSelected(item);
    }
    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, AddBooks.class);
        finish();
        startActivity(refresh);
    }



    public void date(View view)
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                dateSetListener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue)));
        dialog.show();

    }
    public void dateChange()
    {

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = month + "/" + year;
                publish.setText(date);
                dater=1;
            }
        };
    }
    public boolean validate()
    {

        if(name.getText().toString().isEmpty())
        {

            name_layout.setError(getResources().getString(R.string.nameerror));}
        else
            name_layout.setError(null);
        if(author.getText().toString().isEmpty())
            author_layout.setError(getResources().getString(R.string.authorerror));
        else
            author_layout.setError(null);
        if(exam_type.getSelectedItem().toString().equals("SELECT EXAM")) {
            // exam_error.setTextColor(Color.RED);
            exam_error.setError("Exam type not selected");
        }
        else
            exam_error.setError(null);
        if(subject.getSelectedItem().toString().equals("SELECT SUBJECT")) {
            // sub_error.setTextColor(Color.RED);
            sub_error.setError("Exam type not selected");
        }
        else
            sub_error.setError(null);
        if(rating.getSelectedItem().toString().equals("REUSABILITY RATING")) {
            // reuse_error.setTextColor(Color.RED);
            reuse_error.setError("Exam type not selected");
        }
        else
            reuse_error.setError(null);
        if(state.getSelectedItem().toString().equals("SELECT STATE")) {
            // reuse_error.setTextColor(Color.RED);
            state_error.setError("Exam type not selected");
        }
        else
            state_error.setError(null);
        if(city.getSelectedItem().toString().equals("SELECT CITY")) {
            // reuse_error.setTextColor(Color.RED);
            city_error.setError("Exam type not selected");
        }
        else
            city_error.setError(null);
        if(donate.isChecked()==false&&sell.isChecked()==false)
            radio_error.setError("sels");
        else
            radio_error.setError(null);
        LinearLayout add=findViewById(R.id.book_add);
        if(dater==0)
            date_error.setError("jai hind");
        else
            date_error.setError(null);


        if(flag==0)
        {
            book_error.setError("Cover photo is mandatory");

        }
        else
            book_error.setError(null);
        if(privacy.isChecked()==false)
        {
            Snackbar snackbar=Snackbar.make(add, getResources().getString(R.string.agreewarn), Snackbar.LENGTH_LONG);
            View snak=snackbar.getView();
            snak.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.blue));
            snackbar.show();
        }

        if((!name.getText().toString().isEmpty())&&(!author.getText().toString().isEmpty())&&(!exam_type.getSelectedItem().toString().equals("SELECT EXAM"))&&(!subject.getSelectedItem().toString().equals("SELECT SUBJECT"))&&(!rating.getSelectedItem().toString().equals("REUSABILITY RATING"))&&(!state.getSelectedItem().toString().equals("SELECT STATE"))&&(!city.getSelectedItem().toString().equals("SELECT CITY"))&&(donate.isChecked()==true||sell.isChecked()==true)&&dater!=0&&flag!=0&&privacy.isChecked()==true)
            return true;
        else
            return false;



    }






    public void submit(View view)
    {
        if(validate()) {
            Toast.makeText(this, "done", Toast.LENGTH_LONG).show();
            upload();
        }






    }


    public void capture(View view) {

        flag=1;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null)
        {
            startActivityForResult(intent,0);

        }


    }
    public void image1(View view) {


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null)
            startActivityForResult(intent,1);
    }
    public void image2(View view) {


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null)
            startActivityForResult(intent,2);
    }
    public void image3(View view) {


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null)
            startActivityForResult(intent,3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(data!=null){
            Bitmap b=(Bitmap) data.getExtras().get("data");
            switch (requestCode) {
                case 0:
                    book_photo.setImageBitmap(b);
                    break;
                case 1:
                    img1.setImageBitmap(b);
                    break;
                case 2:
                    img2.setImageBitmap(b);
                    break;
                case 3:
                    img3.setImageBitmap(b);
                    break;
            }

        }}




}

