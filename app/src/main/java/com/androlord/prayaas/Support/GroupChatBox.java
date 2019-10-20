package com.androlord.prayaas.Support;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.androlord.prayaas.Adapters.RecyclerViewGroupChatAdapter;
import com.androlord.prayaas.DataClass.GroupMessage;
import com.androlord.prayaas.NavigationFragments.GroupChat;
import com.androlord.prayaas.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupChatBox extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText composer;
    Button send;
    String currentUser;
    RecyclerViewGroupChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_box);
        init();
    }
    ArrayList<GroupMessage> list=new ArrayList<GroupMessage>();

    private void init() {
        Intent intent=getIntent();
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        currentUser=email.substring(0,email.indexOf('@'));
        final HashMap<String,String > mp= (HashMap<String, String>) intent.getSerializableExtra("Data");
        recyclerView=findViewById(R.id.grpchatbox);
        recyclerView.setLayoutManager(new LinearLayoutManager(GroupChatBox.this));

        getData(mp);

        adapter=new RecyclerViewGroupChatAdapter(list,currentUser);
        recyclerView.setAdapter(adapter);

        composer=findViewById(R.id.grpMessgeContent);
        send=findViewById(R.id.grpsendMessege);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupMessage groupMessage=new GroupMessage(composer.getText().toString().trim(),currentUser,System.currentTimeMillis());
                composer.setText("");
                FirebaseDatabase.getInstance().getReference().child(mp.get("Name")).child(String.valueOf(groupMessage.time)).setValue(groupMessage);
            }
        });

    }

    private void getData(final HashMap<String,String > mp) {
        FirebaseDatabase.getInstance().getReference().child(mp.get("Name")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    GroupMessage groupMessage=new GroupMessage("as0","12",12);
                    for(DataSnapshot dataSnapshot11:dataSnapshot1.getChildren())
                    {
                        if(dataSnapshot11.getKey().equalsIgnoreCase("user"))
                            groupMessage.user=(String)dataSnapshot11.getValue();
                        else if(dataSnapshot11.getKey().equalsIgnoreCase("messege"))
                            groupMessage.messege=(String)dataSnapshot11.getValue();
                        else
                            groupMessage.time=(Long)dataSnapshot11.getValue();
                    }
                    Log.d("ak47", "onDataChange: "+groupMessage.messege);
                    list.add(groupMessage);

//                    HashMap<String,String> hashMap=new HashMap<String,String>();
//                    hashMap=(HashMap<String,String>)dataSnapshot1.getValue();
//                    Log.d("ak47", "onDataChange: "+hashMap);
//                    Log.d("ak47",hashMap.get("time"));
                    //GroupMessage groupMessage=dataSnapshot1.getValue(GroupMessage.class);
                    //Log.d("ak47", "onDataChange: "+groupMessage.messege);
                    //GroupMessage groupMessage=new GroupMessage(hashMap.get("messege"),hashMap.get("user"),Long.valueOf(hashMap.get("time")));
                }
                adapter.notifyDataSetChanged();
                //Log.d("ak47",dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
