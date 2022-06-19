package com.tabutech.eddysap.View.Activities.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityOptionsCompat;

import com.bumptech.glide.Glide;
import com.tabutech.eddysap.Common.Common;
import com.tabutech.eddysap.Model.ChatList;
import com.tabutech.eddysap.R;
import com.tabutech.eddysap.View.Activities.Chat.ChatsActivity;
import com.tabutech.eddysap.View.Activities.Display.ViewProfileImageActivity;
import com.tabutech.eddysap.View.Activities.Profile.ProfileActivity;
import com.tabutech.eddysap.View.Activities.Profile.UserProfileActivity;
import com.tabutech.eddysap.interfaces.OnCallBack;

public class DialogViewUser {
    private Context context;
    private Dialog dialog;

    public DialogViewUser(Context context ,ChatList chatList) {
        this.context = context;
        initialize(chatList);
    }

    public void initialize(ChatList list){
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        dialog.setContentView(R.layout.dialog_view_user);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);

        ImageButton btnChat,btnCall,btnVideo,btnReport;
        ImageView profile;
        TextView userName;

        btnChat = dialog.findViewById(R.id.btn_chat);
        btnCall = dialog.findViewById(R.id.btn_call);
        btnVideo = dialog.findViewById(R.id.btn_video);
        btnReport = dialog.findViewById(R.id.info);

        profile = dialog.findViewById(R.id.image_profile);
        userName = dialog.findViewById(R.id.userName);

        userName.setText(list.getUserName());
        Glide.with(context).load(list.getProfileImage()).into(profile);


        btnChat.setOnClickListener(v ->{
            context.startActivity(new Intent(context, ChatsActivity.class)
                    .putExtra("userId",list.getUserId())
                    .putExtra("userName",list.getUserName())
                    .putExtra("userProfile",list.getProfileImage()));
            dialog.dismiss();
        });

        btnCall.setOnClickListener(v ->{
            Toast.makeText(context, "Call Clicked", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        btnVideo.setOnClickListener(v ->{
            Toast.makeText(context, "Video Clicked", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        btnReport.setOnClickListener(v ->{

            context.startActivity(new Intent(context, UserProfileActivity.class)
                    .putExtra("userId",list.getUserId())
                    .putExtra("userProfile",list.getProfileImage())
                    .putExtra("userName",list.getUserName()));
            dialog.dismiss();
        });

        profile.setOnClickListener(v ->{
            profile.invalidate();
            Drawable dr = profile.getDrawable();
            Common.IMAGE_BITMAP = ((BitmapDrawable)dr.getCurrent()).getBitmap();
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    (Activity) context,profile,"image");
            Intent intent = new Intent(context, ViewProfileImageActivity.class);
            context.startActivity(intent,compat.toBundle());
        });
    }

    public void show(){
        dialog.show();

    }
}
