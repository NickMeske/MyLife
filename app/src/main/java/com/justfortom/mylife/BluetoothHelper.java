package com.justfortom.mylife;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by nmesk on 6/18/2016.
 */
public class BluetoothHelper {
    private BluetoothAdapter btAdapter;

    public BluetoothHelper() {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public List<String> GetDevices() {
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();

        List<String> s = new ArrayList<String>();
        for (BluetoothDevice bt : pairedDevices)
            s.add(bt.getName());

        return s;
    }
}
