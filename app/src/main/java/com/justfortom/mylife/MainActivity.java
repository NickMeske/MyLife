package com.justfortom.mylife;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    public static Database db;
    private Starter starterThing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initialize database
        try {
            db = new Database(getApplicationContext());
            db.CreateSchema();
        } catch (Exception ex) {
            Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }


        final List<String> ids1 = new ArrayList<>();
        ids1.add("test1");
        ids1.add("anothertest");

        try {
            //Event newEvent = new Event(db, "TestEventName", new Date(),new Date(),  ids1, ids1);
            //System.out.print(newEvent.eventName);
        } catch (Exception ex) {
            String exc = ex.getMessage();
            Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }


//        starterThing = new Starter();

        //add options to home screen
        ArrayList<String> options = new ArrayList<>();
        options.add(getString(R.string.main_menu_item_add_new_event));
        options.add(getString(R.string.main_menu_item_reset_events));
        options.add(getString(R.string.main_menu_item_rate_app));

        final ListView myList = (ListView) findViewById(R.id.mainMenuOptions);
        AndroidHelper.AddItemsToList(this, myList, options);

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String entry = myList.getItemAtPosition(position).toString();

                if (entry.equals(getString(R.string.main_menu_item_add_new_event))) {
                    Intent intent = new Intent(getApplicationContext(), AddNewEvent.class);
                    startActivity(intent);
                } else if (entry.equals(getString(R.string.main_menu_item_rate_app))) {
                    try {
                        Event myEvent = Event.Find("testEventName", db);
                        Toast.makeText(MainActivity.this, myEvent.eventName, Toast.LENGTH_SHORT).show();
                    } catch (Exception ex) {
                        Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else if (entry.equals(getString(R.string.main_menu_item_reset_events))) {

//                    try {
//                        Event newEvent = new Event(db, "testEventName", new Date(), new Date(), ids1, ids1);
//                    } catch (Exception ex) {
//                        Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
//                    }

                    db.DeleteAll(Database.TABLE_NAME.EVENTS);
                }
                Toast.makeText(MainActivity.this, entry, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    public void Stop(View view) {
//        starterThing.StopListening();
//    }
//
//    public void Start(View view) {
//        starterThing.StartListening();
//    }


}
