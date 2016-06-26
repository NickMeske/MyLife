package com.justfortom.mylife;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import java.util.ArrayList;
import java.util.List;


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
            //schema probably already exists
        }

        //add options to home screen
        ArrayList<String> options = new ArrayList<>();
        options.add(getString(R.string.main_menu_item_add_new_event));
        options.add(getString(R.string.main_menu_item_reset_events));
        options.add(getString(R.string.main_menu_item_rate_app));
        options.add(getString(R.string.main_menu_item_start_watching));

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

                } else if (entry.equals(getString(R.string.main_menu_item_reset_events))) {

                    db.DeleteAll(Database.TABLE_NAME.EVENTS);
                    db.DeleteAll(Database.TABLE_NAME.RESULTING_ACTIONS);
                    db.DeleteAll(Database.TABLE_NAME.EVENT_TRIGGERS);
                } else if (entry.equals(getString(R.string.main_menu_item_start_watching))) {
                    StartWatchingForEvents();
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

    private void StartWatchingForEvents() {
        //watch for bluetooth connections
        StartListening();


        //watch for location movements
    }

    public void StartListening() {
        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
//        IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);


        try {
            this.registerReceiver(mReceiver, filter1);
//            this.registerReceiver(mReceiver, filter2);
            this.registerReceiver(mReceiver, filter3);
        } catch (Exception ex) {

            ex.printStackTrace();
            String exString = ex.toString();
            String ex2 = ex.getMessage();
        }

    }

    public void StopListening() {
        this.unregisterReceiver(mReceiver);
    }

    //The BroadcastReceiver that listens for bluetooth broadcasts
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);


//            PackageManager packageManager = getPackageManager();
//            Intent openInten = new Intent(Intent.CATEGORY_APP_MUSIC);

//            List activities = packageManager.queryIntentActivities(openInten,
//                    PackageManager.MATCH_ALL);
//            boolean isIntentSafe = activities.size() > 0;

//            if (device.getName().toLowerCase().contains("ihome")) {

            String deviceName = device.getName();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //Device found
            } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                //Device is now connected
                //search triggers for Bluetooth devices where it has this device
                Database myDB = new Database(getApplicationContext());

                try {
                    //find triggers
                    List<Trigger> triggers = Trigger.Find(myDB, Trigger.TRIGGER_TYPE.BLUETOOTH, deviceName);

                    //find events from those triggers
                    List<String> eventIDs = new ArrayList<>();
                    for (Trigger tempTrigger : triggers) {
                        Event.FindEventsFromTrigger(myDB, tempTrigger.ID, eventIDs);
                    }

                    //find resulting actions
                    List<String> raIDs = new ArrayList<>();
                    for (String id : eventIDs) {
                        Event tempEvent = Event.Find(id, myDB);

                        for (String raID : tempEvent.resultingActionIDs) {
                            if (!raIDs.contains(raID)) {
                                raIDs.add(raID);
                            }
                        }
                    }

                    for (String resultingAction : raIDs) {
                        ResultingAction ra = ResultingAction.Find(myDB, resultingAction);
                        Toast.makeText(MainActivity.this, "Found RA: " + ra, Toast.LENGTH_SHORT).show();
                    }


                } catch (IllegalArgumentException ex) {
                    Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }


            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //Done searching
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                //Device is about to disconnect
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                //Device has disconnected
            }
//            }
        }
    };
}
