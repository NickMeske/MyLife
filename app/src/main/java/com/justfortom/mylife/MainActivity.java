package com.justfortom.mylife;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import java.util.ArrayList;
import java.util.Objects;


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
        db = new Database(getApplicationContext());
        db.CreateSchema();
//        starterThing = new Starter();

        //add options to home screen
        ArrayList<String> options = new ArrayList<>();
        options.add("Add Events To Watch");
        options.add("Reset all Events");
        options.add("Rate this App");

        final ListView myList = (ListView) findViewById(R.id.mainMenuOptions);
        AndroidHelper.AddItemsToList(this, myList, options);

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String entry = myList.getItemAtPosition(position).toString();

                if (entry.equals("Add Events To Watch")) {
                    Intent intent = new Intent(getApplicationContext(), AddNewEvent.class);
                    startActivity(intent);
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
