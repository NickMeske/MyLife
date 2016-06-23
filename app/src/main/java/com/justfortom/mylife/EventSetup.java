package com.justfortom.mylife;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceCategory;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class EventSetup extends AppCompatActivity {

    private Event myEvent;
    private Database myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_setup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDB = new Database(getApplicationContext());
        try {
            Intent myIntent = getIntent();
            Event tempEvent = (Event) myIntent.getSerializableExtra("OBJECT");
            if (tempEvent != null) {
                myEvent = tempEvent;
                EditText editName = (EditText) findViewById(R.id.txtEventName);
                editName.setText(tempEvent.eventName);

            } else {

            }
        } catch (Exception ex) {
            //unable to find an item
        }

    }

    public void SaveEvent(View view) {
        EditText eventNameView = (EditText) findViewById(R.id.txtEventName);
        String eventName = eventNameView.getText().toString();

        if (myEvent == null) {
            //saving a new event
            try {
                myEvent = new Event(myDB, eventName);
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG);
            }
        } else {
            //updating an existing event

        }
    }

}
