package com.justfortom.mylife;

import android.database.Cursor;

/**
 * Created by nmesk on 6/19/2016.
 */
public class Trigger {
    int ID;
    TRIGGER_TYPE type;
    String typeInfo;

    public enum TRIGGER_TYPE {
        BLUETOOTH,
        LOCATION
    }

    /**
     * Constructor that adds a trigger to the database
     *
     * @param myDB
     * @param type     The type of trigger that will start an event. Bluetooth connection, location change, etc...
     * @param typeInfo The action that relates to the type of trigger. For example, if it's a bluetooth connection, action will be the 'connected-to-device' option
     */
    public Trigger(Database myDB, TRIGGER_TYPE type, String typeInfo) {
        this.type = type;
        this.typeInfo = typeInfo;

        this.ID = InsertTrigger(myDB, type, typeInfo);
    }

    public Trigger() {

    }

    public static Trigger Find(Database myDB, int id) throws Exception {
        Cursor results = GetTrigger(myDB, id);
        if (results.getCount() == 0) {
            throw new Exception("Could not find any Triggers with that id");
        }

        results.moveToFirst();
        Trigger tempTrigger = new Trigger();
        tempTrigger.ID = results.getInt(0);
        tempTrigger.type = Enum.valueOf(TRIGGER_TYPE.class, results.getString(1));
        tempTrigger.typeInfo = results.getString(2);

        return tempTrigger;
    }

    private int InsertTrigger(Database myDB, Trigger.TRIGGER_TYPE type, String typeInfo) {
        int id = myDB.GetNextID(Database.TABLE_NAME.EVENT_TRIGGERS.name());
        String sql = String.format("INSERT INTO %S VALUES (%d, '%s', '%s')", Database.TABLE_NAME.EVENT_TRIGGERS.name(), id, type.name(), typeInfo);
        myDB.ExecuteSql(sql);
        return id;
    }

    private static Cursor GetTrigger(Database myDB, int triggerID) {
        String sql = String.format("SELECT * FROM %S WHERE %s == %d", Database.TABLE_NAME.EVENT_TRIGGERS.name(), Database.COLUMN.ID.name(), triggerID);
        return myDB.Query(sql);
    }

}
