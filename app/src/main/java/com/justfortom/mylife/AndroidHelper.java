package com.justfortom.mylife;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by nmesk on 6/15/2016.
 */
public class AndroidHelper {
    public static void AddItemsToList(Context context, ListView listView, List<String> items) {
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(listAdapter);
    }

}
