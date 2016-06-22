package com.justfortom.mylife;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by nmesk on 6/14/2016.
 */
public class Database {

    public enum TABLE_NAME {
        EVENTS,
        EVENT_TRIGGERS,
        RESULTING_ACTIONS
    }

    public enum COLUMN {
        ID,
        EVENTS_EVENT_NAME,
        EVENTS_EVENT_TRIGGER_IDS,
        EVENTS_RESULTING_ACTION_IDS,
        EVENTS_START_TIME,
        EVENTS_END_TIME,
        RA_TYPE,
        RA_ACTION,
        TRIGGERS_TYPE,
        TRIGGERS_INFO
    }
    //database info
    public static final String DATABASE_NAME = "life.db";
    public static final int DATABASE_VERSION = 1;

    private DBHelper myHelper;
    private SQLiteDatabase db;

    public Database(Context context) {
        myHelper = new DBHelper(context);
    }

    private boolean TableExists(String tableName) {
        db = myHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    private void RemoveTable(String tableName) {
        db = myHelper.getWritableDatabase();

        if (TableExists(tableName)) {
            String sqlExecute = "DROP TABLE " + tableName;
            db.execSQL(sqlExecute);
        } else {
            throw new IllegalArgumentException("Unable to find table: " + tableName);
        }
    }

    public void CreateTable(String tableName, List<DatabaseColumn> columns) {
        //create table string something like:
        //CREATE TABLE <TableName> (<ColumnName> <Type> [PRIMARY KEY], ...)
        if (tableName.isEmpty() || columns.isEmpty()) {
            throw new IllegalArgumentException("Problem with either table name or columns");
        }

        if (TableExists(tableName)) {
            //RemoveTable(tableName);
            throw new IllegalArgumentException("Table already exists");
        }

        String columnString;
        ArrayList<String> columnStrings = new ArrayList<>();

        for (DatabaseColumn column : columns) {
            if (column.IsPrimaryKey) {
                columnStrings.add(String.format("%S %S PRIMARY KEY", column.getColumnName(), column.getColumnType()));
            } else {
                columnStrings.add(String.format("%S %S", column.getColumnName(), column.getColumnType()));
            }
        }

        String sqlExecute = String.format("CREATE TABLE %S(%S)", tableName, TextUtils.join(", ", columnStrings));

        db = myHelper.getWritableDatabase();
        db.execSQL(sqlExecute);
    }

    public void DeleteAll(TABLE_NAME tableName) {
        String sql = "Delete from " + tableName.name();
        db = myHelper.getWritableDatabase();
        db.execSQL(sql);
    }

    private int GetNextID(String tableName) {
        db = myHelper.getReadableDatabase();
        String SQL = "Select MAX(ID) from " + tableName;
        Cursor result = db.rawQuery(SQL, null);

        result.moveToFirst();
        int maxID = result.getInt(0);
        int nextID = maxID += 1;

        return nextID;
    }


    /**
     * Create initial schema on startup
     */
    public void CreateSchema() {
        //create events table
        ArrayList<DatabaseColumn> eventColumns = new ArrayList<>();
        eventColumns.add(new DatabaseColumn(COLUMN.ID.name(), DATABASE_COLUMN_TYPE.INTEGER, true));
        eventColumns.add(new DatabaseColumn(COLUMN.EVENTS_EVENT_NAME.name(), DATABASE_COLUMN_TYPE.TEXT, false));
        eventColumns.add(new DatabaseColumn(COLUMN.EVENTS_START_TIME.name(), DATABASE_COLUMN_TYPE.TEXT, false));
        eventColumns.add(new DatabaseColumn(COLUMN.EVENTS_END_TIME.name(), DATABASE_COLUMN_TYPE.TEXT, false));
        eventColumns.add(new DatabaseColumn(COLUMN.EVENTS_RESULTING_ACTION_IDS.name(), DATABASE_COLUMN_TYPE.TEXT, false));
        eventColumns.add(new DatabaseColumn(COLUMN.EVENTS_EVENT_TRIGGER_IDS.name(), DATABASE_COLUMN_TYPE.TEXT, false));
        CreateTable(TABLE_NAME.EVENTS.name(), eventColumns);


        //create resulting_actions table
        ArrayList<DatabaseColumn> resultingActionColumns = new ArrayList<>();
        resultingActionColumns.add(new DatabaseColumn(COLUMN.ID.name(), DATABASE_COLUMN_TYPE.INTEGER, true));
        resultingActionColumns.add(new DatabaseColumn(COLUMN.RA_TYPE.name(), DATABASE_COLUMN_TYPE.TEXT, false));
        resultingActionColumns.add(new DatabaseColumn(COLUMN.RA_ACTION.name(), DATABASE_COLUMN_TYPE.TEXT, false));
        CreateTable(TABLE_NAME.RESULTING_ACTIONS.name(), eventColumns);


        ArrayList<DatabaseColumn> triggerColumns = new ArrayList<>();
        triggerColumns.add(new DatabaseColumn(COLUMN.ID.name(), DATABASE_COLUMN_TYPE.INTEGER, true));
        triggerColumns.add(new DatabaseColumn(COLUMN.TRIGGERS_TYPE.name(), DATABASE_COLUMN_TYPE.TEXT, false));
        triggerColumns.add(new DatabaseColumn(COLUMN.TRIGGERS_INFO.name(), DATABASE_COLUMN_TYPE.TEXT, false));
        CreateTable(TABLE_NAME.EVENT_TRIGGERS.name(), eventColumns);

    }


    public int InsertEvent(String eventName, Date startTime, Date endTime, List<String> resultingIDs, List<String> triggerIDs) {

        int id = GetNextID(TABLE_NAME.EVENTS.name());
        String start = AndroidHelper.DateFormat().format(startTime);
        String end = AndroidHelper.DateFormat().format(endTime);

        String sql = String.format("INSERT INTO %S VALUES (%d, '%s','%s','%s','%s','%s')",
                TABLE_NAME.EVENTS.name(), id,
                eventName, start, end, TextUtils.join("|",
                        resultingIDs.toArray()), TextUtils.join("|", triggerIDs.toArray()));

        db = myHelper.getWritableDatabase();
        db.execSQL(sql);
        return id;
    }

    public Cursor GetEvent(String eventName) {
        String sql = String.format("SELECT * FROM %S WHERE %s = '%s'", TABLE_NAME.EVENTS.name(), COLUMN.EVENTS_EVENT_NAME, eventName);
        db = myHelper.getReadableDatabase();
        Cursor results = db.rawQuery(sql, null);
        return results;
    }

    public int InsertTrigger(Trigger.TRIGGER_TYPE type, String typeInfo) {
        int id = GetNextID(TABLE_NAME.EVENT_TRIGGERS.name());
        String sql = String.format("INSERT INTO %S VALUES (%d, '%s', '%s')", TABLE_NAME.EVENT_TRIGGERS.name(), id, type.name(), typeInfo);
        db = myHelper.getWritableDatabase();
        db.execSQL(sql);

        return id;
    }

    public Cursor GetTrigger(int triggerID) {
        String sql = String.format("SELECT * FROM %S WHERE %s == %d", TABLE_NAME.EVENT_TRIGGERS.name(), COLUMN.ID.name(), triggerID);
        db = myHelper.getReadableDatabase();
        return db.rawQuery(sql, null);
    }

    public int InsertResultingAction(ResultingAction.RA_TYPE type, String typeInfo) {
        int id = GetNextID(TABLE_NAME.RESULTING_ACTIONS.name());
        String sql = String.format("INSERT INTO %S VALUES (%d, '%s', '%s')", TABLE_NAME.RESULTING_ACTIONS.name(), id, type.name(), typeInfo);
        db = myHelper.getWritableDatabase();
        db.execSQL(sql);
        return id;
    }

    public Cursor GetResultingAction(int raID) {
        String sql = String.format("SELECT * FROM %S WHERE %s == %d", TABLE_NAME.RESULTING_ACTIONS.name(), COLUMN.ID.name(), raID);
        db = myHelper.getReadableDatabase();
        return db.rawQuery(sql, null);
    }
}


