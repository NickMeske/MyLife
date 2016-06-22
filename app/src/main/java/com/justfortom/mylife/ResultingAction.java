package com.justfortom.mylife;

import android.database.Cursor;

/**
 * Created by nmesk on 6/19/2016.
 */
public class ResultingAction {

    int ID;
    RA_TYPE type;
    String action;

    enum RA_TYPE {
        OPEN_APP,
        ENABLE_UTIL,
        DISABLE_UTIL
    }

    public ResultingAction(Database myDB, RA_TYPE type, String action) {
        this.type = type;
        this.action = action;
        this.ID = myDB.InsertResultingAction(type, action);
    }

    public ResultingAction() {

    }

    public static ResultingAction Find(Database myDB, int id) throws Exception {
        Cursor results = myDB.GetResultingAction(id);

        if (results.getCount() == 0) {
            throw new Exception("Unable to find resulting action.");
        }

        results.moveToFirst();
        ResultingAction tempAction = new ResultingAction();
        tempAction.ID = results.getInt(0);
        tempAction.type = Enum.valueOf(RA_TYPE.class, results.getString(1));
        tempAction.action = results.getString(2);

        return tempAction;
    }

}
