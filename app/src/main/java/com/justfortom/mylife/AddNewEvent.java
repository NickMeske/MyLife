package com.justfortom.mylife;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AddNewEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<String> eventTypes = new ArrayList<>();
        eventTypes.add(getString(R.string.event_type_bluetooth));
        eventTypes.add(getString(R.string.event_type_location_enter));

        final ListView lvw = (ListView) findViewById(R.id.lvwEventTypes);
        AndroidHelper.AddItemsToList(this, lvw, eventTypes);
        lvw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String entry = lvw.getItemAtPosition(position).toString();

                if (entry.equals(getString(R.string.event_type_bluetooth))) {
                    Intent intent = new Intent(getApplicationContext(), BluetoothConnectionSetup.class);
                    intent.putExtra(getString(R.string.extra_event_type), getString(R.string.event_type_bluetooth));
                    startActivity(intent);
                } else if (entry.equals(getString(R.string.event_type_location_enter))) {
                    Intent intent = new Intent(getApplicationContext(), LocationEventSetup.class);
                    intent.putExtra(getString(R.string.extra_event_type), getString(R.string.event_type_location_enter));
                    startActivity(intent);
                }
            }
        });

    }

}
