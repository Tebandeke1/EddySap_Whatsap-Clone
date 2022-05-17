package com.tabutech.eddysap.Adaptor;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tabutech.eddysap.Model.ChatList;
import com.tabutech.eddysap.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdaptor extends RecyclerView.Adapter<ChatListAdaptor.Holder> {

    private List<ChatList> list;
    private Context context;

    public ChatListAdaptor(List<ChatList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatListAdaptor.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_chart_list,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdaptor.Holder holder, int position) {

        ChatList chatList = list.get(position);

        holder.description.setText(chatList.getDescription());
        holder.date.setText(chatList.getDate());
        holder.name.setText(chatList.getUserName());

        //for this we needed a library to process the image
        Glide.with(context).load(chatList.getProfileImage()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private TextView date,name,description;
        private CircleImageView imageView;
        public Holder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.profile_image);
            date = itemView.findViewById(R.id.last_seen);
            name = itemView.findViewById(R.id.userName);
            description = itemView.findViewById(R.id.meso_desc);

        }
    }
}
