package com.tabutech.eddysap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.tabutech.eddysap.View.MainActivity;
import com.tabutech.eddysap.View.Activities.starUp.WelcomeScreenActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        auth = FirebaseAuth.getInstance();
        new Handler().postDelayed(() ->{
            if (auth.getCurrentUser() == null) {
                startActivity(new Intent(SplashScreenActivity.this, WelcomeScreenActivity.class));
                finish();
            }else {
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish();
            }
        },2000);
    }
}