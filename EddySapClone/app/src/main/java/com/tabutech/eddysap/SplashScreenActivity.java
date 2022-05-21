package com.tabutech.eddysap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.tabutech.eddysap.View.MainActivity;
import com.tabutech.eddysap.View.starUp.WelcomeScreenActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(() ->{
            startActivity(new Intent(SplashScreenActivity.this, WelcomeScreenActivity.class));
            finish();
        },2000);
    }
}