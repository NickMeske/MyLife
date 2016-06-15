package com.justfortom.mylife;

import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public Database db;
    private Starter starterThing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        starterThing = new Starter();

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

    public void Start(View view) {
        starterThing.StartListening();
    }

    public void Stop(View view) {
        starterThing.StopListening();
    }

    public void CreateTable(View view) {
        try {

//            ArrayList<DatabaseColumn> newColumns = new ArrayList<>();
//            newColumns.add(new DatabaseColumn("ID", DATABASE_COLUMN_TYPE.INTEGER, true));
//            newColumns.add(new DatabaseColumn("Test", DATABASE_COLUMN_TYPE.BLOB, false));

            db = new Database(getApplicationContext());
            db.RemoveTable("TestTable");

            //db.CreateTable("TestTable", newColumns);

        } catch (Exception ex) {
            Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }


    }
}
