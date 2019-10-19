package com.androlord.prayaas.NavigationFragments;

import android.content.Context;
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

import com.androlord.prayaas.Adapters.RecyclerViewChatList;
import com.androlord.prayaas.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Chats extends Fragment {
    ArrayList<Map<String,String>> contacts=new ArrayList<Map<String, String>>();

    private FirebaseDatabase database ;
    private DatabaseReference myRef;

    RecyclerViewChatList recyclerViewChatList;

    public Chats() {
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vh= inflater.inflate(R.layout.fragment_chats, container, false);
        intit(vh);
        getdata();


        return vh;
    }

    private void getdata() {
        Log.d("ak47", "getdata: " );
        String c= FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        String currentUser=c.substring(0,c.indexOf('@'));
        database = FirebaseDatabase.getInstance();
        String email=String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        myRef = database.getReference().child("Communication").child(currentUser).child("Contacts");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                contacts.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    String user=dataSnapshot1.getKey();
                    for (DataSnapshot dataSnapshot2:dataSnapshot1.getChildren())
                    {
                        String Title=dataSnapshot2.getKey();
                        HashMap<String,String> mp=new HashMap<String,String>();
                        String Cover=(String) dataSnapshot2.getValue();
                        mp.put("Title",Title);
                        mp.put("User",user);
                        mp.put("Cover",Cover);
                        Log.d("ak47", "onDataChange: "+mp+" added");
                        contacts.add(mp);
                        Log.d("logno", "onDataChange: "+contacts);
                    }

                }
                recyclerViewChatList.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void intit(View vh) {
        RecyclerView recyclerView=vh.findViewById(R.id.chatList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewChatList=new RecyclerViewChatList(contacts,getContext());
        recyclerView.setAdapter(recyclerViewChatList);
    }


}
