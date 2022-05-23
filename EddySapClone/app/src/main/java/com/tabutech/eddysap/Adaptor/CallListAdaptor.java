package com.tabutech.eddysap.Adaptor;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tabutech.eddysap.Model.CallList;
import com.tabutech.eddysap.Model.ChatList;
import com.tabutech.eddysap.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CallListAdaptor extends RecyclerView.Adapter<CallListAdaptor.Holder> {

    private List<CallList> list;
    private Context context;

    public CallListAdaptor(List<CallList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CallListAdaptor.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_call_list,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallListAdaptor.Holder holder, int position) {

        CallList callList = list.get(position);

        holder.date.setText(callList.getDate());
        holder.name.setText(callList.getUserName());

        if (callList.getCallType().equals("missed")){
            holder.arrow.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_call_missed_24));
            holder.arrow.getDrawable().setTint(context.getResources().getColor(android.R.color.holo_red_dark));
        }else if(callList.getCallType().equals("made")){
            holder.arrow.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_call_made_24));
            holder.arrow.getDrawable().setTint(context.getResources().getColor(android.R.color.holo_green_dark));
        }else{
            holder.arrow.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_call_received_24));
            holder.arrow.getDrawable().setTint(context.getResources().getColor(android.R.color.holo_blue_dark));
        }
        //for this we needed a library to process the image
        Glide.with(context).load(callList.getUserProfile()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private TextView date,name;
        private CircleImageView imageView;
        private ImageView arrow;
        public Holder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.profile_image);
            date = itemView.findViewById(R.id.date);
            name = itemView.findViewById(R.id.userName);
            arrow = itemView.findViewById(R.id.img_arrow);

        }
    }
}
