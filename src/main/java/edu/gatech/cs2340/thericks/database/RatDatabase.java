package edu.gatech.cs2340.thericks.database;


import java.sql.Connection;
import java.util.List;

import edu.gatech.cs2340.thericks.models.RatData;
import edu.gatech.cs2340.thericks.models.RatDataSource;
import edu.gatech.cs2340.thericks.models.RatFilter;
import edu.gatech.cs2340.thericks.utils.DataLoadedCallback;
import edu.gatech.cs2340.thericks.utils.Log;

/**
 * Class representing a database of rat data.  This class is used as an interface between the
 * database and the activities.
 *
 * Created by Ben Lashley on 10/18/2017.
 */

public class RatDatabase implements RatDataSource {

    private static final String TAG = RatDatabase.class.getSimpleName();
    private Connection connection;

    /**
     * initializes the SQLite Database to a RatDatabaseHandler 
     */
    public RatDatabase() {
        open();
    }

    private void open() {
        connection = RatDatabaseHandler.provideDatabaseConnection();
    }

    /**
     * Loads in a list of RatData Objects
     * @param a the ArrayAdapter that returns the views for each RatData Object
     * @param data the list of RatData Objects whose views will be added
     * @param filter the filters used to select certain RatData Objects
     */
    public void loadData(DataLoadedCallback callback, List<RatData> data, RatFilter filter) {
        if (!LoadRatDataTask.isReady()) {
            Log.d(TAG, "LoadRatDataTask was not ready to load data or data was already loaded");
            if (data != null) {
                data.clear();
                data.addAll(getFilteredRatData(filter));
                if (callback != null) {
                	callback.notifyDataLoaded();
                }
            }
            return;
        }
        Log.d(TAG, "Creating new LoadRatDataTask");
        LoadRatDataTask loadData = new LoadRatDataTask();
        loadData.attachViews(callback, data, filter);
        loadData.start();;
    }

    @Override
    public void createRatData(int key, String createdDateTime, String locationType, int incidentZip,
                              String incidentAddress, String city, String borough, double latitude,
                              double longitude) {
        RatDataDAO.createRatData(key, createdDateTime, locationType, incidentZip,
                incidentAddress, city, borough, latitude, longitude);
    }

    @Override
    public void deleteRatData(RatData data) {
        RatDataDAO.deleteRatData(connection, data.getKey());
    }


    @Override
    public List<RatData> getAllRatData() {
        return RatDataDAO.getAllRatData(connection);
    }

    /**
     * Filters initial list of RatData Objects
     * @param filter the filters used to select certain RatData Objects
     * @return a list of filtered RatData Objects
     */
    public List<RatData> getFilteredRatData(RatFilter filter) {
        // return RatDataDAO.applyFilters(RatDataDAO.getAllRatData(db), filters);
        return RatDataDAO.getFilteredRatData(connection, filter.getPredicates());
    }

// --Commented out by Inspection START (11/13/2017 1:34 AM):
//    @Override
//    public RatData findRatDataByKey(int key) {
//        return RatDataDAO.findRatDataByKey(db, key);
//    }
// --Commented out by Inspection STOP (11/13/2017 1:34 AM)
}
