package com.tabutech.eddysap.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tabutech.eddysap.Model.Users.Users;
import com.tabutech.eddysap.R;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdaptor extends RecyclerView.Adapter<ContactAdaptor.ViewHolder> {

    private List<Users> users;
    private Context context;

    public ContactAdaptor(List<Users> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_contact_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Users user = users.get(position);

        holder.userName.setText(user.getUserName());
        holder.description.setText(user.getBio());

        Glide.with(context).load(user.getImageProfile()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView imageView;
        private final TextView userName;
        private final TextView description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.profile_image);
            userName =  itemView.findViewById(R.id.tv_name);
            description = itemView.findViewById(R.id.tv_desc);
        }
    }
}
