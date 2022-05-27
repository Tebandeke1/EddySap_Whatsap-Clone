package com.tabutech.eddysap.View.Profile;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tabutech.eddysap.R;
import com.tabutech.eddysap.databinding.ActivityProfileBinding;

import java.util.HashMap;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    private FirebaseFirestore firestore;
    private FirebaseUser firebaseUser;
    private BottomSheetDialog bottomSheetDialog;
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
    }

    private void initActionClick() {
        binding.fabCamera.setOnClickListener(view -> {
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

            Toast.makeText(this, "Camera pressed", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(view);

        Objects.requireNonNull(bottomSheetDialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        bottomSheetDialog.setOnDismissListener(dialogInterface -> bottomSheetDialog = null);

        bottomSheetDialog.show();
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
        if (requestCode == GAlLERY_REQUEST_CODE & resultCode == RESULT_OK &data != null & (data != null ? data.getData() : null) != null){

            image = data.getData();

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