package com.justfortom.mylife;

import android.database.Cursor;
import android.text.TextUtils;

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

        this.ID = InsertEvent(myDB, eventName, this.startTime, this.endTime, resultingActionIDs, triggerIDs);
    }

    private Event() {
        //empty constructor
    }

    public static List<Event> FindAll(Database myDB) throws Exception {
        Cursor results = GetEvents(myDB);

        if (results.getCount() < 1) {
            throw new Exception("No events found.");
        }
        results.moveToFirst();

        List<Event> events = new ArrayList<>();
        while (!results.isAfterLast()) {
            Event tempEvent = new Event();
            tempEvent.ID = results.getInt(0);
            tempEvent.eventName = results.getString(1);
            tempEvent.startTime = AndroidHelper.DateFormat().parse(results.getString(2));
            tempEvent.endTime = AndroidHelper.DateFormat().parse(results.getString(3));
            tempEvent.resultingActionIDs = Arrays.asList(results.getString(4).split("|"));
            events.add(tempEvent);
            results.moveToNext();
        }
        return events;
    }

    public static Event Find(String eventName, Database myDB) throws Exception {
        Cursor results = GetEvent(myDB, eventName);

        if (results.getCount() < 1) {
            throw new Exception("No events found with name: " + eventName);
        }

        results.moveToFirst();
        Event tempEvent = new Event();
        tempEvent.ID = results.getInt(0);
        tempEvent.eventName = results.getString(1);
        tempEvent.startTime = AndroidHelper.DateFormat().parse(results.getString(2));
        tempEvent.endTime = AndroidHelper.DateFormat().parse(results.getString(3));
        tempEvent.resultingActionIDs = Arrays.asList(results.getString(4).split("|"));

        return tempEvent;
    }


    private int InsertEvent(Database myDB, String eventName, Date startTime, Date endTime, List<String> resultingIDs, List<String> triggerIDs) {

        int id = myDB.GetNextID(Database.TABLE_NAME.EVENTS.name());
        String start = AndroidHelper.DateFormat().format(startTime);
        String end = AndroidHelper.DateFormat().format(endTime);

        String sql = String.format("INSERT INTO %S VALUES (%d, '%s','%s','%s','%s','%s')",
                Database.TABLE_NAME.EVENTS.name(), id,
                eventName, start, end, TextUtils.join("|",
                        resultingIDs.toArray()), TextUtils.join("|", triggerIDs.toArray()));

        myDB.ExecuteSql(sql);
        return id;
    }

    private static Cursor GetEvent(Database myDB, String eventName) {
        String sql = String.format("SELECT * FROM %S WHERE %s = '%s'", Database.TABLE_NAME.EVENTS.name(), Database.COLUMN.EVENTS_EVENT_NAME, eventName);
        return myDB.Query(sql);
    }

    private static Cursor GetEvents(Database myDB) {
        String sql = String.format("SELECT * FROM %S", Database.TABLE_NAME.EVENTS.name());
        return myDB.Query(sql);
    }
}
