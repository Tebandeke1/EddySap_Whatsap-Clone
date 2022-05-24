package com.tabutech.eddysap.View.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tabutech.eddysap.Model.Users.Users;
import com.tabutech.eddysap.R;
import com.tabutech.eddysap.View.MainActivity;
import com.tabutech.eddysap.databinding.ActivitySetUserInfoBinding;

public class SetUserInfoActivity extends AppCompatActivity {

private ActivitySetUserInfoBinding binding;
private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_set_user_info);

        dialog = new ProgressDialog(this);
        initNextButton();

    }

    private void initNextButton() {
        binding.btnProfile.setOnClickListener( v ->{

            if (TextUtils.isEmpty(binding.nameEnter.getText().toString())){
                Toast.makeText(getApplicationContext(), "Please make sure Name is filled.", Toast.LENGTH_SHORT).show();
            }else {
                doUpdate();
            }
        });

        binding.userProImage.setOnClickListener( v -> {

        });
    }

    private void doUpdate() {
        dialog.setMessage("Please wait.....");
        dialog.show();
        FirebaseFirestore restore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){

            String userID = user.getUid();

            Users users = new Users(userID,
                    binding.nameEnter.getText().toString().trim(),
                    user.getPhoneNumber(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "");
            restore.collection("Users").document(user.getUid()).set(users)
                    //.update("userName",binding.nameEnter.getText().toString().trim())
                    .addOnSuccessListener(unused -> {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Update Successful.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Update failed....", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Please sign in first.", Toast.LENGTH_SHORT).show();
        }
    }
}