package com.tabutech.eddysap.View.Activities.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tabutech.eddysap.Adaptor.ChatsAdaptor;
import com.tabutech.eddysap.Model.Chats.Chat;
import com.tabutech.eddysap.R;
import com.tabutech.eddysap.View.Activities.Dialog.DialogReviewSendImage;
import com.tabutech.eddysap.View.Activities.Profile.UserProfileActivity;
import com.tabutech.eddysap.databinding.ActivityChatsBinding;
import com.tabutech.eddysap.interfaces.OnReadChatCallBack;
import com.tabutech.eddysap.managers.ChatServices;
import com.tabutech.eddysap.services.FireBaseServices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ChatsActivity extends AppCompatActivity {

    private ActivityChatsBinding binding;
    private String receiverId;

    private ChatsAdaptor adaptor;
    private final List<Chat>  chatList = new ArrayList<>();
    private String userProfile;
    private String name;
    private boolean isActionShown = false;
    private ChatServices chatServices;
    private int GAlLERY_REQUEST_CODE = 2222;
    private Uri image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_chats);

        initialize();
        initBtnClick();
        readChats();

    }

    private void initialize(){
        Intent intent = getIntent();
        name = intent.getStringExtra("userName");
        receiverId = intent.getStringExtra("userId");
        userProfile = intent.getStringExtra("userProfile");

        chatServices = new ChatServices(this,receiverId);

        if (receiverId != null){
            binding.tvUsername.setText(name);

            if (userProfile.equals("")){
                binding.imageProfile.setImageResource(R.drawable.place_holder);//set default image if user has no profile image

            }else{
                Glide.with(this).load(userProfile).into(binding.imageProfile);
            }
        }

        binding.imageBack.setOnClickListener(v -> finish());

        binding.editMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (TextUtils.isEmpty(binding.editMessage.getText().toString()))
                    binding.btnSend.setImageDrawable(getDrawable(R.drawable.keyboard_voice));

                else binding.btnSend.setImageDrawable(getDrawable(R.drawable.send));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

//        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
//                RecyclerView.VERTICAL,true);
//        layoutManager.setStackFromEnd(true);
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        binding.imageAttachment.setOnClickListener(v ->{
            if (isActionShown) {
                binding.cardView.setVisibility(View.VISIBLE);
                isActionShown = false;
            }else {
                binding.cardView.setVisibility(View.GONE);
                isActionShown = true;
            }
        });
        adaptor = new ChatsAdaptor(chatList,this);
        binding.recycleView.setAdapter(adaptor);

        binding.btnGallery.setOnClickListener(v ->{
            openGallery();
        });

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

           // uploadToFireBase();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),image);

                reviewImage(bitmap);
            }catch (Exception e){
                e.getMessage();
            }

        }
    }

    private void reviewImage(Bitmap bitmap){
        new DialogReviewSendImage(ChatsActivity.this,bitmap).show(() -> {
            //to upload image to firebase storage and to get image url

            //hide action button

            binding.action.setVisibility(View.GONE);
            isActionShown = false;
            if (image != null){
                new FireBaseServices(ChatsActivity.this).uploadSendImageToFireBase(image, new FireBaseServices.OnCallBack() {
                    @Override
                    public void onUpLoadSuccess(String imageUri) {
                        //send chat image
                        chatServices.sendImage(imageUri);
                    }

                    @Override
                    public void onUpLoadFailed(Exception e) {
                        e.printStackTrace();
                    }
                });
            }

        });
    }

    private void readChats() {
        chatServices.readChatData(new OnReadChatCallBack() {
            @Override
            public void onReadSuccess(List<Chat> chatList) {

                adaptor.setList(chatList);
            }

            @Override
            public void onReadFailed() {

            }
        });
    }

    public void initBtnClick(){

        binding.btnSend.setOnClickListener(v ->{
            if (!TextUtils.isEmpty(binding.editMessage.getText().toString())){
                chatServices.sendTxtMsg(binding.editMessage.getText().toString());

                binding.editMessage.setText("");
            }
        });

        binding.toolbar.setOnClickListener(v ->{
            startActivity(new Intent(ChatsActivity.this, UserProfileActivity.class)
            .putExtra("userId",receiverId)
            .putExtra("userProfile",userProfile)
            .putExtra("userName",name));
        });

        //initialize
        //binding.recordButton.setRecordView(RecordView)


    }

}