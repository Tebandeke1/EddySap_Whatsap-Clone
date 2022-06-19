package com.tabutech.eddysap.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tabutech.eddysap.Model.Chats.Chat;
import com.tabutech.eddysap.R;

import java.util.List;


public class ChatsAdaptor extends RecyclerView.Adapter<ChatsAdaptor.ViewHolder> {

    private List<Chat> chatList;
    private Context context;

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    private FirebaseUser firebaseUser;

    public ChatsAdaptor(List<Chat> chatList, Context context) {
        this.chatList = chatList;
        this.context = context;
    }
    public void setList(List<Chat> list){
        this.chatList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatsAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_TYPE_LEFT) {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsAdaptor.ViewHolder holder, int position) {

        holder.bind(chatList.get(position));
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textMessage;
        private LinearLayout layoutText,layoutImage;
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.tv_text_message);
            layoutText = itemView.findViewById(R.id.layout_text);
            layoutImage = itemView.findViewById(R.id.layout_image);
            imageView = itemView.findViewById(R.id.chat_image);
        }
        void bind(Chat chat){

            switch (chat.getType()){
                case "TYPE":

                    layoutText.setVisibility(View.VISIBLE);
                    layoutImage.setVisibility(View.GONE);

                    textMessage.setText(chat.getTextMessage());
                    break;
                case "IMAGE":

                    layoutText.setVisibility(View.GONE);
                    layoutImage.setVisibility(View.VISIBLE);

                    Glide.with(context).load(chat.getUri()).into(imageView);
                    break;
            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (chatList.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}
