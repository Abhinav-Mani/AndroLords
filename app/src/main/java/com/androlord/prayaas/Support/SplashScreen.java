package com.androlord.prayaas.Support;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.androlord.prayaas.R;

public class SplashScreen extends AppCompatActivity {
    ImageView logo,prayaas;
    TextView beEd,beEmp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splach_screen_layout);
        logo=findViewById(R.id.logo);
        prayaas=findViewById(R.id.prayaas);
        beEd=findViewById(R.id.beEd);
        beEmp=findViewById(R.id.beEmp);
        beEd.setTranslationX(-1000f);
        beEmp.setTranslationX(+1000f);


        logo.setAlpha(0f);
        prayaas.setAlpha(0f);

        logo.animate().alpha(1f).setDuration(500);
        prayaas.animate().alpha(1f).setDuration(500);
        beEd.animate().translationXBy(1000f).setDuration(1000);
        beEmp.animate().translationXBy(-1000f).setDuration(1000);
    }
}