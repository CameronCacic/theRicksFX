package edu.gatech.cs2340.thericks.database;
//adam was here\\

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import edu.gatech.cs2340.thericks.models.RatData;
import edu.gatech.cs2340.thericks.utils.Log;

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
    
    private static final String ALL_COLUMNS = "(" + COLUMN_KEY + ","
    												+ COLUMN_DATE_TIME + ","
    												+ COLUMN_LOC_TYPE + ","
    												+ COLUMN_ZIP + ","
    												+ COLUMN_ADDRESS + ","
    												+ COLUMN_CITY + ","
    												+ COLUMN_BOROUGH + ","
    												+ COLUMN_LATITUDE + ","
    												+ COLUMN_LONGITUDE + ")";

    private PreparedStatement insertOrReplaceStatement;

    RatDataDAO (Connection connection) {
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
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
			insertOrReplaceStatement = connection.prepareStatement("INSERT OR REPLACE INTO " 
																	+ TABLE_RAT_DATA
																	+ " " + ALL_COLUMNS
																	+ " VALUES (?,?,?,?,?,?,?,?,?)");
		} catch (SQLException e) {
			e.printStackTrace();
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
    void createRatData(int key, String createdDateTime,
                              String locationType, int incidentZip, String incidentAddress,
                              String city, String borough, double latitude, double longitude) {

    	if (insertOrReplaceStatement != null) {
    		try {
				insertOrReplaceStatement.setInt(1, key);
				insertOrReplaceStatement.setString(2, createdDateTime);
				insertOrReplaceStatement.setString(3, locationType);
				insertOrReplaceStatement.setInt(4, incidentZip);
				insertOrReplaceStatement.setString(5, incidentAddress);
				insertOrReplaceStatement.setString(6, city);
				insertOrReplaceStatement.setString(7, borough);
				insertOrReplaceStatement.setDouble(8, latitude);
				insertOrReplaceStatement.setDouble(9, longitude);
				insertOrReplaceStatement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    }



    /**
     * Convert result's current position into rat data
     * @param result the ResultSet used for traversing
     * @return RatData Object
     */
    private RatData cursorToRatData(ResultSet result) {
    	RatData rtnData = null;
        try {
			rtnData =  new RatData(result.getInt(COLUMN_KEY),
			        result.getString(COLUMN_DATE_TIME),
			        result.getString(COLUMN_LOC_TYPE),
			        result.getInt(COLUMN_ZIP),
			        result.getString(COLUMN_ADDRESS),
			        result.getString(COLUMN_CITY),
			        result.getString(COLUMN_BOROUGH),
			        result.getDouble(COLUMN_LATITUDE),
			        result.getDouble(COLUMN_LONGITUDE));
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return rtnData;
    }

    /**
     * Removes all rat data with the provided key
     * @param connection the connection to the database where the RatData Object will be deleted
     * @param key the RatData Object's unique key
     */
    void deleteRatData(Connection connection, int key) {
        try {
			Statement statement = connection.createStatement();
			statement.executeUpdate("DELETE FROM " + TABLE_RAT_DATA + " WHERE " + COLUMN_KEY + " = " + key);
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
    List<RatData> getAllRatData(Connection connection) {
        List<RatData> ratDataList = new ArrayList<>(INITIAL_CAPACITY);
        String selectAllQuery = "SELECT * FROM " + TABLE_RAT_DATA;

        try {
        	Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(selectAllQuery);

            // Loop through all rows and add as new rat data instance
			while (result.next()) {
			    // Create rat data from values of the current row
			    RatData data = cursorToRatData(result);
			    // Add rat data to list
			    ratDataList.add(data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
        
        return ratDataList;
    }

    List<RatData> getFilteredRatData(Connection connection,
                                            Collection<Predicate<RatData>> filters) {
    	Log.d(TAG, "Loading filtered rat data");
    	
        List<RatData> ratDataList = new ArrayList<>(INITIAL_CAPACITY);
        String selectAllQuery = "SELECT * FROM " + TABLE_RAT_DATA;

        Predicate<RatData> allPredicates = filters.stream().reduce(f -> true, Predicate::and);

        try {
        	Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(selectAllQuery);

            // Loop through all rows and add as new rat data instance
			while (result.next()) {
			    // Create rat data from values of the current row
			    RatData data = cursorToRatData(result);
			    
			    // Add rat data to list
			    if (allPredicates.test(data)) {
			    	ratDataList.add(data);
			    }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return ratDataList;
    }
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

