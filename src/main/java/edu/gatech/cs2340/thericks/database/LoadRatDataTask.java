package edu.gatech.cs2340.thericks.database;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import edu.gatech.cs2340.thericks.R;
import edu.gatech.cs2340.thericks.models.RatData;
import edu.gatech.cs2340.thericks.models.RatFilter;

/**
 * Async task that loads in rat data from the csv file and inserts it into a SQLite database.
 *
 * Created by Ben Lashley on 10/18/2017.
 */

class LoadRatDataTask extends AsyncTask<SQLiteDatabase, Void, Long> {
    private static final String TAG = LoadRatDataTask.class.getSimpleName();

    private static boolean isLoadingData = false;
    private static boolean doneLoading = false;

    private ArrayAdapter adapter;
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

    @Override
    protected Long doInBackground(SQLiteDatabase ... dbs) {
        isLoadingData = true;
        SQLiteDatabase db = dbs[0];
        db.beginTransaction();
        long lineCount = 0;
        try {
            InputStream input = RatTrackerApplication.getAppContext().getResources()
                    .openRawResource(R.raw.rat_data);
            BufferedReader br = new BufferedReader(new InputStreamReader(input,
                    StandardCharsets.UTF_8));

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
                RatDataDAO.createRatData(db, key, createdDateTime, locationType, incidentZip,
                        incidentAddress, city, borough, latitude, longitude);
                line = br.readLine();
            }
            br.close();

            db.setTransactionSuccessful();
        } catch (IOException e) {
            Log.e(TAG, "error reading rat data", e);
        } catch (SQLException e) {
            Log.e(TAG, "error inserting into database", e);
        } finally {
            // Let database know we are done inserting data
            db.endTransaction();
        }
        return lineCount;
    }

    /**
     * Mark that data is done loading and update any provided views
     * @param lineCount the number of loaded lines
     */
    @Override
    protected void onPostExecute(Long lineCount) {
        Log.d(TAG, "Loaded " + lineCount + " rat data entries");
        // Done loading data
        doneLoading = true;
        isLoadingData = false;
        // Update passed in UI
        if ((data != null) && (adapter != null)) {
            data.clear();
            RatDatabase db = new RatDatabase();
            data.addAll(db.getFilteredRatData(new RatFilter(filters)));
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Returns true if data is ready to start loading into the database for the first time
     * @return true/false is data is ready
     */
    static boolean isReady() {
        return !isLoadingData && !doneLoading;
    }

    /**
     * Provides views from an activity calling the task, allowing for the UI to be asynchronously
     * updated in onPostExecute
     * @param a the ArrayAdapter that returns the views for each RatData Object
     * @param data the list of RatData Objects whose views will be added
     * @param filter the filters used to select certain RatData Objects
     */
    void attachViews(ArrayAdapter a, List<RatData> data, RatFilter filter) {
        adapter = a;
        this.data = new ArrayList<>(data);
        this.filters = new ArrayList<>(filter.getPredicates());
    }
}
