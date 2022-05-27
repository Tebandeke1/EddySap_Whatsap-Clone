package com.tabutech.eddysap.View.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tabutech.eddysap.R;
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