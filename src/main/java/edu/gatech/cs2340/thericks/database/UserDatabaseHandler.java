package edu.gatech.cs2340.thericks.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Singleton class that handles the user database connection.
 * Provides a single connection throughout the
 * application's life cycle.
 *
 * Created by Ben Lashley on 10/31/2017.
 */

final class UserDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "users.db";

    private static final UserDatabaseHandler instance =
            new UserDatabaseHandler(RatTrackerApplication.getAppContext());

    /**
     * Creates and/or opens a database that will be used for reading and writing
     * @return writable database
     */
    static synchronized SQLiteDatabase provideWritableDatabase() {
        return instance.getWritableDatabase();
    }

    private UserDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        UserDAO.onCreate(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        UserDAO.onUpgrade(sqLiteDatabase, oldVersion, newVersion);
    }

}
