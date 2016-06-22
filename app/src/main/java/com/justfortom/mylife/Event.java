package com.justfortom.mylife;

import android.database.Cursor;
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

    //Insert new event
    public Event(Database myDB, String eventName, Date startTime, Date endTime, List<String> resultingActionIDs, List<String> triggerIDs) {
        //insert new event into DB
        this.eventName = eventName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.resultingActionIDs = resultingActionIDs;
        this.triggerIDs = triggerIDs;

        this.ID = myDB.InsertEvent(eventName, this.startTime, this.endTime, resultingActionIDs, triggerIDs);
    }

    private Event() {
        //empty constructor
    }

    public static Event Find(String eventName, Database myDB) throws Exception {
        Cursor results = myDB.GetEvent(eventName);

        results.moveToFirst();
        int num = results.getCount();

        if (num < 1) {
            throw new Exception("No events found with name: " + eventName);
        }

        Event tempEvent = new Event();
        tempEvent.ID = results.getInt(0);
        tempEvent.eventName = results.getString(1);
        tempEvent.startTime = AndroidHelper.DateFormat().parse(results.getString(2));
        tempEvent.endTime = AndroidHelper.DateFormat().parse(results.getString(3));
        tempEvent.resultingActionIDs = Arrays.asList(results.getString(4).split("|"));

        return tempEvent;
    }

}
