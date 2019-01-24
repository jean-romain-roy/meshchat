package com.example.meshchat2;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import static com.example.meshchat2.Constants.*;

public class Fragment_Convo extends Fragment {

    // Parent
    private Activity_Main myActivity;

    // Layout Views
    private TextView textViewStatus;
    private ListView listViewConvo;
    private EditText editTextMessage;
    private Button buttonSend;

    // Name of the connected device
    private String connectedDeviceName = null;
    private String connectedDeviceMAC = null;

    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_convo, container, false);

        // Parent
        myActivity = ((Activity_Main) getActivity());

        // UI Init
        textViewStatus = rootView.findViewById(R.id.textview_status);
        buttonSend = rootView.findViewById(R.id.button_send);
        listViewConvo = rootView.findViewById(R.id.listview_convo);
        editTextMessage = rootView.findViewById(R.id.edittext_message);

        // Get the device MAC address
        connectedDeviceMAC = myActivity.targetMAC;

        // Start
        setupChat();

        return rootView;
    }

    private void setupChat() {

        // Initialize the array adapter for the conversation thread
        listViewConvo.setAdapter(myActivity.conversationArrayAdapter);

        // Initialize the send button with a listener that for click events
        buttonSend.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                String message = editTextMessage.getText().toString();
                sendMessage(message);
            }
        });

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer();
    }


    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (myActivity.bluetoothChatService.getState() != STATE_CONNECTED) {
            myActivity.toast(getString(R.string.not_connected));
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            myActivity.bluetoothChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            editTextMessage.setText(mOutStringBuffer);
        }
    }

}