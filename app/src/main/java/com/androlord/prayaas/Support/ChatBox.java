package com.androlord.prayaas.Support;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androlord.prayaas.Adapters.RecyclerViewChatAdapter;
import com.androlord.prayaas.DataClass.Messege;
import com.androlord.prayaas.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChatBox extends AppCompatActivity {
    private FirebaseDatabase database ;
    private DatabaseReference myRefCurrent,myRefUser2,databaseReference;
    FirebaseAuth mAuth;
    double lat,lng;
    HashMap<String,String > data;
    String currentUser,user2;
    private LocationManager locationManager;
    private String provider;
    RecyclerView chatbox;
    EditText messegeComposer;
    Button send,location;

    ArrayList<Messege> messeges=new ArrayList<Messege>();

    RecyclerViewChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);
        Log.e("ak47","ChatBox");

        init();
        getmesseges();





    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void locate()
    {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            Toast.makeText(this,"Hiiiiii",Toast.LENGTH_LONG).show();
            // for Activity#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);


        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            //onLocationChanged(location);
            lat = location.getLatitude();
            lng = location.getLongitude();
        } else {
            Toast.makeText(this,"location not available",Toast.LENGTH_LONG).show();
        }


    }



    private void getmesseges() {
        final ArrayList<Messege> sent=new ArrayList<Messege>();
        final ArrayList<Messege> recive=new ArrayList<Messege>();


        myRefCurrent = database.getReference().child("Communication").child(currentUser).child("Messege").child(user2);
        myRefUser2 = database.getReference().child("Communication").child(user2).child("Messege").child(currentUser);
        myRefCurrent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sent.clear();
                messeges.clear();

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    Log.e("ak47",dataSnapshot1.getValue()+" "+dataSnapshot1.getKey());
                    Messege m=new Messege((String) dataSnapshot1.getValue(),Long.valueOf(dataSnapshot1.getKey()),true);
                    sent.add(m);
                }
                messeges.addAll(sent);
                messeges.addAll(recive);
                Collections.sort(messeges);
                chatAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        myRefUser2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recive.clear();
                messeges.clear();

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    Messege m=new Messege((String) dataSnapshot1.getValue(),Long.valueOf(dataSnapshot1.getKey()),false);
                    recive.add(m);
                }
                messeges.addAll(sent);
                messeges.addAll(recive);
                Collections.sort(messeges);
                chatAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void init() {


        location=findViewById(R.id.locate);
        RecyclerView recyclerView=findViewById(R.id.chatbox);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ChatBox.this);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        chatAdapter=new RecyclerViewChatAdapter(messeges);
        recyclerView.setAdapter(chatAdapter);

        Intent intent=getIntent();
        mAuth=FirebaseAuth.getInstance();
        String c=mAuth.getCurrentUser().getEmail().toString();
        currentUser=c.substring(0,c.indexOf('@'));

        chatbox=findViewById(R.id.chatbox);
        messegeComposer=findViewById(R.id.MessgeContent);
        send=findViewById(R.id.sendMessege);

        database = FirebaseDatabase.getInstance();
        databaseReference=database.getReference().child("Communication");
        location.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                locate();
                String messege="LOCATION:-"+lat+"||"+lng;
                long ts=(long)System.currentTimeMillis();
                databaseReference.child(currentUser).child("Messege").child(user2).child(String.valueOf(ts)).setValue(messege);
                messegeComposer.setText("");
                // String uri = String.format(Locale.ENGLISH, "geo:%f,%f", lat, lng );
                String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng + " ("+")";

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                startActivity(intent);

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messege=messegeComposer.getText().toString().trim();
                long ts=(long)System.currentTimeMillis();
                databaseReference.child(currentUser).child("Messege").child(user2).child(String.valueOf(ts)).setValue(messege);
                messegeComposer.setText("");
            }
        });





        Log.e("ak47","ChatBoxinit");

        if(intent.hasExtra("Data"))
        {
            Log.e("ak47","ChatBoxi23");
            data=(HashMap<String,String >)intent.getSerializableExtra("Data");
            String owner=data.get("User");
            user2=owner;
            Log.d("ak47", "init: "+user2);
            String title=data.get("Title");
            String coveruri=data.get("Cover");
            Log.e("ak47", "init: "+data );
            if(data.get("Source").equalsIgnoreCase("0"))
            {

                Log.e("ak47","ChatBoxtry"+currentUser+"->"+owner+" "+title+" "+coveruri);
                user2=owner;
                databaseReference.child(currentUser).child("Contacts").child(owner).child(title).setValue(coveruri);
                databaseReference.child(owner).child("Contacts").child(currentUser).child(title).setValue(coveruri);
                Log.e("ak47","ChatBoxsucess");
            }
        }
    }
}
