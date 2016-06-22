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
        this.ID = InsertResultingAction(myDB, type, action);
    }

    public ResultingAction() {

    }

    public static ResultingAction Find(Database myDB, int id) throws Exception {
        Cursor results = GetResultingAction(myDB, id);

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


    private int InsertResultingAction(Database myDB, ResultingAction.RA_TYPE type, String typeInfo) {
        int id = myDB.GetNextID(Database.TABLE_NAME.RESULTING_ACTIONS.name());
        String sql = String.format("INSERT INTO %S VALUES (%d, '%s', '%s')", Database.TABLE_NAME.RESULTING_ACTIONS.name(), id, type.name(), typeInfo);
        myDB.ExecuteSql(sql);
        return id;
    }

    private static Cursor GetResultingAction(Database myDB, int raID) {
        String sql = String.format("SELECT * FROM %S WHERE %s == %d", Database.TABLE_NAME.RESULTING_ACTIONS.name(), Database.COLUMN.ID.name(), raID);
        return myDB.Query(sql);
    }
}
