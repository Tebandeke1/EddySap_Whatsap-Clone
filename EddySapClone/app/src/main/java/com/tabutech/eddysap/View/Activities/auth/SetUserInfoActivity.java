package com.tabutech.eddysap.View.Activities.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tabutech.eddysap.BuildConfig;
import com.tabutech.eddysap.Model.Users.Users;
import com.tabutech.eddysap.R;
import com.tabutech.eddysap.View.Activities.Chat.ChatsActivity;
import com.tabutech.eddysap.View.MainActivity;
import com.tabutech.eddysap.databinding.ActivitySetUserInfoBinding;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class SetUserInfoActivity extends AppCompatActivity {

private ActivitySetUserInfoBinding binding;
private ProgressDialog dialog;
    private BottomSheetDialog bottomSheetDialog;
    private Uri image;
    private int GAlLERY_REQUEST_CODE = 555;
    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_set_user_info);

        //check, is a user new or not
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        db.collection("Users").document(user.getUid())
                .get().addOnCompleteListener(task -> {
                    binding.nameEnter.setText(task.getResult().getString("userName"));
                    Glide.with(SetUserInfoActivity.this)
                            .load(task.getResult().getString("imageProfile")).into(binding.userProImage);
                });


        dialog = new ProgressDialog(this);
        initNextButton();

    }

    private void initNextButton() {
        binding.btnProfile.setOnClickListener( v ->{

            if (TextUtils.isEmpty(binding.nameEnter.getText().toString())){
                Toast.makeText(getApplicationContext(), "Please make sure Name is filled.", Toast.LENGTH_SHORT).show();
            }else {
                uploadToFireBase();
                startActivity(new Intent(SetUserInfoActivity.this, MainActivity.class));
                finish();
            }
        });

        binding.userProImage.setOnClickListener( v -> {
            showButtonPickPhoto();
        });
    }

    private void showButtonPickPhoto() {

        bottomSheetDialog = new BottomSheetDialog(this);

        View view = getLayoutInflater().inflate(R.layout.button_sheet_pick,null);

        view.findViewById(R.id.gallery).setOnClickListener(v ->{

            bottomSheetDialog.dismiss();
            openGallery();
        });

        view.findViewById(R.id.camera_profile).setOnClickListener( v ->{

            checkCameraPermissions();
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(view);

        Objects.requireNonNull(bottomSheetDialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        bottomSheetDialog.setOnDismissListener(dialogInterface -> bottomSheetDialog = null);

        bottomSheetDialog.show();
    }

    private void checkCameraPermissions() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},221);
        }else if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},222);
        }else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp = new SimpleDateFormat("yyyyMMDD_HHmmss", Locale.getDefault()).format(new Date());
        String imageFile = "IMG_"+timeStamp+".jpg";

        try {
            File file = File.createTempFile(imageFile, String.valueOf(getExternalFilesDir(Environment.DIRECTORY_PICTURES)));
            image = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+".provider",file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,image);
            intent.putExtra("listPhotoName",imageFile);
            startActivityForResult(intent,400);



        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private  void openGallery() {

        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select image"),GAlLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GAlLERY_REQUEST_CODE
                & resultCode == RESULT_OK
                &data != null
                & (data != null ? data.getData() : null) != null){

            image = data.getData();
            Glide.with(SetUserInfoActivity.this).load(image).into(binding.userProImage);
            //uploadToFireBase();

        }
        if (requestCode == 400 & resultCode == RESULT_OK){
            Glide.with(SetUserInfoActivity.this).load(image).into(binding.userProImage);
            //uploadToFireBase();
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),image);
//
//                binding.imageProfile.setImageBitmap(bitmap);
//            }catch (Exception e){
//                e.getMessage();
//            }
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver resolver = getContentResolver();
        MimeTypeMap typeMap = MimeTypeMap.getSingleton();
        return  typeMap.getExtensionFromMimeType(resolver.getType(uri));
    }

    private void uploadToFireBase() {

        if (image != null){
            dialog.setMessage("Loading.....");
            //dialog.show();

            StorageReference reference = FirebaseStorage.getInstance().getReference().child("ImagesProfiles/"+
                    System.currentTimeMillis()+"."+getFileExtension(image));

            reference.putFile(image).addOnSuccessListener(taskSnapshot -> {

                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                while (!task.isSuccessful());
                Uri download = task.getResult();

                final String download_urls = String.valueOf(download);

                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("imageProfile",download_urls);
                hashMap.put("userName",binding.nameEnter.getText().toString());

                dialog.dismiss();
                db.collection("Users").document(user.getUid()).update(hashMap)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(getApplicationContext(), "Information uploaded successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();

                            //getData();
                        }).addOnFailureListener(e -> {

                    Toast.makeText(getApplicationContext(), "upload failed.", Toast.LENGTH_SHORT).show();
                });
            }).addOnFailureListener(e -> {

                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Upload failed.", Toast.LENGTH_SHORT).show();
            });
        }
    }

}