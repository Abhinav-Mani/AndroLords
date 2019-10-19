package com.androlord.prayaas.NavigationFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androlord.prayaas.Adapters.RecyclerViewAdapterEbooks;
import com.androlord.prayaas.DataClass.EbookDetails;
import com.androlord.prayaas.DataClass.uploadPDF;
import com.androlord.prayaas.MainActivity;
import com.androlord.prayaas.R;
import com.androlord.prayaas.Support.AddEbook;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


public class Ebooks extends Fragment {

    FloatingActionButton floatingActionButton;
    StorageReference storageReference;

    DatabaseReference databaseReference;

    RecyclerViewAdapterEbooks recyclerViewAdapterEbooks;

    ArrayList<EbookDetails> list=new ArrayList<EbookDetails>();




    public Ebooks() {

    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vh=inflater.inflate(R.layout.fragment_ebooks2, container, false);
        intit(vh);
        getdata();
        return vh;
    }

    private void intit(View vh) {
        floatingActionButton=vh.findViewById(R.id.addebooks);
        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("Ebooks");
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddEbook.class));


            }
        });
        list=new ArrayList<EbookDetails>();
        RecyclerView recyclerView=vh.findViewById(R.id.recyclerViewListEbooks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewAdapterEbooks=new RecyclerViewAdapterEbooks(getContext(),list);
        recyclerView.setAdapter(recyclerViewAdapterEbooks);



    }

    private void getdata() {
        Log.d("ak47", "getdata: ");
        databaseReference=FirebaseDatabase.getInstance().getReference("Ebooks");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    HashMap<String,String > mp=new HashMap<String,String>();
                    mp=(HashMap<String,String >)dataSnapshot1.getValue();
                    Log.d("ak47",dataSnapshot1.getKey()+"->"+mp);
                    EbookDetails ebookDetails=new EbookDetails();
                    ebookDetails.name=mp.get("name");
                    ebookDetails.url=mp.get("url");
                    list.add(ebookDetails);
                }
                recyclerViewAdapterEbooks.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
