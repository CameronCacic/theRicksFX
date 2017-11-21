package edu.gatech.cs2340.thericks.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton class that handles the rat database connection.
 * Provides a single connection throughout the
 * application's life cycle.
 *
 * Created by Ben Lashley on 10/9/2017.
 */

final class RatDatabaseHandler {
    //private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "rat_data.db";

    private static final RatDatabaseHandler instance =
            new RatDatabaseHandler();

    private Connection connection;
    
    /**
     * Creates and/or opens a database that will be used for reading and writing
     * @return writable database
     */
    static synchronized Connection provideDatabaseConnection() {
        return instance.getDatabaseConnection();
    }

    private RatDatabaseHandler() {
        try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public Connection getDatabaseConnection() {
    	return connection;
    }


    public void onCreate(Connection connection) {
        RatDataDAO.onCreate(connection);
    }


    public void onUpgrade(Connection connection, int oldVersion, int newVersion) {
        RatDataDAO.onUpgrade(connection, oldVersion, newVersion);
    }
}
