package com.androlord.prayaas.NavigationFragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androlord.prayaas.Adapters.RecyclerViewAdapterMyBooks;
import com.androlord.prayaas.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class YourBooks extends Fragment {

    public YourBooks() {
        // Required empty public constructor
    }




    FloatingActionButton addBooks;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerViewAdapterMyBooks recyclerViewAdapterMyBooks;

    private FirebaseDatabase database ;
    private DatabaseReference myRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vh= inflater.inflate(R.layout.fragment_your_books, container, false);
        intit(vh);
        return vh;
    }


    private void intit(View vh) {
        addBooks=vh.findViewById(R.id.addbooks);
        addBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),com.androlord.prayaas.Support.AddBooks.class));

            }
        });
        final ArrayList<Map<String,String>> list=new ArrayList<Map<String,String>>();
        recyclerView=vh.findViewById(R.id.yourbookrecyclerview);
        linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewAdapterMyBooks=new RecyclerViewAdapterMyBooks(list,getContext());
        recyclerView.setAdapter(recyclerViewAdapterMyBooks);
        database = FirebaseDatabase.getInstance();
        String email=String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        myRef = database.getReference(email.substring(0,email.indexOf('@')));
        final DatabaseReference reference=database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                Log.d("ak47", "onDataChange:-> "+dataSnapshot.getKey());
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    Log.d("ak47", dataSnapshot1.getKey()+"onDataChange:-> "+dataSnapshot1.getValue());
                    reference.child((String) dataSnapshot1.getValue()).child(dataSnapshot1.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, String> mp= new HashMap<>();
                            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                            {
                                //dataSnapshot.getKey();

                                mp.put(dataSnapshot1.getKey(),(String) dataSnapshot1.getValue());
                                Log.d("ak47", "onDataChange: "+dataSnapshot1.getKey()+"->"+dataSnapshot1.getValue());

                            }
                            Log.e("ak47", "value added");
                            if(mp.get("Title")!=null)
                                list.add(mp);
                            Log.e("ak47", "onDataChange:title:- "+mp.get("Title") );
                            Log.e("ak47", "onDataChange: notified" );
                            recyclerViewAdapterMyBooks.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
