package com.justfortom.mylife;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by nmesk on 6/19/2016.
 */
public class Event {

    int ID;
    String eventName;
    Date startTime;
    Date endTime;
    List<String> resultingActionIDs;
    List<String> triggerIDs;

    public Event(Database myDB, String eventName, Date startTime, Date endTime, List<String> resultingActionIDs, List<String> triggerIDs) {
        //insert new event into DB
        this.eventName = eventName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.resultingActionIDs = resultingActionIDs;
        this.triggerIDs = triggerIDs;

        myDB.InsertEvent(this.eventName, this.startTime, this.endTime, resultingActionIDs, triggerIDs);
    }

    private Event() {
    }


    public static Event Find(String eventName, int id) {
        return null;
    }

    public static List<Event> FindAll(String eventName, Database myDB) throws Exception {
        List<Event> myEvents = new ArrayList<>();
        Cursor results = myDB.GetEvent(eventName);

        results.moveToFirst();
        int num = results.getCount();

        if (num < 1) {
            throw new Exception("No events found with name: " + eventName);
        }

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        while (!results.isLast()) {
            Event tempEvent = new Event();
            tempEvent.ID = results.getInt(0);
            tempEvent.eventName = results.getString(1);

            tempEvent.startTime = format.parse(results.getString(2));
            tempEvent.endTime = format.parse(results.getString(3));
            tempEvent.resultingActionIDs = Arrays.asList(results.getString(4).split("|"));

            myEvents.add(tempEvent);
            results.moveToNext();
        }
        return myEvents;
    }

}