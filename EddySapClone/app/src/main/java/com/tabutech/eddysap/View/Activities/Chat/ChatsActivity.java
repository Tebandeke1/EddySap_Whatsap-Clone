package com.tabutech.eddysap.View.Activities.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;

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
import com.tabutech.eddysap.View.Activities.Profile.UserProfileActivity;
import com.tabutech.eddysap.databinding.ActivityChatsBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ChatsActivity extends AppCompatActivity {

    private ActivityChatsBinding binding;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String receiverId;

    private ChatsAdaptor adaptor;
    private List<Chat> chatList;
    private String userProfile;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_chats);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();


        Intent intent = getIntent();
        name = intent.getStringExtra("userName");
        receiverId = intent.getStringExtra("userId");
        userProfile = intent.getStringExtra("userProfile");

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

        initBtnClick();

        chatList = new ArrayList<>();

//        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
//                RecyclerView.VERTICAL,true);
//        layoutManager.setStackFromEnd(true);
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        readChats();

    }

    public void initBtnClick(){

        binding.btnSend.setOnClickListener(v ->{
            if (!TextUtils.isEmpty(binding.editMessage.getText().toString())){
                sendMessage(binding.editMessage.getText().toString());

                binding.editMessage.setText("");
            }
        });

        binding.toolbar.setOnClickListener(v ->{
            startActivity(new Intent(ChatsActivity.this, UserProfileActivity.class)
            .putExtra("userId",receiverId)
            .putExtra("userProfile",userProfile)
            .putExtra("userName",name));
        });

    }

    private void sendMessage(String toString) {

        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd:MM:yyyy");
        String today = format.format(date);

        Calendar calender = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        String time = df.format(calender.getTime());

        Chat chat = new Chat(
                today+","+time
                ,toString
                ,"TYPE"
                ,user.getUid()
                ,receiverId
        );

        reference.child("Chats").push().setValue(chat).addOnSuccessListener(unused -> Log.d("Send","onSuccess")).addOnFailureListener(e -> Log.d("Send",e.getMessage()));

        //add to chatList
        DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference("ChatList").child(user.getUid()).child(receiverId);
        chatRef1.child("chatId").setValue(receiverId);

        DatabaseReference chatRef2 = FirebaseDatabase.getInstance().getReference("ChatList").child(receiverId).child(user.getUid());
        chatRef2.child("chatId").setValue(user.getUid());
    }

    public void readChats(){

        try {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Chats").addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    chatList.clear();
                    for(DataSnapshot snapshot1 : snapshot.getChildren()){

                        Chat chat = snapshot1.getValue(Chat.class);

                        if (chat != null && chat.getSender().equals(user.getUid()) && chat.getReceiver().equals(receiverId)
                                || Objects.requireNonNull(chat).getReceiver().equals(user.getUid()) && chat.getSender().equals(receiverId)
                        ) {
                            chatList.add(chat);
                        }

                    }
                    if (adaptor!= null){
                        adaptor.notifyDataSetChanged();
                    }else {
                        adaptor = new ChatsAdaptor(chatList,ChatsActivity.this);
                        binding.recycleView.setAdapter(adaptor);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            e.getStackTrace();
        }


    }
}