package com.androlord.prayaas.Support;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.androlord.prayaas.Adapters.RecyclerViewChatList;
import com.androlord.prayaas.Adapters.RecyclerViewReportList;
import com.androlord.prayaas.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminPanel extends AppCompatActivity {

    ArrayList<Map<String,String>> contacts=new ArrayList<Map<String, String>>();

    private FirebaseDatabase database ;
    private DatabaseReference myRef;

    RecyclerViewReportList adapter;

    ArrayList<HashMap<String,String>> list= new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        intit();
        getdata();

    }

    private void intit() {
        RecyclerView recyclerView=findViewById(R.id.adminReportList);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminPanel.this));
        adapter=new RecyclerViewReportList(AdminPanel.this,list);
        recyclerView.setAdapter(adapter);
    }
    private void getdata(){
        Log.d("ak47", "getdata: ");
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference("Reports");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("ak47", "onDataChange: ");
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    list.clear();
                    Log.d("ak47", "onDataChange: "+dataSnapshot1);
                    database.getReference().child((String) dataSnapshot1.getValue()).child(dataSnapshot1.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.d("ak47", "onDataChange:inside "+dataSnapshot);
                            HashMap<String ,String> hashMap=new HashMap<String ,String>();
                            for(DataSnapshot dataSnapshot11:dataSnapshot.getChildren())
                            {
                                hashMap.put(dataSnapshot11.getKey(),(String) dataSnapshot11.getValue());
                            }
                            Log.d("ak47", "onDataChange:-added "+hashMap);
                            list.add(hashMap);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
