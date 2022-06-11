package com.tabutech.eddysap.View.Activities.Contact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.tabutech.eddysap.Adaptor.ContactAdaptor;
import com.tabutech.eddysap.Model.Users.Users;
import com.tabutech.eddysap.R;
import com.tabutech.eddysap.databinding.ActivityContactBinding;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity {

    private ActivityContactBinding binding;
    private ContactAdaptor adaptor;
    private FirebaseFirestore firestore;
    private FirebaseUser user;
    private List<Users> users = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_contact);

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        user = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        if (user != null){
            getContactList();

        }

    }

    private void getContactList() {

        firestore.collection("Users").get().addOnSuccessListener(queryDocumentSnapshots -> {

            for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                String  id = snapshot.getString("userId");
                String name = snapshot.getString("userName");
                String image = snapshot.getString("imageProfile");
                String bio = snapshot.getString("bio");

                Users users1 = new Users();
                users1.setBio(bio);
                users1.setUserId(id);
                users1.setImageProfile(image);
                users1.setUserName(name);


                if (id != null && !id.equals(user.getUid())){
                    users.add(users1);
                }

            }
            adaptor = new ContactAdaptor(users,ContactActivity.this);

            binding.recycleView.setAdapter(adaptor);


            if ( users.size() == 1){
                binding.numberOfPeople.setVisibility(View.VISIBLE);
                binding.numberOfPeople.setText(users.size()+" Contact");

            }else if( users.size() != 0 && users.size() > 1){
                binding.numberOfPeople.setVisibility(View.VISIBLE);
                binding.numberOfPeople.setText(users.size()+" Contacts");
            }
        });
    }
}