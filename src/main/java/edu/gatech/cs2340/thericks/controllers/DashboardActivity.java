package edu.gatech.cs2340.thericks.controllers;

import java.io.IOException;

import edu.gatech.cs2340.thericks.models.User;
import edu.gatech.cs2340.thericks.utils.Log;
import edu.gatech.cs2340.thericks.utils.ResultObtainedCallback;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Created by Cameron on 10/6/2017.
 * Dash-map activity in dash mode provides numerous activity options for a logged in user
 * to engage in. Defaults to dash mode, upon selecting map, dash-map switches to map mode,
 * where users can view rat data displayed on a Google Map, filtered by date
 */
public class DashboardActivity extends VBox {

    private static final String TAG = MainActivity.class.getSimpleName();

    private User user;
    
    private ResultObtainedCallback<Integer> callback;
    
    @FXML
    private Button mapButton;
    
    @FXML
    private Button graphButton;
    
    @FXML
    private Button listRatDataButton;
    
    @FXML
    private Button profileButton;
    
    @FXML
    private Button settingsButton;
    
    @FXML
    private Button reportRatButton;
    
    @FXML
    private Button logoutButton;

    public DashboardActivity(User u, ResultObtainedCallback<Integer> call) {
    	callback = call;
    	user = u;
    	
    	setPadding(new Insets(5));
    	setSpacing(5);
    	setAlignment(Pos.CENTER_LEFT);
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/activity_dashboard.fxml"));
    	loader.setController(this);
    	loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    public void initialize() {

        Log.d(TAG, "Entered dashboard activity");

        assert user != null;
        user.login();
        Log.d(TAG, user.getUsername() + " is logged in = " + user.isLoggedIn());
        
        mapButton.setOnAction(e -> {
            Log.d(TAG, "Rat Map Button pressed");
            callback.onResultObtained(ResultObtainedCallback.RESULT_MAP);
        });

        graphButton.setOnAction(e ->  {
            Log.d(TAG, "Graph button pressed");
            callback.onResultObtained(ResultObtainedCallback.RESULT_GRAPH);
        });

        listRatDataButton.setOnAction(e -> {
            Log.d(TAG, "Rat Data List button pressed");
            callback.onResultObtained(ResultObtainedCallback.RESULT_DATA_LIST);
        });

        profileButton.setOnAction(e -> {
            Log.d(TAG, "Profile button pressed");
            callback.onResultObtained(ResultObtainedCallback.RESULT_PROFILE);
        });

        reportRatButton.setOnAction(e -> {
            Log.d(TAG, "Report a Rat button pushed");
            callback.onResultObtained(ResultObtainedCallback.RESULT_REPORT);
        });

        logoutButton.setOnAction(e -> {
            user.logout();
            Log.d(TAG, "Logout button pressed");
            callback.onResultObtained(ResultObtainedCallback.RESULT_LOGOUT);
        });
    }
}
