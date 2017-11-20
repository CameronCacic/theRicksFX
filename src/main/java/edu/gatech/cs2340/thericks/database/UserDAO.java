package edu.gatech.cs2340.thericks.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.cs2340.thericks.models.Privilege;
import edu.gatech.cs2340.thericks.models.User;
import edu.gatech.cs2340.thericks.utils.Security;

/**
 * Class handling direct access with the SQLite database.  Provides the low-level implementation of
 * UserDatabase's methods for adding, getting, and removing users.
 *
 * Created by Ben Lashley on 10/31/2017.
 */

class UserDAO {
    private static final String TAG = UserDAO.class.getSimpleName();

    private static final String TABLE_USERS = "user_data";

    // Names for rat data table columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_SALT = "salt";
    private static final String COLUMN_PRIVILEGE= "privilege";
    private static final String[] COLUMNS = new String[] { COLUMN_ID, COLUMN_USERNAME,
            COLUMN_PASSWORD, COLUMN_SALT, COLUMN_PRIVILEGE };


    /**
     * Create a user table in the database
     * @param sqLiteDatabase Database where table is stored
     */
    static void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_SALT + " TEXT, " +
                COLUMN_PRIVILEGE + " TEXT" +
                ");";
        sqLiteDatabase.execSQL(query);
    }

    /**
     * Currently re-creates table on all upgrades
     * @param sqLiteDatabase Database being upgraded
     * @param oldVersion previous version of database
     * @param newVersion new version of database
     */
    static void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            // Get rid of the old table
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            // Create new table
            onCreate(sqLiteDatabase);
        }
    }

    /**
     * Insert new user into database if the user does not exist. If the user already exists,
     * replace the existing data.
    */
    static void createUser(SQLiteDatabase db, String username, String password,
                           Privilege privilege) {

        // Create values to fill the new row of the table
        ContentValues values = new ContentValues();
        String salt = Security.generateSalt();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, Security.createEncryptedPassword(password, salt));
        values.put(COLUMN_SALT, salt);
        values.put(COLUMN_PRIVILEGE, privilege.toString());

        // Insert new row into table
        db.insertWithOnConflict(TABLE_USERS, null, values,
                SQLiteDatabase.CONFLICT_REPLACE);
    }


    /**
     * Convert cursor's current position into a User Object
     * @param cursor the cursor for traversing the table
     * @return a new User Object
     */
    private static User cursorToUser(Cursor cursor) {
        // Convert privilege string in table to an enum
        String privilegeStr = cursor.getString(cursor.getColumnIndex(COLUMN_PRIVILEGE));
        Privilege privilege = Privilege.NORMAL;
        if (privilegeStr.equals(Privilege.ADMIN.toString())) {
            privilege = Privilege.ADMIN;
        }
        // Create and return the user object
        String username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
        String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
        String salt = cursor.getString(cursor.getColumnIndex(COLUMN_SALT));
        Log.d(TAG, String.format("Cursor at user with username: %s, password: %s, salt: %s,"
                + " privilege: %s\n", username, password, salt, privilegeStr));
        return new User(username, password, salt, privilege);
    }


    /**
     * Removes the user with the provided key
     *
     * @param db database to search in
     * @param username the username of the user to delete
     */
    static void deleteUser(SQLiteDatabase db, String username) {
        db.delete(TABLE_USERS, COLUMN_USERNAME + "=?", new String[]{ username });
    }



    /**
     * Get single user by id; returns null if user is not found
     * @param db database to search in
     * @param username the username of the user to search for
     * @return user with specified id, null if none found
     */
    static User getUserByUsername(SQLiteDatabase db, String username) {
        // Query the table for entries with the given key
        // NOTE: there should be at most one such entry, since each key is unique.
        //       In the event of multiple such entries, this method uses the first.
        Cursor cursor = db.query(TABLE_USERS, COLUMNS , COLUMN_USERNAME + "=?",
                new String[] { username }, null, null, null, null);

        User user = null;
        // Check if the query returned any entries
        if ((cursor != null) && cursor.moveToFirst()) {    // Entry found
            // Create and return new rat data using values in the entry
            user = cursorToUser(cursor);
            // Free up cursor
            cursor.close();
        }
        if (user != null) {
            Log.d(TAG, "Fetched user with login: " + user.getLogin());
        }
        return user;
    }

    /**
     * Get all users as a list.
     *
     * @param db the database to search in
     * @return all users in a list
     */
    static List<User> getAllUsers(SQLiteDatabase db) {
        List<User> userList = new ArrayList<>();
        String selectAllQuery = "SELECT * FROM " + TABLE_USERS;

        Cursor cursor = db.rawQuery(selectAllQuery, null);

        // Loop through all rows and add as new user instance
        if (cursor.moveToFirst()) {
            do {
                // Create user from values of the current row
                User user = cursorToUser(cursor);
                // Add rat data to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        // Free up cursor
        cursor.close();
        return userList;
    }
}
