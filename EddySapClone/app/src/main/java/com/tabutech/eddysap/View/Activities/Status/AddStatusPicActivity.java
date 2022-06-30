package com.tabutech.eddysap.View.Activities.Status;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.tabutech.eddysap.Model.StatusModel;
import com.tabutech.eddysap.R;
import com.tabutech.eddysap.View.MainActivity;
import com.tabutech.eddysap.databinding.ActivityAddStatusPicBinding;
import com.tabutech.eddysap.managers.ChatServices;
import com.tabutech.eddysap.services.FireBaseServices;

import java.util.UUID;

public class AddStatusPicActivity extends AppCompatActivity {
    private Uri image;
    private ActivityAddStatusPicBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_status_pic);

        //imageView = findViewById(R.id.image_background);
        image = MainActivity.image;

        setInfo();
        initalize();

    }

    private void initalize() {
        binding.btnBack.setOnClickListener(v -> finish());

        binding.textCaption.setOnClickListener(v -> new FireBaseServices(AddStatusPicActivity.this).uploadSendImageToFireBase(image, new FireBaseServices.OnCallBack() {
            @Override
            public void onUpLoadSuccess(String imageUri) {
                StatusModel model = new StatusModel();
                model.setId(UUID.randomUUID().toString());
                model.setCreatedDate(new ChatServices(AddStatusPicActivity.this).getCurrentDate());
                model.setImageStatus(imageUri);
                model.setUserId(FirebaseAuth.getInstance().getUid());
                model.setViewCount("0");
                model.setTextStatus(binding.textCaption.getText().toString());

                new FireBaseServices(AddStatusPicActivity.this).addNewStatus(model, new FireBaseServices.OnNewStatusCallBack() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }

            @Override
            public void onUpLoadFailed(Exception e) {

            }
        }));
    }

    private void setInfo() {
        Glide.with(this).load(image).into(binding.imageBackground);


    }
}