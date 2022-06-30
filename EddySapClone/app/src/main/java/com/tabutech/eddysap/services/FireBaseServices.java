package com.tabutech.eddysap.services;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tabutech.eddysap.Model.StatusModel;

import java.util.HashMap;


public class FireBaseServices {

    private Context context;

    public FireBaseServices(Context context){
        this.context = context;
    }

    public void uploadSendImageToFireBase(Uri uri,OnCallBack onCallBack){

        StorageReference reference = FirebaseStorage.getInstance().getReference().child("ChatImages/"+
                System.currentTimeMillis()+"."+getFileExtension(uri));

        reference.putFile(uri).addOnSuccessListener(taskSnapshot -> {

            Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
            while (!task.isSuccessful());
            Uri download = task.getResult();

            final String download_urls = String.valueOf(download);


            onCallBack.onUpLoadSuccess(download_urls);


        }).addOnFailureListener(onCallBack::onUpLoadFailed);

    }

    private String getFileExtension(Uri uri){
        ContentResolver resolver = context.getContentResolver();
        MimeTypeMap typeMap = MimeTypeMap.getSingleton();
        return  typeMap.getExtensionFromMimeType(resolver.getType(uri));
    }

    public void addNewStatus(StatusModel model, OnNewStatusCallBack onNewStatusCallBack) {

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("Status Daily").document(model.getId()).set(model).addOnSuccessListener(unused -> onNewStatusCallBack.onSuccess()).addOnFailureListener(e -> onNewStatusCallBack.onFailed());
    }

    public interface OnCallBack{
        void onUpLoadSuccess(String imageUri);
        void onUpLoadFailed(Exception e);
    }

    public interface OnNewStatusCallBack{
        void onSuccess();
        void onFailed();
    }
}
