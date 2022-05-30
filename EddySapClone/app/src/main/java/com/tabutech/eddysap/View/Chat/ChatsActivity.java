package com.tabutech.eddysap.View.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.tabutech.eddysap.R;
import com.tabutech.eddysap.databinding.ActivityChatsBinding;

public class ChatsActivity extends AppCompatActivity {

    private ActivityChatsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_chats);

        Intent intent = getIntent();

        String name = intent.getStringExtra("userName");
        String userId = intent.getStringExtra("userId");
        String userProfile = intent.getStringExtra("userProfile");

        if (userId != null){
            binding.tvUsername.setText(name);

            Glide.with(this).load(userProfile).into(binding.imageProfile);
        }

        binding.imageBack.setOnClickListener(v ->{
            finish();
        });
    }
}