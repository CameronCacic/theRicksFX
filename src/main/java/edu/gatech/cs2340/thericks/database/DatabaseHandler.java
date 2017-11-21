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

final class DatabaseHandler {
    //private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "rat_data.db";

    private static final DatabaseHandler instance =
            new DatabaseHandler();

    private Connection connection;
    
    /**
     * Creates and/or opens a database that will be used for reading and writing
     * @return writable database
     */
    static synchronized Connection provideDatabaseConnection() {
        return instance.getDatabaseConnection();
    }

    private DatabaseHandler() {
        try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public Connection getDatabaseConnection() {
    	return connection;
    }
}
