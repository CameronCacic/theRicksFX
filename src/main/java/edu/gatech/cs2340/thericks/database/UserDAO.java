package edu.gatech.cs2340.thericks.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.gatech.cs2340.thericks.models.Privilege;
import edu.gatech.cs2340.thericks.models.User;
import edu.gatech.cs2340.thericks.utils.Log;
import edu.gatech.cs2340.thericks.utils.Security;

/**
 * Class handling direct access with the SQLite database.  Provides the low-level implementation of
 * UserDatabase's methods for adding, getting, and removing users.
 *
 * Created by Ben Lashley on 10/31/2017.
 */

class UserDAO {
    private static final String TAG = UserDAO.class.getSimpleName();

    private static final String TABLE_USERS = "user_data";

    // Names for user data table columns
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_SALT = "salt";
    private static final String COLUMN_PRIVILEGE= "privilege";
    private static final String ALL_COLUMNS = "(" + COLUMN_USERNAME + ","
    											+ COLUMN_PASSWORD + ","
    											+ COLUMN_SALT + ","
    											+ COLUMN_PRIVILEGE + ")";

    private PreparedStatement insertOrReplaceStatement;

    /**
     * Create a user table in the database
     * @param sqLiteDatabase Database where table is stored
     */
    UserDAO(Connection connection) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + "(" +
                COLUMN_USERNAME + " TEXT PRIMARY KEY, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_SALT + " TEXT, " +
                COLUMN_PRIVILEGE + " INTEGER" +
                ");";
        try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
			insertOrReplaceStatement = connection.prepareStatement("INSERT OR REPLACE INTO " 
																	+ TABLE_USERS
																	+ " " + ALL_COLUMNS
																	+ " VALUES (?,?,?,?)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    /**
     * Insert new user into database if the user does not exist. If the user already exists,
     * replace the existing data.
    */
    void createUser(String username, String password, Privilege privilege) {

        if (insertOrReplaceStatement != null) {
    		try {
    			String salt = Security.generateSalt();
				insertOrReplaceStatement.setString(1, username);
				insertOrReplaceStatement.setString(2, Security.createEncryptedPassword(password, salt));
				insertOrReplaceStatement.setString(3, salt);
				insertOrReplaceStatement.setInt(4, privilege.ordinal());
				insertOrReplaceStatement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    }


    /**
     * Convert result's current position into a User Object
     * @param result the ResultSet for traversing the table
     * @return a new User Object
     */
    private User cursorToUser(ResultSet result) {
    	User rtnUser = null;
    	try {
    		Privilege privilege = Privilege.values()[result.getInt(COLUMN_PRIVILEGE)];
    		String username = result.getString(COLUMN_USERNAME);
    		String password = result.getString(COLUMN_PASSWORD);
    		String salt = result.getString(COLUMN_SALT);
    		Log.d(TAG, String.format("Cursor at user with username: %s, password: %s, salt: %s,"
                    + " privilege: %s\n", username, password, salt, privilege));
    		return new User(username, password, salt, privilege);
    	} catch (SQLException e) {
			e.printStackTrace();
      }
        return rtnUser;
    }


    /**
     * Removes the user with the provided key
     *
     * @param connection the connection to the database to search in
     * @param username the username of the user to delete
     */
    void deleteUser(Connection connection, String username) {
    	try {
			Statement statement = connection.createStatement();
			statement.executeUpdate("DELETE FROM " + TABLE_USERS
					+ " WHERE " + COLUMN_USERNAME + " = '" + username + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }



    /**
     * Get single user by id; returns null if user is not found
     * @param db database to search in
     * @param username the username of the user to search for
     * @return user with specified id, null if none found
     */
    User getUserByUsername(Connection connection, String username) {
        User user = null;
        String query = "SELECT FROM " + TABLE_USERS
        		+ " WHERE " + COLUMN_USERNAME + " = '" + username + "'";
        
        try {
        	Statement statement = connection.createStatement();
        	ResultSet result = statement.executeQuery(query);
        	if (result.next()) {
        		user = cursorToUser(result);
        	}
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        
        if (user != null) {
            Log.d(TAG, "Fetched user with login: " + user.getLogin());
        }
        return user;
    }

    /**
     * Get all users as a list.
     *
     * @param db the database to search in
     * @return all users in a list
     */
    List<User> getAllUsers(Connection connection) {
        List<User> userList = new ArrayList<>();
        String selectAllQuery = "SELECT * FROM " + TABLE_USERS;

        try {
        	Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(selectAllQuery);

            // Loop through all rows and add as new user instance
			while (result.next()) {
			    // Create user from values of the current row
			    User user = cursorToUser(result);
			    // Add user to list
			    userList.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
	
        return userList;
    }
}
