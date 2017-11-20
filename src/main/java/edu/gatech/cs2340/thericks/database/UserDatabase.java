package edu.gatech.cs2340.thericks.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import edu.gatech.cs2340.thericks.models.Privilege;
import edu.gatech.cs2340.thericks.models.User;
import edu.gatech.cs2340.thericks.models.UserDataSource;

/**
 * Class representing a database of users.  This class is used as an interface between the user
 * database and the activities.
 *
 * Created by Ben Lashley on 10/31/2017.
 */

public class UserDatabase implements UserDataSource {
    private static final String TAG = UserDatabase.class.getSimpleName();
    private SQLiteDatabase db;

    /**
     * Constructor that initializes SQLite Database to a UserDatabaseHandler
     */
    public UserDatabase() {
        open();
    }

    private void open() {
        db = UserDatabaseHandler.provideWritableDatabase();
    }

    @Override
    public void createUser(String username, String password, Privilege privilege) {
        Log.d(TAG, "Creating user");
        UserDAO.createUser(db, username, password, privilege);
    }

    @Override
    public void deleteUser(String username) {
        UserDAO.deleteUser(db, username);
    }


    @Override
    public User getUserByUsername(String username) {
        return UserDAO.getUserByUsername(db, username);
    }


    @Override
    public List<User> getAllUsers() {
        return UserDAO.getAllUsers(db);
    }

}
