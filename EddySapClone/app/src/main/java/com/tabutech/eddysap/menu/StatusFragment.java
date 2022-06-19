package com.tabutech.eddysap.menu;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tabutech.eddysap.R;
import com.tabutech.eddysap.View.Activities.Profile.ProfileActivity;
import com.tabutech.eddysap.databinding.FragmentStatusBinding;

import java.util.Objects;

public class StatusFragment extends Fragment {

    public StatusFragment() {
        // Required empty public constructor
    }

    private FragmentStatusBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      binding = DataBindingUtil.inflate(inflater,R.layout.fragment_status, container, false);

      getProfileImage();
      return binding.getRoot();
    }

    private void getProfileImage(){

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("Users").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String imageProfile = documentSnapshot.getString("imageProfile");

                Glide.with(requireContext()).load(imageProfile).into(binding.profile);
            }
        }).addOnFailureListener(e -> {

        });
    }
}