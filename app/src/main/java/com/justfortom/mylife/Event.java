package com.justfortom.mylife;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by nmesk on 6/19/2016.
 */
public class Event implements Serializable {

    int ID;
    String eventName;
    Date startTime;
    Date endTime;
    List<String> resultingActionIDs;
    List<String> triggerIDs;

    //Insert new event
    public Event(Database myDB, String eventName) {

        Event tempEvent = null;
        try {
            tempEvent = Event.Find(eventName, myDB);
        } catch (Exception ex) {
            //OK if it fails, it should
        }
        if (tempEvent != null) {
            throw new IllegalArgumentException("That event already exists.");
        }

        //insert new event into DB
        this.eventName = eventName;
        this.startTime = new Date();
        this.endTime = new Date();
        this.resultingActionIDs = new ArrayList<>();
        this.triggerIDs = new ArrayList<>();

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
            throw new IllegalArgumentException("No events found with name: " + eventName);
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

    public void AddResultingAction(Database myDB, ResultingAction.RA_TYPE raType, String action) {
        //if(resultingActionIDs.contains(resultingActionID)){throw new IllegalArgumentException("This event already contains that resulting action.");}

        ResultingAction myAction = new ResultingAction(myDB, raType, action);

        resultingActionIDs.add(String.valueOf(myAction.ID));

        String resultingActs = TextUtils.join("|", resultingActionIDs);

        String sql = String.format("UPDATE %S SET %s = %s WHERE %s == %d", Database.TABLE_NAME.EVENTS.name(), Database.COLUMN.EVENTS_RESULTING_ACTION_IDS.name(), resultingActs, Database.COLUMN.ID.name(), this.ID);
        myDB.ExecuteSql(sql);
    }

    public void RemoveResultingAction(Database myDB, String resultingActionID) {
        if (!resultingActionIDs.contains(resultingActionID)) {
            throw new IllegalArgumentException("This event does not contain that resulting action.");
        }

        //remove it from the database
        ResultingAction action = ResultingAction.Find(myDB, resultingActionID);
        action.Remove(myDB);

        resultingActionIDs.remove(resultingActionID);
        String resultingActs = TextUtils.join("|", resultingActionIDs);

        String sql = String.format("UPDATE %S SET %s = %s WHERE %s == %d", Database.TABLE_NAME.EVENTS.name(), Database.COLUMN.EVENTS_RESULTING_ACTION_IDS.name(), resultingActs, Database.COLUMN.ID.name(), this.ID);
        myDB.ExecuteSql(sql);
    }

    public void AddTrigger(Database myDB, Trigger.TRIGGER_TYPE type, String triggerInfo) {
        //if(triggerIDs.contains(triggerID)){throw new IllegalArgumentException("Trigger already exists for this event.");}
        Trigger myTrigger = new Trigger(myDB, type, triggerInfo);

        triggerIDs.add(String.valueOf(myTrigger.ID));

        String trigs = TextUtils.join("|", triggerIDs);
        String sql = String.format("UPDATE %S SET %s = %s WHERE %s == %d", Database.TABLE_NAME.EVENTS.name(), Database.COLUMN.EVENTS_EVENT_TRIGGER_IDS.name(), trigs, Database.COLUMN.ID.name(), this.ID);
        myDB.ExecuteSql(sql);
    }

    public void RemoveTrigger(Database myDB, String triggerID) {
        if (!triggerIDs.contains(triggerID)) {
            throw new IllegalArgumentException("Trigger doesn't exist for this event.");
        }

        Trigger myTrigger = Trigger.Find(myDB, triggerID);
        myTrigger.Remove(myDB);

        triggerIDs.remove(triggerID);

        String trigs = TextUtils.join("|", triggerIDs);
        String sql = String.format("UPDATE %S SET %s = %s WHERE %s == %d", Database.TABLE_NAME.EVENTS.name(), Database.COLUMN.EVENTS_EVENT_TRIGGER_IDS.name(), trigs, Database.COLUMN.ID.name(), this.ID);
        myDB.ExecuteSql(sql);
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
