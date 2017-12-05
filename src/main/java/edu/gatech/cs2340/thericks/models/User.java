package edu.gatech.cs2340.thericks.models;

import edu.gatech.cs2340.thericks.utils.Log;
import edu.gatech.cs2340.thericks.utils.Security;

/**
 * Created by Ben Lashley on 9/18/2017.
 *
 * Class represents a user of the rat-tracker application.
 */

public class User {
    private static final String TAG = User.class.getSimpleName();

    /** User's secure login data **/
    private final Login loginInfo;

    /** User's privilege status - either normal or admin**/
    private final Privilege privilege;

    /** User's login status **/
    private boolean loggedIn;

    /**
     * Constructor for a new User
     * @param username username
     * @param password password
     * @param salt salt used for hashing
     * @param privilege privilege
     */
    public User(String username, String password, String salt, Privilege privilege) {
        // Generate secure login information
        loginInfo = new Login(username, password, salt);
        loggedIn = false;
        this.privilege = privilege;
        Log.d(TAG, username + password + salt + privilege.toString());
    }

    /**
     * Constructor for a new User without salt for hashing
     * @param username username
     * @param password password
     * @param privilege privilege
     */
    public User(String username, String password, Privilege privilege) {
        this(username, password, Security.generateSalt(), privilege);
    }

    /**
     * Getter for the user's username
     * @return the username
     */
    public String getUsername() {
        if (loginInfo != null) {
            return loginInfo.getUsername();
        } else {
            return "";
        }
    }
    
    public Privilege getPrivilege() {
    	return privilege;
    }

    /**
     * Logs user in
     */
    public void login() {
        loggedIn = true;
    }

    /**
     * Logs user out
     */
    public void logout() {
        loggedIn = false;
    }

    /**
     * Determines if the user is logged in or out
     * @return boolean for user's login status
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Getter for the user's login status
     * @return the user's login info
     */
    public Login getLogin() {
        return loginInfo;
    }
}