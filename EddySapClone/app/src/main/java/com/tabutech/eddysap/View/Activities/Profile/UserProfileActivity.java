package com.tabutech.eddysap.View.Activities.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tabutech.eddysap.R;
import com.tabutech.eddysap.databinding.ActivityUserProfileBinding;

import java.util.Objects;

public class UserProfileActivity extends AppCompatActivity {

    private ActivityUserProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_user_profile);

        Intent intent = getIntent();
        String name = intent.getStringExtra("userName");
        String receiverId = intent.getStringExtra("userId");
        String userProfile = intent.getStringExtra("userProfile");

        if (receiverId != null){
            binding.tvUsername.setTitle(name);

            if (userProfile.equals("")){
                binding.imageProfile.setImageResource(R.drawable.place_holder);//set default image if user has no profile image

            }else{
                Glide.with(this).load(userProfile).into(binding.imageProfile);
            }
        }
        initToolbar();
    }

    private void initToolbar(){
        binding.tvUsername.setNavigationIcon(R.drawable.arrow_back);
        setSupportActionBar(binding.tvUsername);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            finish();
        }else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}