package com.tabutech.eddysap.menu;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tabutech.eddysap.Adaptor.ChatListAdaptor;
import com.tabutech.eddysap.Model.ChatList;
import com.tabutech.eddysap.R;
import com.tabutech.eddysap.databinding.FragmentChatsBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ChatsFragment extends Fragment {

    private static final String TAG = "ChatsFragment";

    public ChatsFragment() {
        // Required empty public constructor
    }
    private FragmentChatsBinding binding;
    private List<ChatList> lists ;

    private ArrayList<String> allUserID;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private FirebaseFirestore firestore;
    private final Handler handler = new Handler();
    private ChatListAdaptor adaptor;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         binding = DataBindingUtil.inflate(inflater,R.layout.fragment_chats, container, false);

         firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
         reference = FirebaseDatabase.getInstance().getReference();
         firestore = FirebaseFirestore.getInstance();

         lists =  new ArrayList<>();
         allUserID = new ArrayList<>();
         adaptor = new ChatListAdaptor(lists,getContext());

         binding.recycleView.setLayoutManager(new LinearLayoutManager(getContext()));

         binding.recycleView.setAdapter(adaptor);


         if (firebaseUser != null){
             getChatList();
         }
         return binding.getRoot();
    }

    private void getChatList() {
//
        binding.progressCircular.setVisibility(View.VISIBLE);

        reference.child("ChatList").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lists.clear();
                allUserID.clear();

                for (DataSnapshot data : snapshot.getChildren()){
                    String userId = Objects.requireNonNull(Objects.requireNonNull(data.child("chatId").getValue()).toString());
                    Toast.makeText(getContext(), firebaseUser.getUid(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Message:"+userId);
                    binding.progressCircular.setVisibility(View.GONE);


                    allUserID.add(userId);

                }

                getUserData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, error.getMessage());


            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getUserData( ) {

        handler.post(() -> {
            for (String uId : allUserID) {
                firestore.collection("Users").document(uId).get()
                        .addOnSuccessListener(documentSnapshot -> {

                            try{
                                ChatList list = new ChatList(
                                        documentSnapshot.getString("userId")
                                        , documentSnapshot.getString("userName")
                                        , "This is description"
                                        , ""
                                        , documentSnapshot.getString("imageProfile"));

                                lists.add(list);
                            }catch (Exception e){
                                e.getStackTrace();
                            }
                            if (adaptor != null){
                                adaptor.notifyItemInserted(0);
                                adaptor.notifyDataSetChanged();
                            }

                        });
            }
        });

    }


}