package edu.gatech.cs2340.thericks.database;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * Class for accessing application level context.  Used in database package to get context.
 *
 * Created by Ben Lashley on 10/18/2017.
 */

public class RatTrackerApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    /**
     * Getter for the app's context
     * @return the context
     */
    public static Context getAppContext() {
        return context;
    }
}
