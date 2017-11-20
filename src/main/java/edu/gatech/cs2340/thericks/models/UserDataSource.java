package edu.gatech.cs2340.thericks.models;

import java.util.List;

/**
 * Interface for a source of user data with basic operations for managing a user table.
 *
 * Created by Ben Lashley on 10/31/2017.
 */

public interface UserDataSource {
    /**
     * Creates a user
     * @param username user's username
     * @param password user's password
     * @param privilege privilege for user
     */
    @SuppressWarnings("unused")
    void createUser(String username, String password, Privilege privilege);

    /**
     * Deletes a user
     * @param username user's username
     */
    void deleteUser(String username);

    /**
     * Getter for a user
     * @param username user's username
     * @return the user with that username
     */
    User getUserByUsername(String username);

    /**
      * Getter for all users
      * @return a list of all users
      */
    List<User> getAllUsers();
}
