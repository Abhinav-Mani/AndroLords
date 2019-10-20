package com.androlord.prayaas.NavigationFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androlord.prayaas.Adapters.RecyclerViewGroupChatList;
import com.androlord.prayaas.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class GroupChat extends Fragment {

    RecyclerViewGroupChatList adapter;
    ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();



    public GroupChat() {

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vh= inflater.inflate(R.layout.fragment_group_chat, container, false);
        init(vh);
        getdata();
        return vh;
    }

    private void getdata() {
        FirebaseDatabase.getInstance().getReference().child("Groups").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    HashMap<String ,String> hashMap=new HashMap<String ,String>();
                    hashMap.put("Name",dataSnapshot1.getKey());
                    hashMap.put("Cover",(String) dataSnapshot1.getValue());

                    list.add(hashMap);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init(View vh) {
        RecyclerView recyclerView=vh.findViewById(R.id.CommunityList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        adapter=new RecyclerViewGroupChatList(list,getContext());
        recyclerView.setAdapter(adapter);

    }

}
