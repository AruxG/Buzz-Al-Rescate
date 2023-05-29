package com.example.buzzalrescate.bd;

import android.provider.BaseColumns;

public class DbContract {

    static final String SQL_CREATE_ENTRIES_RANKING =
            "CREATE TABLE " + DbContract.DbEntry.TABLE_NAME_RANKING + " (" +
                    DbContract.DbEntry._ID + " INTEGER PRIMARY KEY," +
                    DbEntry.COLUMN_NAME_PLAYER + " TEXT," +
                    DbEntry.COLUMN_NAME_MAX + " INT)";

    static final String SQL_DELETE_ENTRIES_RANKING =
            "DROP TABLE IF EXISTS " + DbContract.DbEntry.TABLE_NAME_RANKING;

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DbContract() {}

    /* Inner class that defines the table contents */
    static class DbEntry implements BaseColumns {
        static final String TABLE_NAME_RANKING = "ranking";
        static final String COLUMN_NAME_MAX = "maximapuntuacion";
        static final String COLUMN_NAME_PLAYER = "player";

    }
}
