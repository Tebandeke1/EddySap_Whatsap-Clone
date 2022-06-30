package com.tabutech.eddysap.managers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tabutech.eddysap.Adaptor.ChatsAdaptor;
import com.tabutech.eddysap.Model.Chats.Chat;
import com.tabutech.eddysap.View.Activities.Chat.ChatsActivity;
import com.tabutech.eddysap.interfaces.OnReadChatCallBack;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ChatServices {

    private Context context;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();
    private String receiverId;

    public ChatServices( Context context , String str){

        this.context = context;
        this.receiverId = str;
    }

    public ChatServices(Context context) {
        this.context = context;
    }

    public void readChatData(OnReadChatCallBack chatCallBack){
        reference.child("Chats").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Chat> chatList = new ArrayList<>();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){

                    Chat chat = snapshot1.getValue(Chat.class);

                    try{
                        if (chat != null && chat.getSender().equals(user.getUid()) && chat.getReceiver().equals(receiverId)
                                || Objects.requireNonNull(chat).getReceiver().equals(user.getUid()) && chat.getSender().equals(receiverId)
                        ) {
                            chatList.add(chat);
                        }
                    }catch (Exception e){e.printStackTrace();}

                }
                chatCallBack.onReadSuccess(chatList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                chatCallBack.onReadFailed();
            }
        });

    }

    public void sendTxtMsg(String text){


        Chat chat = new Chat(
                getCurrentDate()
                ,text,
                ""
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

    public void sendImage(String imageUrl){


        Chat chat = new Chat(
                getCurrentDate()
                ,""
                ,imageUrl,
                "IMAGE"
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
    public String getCurrentDate(){
        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd:MM:yyyy");
        String today = format.format(date);

        Calendar calender = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        String time = df.format(calender.getTime());
        return today+","+time;
    }

}
