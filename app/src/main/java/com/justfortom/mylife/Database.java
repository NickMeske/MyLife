package com.justfortom.mylife;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by nmesk on 6/14/2016.
 */
public class Database {
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

    public void RemoveTable(String tableName) {
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

    public void InsertRow(String tableName, String[] columns, String[] values) {
        if (!TableExists(tableName)) {
            throw new IllegalArgumentException("Unable to find table: " + tableName);
        }

        if (columns.length != values.length) {
            throw new IllegalArgumentException("The number of columns needs to equal the number of values.");
        }

        db = myHelper.getWritableDatabase();
        ContentValues vals = new ContentValues();

        for (int i = 0; i < columns.length; i++) {
            vals.put(columns[i], values[i]);
        }

        db.insertOrThrow(tableName, null, vals);
    }

    /**
     * Create initial schema on startup
     */
    public void CreateSchema() {
        //create events table
        ArrayList<DatabaseColumn> eventColumns = new ArrayList<>();
        eventColumns.add(new DatabaseColumn("id", DATABASE_COLUMN_TYPE.INTEGER, true));
        eventColumns.add(new DatabaseColumn("event_type", DATABASE_COLUMN_TYPE.TEXT, false));
        eventColumns.add(new DatabaseColumn("event_name", DATABASE_COLUMN_TYPE.TEXT, false));
        eventColumns.add(new DatabaseColumn("resulting_action_ids", DATABASE_COLUMN_TYPE.TEXT, false));
        try {
            CreateTable("events", eventColumns);
        } catch (Exception ex) {
            //table probably exists, ignore for now
        }

        //create resulting_actions table
        ArrayList<DatabaseColumn> resultingActionColumns = new ArrayList<>();
        resultingActionColumns.add(new DatabaseColumn("id", DATABASE_COLUMN_TYPE.INTEGER, true));
        resultingActionColumns.add(new DatabaseColumn("resulting_action_type", DATABASE_COLUMN_TYPE.TEXT, false));
        resultingActionColumns.add(new DatabaseColumn("resulting_action_action", DATABASE_COLUMN_TYPE.TEXT, false));
        resultingActionColumns.add(new DatabaseColumn("start_time", DATABASE_COLUMN_TYPE.TEXT, false));
        resultingActionColumns.add(new DatabaseColumn("end_time", DATABASE_COLUMN_TYPE.TEXT, false));
        try {
            CreateTable("resulting_actions", eventColumns);
        } catch (Exception ex) {
            //table probably exists, ignore for now
        }
    }

}


