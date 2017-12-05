package edu.gatech.cs2340.thericks.models;

import java.io.Serializable;

/**
 * Created by Ben Lashley on 9/18/2017.
 *
 * Class represents a single user's login information.  It implements serializable so it can be
 * passed as a parcel from the login screen.
 */
public class Login implements Serializable {

    /**
	 * Serialized ID
	 */
	private static final long serialVersionUID = -1334176625972695133L;

	/** Username for login **/
    private final String username;

    /** The hashed password for the login **/
    private final String securePassword;

    /** Random salt for hashing **/
    private final String salt;


    /**
     * Creates a new login
     * @param username the username
     * @param password the password
     * @param salt the password salt
     */
    Login(String username, String password, String salt) {
        this.username = username;
        this.salt = salt;
        securePassword = password;
    }

    /**
     * Getter for username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for password
     * @return a secure password
     */
    public String getSecurePassword() {
        return securePassword;
    }

    /**
     * Getter for salt used for hashing
     * @return salt
     */
    public String getSalt() {
        return salt;
    }

    @Override
    public String toString() {
        return String.format("Username: %s\nEncrypted Password: %s\n", username,
                getSecurePassword());
    }
}
