package com.tabutech.eddysap.menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tabutech.eddysap.Adaptor.ChatListAdaptor;
import com.tabutech.eddysap.Model.ChatList;
import com.tabutech.eddysap.R;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {


    private List<ChatList> lists = new ArrayList<>();
    private RecyclerView recyclerView;
    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_chats, container, false);

         recyclerView = view.findViewById(R.id.recycleView);

         recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
         getListItems();
         return view;
    }

    private void getListItems() {
        lists.add(new ChatList("","","","",""));
        lists.add(new ChatList("","","","",""));
        lists.add(new ChatList("","","","",""));

        recyclerView.setAdapter(new ChatListAdaptor(lists,getContext()));
    }
}