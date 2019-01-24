package com.example.meshchat2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment_Forum extends Fragment {

    // Parent
    Activity_Main myActivity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forum, container, false);

        // Parent
        myActivity = ((Activity_Main) getActivity());

        FloatingActionButton buttonAdd = rootView.findViewById(R.id.button_add);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myActivity.startAddDevice();
            }
        });


        return rootView;
    }
}
