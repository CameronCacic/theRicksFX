package edu.gatech.cs2340.thericks.controllers;

import java.io.IOException;

import edu.gatech.cs2340.thericks.database.UserDatabase;
import edu.gatech.cs2340.thericks.models.Privilege;
import edu.gatech.cs2340.thericks.models.User;
import edu.gatech.cs2340.thericks.utils.Log;
import edu.gatech.cs2340.thericks.utils.Security;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;

/**
 * Created by Ben Lashley on 9/19/2017.
 * Provides a window for the user to enter their data into to
 * create a new User. If username and password entered do not
 * meet the standards imposed by Security, a new account cannot
 * be created the user is prompted with what field is insufficient
 * and what they need to add to fix it
 */
public class RegisterActivity  {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    @FXML
    private TextField usernameEntry;
    
    @FXML
    private TextField passwordEntry;
    
    @FXML
    private TextField passwordReentry;
    
    @FXML
    private RadioButton normalPrivRadio;
    
    @FXML
    private RadioButton adminPrivRadio;

    @FXML
    private Text invalidUsername;
    
    @FXML
    private Text invalidPassword;
    
    @FXML
    private Text passwordMismatch;
    
    @FXML
    private Button createAccount;

    private UserDatabase db;

    public RegisterActivity() {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("activity_registration.fxml"));
    	loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    @FXML
    public void initialize() {

        db = new UserDatabase();

        // Hide warning messages
        invalidUsername.setVisible(false);
        invalidPassword.setVisible(false);
        passwordMismatch.setVisible(false);
        
        ToggleGroup group = new ToggleGroup();
        normalPrivRadio.setToggleGroup(group);
        adminPrivRadio.setToggleGroup(group);
        group.selectToggle(normalPrivRadio);

        createAccount.setOnAction(e -> {
            Log.d(TAG, "Attempting to create new user account");
            String username = usernameEntry.getText();
            String password = passwordEntry.getText();
            String reenteredPassword = passwordReentry.getText();
            Privilege user_privilege = null;
            boolean usernameTaken = (db.getUserByUsername(username) != null);
            boolean validEntries = true;

            if (!Security.validateUsername(username) || usernameTaken) {
                Log.d(TAG, "Username not valid");
                invalidUsername.setVisible(true);
                validEntries = false;
            } else {
                invalidUsername.setVisible(false);
            }

            if (!Security.validatePassword(password)) {
                invalidPassword.setVisible(true);
                validEntries = false;
            } else {
                invalidPassword.setVisible(false);
            }

            if (!password.equals(reenteredPassword)) {
                passwordMismatch.setVisible(true);
                validEntries = false;
            } else {
                passwordMismatch.setVisible(false);
            }

            if (group.getSelectedToggle().equals(adminPrivRadio)) {
                user_privilege = Privilege.ADMIN;
            } else if (group.getSelectedToggle().equals(normalPrivRadio)) {
                user_privilege = Privilege.NORMAL;
            } else {
                // No privilege button selected
                validEntries = false;
            }

            if (validEntries) {
                db.createUser(username, password, user_privilege);

                // User object to pass to dashboard activity.
                User u = new User(username, password, user_privilege);

//                Context context = v.getContext();
//                Intent intent = new Intent(context, DashMapActivity.class);
//                intent.putExtra("edu.gatech.cs2340.thericks.User", u);
//                context.startActivity(intent);
            }
        });
    }
}
