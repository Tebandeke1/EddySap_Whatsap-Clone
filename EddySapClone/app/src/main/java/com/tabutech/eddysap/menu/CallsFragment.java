package com.tabutech.eddysap.menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tabutech.eddysap.Adaptor.CallListAdaptor;
import com.tabutech.eddysap.Model.CallList;
import com.tabutech.eddysap.R;

import java.util.ArrayList;
import java.util.List;


public class CallsFragment extends Fragment {

    private RecyclerView recyclerView;
    List<CallList> lists = new ArrayList<>();
    public CallsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_calls, container, false);

        recyclerView = view.findViewById(R.id.recycleView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        getData();
        return view;
    }

    private void getData() {

        lists.add(new CallList("1","Bobi wine","23/02/2022 11:20am","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTThLlO--CKV-TsRKKt9hGdbZTRzB_xSP4YLQ&usqp=CAU","made"));
        lists.add(new CallList("2","Spice Diana","30/05/2022 10:40pm","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRsy3FoWorbAhpKDxM3OKQjwew0LwvhYvlLxg&usqp=CAU","missed"));
        lists.add(new CallList("3","Eddy Kenzo","24/04/2022 06:34pm","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQOzTRDtVlQ6A055Y8NAlTViBiDcb5_vhDDvw&usqp=CAU","received"));

        recyclerView.setAdapter(new CallListAdaptor(lists,getContext()));
    }
}