package com.justfortom.mylife;

/**
 * Created by nmesk on 6/14/2016.
 */
public class DatabaseColumn {
    public String getColumnName() {
        return ColumnName;
    }

    public DATABASE_COLUMN_TYPE getColumnType() {
        return ColumnType;
    }

    public Boolean getPrimaryKey() {
        return IsPrimaryKey;
    }

    String ColumnName;
    DATABASE_COLUMN_TYPE ColumnType;
    Boolean IsPrimaryKey;

    public DatabaseColumn(String columnName, DATABASE_COLUMN_TYPE columnType, boolean isPrimaryKey) {
        this.ColumnName = columnName;
        this.ColumnType = columnType;
        this.IsPrimaryKey = isPrimaryKey;
    }
}
