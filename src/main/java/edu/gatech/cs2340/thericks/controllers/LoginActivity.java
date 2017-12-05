package edu.gatech.cs2340.thericks.controllers;

import java.io.IOException;

import edu.gatech.cs2340.thericks.database.UserDatabase;
import edu.gatech.cs2340.thericks.models.User;
import edu.gatech.cs2340.thericks.models.UserDataSource;
import edu.gatech.cs2340.thericks.utils.Log;
import edu.gatech.cs2340.thericks.utils.ResultObtainedCallback;
import edu.gatech.cs2340.thericks.utils.Security;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Provides a username and password field for the user to enter their
 * username and password into, then passes that data to Security and
 * the UserTable to provide a login function
 */
public class LoginActivity extends VBox {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @FXML
    private TextField username;
    
    @FXML
    private TextField password;
    
    @FXML
    private Button login;
    
    @FXML
    private Button cancel;
    
    @FXML
    private Text error;
    
    private ResultObtainedCallback<User> callback;

    /**
     * Creates a new Login activity
     * @param call
     */
    public LoginActivity(ResultObtainedCallback<User> call) {
    	callback = call;
    	
    	setPadding(new Insets(5));
    	setSpacing(5);
    	setAlignment(Pos.CENTER);
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/activity_login.fxml"));
    	loader.setController(this);
    	loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Initializes the login activity
     * SHOULD ONLY BE REFLECTIVELY CALLED BY AN FXMLLOADER
     */
    public void initialize() {

        error.setVisible(false);

        login.setOnAction(e -> {
                Log.d(TAG, "Attempt to log into account");

                String enteredUsername = username.getText().toString();
                String enteredPassword = password.getText().toString();

                UserDataSource db = new UserDatabase();
                User u = db.getUserByUsername(enteredUsername);
                if ((u != null) && (u.getLogin() != null)) {
                	
                    Log.d(TAG, "Checking password for login: " + u.getLogin());
                    
                    if (Security.checkPassword(enteredPassword, u.getLogin())) {
                        
                    	Log.d(TAG, "Successfully logged into user account: " + u.getUsername());
                        
                    	callback.onResultObtained(u);
                    	
                    } else {
                        Log.d(TAG, "Incorrect password");
                        error.setVisible(true);
                    }
                } else {
                    Log.d(TAG, "Incorrect username");
                    error.setVisible(true);
                }
            }
        );
        
        cancel.setOnAction(e -> {
        	
        	Log.d(TAG, "Canceling login");
        	callback.onResultObtained(null);
        	
        });
    }
}
