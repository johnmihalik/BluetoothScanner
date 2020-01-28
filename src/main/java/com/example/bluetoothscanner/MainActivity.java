package com.example.bluetoothscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    TextView statusTextView;
    ListView deviceListView;

    ArrayList<String> deviceList;
    ArrayAdapter<String> arrayAdapter;

    // Can't enable or test Bluetooth in the emulator and I do not have an Android phone..
    BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusTextView = findViewById(R.id.statusTextView);
        deviceListView = findViewById(R.id.deviceListView);

        // Check if available
        if (btAdapter == null) {
            statusTextView.setText("Bluetooth NOT Available");
        } else {
            statusTextView.setText("Bluetooth Available");
            btAdapter.startDiscovery();

        }

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);

        registerReceiver(receiver, intentFilter);

        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, deviceList);
        deviceListView.setAdapter(arrayAdapter);

    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                deviceList.add(device.getName() + ", MAC = " + device.getAddress());
                arrayAdapter.notifyDataSetChanged();
            }
        }
    };

    // Toast message
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
