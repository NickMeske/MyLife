package com.justfortom.mylife;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nmesk on 6/15/2016.
 */
public class AndroidHelper {
    public static void AddItemsToList(Context context, ListView listView, List<String> items) {
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(listAdapter);

    }

    public static void AddItemsToCheckboxList(Context context, ListView listView, List<String> items) {
        ArrayAdapter<String> listAdadpter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_checked, items);
        listView.setAdapter(listAdadpter);
    }

    public static SimpleDateFormat DateFormat() {
        return new SimpleDateFormat("HH:mm");
    }

    public static void FillResultingActions(Database myDB, Context context, ListView listView, List<String> resultingActionIDs) {

        //Can add more information here
        List<String> raIDs = new ArrayList<>();
        for (String resAction : resultingActionIDs) {
            ResultingAction tempAction = ResultingAction.Find(myDB, resAction);
            raIDs.add(String.valueOf(tempAction.ID));
        }
        AddItemsToList(context, listView, raIDs);
    }

    public static void FillTriggers(Database myDB, Context context, ListView listView, List<String> triggerIDs) {
        //Can add more information here
        List<String> raIDs = new ArrayList<>();
        for (String trigger : raIDs) {
            Trigger tempTrigger = Trigger.Find(myDB, trigger);

            raIDs.add(String.valueOf(tempTrigger.ID));
        }
        AddItemsToList(context, listView, raIDs);
    }

}
