package com.example.meshchat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import classes.Conversation;
import listview_adapters.Conversation_List;

public class Fragment_Forum extends Fragment {

    private Conversation_List myListViewAdapter;
    private ListView myListView;

    private ArrayList<Conversation> myConversations_;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forum, container, false);

        // UI Init
        myListView = rootView.findViewById(R.id.list_layout);

        myListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {

                Conversation conversationClicked = myListViewAdapter.getItem(position);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
    }

    private void updateUI(){

        // Build custom adapter
        myListViewAdapter = new Conversation_List(getContext(), myConversations_);

        // Set adapter to the listview
        myListView.setAdapter(myListViewAdapter);
    }
}