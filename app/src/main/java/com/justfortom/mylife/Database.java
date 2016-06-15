package com.justfortom.mylife;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
}


