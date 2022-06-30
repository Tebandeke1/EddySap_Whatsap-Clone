package com.tabutech.eddysap.View.Activities.Profile;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tabutech.eddysap.BuildConfig;
import com.tabutech.eddysap.Common.Common;
import com.tabutech.eddysap.R;
import com.tabutech.eddysap.View.Activities.Display.ViewProfileImageActivity;
import com.tabutech.eddysap.View.Activities.starUp.WelcomeScreenActivity;
import com.tabutech.eddysap.databinding.ActivityProfileBinding;

import java.io.File;
import java.io.IOException;
import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    private FirebaseFirestore firestore;
    private FirebaseUser firebaseUser;
    private BottomSheetDialog bottomSheetDialog,bottomSheetDialogName;
    private Uri image;

    private ProgressDialog dialog;

    private static int GAlLERY_REQUEST_CODE =2222;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_profile);

        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        dialog = new ProgressDialog(this);

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        if (firebaseUser != null){
            getData();
        }
        initActionClick();

        initNameLayout();
    }

    private void initNameLayout() {

        binding.editNameLayout.setOnClickListener(v -> showLayoutEdit());
    }

    private void showLayoutEdit() {

        bottomSheetDialogName = new BottomSheetDialog(this);

        View view =  getLayoutInflater().inflate(R.layout.bottom_sheet_edit_name,null);

        EditText nameTxt = view.findViewById(R.id.enter_name);
        view.findViewById(R.id.btn_save).setOnClickListener( v->{

            if (TextUtils.isEmpty(nameTxt.getText().toString())){
                Toast.makeText(getApplicationContext(), "Make sure name field is not empty.", Toast.LENGTH_SHORT).show();
            }else {
                doUpdate(nameTxt.getText().toString());
                bottomSheetDialogName.dismiss();
               // Toast.makeText(getApplicationContext(), , Toast.LENGTH_SHORT).show();
            }

        });

        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> bottomSheetDialogName.dismiss());

        bottomSheetDialogName.setContentView(view);
        Objects.requireNonNull(bottomSheetDialogName.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        bottomSheetDialogName.setOnDismissListener(dialogInterface -> bottomSheetDialogName = null);

        bottomSheetDialogName.show();
    }
    private void doUpdate(String name) {
        dialog.setMessage("Please wait.....");
        dialog.show();
        firestore.collection("Users").document(firebaseUser.getUid()).update("userName",name)
                .addOnSuccessListener(unused -> {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Name updated successfully", Toast.LENGTH_SHORT).show();
                    getData();
                });

    }

    private void initActionClick() {
        binding.fabCamera.setOnClickListener(view -> showButtonPickPhoto());

        binding.imageProfile.setOnClickListener(v ->{

            binding.imageProfile.invalidate();
            Drawable dr = binding.imageProfile.getDrawable();
            Common.IMAGE_BITMAP = ((BitmapDrawable)dr.getCurrent()).getBitmap();
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    ProfileActivity.this,binding.imageProfile,"image");
            Intent intent = new Intent(ProfileActivity.this, ViewProfileImageActivity.class);
            startActivity(intent,compat.toBundle());
        });

        binding.signOut.setOnClickListener(v -> signOut());
    }

    private void signOut() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);

        builder.setTitle("SignOut Dialog!")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    dialogInterface.cancel();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(ProfileActivity.this, WelcomeScreenActivity.class));
                    finish();

                })
                .setNegativeButton("No",((dialogInterface, i) -> dialogInterface.cancel()));

        AlertDialog dialog = builder.create();
        dialog.show();
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
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFile = "IMG_"+timeStamp+".jpg";

        try {
            File file = File.createTempFile("IMG_"+timeStamp,".jpg",
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES));
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

            uploadToFireBase();

        }
        if (requestCode == 400 & resultCode == RESULT_OK){
            uploadToFireBase();
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

                dialog.dismiss();
                firestore.collection("Users").document(firebaseUser.getUid()).update(hashMap)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(getApplicationContext(), "Image uploaded successfully.", Toast.LENGTH_SHORT).show();

                            getData();
                        }).addOnFailureListener(e -> {

                    Toast.makeText(getApplicationContext(), "upload failed.", Toast.LENGTH_SHORT).show();
                        });
            }).addOnFailureListener(e -> {

                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Upload failed.", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void getData() {
        firestore.collection("Users").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String name = documentSnapshot.getString("userName");
                String phone = documentSnapshot.getString("userPhone");
                String imageProfile = documentSnapshot.getString("imageProfile");

                binding.tvName.setText(name);
                binding.tvNumber.setText(phone);
                Glide.with(ProfileActivity.this).load(imageProfile).into(binding.imageProfile);
            }
        }).addOnFailureListener(e -> {

        });
    }
}