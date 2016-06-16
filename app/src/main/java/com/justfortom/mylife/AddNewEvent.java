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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<String> eventTypes = new ArrayList<>();
        eventTypes.add("New Location Change Event");
        eventTypes.add("New Bluetooth Connection Event");

        final ListView lvw = (ListView) findViewById(R.id.lvwEventTypes);
        AndroidHelper.AddItemsToList(this, lvw, eventTypes);
        lvw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String entry = lvw.getItemAtPosition(position).toString();

                if (entry.contains("Bluetooth")) {
                    Intent intent = new Intent(getApplicationContext(), EventSetup.class);
                    intent.putExtra("EVENT_TYPE", "BLUETOOTH");
                    startActivity(intent);
                } else if (entry.contains("Location")) {
                    Intent intent = new Intent(getApplicationContext(), EventSetup.class);
                    intent.putExtra("EVENT_TYPE", "LOCATION");
                    startActivity(intent);
                }
            }
        });

    }

}
