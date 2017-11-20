package edu.gatech.cs2340.thericks.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import edu.gatech.cs2340.thericks.models.RatData;

/**
 * Class handling direct access with the SQLite database.  Provides the low-level implementation of
 * RatDatabase's methods for adding, getting, and removing rat data.
 *
 * Created by Ben Lashley on 10/18/2017.
 */

class RatDataDAO {

    private static final String TAG = RatDataDAO.class.getSimpleName();

    private static final int INITIAL_CAPACITY = 100100;

    private static final String TABLE_RAT_DATA = "rat_data";

    // Names for rat data table columns
    private static final String COLUMN_KEY = "key";
    private static final String COLUMN_DATE_TIME = "date_and_time";
    private static final String COLUMN_LOC_TYPE= "location_type";
    private static final String COLUMN_ZIP = "incident_zip";
    private static final String COLUMN_ADDRESS = "incident_address";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_BOROUGH = "borough";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
// --Commented out by Inspection START (11/13/2017 1:35 AM):
//    private static final String[] COLUMNS = new String[] { COLUMN_KEY, COLUMN_DATE_TIME,
//            COLUMN_LOC_TYPE, COLUMN_ZIP, COLUMN_ADDRESS, COLUMN_CITY, COLUMN_BOROUGH,
//            COLUMN_LATITUDE, COLUMN_LONGITUDE };
// --Commented out by Inspection STOP (11/13/2017 1:35 AM)

    static void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "Creating database");
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_RAT_DATA + "(" +
                COLUMN_KEY + " INTEGER PRIMARY KEY, " +
                COLUMN_DATE_TIME + " TEXT, " +
                COLUMN_LOC_TYPE + " TEXT, " +
                COLUMN_ZIP + " INTEGER, " +
                COLUMN_ADDRESS + " TEXT, " +
                COLUMN_CITY + " TEXT, " +
                COLUMN_BOROUGH + " TEXT, " +
                COLUMN_LATITUDE + " REAL, " +
                COLUMN_LONGITUDE + " REAL" +
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
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RAT_DATA);
            // Create new table
            onCreate(sqLiteDatabase);
        }
    }

    /**
     * Insert new rat data into database if key does not exist. If key already exists,
     * replace the existing data.
     * @param db the SQLiteDatabase where the RatData Object will be inserted
     * @param key unique key
     * @param createdDateTime date and time of creation
     * @param locationType location
     * @param incidentZip zip code
     * @param incidentAddress address
     * @param city city
     * @param borough borough
     * @param latitude latitude
     * @param longitude longitude
     */
    static void createRatData(SQLiteDatabase db, int key, String createdDateTime,
                              String locationType, int incidentZip, String incidentAddress,
                              String city, String borough, double latitude, double longitude) {

        // Create values to fill the new row of the table
        ContentValues values = new ContentValues();
        values.put(COLUMN_KEY, key);
        values.put(COLUMN_DATE_TIME, createdDateTime);
        values.put(COLUMN_LOC_TYPE, locationType);
        values.put(COLUMN_ZIP, incidentZip);
        values.put(COLUMN_ADDRESS, incidentAddress);
        values.put(COLUMN_CITY, city);
        values.put(COLUMN_BOROUGH, borough);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);

        // Insert new row into table
        db.insertWithOnConflict(TABLE_RAT_DATA, null, values,
                SQLiteDatabase.CONFLICT_REPLACE);
    }



    /**
     * Convert cursor's current position into rat data
     * @param cursor the Cursor used for traversing
     * @return RatData Object
     */
    private static RatData cursorToRatData(Cursor cursor) {
        return new RatData(cursor.getInt(cursor.getColumnIndex(COLUMN_KEY)),
                cursor.getString(cursor.getColumnIndex(COLUMN_DATE_TIME)),
                cursor.getString(cursor.getColumnIndex(COLUMN_LOC_TYPE)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_ZIP)),
                cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS)),
                cursor.getString(cursor.getColumnIndex(COLUMN_CITY)),
                cursor.getString(cursor.getColumnIndex(COLUMN_BOROUGH)),
                cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE)),
                cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE)));
    }



    /**
     * Removes all rat data with the provided key
     * @param db the SQLiteDatabase where the RatData Object will be deleted
     * @param key the RatData Object's unique key
     */
    static void deleteRatData(SQLiteDatabase db, int key) {
        db.delete(TABLE_RAT_DATA, "key=?", new String[]{key +""});
    }



// --Commented out by Inspection START (11/13/2017 1:35 AM):
//    /**
//     * Get single piece of rat data by key; returns null if data is not found
//     * @param db the SQLiteDatabase where the RatData Object will be searched for
//     * @param key the RatData Object's unique key
//     * @return data the RatDat Object with the associated unique key
//     */
//    static RatData findRatDataByKey(SQLiteDatabase db, int key) {
//
//        // Query the table for entries with the given key
//        // NOTE: there should be at most one such entry, since each key is unique.
//        //       In the event of multiple such entries, this method uses the first.
//        Cursor cursor = db.query(TABLE_RAT_DATA, COLUMNS , COLUMN_KEY + "=?",
//                new String[] { String.valueOf(key) }, null, null,
//                null, null);
//
//        RatData data = null;
//        // Check if the query returned any entries
//        if (cursor != null) {    // Entry found
//            cursor.moveToFirst();
//            // Create and return new rat data using values in the entry
//            data = cursorToRatData(cursor);
//            // Free up cursor
//            cursor.close();
//        }
//        return data;
//    }
// --Commented out by Inspection STOP (11/13/2017 1:35 AM)

    /**
     * Get all rat data as a list
     * @param db the SQLiteDatabase that houses all the RatData Objects
     * @return a list of RatData Objects
     */
    static List<RatData> getAllRatData(SQLiteDatabase db) {
        List<RatData> ratDataList = new ArrayList<>(INITIAL_CAPACITY);
        String selectAllQuery = "SELECT * FROM " + TABLE_RAT_DATA;

        Cursor cursor = db.rawQuery(selectAllQuery, null);

        // Loop through all rows and add as new rat data instance
        if (cursor.moveToFirst()) {
            do {
                // Create rat data from values of the current row
                RatData data = cursorToRatData(cursor);
                // Add rat data to list
                ratDataList.add(data);
            } while (cursor.moveToNext());
        }
        // Free up cursor
        cursor.close();
        return ratDataList;
    }

    static List<RatData> getFilteredRatData(SQLiteDatabase db,
                                            Collection<Predicate<RatData>> filters) {
        List<RatData> ratDataList = new ArrayList<>(INITIAL_CAPACITY);
        String selectAllQuery = "SELECT * FROM " + TABLE_RAT_DATA;

        Cursor cursor = db.rawQuery(selectAllQuery, null);

        Predicate<RatData> allPredicates = filters.stream().reduce(f -> true, Predicate::and);

        // Loop through all rows and add as new rat data instance
        if (cursor.moveToFirst()) {
            do {
                // Create rat data from values of the current row
                RatData data = cursorToRatData(cursor);
                // Apply filters
                if (allPredicates.test(data)) {
                    // Add rat data to list
                    ratDataList.add(data);
                }
            } while (cursor.moveToNext());
        }
        // Free up cursor
        cursor.close();
        return ratDataList;
    }

// --Commented out by Inspection START (11/13/2017 1:30 AM):
//    /**
//     * Returns list of RatData Objects that satisfy all of the provided filters.
//     *
//     * @param filters List of filters to apply
//     * @return List of RatData Objects in full list satisfying all filters
//     */
//    static List<RatData> applyFilters(Collection<RatData> fullList,
//                                      Collection<Predicate<RatData>> filters) {
//        Predicate<RatData> allPredicates = filters.stream().reduce(f -> true, Predicate::and);
//        return fullList.stream().filter(allPredicates).collect(Collectors.toList());
//    }
// --Commented out by Inspection STOP (11/13/2017 1:30 AM)
}
