package com.androlord.prayaas.NavigationFragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.androlord.prayaas.Adapters.RecyclerViewAdapterBooks;
import com.androlord.prayaas.MainActivity;
import com.androlord.prayaas.R;
import com.androlord.prayaas.Support.ParseCity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.app.Activity.RESULT_OK;

public class BooksAvailable extends Fragment {
    RecyclerView recyclerView;
    Spinner state_search,city_search,exam_type_search;
    Button search;
    LinearLayout linearLayout;
    private final int REQ_CODE_SPEECH_INPUT = 2;
    ImageView location;
    ArrayAdapter stat,cit,exam;

    LinearLayoutManager linearLayoutManager;
    RecyclerViewAdapterBooks recyclerViewAdapterBooks;
    static AutoCompleteTextView mAutoCompleteTextView;
    private ImageView mSpeechButton;

    private FirebaseDatabase database ;
    private DatabaseReference myRef;


    public BooksAvailable() {

    }
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.enter_journal_message));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (Exception e) {

        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mAutoCompleteTextView.setText(result.get(0));
                    search.callOnClick();
                }
                break;
        }
    }
    public void check() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!mAutoCompleteTextView.getText().toString().trim().equals("")) {
                    search.setVisibility(View.VISIBLE);

                } else {
                    search.setVisibility(View.GONE);
                }
                check();
            }
        };

        handler.postDelayed(runnable, 1000);


    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vh = inflater.inflate(R.layout.fragment_books_available, container, false);
        final ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

        location=vh.findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
                {
                   // Log.e(TAG, "setxml: peremission prob");
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
                }else {
                    mAutoCompleteTextView.setText("Ranchi");
                }
            }
        });
        mAutoCompleteTextView=vh.findViewById(R.id.autoComplete);

        InputStream inputStream = getResources().openRawResource(R.raw.cities);
        Log.d("csv","csv");
        ParseCity city = new ParseCity(inputStream);
        List<String> cities = city.getCity(getContext());

        Log.e("check", Integer.toString(cities.size()));
        ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,
                cities);
        mAutoCompleteTextView.setThreshold(1);
        mAutoCompleteTextView.setAdapter(mArrayAdapter);
        linearLayout=vh.findViewById(R.id.available_book);
        mSpeechButton=vh.findViewById(R.id.voice_search);
        mSpeechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });

        mAutoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    check();
                } else
                    search.setVisibility(View.GONE);
            }
        });


        search=vh.findViewById(R.id.search);
        recyclerView = vh.findViewById(R.id.available_book_list);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        final RecyclerViewAdapterBooks adapterBooks = new RecyclerViewAdapterBooks(list, getContext());
        recyclerView.setAdapter(adapterBooks);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                linearLayout.setVisibility(View.VISIBLE);
                Log.d("ak47", "onClick: ");
                database = FirebaseDatabase.getInstance();

                String city=mAutoCompleteTextView.getText().toString().trim();
                Log.d("city", "onClick: "+city);
                if(city.equalsIgnoreCase(""))
                    myRef = database.getReference("Ranchi");
                else
                    myRef=database.getReference(city);


                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Map<String ,String> mp=new HashMap<>();
                            String exam="";
                            exam="Select Exam";
                            mp=(Map<String, String>) dataSnapshot1.getValue();

                            if(mp.get("Exam").equalsIgnoreCase(exam)||exam.equalsIgnoreCase("Select Exam"))
                            {
                                Log.d("ak47", "onDataChange: "+mp.get("Exam")+" "+exam );
                                list.add(mp);
                            }
                            else
                            {
                                Log.e("ak47", "onDataChange: "+mp.get("Exam")+" "+exam );
                            }

                            Log.d("ak47", "onDataChange: " + dataSnapshot.getKey() + "->" + dataSnapshot.getValue());
                            dataSnapshot.getKey();
                        }
                        //Log.e("ak47", list.get(0).get("Title") + "onDataChange: " + list.size());
                        adapterBooks.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });






        return vh;
    }

}





