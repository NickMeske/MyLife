package com.justfortom.mylife;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

public class TriggerSetup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger_setup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final RadioButton bluetoothButton = (RadioButton) findViewById(R.id.radBluetooth);
        final RadioButton locationButton = (RadioButton) findViewById(R.id.radLocation);

        bluetoothButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LinearLayout myLayout = (LinearLayout) findViewById(R.id.layoutTrigger);

                if (bluetoothButton.isChecked()) {
                    ShowBluetooth();
                } else if (locationButton.isChecked()) {
                    ShowLocation();
                }
            }
        });


        LinearLayout myLayout = (LinearLayout) findViewById(R.id.layoutTrigger);
        ShowBluetooth();
    }


    private void ShowBluetooth() {


        BluetoothHelper btHelper = new BluetoothHelper();
        List<String> btItems = btHelper.GetDevices();

        ListView list = (ListView) findViewById(R.id.listOptions);
        AndroidHelper.AddItemsToCheckboxList(TriggerSetup.this, list, btItems);

        //myLayout.addView(list);
    }

    private void ShowLocation() {
        List<String> items = new ArrayList<>();
        items.add("");

        ListView list = (ListView) findViewById(R.id.listOptions);
        AndroidHelper.AddItemsToCheckboxList(TriggerSetup.this, list, items);
    }
}
