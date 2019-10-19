package com.androlord.prayaas.Support;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.androlord.prayaas.MainActivity;
import com.androlord.prayaas.R;

public class SplashScreen extends AppCompatActivity {
    ImageView logo,prayaas;
    TextView beEd,beEmp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logo=findViewById(R.id.logo);
        prayaas=findViewById(R.id.prayaas);
        beEd=findViewById(R.id.beEd);
        beEmp=findViewById(R.id.beEmp);
        beEd.setTranslationX(-1000f);
        beEmp.setTranslationX(+1000f);


        logo.setAlpha(0f);
        prayaas.setAlpha(0f);

        logo.animate().alpha(1f).setDuration(2000);
        prayaas.animate().alpha(1f).setDuration(2000);
        beEd.animate().translationXBy(1000f).setDuration(2000);
        beEmp.animate().translationXBy(-1000f).setDuration(2000);

        Handler handler = new Handler();
        final Intent i = new Intent(SplashScreen.this, MainActivity.class);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(i);
                finish();
            }
        }, 2000);

    }
}