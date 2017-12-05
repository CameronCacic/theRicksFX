package edu.gatech.cs2340.thericks.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton class that handles the database connection.
 * Provides a single connection throughout the
 * application's life cycle.
 *
 * Created by Ben Lashley on 10/9/2017.
 */

public final class DatabaseHandler {
    //private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "rat_tracker_database.db";

    private static final DatabaseHandler instance =
            new DatabaseHandler();

    private Connection connection;
    
    /**
     * Creates and/or opens a database that will be used for reading and writing
     * @return writable database
     */
    static Connection provideDatabaseConnection() {
        return instance.getDatabaseConnection();
    }

    /**
     * Creates the singleton instance of DatabaseHandler
     */
    private DatabaseHandler() {
        try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Gets a valid database connection
     * @return database connection
     */
    public Connection getDatabaseConnection() {
    	return connection;
    }
    
    /**
     * Closes the database connection
     */
    public static void closeInstance() {
    	try {
			instance.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
 }
