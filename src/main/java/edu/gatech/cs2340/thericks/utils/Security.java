package edu.gatech.cs2340.thericks.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import edu.gatech.cs2340.thericks.models.Login;

/**
 * Created by Ben Lashley on 9/21/2017.
 *
 * Class handles security for login system, including password encryption and login validation.
 */

public class Security {

    // Number of iterations for password encryption
    private static final int ITERATIONS = 20000;

    // Size in bytes of salt
    private static final int SALT_LENGTH = 16;

    /** Size in bytes of encrypted key **/
    private static final int KEY_LENGTH = 32;

    // See validatePassword() for complete password criteria
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 32;

    private static final int MIN_USERNAME_LENGTH = 6;
    private static final int MAX_USERNAME_LENGTH = 32;

    // Special characters allowed for password
    private static final String SPECIAL_CHARACTERS = "!@#$%&?_";


    /**
     * Generates a secure, random salt for encryption.
     * @return salt
     */
    public static String generateSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] saltBytes = new byte[SALT_LENGTH];
        sr.nextBytes(saltBytes);
        return bytesToString(saltBytes);
    }


    /**
     * Creates encrypted password from user's password and random salt.
     *
     * @param password user's (unencrypted) password
     * @param salt randomly generated salt
     * @return encrypted key as byte[]
     */
    public static String createEncryptedPassword(String password, String salt) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), ITERATIONS,
                KEY_LENGTH * 8);
        SecretKeyFactory f = null;
        try {
            f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            Log.e("Login", "Algorithm requested for encryption does not exist.");
            e.printStackTrace();
        }
        if (f == null) {
            return null;
        }
        try {
            byte[] pw = f.generateSecret(spec).getEncoded();
            return bytesToString(pw);
        } catch (InvalidKeySpecException e) {
            Log.e("Login", "Invalid specifications for secure key.");
            e.printStackTrace();
        }
        Log.e("Login", "Failed to create secure key.");
        return null;
    }


    /**
     * Checks if a new user's desired username is valid.
     *
     * @param username the desired username
     * @return true if valid, false if not
     */
    public static boolean validateUsername(String username) {
        // Check if username is valid
        if ((username == null) || username.isEmpty()) {
            return false;
        } else if ((username.length() < MIN_USERNAME_LENGTH) || (username.length()
                > MAX_USERNAME_LENGTH)) {
            return false;
        }
        return true;
    }

    /**
     * Checks if password meets criteria to be considered valid.
     * Criteria (must meet all):
     *  - at least one digit
     *  - at least one lowercase letter
     *  - at least one uppercase letter
     *  - no whitespace
     *  - length between min and max (inclusive)
     *
     * @param pw the password to validate
     * @return true if password is valid, false if not
     */
    public static boolean validatePassword(CharSequence pw) {
        if (pw == null) {
            return false;
        }
        if ((pw.length() < MIN_PASSWORD_LENGTH) || (pw.length() > MAX_PASSWORD_LENGTH)) {
            return false;
        }

        boolean hasInvalidChar = false;
        boolean hasDigit = false;
        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasWhiteSpace = false;

        for (int i = 0; i < pw.length(); i++) {
            char c = pw.charAt(i);
            if (!hasDigit && Character.isDigit(c)) {
                hasDigit = true;
            } else if (!hasLower && Character.isLowerCase(c)) {
                hasLower = true;
            } else if (!hasUpper && Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isWhitespace(c)) {
                hasWhiteSpace = true;
                break;
            } else if (!Character.isLetterOrDigit(c) && (SPECIAL_CHARACTERS.indexOf(c) < 0)) {
                hasInvalidChar = true;
                break;
            }
        }
        return hasDigit && hasLower && hasUpper && !hasWhiteSpace && !hasInvalidChar;
    }

    /**
     * Check if provided password gives correct encryption (i.e. password entered is correct)
     *
     * @param pw the password to check
     * @param login the login object
     * @return true if the password is correct, false if not
     */
    public static boolean checkPassword(String pw, Login login) {
        // Check that pw is valid
        if (!validatePassword(pw)) {
            return false;
        }
        String attempt = createEncryptedPassword(pw, login.getSalt());

        Log.d("Login", "Attempted password encrypted to: " + attempt);
        Log.d("Login", "Actual encrypted password: " + login.getSecurePassword());

        return (attempt != null) && attempt.equals(login.getSecurePassword());
    }

    /**
     * Converts array of bytes into a hexadecimal string.
     *
     * @param arr array of bytes
     * @return Secure password as a readable hexadecimal string
     */
    private static String bytesToString(byte[] arr) {
        if (arr == null) {
            return "INVALID";
        }
        final StringBuilder builder = new StringBuilder();
        for (byte b : arr) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
