package com.justfortom.mylife;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TriggerSetup extends AppCompatActivity {

    public boolean isBluetoothChecked() {
        return bluetoothChecked;
    }

    public void setBluetoothChecked(boolean bluetoothChecked) {
        this.bluetoothChecked = bluetoothChecked;
    }

    private boolean bluetoothChecked;
    private int eventID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger_setup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //need to retrieve the ID of the event
        Intent myIntent = getIntent();
        int eventID = myIntent.getIntExtra("EVENT_ID", -1);
        if (eventID == -1) {
            throw new RuntimeException("The event ID was not passed in. Need to save the event before adding triggers.");
        }
        this.eventID = eventID;

        final RadioButton bluetoothButton = (RadioButton) findViewById(R.id.radBluetooth);
        final RadioButton locationButton = (RadioButton) findViewById(R.id.radLocation);

        bluetoothButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (bluetoothButton.isChecked()) {
                    setBluetoothChecked(true);
                    ShowBluetooth();
                } else if (locationButton.isChecked()) {
                    setBluetoothChecked(false);
                    ShowLocation();
                }
            }
        });

        //show bluetooth option initially
        setBluetoothChecked(true);
        ShowBluetooth();
    }

    private void ShowBluetooth() {

        TextView myView = (TextView) findViewById(R.id.textTriggerInfo);
        myView.setText("Select Devices that this applies to:");

        BluetoothHelper btHelper = new BluetoothHelper();
        List<String> btItems = btHelper.GetDevices();

        ListView list = (ListView) findViewById(R.id.listOptions);
        AndroidHelper.AddItemsToCheckboxList(TriggerSetup.this, list, btItems);

    }

    private void ShowLocation() {
        TextView myView = (TextView) findViewById(R.id.textTriggerInfo);
        myView.setText("Select locations that this applies to:");

        List<String> items = new ArrayList<>();
        items.add("");

        ListView list = (ListView) findViewById(R.id.listOptions);
        AndroidHelper.AddItemsToCheckboxList(TriggerSetup.this, list, items);
    }

    public void SaveTrigger(View view) {
        Database myDB = new Database(getApplicationContext());
        ListView info = (ListView) findViewById(R.id.listOptions);
        long[] checkedPos = info.getCheckedItemIds();
        List<String> items = new ArrayList<>();

        for (long checked : checkedPos) {
            int pos = (int) checked;
            String tempItem = info.getItemAtPosition(pos).toString();
            items.add(tempItem);
        }

        if (items.isEmpty()) {
            Toast.makeText(TriggerSetup.this, "Select at least one item from the list.", Toast.LENGTH_LONG).show();
            return;
        }

        if (isBluetoothChecked()) {
            //save bluetooth trigger
            Event myEvent = Event.Find(eventID, myDB);

            //combine selected items into one string for DB storage
            String triggerInfo = TextUtils.join("|", items);

            myEvent.AddTrigger(myDB, Trigger.TRIGGER_TYPE.BLUETOOTH, triggerInfo);
        } else {
            //save location trigger

        }

    }
}
