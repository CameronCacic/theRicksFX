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
    
    private RatDataDAO dao;
    private Connection connection;

    /**
     * initializes the SQLite Database to a RatDatabaseHandler 
     */
    public RatDatabase() {
    	connection = DatabaseHandler.provideDatabaseConnection();
    	dao = new RatDataDAO(connection);
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
        }
        Log.d(TAG, "Creating new LoadRatDataTask");
        LoadRatDataTask loadData = new LoadRatDataTask(callback, data, filter);
        new Thread(loadData).start();
    }

    @Override
    public void createRatData(int key, String createdDateTime, String locationType, int incidentZip,
                              String incidentAddress, String city, String borough, double latitude,
                              double longitude) {
        dao.createRatData(key, createdDateTime, locationType, incidentZip,
                incidentAddress, city, borough, latitude, longitude);
    }

    @Override
    public void deleteRatData(RatData data) {
        dao.deleteRatData(connection, data.getKey());
    }


    @Override
    public List<RatData> getAllRatData() {
        return dao.getAllRatData(connection);
    }

    /**
     * Filters initial list of RatData Objects
     * @param filter the filters used to select certain RatData Objects
     * @return a list of filtered RatData Objects
     */
    List<RatData> getFilteredRatData(RatFilter filter) {
        // return RatDataDAO.applyFilters(RatDataDAO.getAllRatData(db), filters);
        return dao.getFilteredRatData(connection, filter.getPredicates());
    }

    @Override
    public RatData findRatDataByKey(int key) {
        return dao.findRatDataByKey(connection, key);
    }
}
