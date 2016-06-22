package com.justfortom.mylife;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AddNewEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            ListExistingEvents();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG);
        }

    }

    private void ListExistingEvents() throws Exception {
        Database myDB = new Database(getApplicationContext());
        List<Event> events = Event.FindAll(myDB);
        List<String> eventNames = new ArrayList<>();

        for (Event event : events) {
            eventNames.add(event.eventName);
        }

        ListView view = (ListView) findViewById(R.id.lvwExistingEvents);
        AndroidHelper.AddItemsToCheckboxList(getApplicationContext(), view, eventNames);

    }
}
