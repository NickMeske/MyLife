package com.justfortom.mylife;

public enum DATABASE_COLUMN_TYPE {
    NULL,
    TEXT, //The value is a signed integer, stored in 1, 2, 3, 4, 6, or 8 bytes depending on the magnitude of the value.
    INTEGER, //The value is a signed integer, stored in 1, 2, 3, 4, 6, or 8 bytes depending on the magnitude of the value.
    REAL, //The value is a floating point value, stored as an 8-byte IEEE floating point number.
    BLOB //The value is a blob of data, stored exactly as it was input.
}
