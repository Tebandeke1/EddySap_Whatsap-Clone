package com.tabutech.eddysap.View.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tabutech.eddysap.Common.Common;
import com.tabutech.eddysap.R;
import com.tabutech.eddysap.View.Display.ViewProfileImageActivity;
import com.tabutech.eddysap.View.Profile.ProfileActivity;
import com.tabutech.eddysap.databinding.ActivitySettingsBinding;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_settings);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        firestore = FirebaseFirestore.getInstance();

        binding.profileLayout.setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));
        });

        if (firebaseUser != null){
            getData();
        }

        showImage();
    }

    private void showImage() {

        binding.imageProfile.setOnClickListener(v ->{
            binding.imageProfile.invalidate();
            Drawable dr = binding.imageProfile.getDrawable();

            Common.IMAGE_BITMAP = ((BitmapDrawable)dr.getCurrent()).getBitmap();

            ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    SettingsActivity.this,binding.imageProfile,"image");

            Intent intent = new Intent(SettingsActivity.this, ViewProfileImageActivity.class);
            startActivity(intent,compat.toBundle());
        });

    }

    private void getData() {
        firestore.collection("Users").document(firebaseUser.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {

                    String name = Objects.requireNonNull(documentSnapshot.get("userName")).toString();
                    String imageProfile = documentSnapshot.getString("imageProfile");
                    binding.settingsUserName.setText(name);
                    Glide.with(SettingsActivity.this).load(imageProfile).into(binding.imageProfile);

        }).addOnFailureListener(e -> {

            Toast.makeText(getApplicationContext(), "There is a problem..", Toast.LENGTH_SHORT).show();
        });
    }
}