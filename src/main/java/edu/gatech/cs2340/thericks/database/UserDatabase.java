package edu.gatech.cs2340.thericks.database;

import java.sql.Connection;
import java.util.List;

import edu.gatech.cs2340.thericks.models.Privilege;
import edu.gatech.cs2340.thericks.models.User;
import edu.gatech.cs2340.thericks.models.UserDataSource;
import edu.gatech.cs2340.thericks.utils.Log;

/**
 * Class representing a database of users.  This class is used as an interface between the user
 * database and the activities.
 *
 * Created by Ben Lashley on 10/31/2017.
 */

public class UserDatabase implements UserDataSource {
    private static final String TAG = UserDatabase.class.getSimpleName();
    
    private Connection connection;
    private UserDAO dao;

    /**
     * Constructor that initializes SQLite Database to a UserDatabaseHandler
     */
    public UserDatabase() {
        connection = DatabaseHandler.provideDatabaseConnection();
        dao = new UserDAO(connection);
    }

    @Override
    public void createUser(String username, String password, Privilege privilege) {
        Log.d(TAG, "Creating user");
        dao.createUser(username, password, privilege);
    }

    @Override
    public void deleteUser(String username) {
        dao.deleteUser(connection, username);
    }


    @Override
    public User getUserByUsername(String username) {
        return dao.getUserByUsername(connection, username);
    }


    @Override
    public List<User> getAllUsers() {
        return dao.getAllUsers(connection);
    }

}
