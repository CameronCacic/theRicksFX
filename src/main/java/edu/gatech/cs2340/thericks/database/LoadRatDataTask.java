package edu.gatech.cs2340.thericks.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import edu.gatech.cs2340.thericks.models.RatData;
import edu.gatech.cs2340.thericks.models.RatFilter;
import edu.gatech.cs2340.thericks.utils.DataLoadedCallback;
import edu.gatech.cs2340.thericks.utils.Log;
import javafx.concurrent.Task;

/**
 * Async task that loads in rat data from the csv file and inserts it into a SQLite database.
 *
 * Created by Ben Lashley on 10/18/2017.
 */
public class LoadRatDataTask extends Task<Long> {
    private static final String TAG = LoadRatDataTask.class.getSimpleName();

    private static boolean isLoadingData = false;
    private static boolean doneLoading = false;

    private DataLoadedCallback callback;
    private List<RatData> data;
    private List<Predicate<RatData>> filters;

    // Indexes for relevant columns in raw/rat_data.csv
    private static final int UNIQUE_KEY_NUMBER = 0;
    private static final int CREATED_TIME_NUMBER = 1;
    private static final int LOCATION_TYPE_NUMBER = 7;
    private static final int INCIDENT_ZIP_NUMBER = 8;
    private static final int INCIDENT_ADDRESS_NUMBER = 9;
    private static final int CITY_NUMBER = 16;
    private static final int BOROUGH_NUMBER = 23;
    private static final int LATITUDE_NUMBER = 49;
    private static final int LONGITUDE_NUMBER = 50;

    /**
     * Returns true if data is ready to start loading into the database for the first time
     * @return true/false is data is ready
     */
    static boolean isReady() {
        return !isLoadingData;
    }

    /**
     * Provides views from an activity calling the task, allowing for the UI to be asynchronously
     * updated in succeeded
     * @param callback the callback to notify when the data finishes loading
     * @param data the List to fill with ratdata
     * @param filter the filter to filter the ratdata with
     */
    LoadRatDataTask(DataLoadedCallback callback, List<RatData> data, RatFilter filter) {
        this.callback = callback;
        this.data = data;
        this.filters = new ArrayList<>(filter.getPredicates());
    }

	@Override
	protected Long call() throws Exception {
		isLoadingData = true;
        long lineCount = 0;
        if (!doneLoading) {
			try {
				Log.d(TAG, "Reading in the csv");
				InputStream input = getClass().getResourceAsStream("/raw/rat_data.csv");
				BufferedReader br = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));

				DatabaseHandler.provideDatabaseConnection().setAutoCommit(false);
				RatDataDAO dao = new RatDataDAO(DatabaseHandler.provideDatabaseConnection());

				String line;
				br.readLine(); //get rid of header line
				line = br.readLine();
				while (line != null) {
					lineCount++;
					String[] tokens = line.split(",", -1);

					int key;
					int incidentZip;
					double longitude;
					double latitude;
					// Record relevant data from tokens.
					try {
						key = Integer.parseInt(tokens[UNIQUE_KEY_NUMBER]);
					} catch (NumberFormatException e) {
						key = 0;
					}
					String createdDateTime = tokens[CREATED_TIME_NUMBER];
					String locationType = tokens[LOCATION_TYPE_NUMBER];
					try {
						incidentZip = Integer.parseInt(tokens[INCIDENT_ZIP_NUMBER]);
					} catch (NumberFormatException e) {
						incidentZip = 0;
					}
					String incidentAddress = tokens[INCIDENT_ADDRESS_NUMBER];
					String city = tokens[CITY_NUMBER];
					String borough = tokens[BOROUGH_NUMBER];
					try {
						latitude = Double.parseDouble(tokens[LATITUDE_NUMBER]);
					} catch (NumberFormatException e) {
						latitude = 0;
					}
					try {
						longitude = Double.parseDouble(tokens[LONGITUDE_NUMBER]);
					} catch (NumberFormatException e) {
						longitude = 0;
					}
					// Add new rat data to database
					dao.createRatData(key, createdDateTime, locationType, incidentZip, incidentAddress, city, borough,
							latitude, longitude);
					Log.v(TAG, "Rat data " + lineCount + " read in");
					line = br.readLine();
				}
				br.close();
				DatabaseHandler.provideDatabaseConnection().commit();
				DatabaseHandler.provideDatabaseConnection().setAutoCommit(true);
				doneLoading = true;

			} catch (IOException e) {
				Log.e(TAG, "Error reading rat data", e);
			} catch (SQLException e1) {
				Log.e(TAG, "Error communicating with database", e1);
			} 
		}
		if (data != null) {
            data.clear();
            RatDatabase db = new RatDatabase();
            data.addAll(db.getFilteredRatData(new RatFilter(filters)));
        }
		
		return lineCount;
	}
	
	@Override
	protected void succeeded() {
		super.succeeded();
		Log.d(TAG, "Loaded " + getValue() + " rat data entries");
        // Done loading data
        isLoadingData = false;
        // Update passed in UI
        
        if (callback != null) {
        	callback.notifyDataLoaded();
        }
	}
}
