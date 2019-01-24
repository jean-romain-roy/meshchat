
package com.example.meshchat2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import classes.Contact;
import prompts.OneInput;


public class Fragment_AddDevice extends Fragment {

    // Parent
    private Activity_Main myActivity;

    // UI Init
    private ListView pairedListView;
    private ListView newDevicesListView;
    private TextView textViewNew;
    private Button buttonScan, buttonAvailable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_adddevice, container, false);

        // Parent
        myActivity = ((Activity_Main) getActivity());

        // UI Init
        pairedListView = rootView.findViewById(R.id.listview_paired);
        newDevicesListView = rootView.findViewById(R.id.listview_new);
        textViewNew = rootView.findViewById(R.id.textview_new);
        buttonScan = rootView.findViewById(R.id.button_scan);
        buttonAvailable = rootView.findViewById(R.id.button_available);

        newDevicesListView.setVisibility(View.GONE);
        textViewNew.setVisibility(View.GONE);

        // Initialize the button to perform device discovery
        buttonScan.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                makeVisible();
                myActivity.doDiscovery();
                v.setEnabled(false);
            }
        });

        buttonAvailable.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ensure this device is discoverable by others
                myActivity.ensureDiscoverable();
            }
        });

        // Find and set up the ListView for paired devices
        pairedListView.setAdapter(myActivity.mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // Find and set up the ListView for newly discovered devices
        newDevicesListView.setAdapter(myActivity.mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        return rootView;
    }

    private void makeVisible() {
        newDevicesListView.setVisibility(View.VISIBLE);
        textViewNew.setVisibility(View.VISIBLE);
    }


    // The on-click listener for all devices in the ListViews
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

            // Cancel discovery because it's costly and we're about to connect
            myActivity.bluetoothAdapter.cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            final String deviceMAC = info.substring(info.length() - 17);
            final String deviceName = info.substring(0,info.length() - 17).trim();

            new OneInput(myActivity, "Add Contact", "Choose a name for " + deviceName, new OneInput.RESPONSE() {
                @Override
                public void text_returned(String name) {
                    if(name != null){
                        if(!name.isEmpty()){
                            Contact newContact = new Contact(name,deviceMAC,deviceName);
                            myActivity.toast(newContact.toString());
                        }
                    }
                }
            });

        }
    };

}
