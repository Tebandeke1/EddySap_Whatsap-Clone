package com.tabutech.eddysap.View.Activities.starUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.tabutech.eddysap.R;
import com.tabutech.eddysap.View.Activities.auth.PhoneLoginActivity;

public class WelcomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        
        Button btnAgree = findViewById(R.id.agree);
        
        btnAgree.setOnClickListener( b -> {
            startActivity(new Intent(WelcomeScreenActivity.this, PhoneLoginActivity.class));
            finish();
        });
    }
}