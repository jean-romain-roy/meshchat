package com.example.meshchat2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

import static com.example.meshchat2.Constants.*;

public class Activity_Main extends AppCompatActivity {

    // UI
    private FrameLayout fragmentContainer;

    // Local Bluetooth adapter
    public BluetoothAdapter bluetoothAdapter = null;

    // Member object for the chat services
    public BluetoothChatService bluetoothChatService = null;

    // Navigation
    private boolean inConvo = false;
    private boolean inForum = false;
    private boolean inAddDevice = false;


    // Set of currently paired devices
    public Set<BluetoothDevice> pairedDevices;

    // Member fields
    public ArrayAdapter<String> mPairedDevicesArrayAdapter;
    public ArrayAdapter<String> mNewDevicesArrayAdapter;

    // BT
    public HashSet devicesMAC;

    // Variables
    public String targetMAC;
    public String targetName;

    // Array adapter for the conversation thread
    public ArrayAdapter<String> conversationArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI
        fragmentContainer = findViewById(R.id.fragmentContainer);

        // Get local Bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        // Get a set of currently paired devices
        pairedDevices = bluetoothAdapter.getBondedDevices();

        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String noDevices = getResources().getText(R.string.none_paired).toString();
            mPairedDevicesArrayAdapter.add(noDevices);
        }

        startForum();
    }

    private void startForum(){

        inForum = true;
        inAddDevice = false;
        inConvo = false;

        if(bluetoothChatService != null) {
            bluetoothChatService.stop();
            bluetoothChatService = null;
        }

        Fragment forumFragment = new Fragment_Forum();
        openFragment(forumFragment);
    }

    public void startAddDevice() {

        inForum = false;
        inAddDevice = true;
        inConvo = false;

        Fragment addDeviceFragment = new Fragment_AddDevice();
        openFragment(addDeviceFragment);
    }

    public void startConvo(){

        inForum = false;
        inAddDevice = false;
        inConvo = true;


        // Initialize the BluetoothChatService to perform bluetooth connections
        bluetoothChatService = new BluetoothChatService(this, mHandler);

        // Init conversation array
        conversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);

        // Get the BLuetoothDevice object
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(targetMAC);

        // Attempt to connect to the device
        bluetoothChatService.connect(device);

        Fragment convoFragment = new Fragment_Convo();
        openFragment(convoFragment);
    }

    private void openFragment(final Fragment myFragment){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentContainer.getId(), myFragment);
        transaction.commit();
    }

    public void ensureDiscoverable() {
        if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 500);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    public void doDiscovery() {

        mNewDevicesArrayAdapter.clear();
        devicesMAC = new HashSet();

        // If we're already discovering, stop it
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }

        int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

        // Request discover from BluetoothAdapter
        bluetoothAdapter.startDiscovery();
    }

    @Override
    public void onStart() {
        super.onStart();

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (bluetoothChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (bluetoothChatService.getState() == STATE_NONE) {
                // Start the Bluetooth chat services
                bluetoothChatService.start();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Stop the Bluetooth chat services
        if (bluetoothChatService != null){
            bluetoothChatService.stop();
        }

        // Make sure we're not doing discovery anymore
        if (bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }

    // The Handler that gets information back from the BluetoothChatService
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:

                    switch (msg.arg1) {
                        case STATE_CONNECTED:
                            toast(getString(R.string.title_connected_to) + " " + targetName);
                            break;
                        case STATE_CONNECTING:
                            toast(getString(R.string.title_connecting));
                            break;
                        case STATE_LISTEN:
                        case STATE_NONE:
                            toast(getString(R.string.title_not_connected));
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    conversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    conversationArrayAdapter.add(targetName+":  " + readMessage);
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    targetName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to "
                            + targetName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {

                    if(devicesMAC != null){
                        if(!devicesMAC.contains(device.getAddress())){
                            devicesMAC.add(device.getAddress());
                            mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                        }
                    }

                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    toast(noDevices);
                }
            }
        }
    };


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    toast("Bluetooth Enabled!");

                } else {
                    // User did not enable Bluetooth or an error occured
                    toast("Bluetooth Disabled!");
                    finish();
                }
        }
    };

    public void toast(final String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if(inConvo){
            startForum();
        }else if(inAddDevice){
            startForum();
        }
    }


}
