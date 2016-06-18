package com.justfortom.mylife;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BluetoothConnectionSetup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connection_setup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            BluetoothHelper myBluetooth = new BluetoothHelper();
            ListView bluetoothItems = (ListView) findViewById(R.id.listBluetoothDevices);
            AndroidHelper.AddItemsToCheckboxList(this, bluetoothItems, myBluetooth.GetDevices());
        } catch (Exception ex) {
            Snackbar.make(this.findViewById(R.id.listBluetoothDevices), "Unable to retrieve list of Bluetooth Devices", Snackbar.LENGTH_INDEFINITE).show();
            //Toast.makeText(BluetoothConnectionSetup.this, "Unable to retrieve list of bluetooth devices", Toast.LENGTH_SHORT).show();
        }


//        AndroidHelper.AddItemsToList(this, bluetoothItems,stuff);
//        AndroidHelper.CheckBoxList(getApplicationContext(), bluetoothItems, null);
    }

}
