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
import javafx.scene.layout.VBox;

/**
 * Created by Cameron on 10/6/2017.
 * Dashboard activity for selecting what activity the user would like to engage in
 */
public class DashboardActivity extends VBox {

    private static final String TAG = MainActivity.class.getSimpleName();
    
    private static final double BUTTON_WIDTH = 300;
    private static final double BUTTON_HEIGHT = 75;

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
    
    @FXML
    private Button filterButton;

    /**
     * Creates a new Dashboard activity and loads the FXML
     * @param u the user to verify login on
     * @param call the callback to pass the user's selection back to
     */
    public DashboardActivity(User u, ResultObtainedCallback<Integer> call) {
    	callback = call;
    	user = u;
    	
    	setPadding(new Insets(5, 20, 5, 5));
    	setSpacing(20);
    	setAlignment(Pos.CENTER_LEFT);
    	setStyle("-fx-font-size: 32pt;");
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/activity_dashboard.fxml"));
    	loader.setController(this);
    	loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Initializes the dashboard
     * SHOULD ONLY BE REFLECTIVELY CALLED BY AN FXMLLOADER
     */
    public void initialize() {

        Log.d(TAG, "Entered dashboard activity");

        assert user != null;
        user.login();
        Log.d(TAG, user.getUsername() + " is logged in = " + user.isLoggedIn());
        
        mapButton.setPrefWidth(BUTTON_WIDTH);
        mapButton.setPrefHeight(BUTTON_HEIGHT);
        mapButton.setOnAction(e -> {
            Log.d(TAG, "Rat Map Button pressed");
            callback.onResultObtained(ResultObtainedCallback.RESULT_MAP);
        });

        graphButton.setPrefWidth(BUTTON_WIDTH);
        graphButton.setPrefHeight(BUTTON_HEIGHT);
        graphButton.setOnAction(e ->  {
            Log.d(TAG, "Graph button pressed");
            callback.onResultObtained(ResultObtainedCallback.RESULT_GRAPH);
        });

        listRatDataButton.setPrefWidth(BUTTON_WIDTH);
        listRatDataButton.setPrefHeight(BUTTON_HEIGHT);
        listRatDataButton.setOnAction(e -> {
            Log.d(TAG, "Rat Data List button pressed");
            callback.onResultObtained(ResultObtainedCallback.RESULT_DATA_LIST);
        });

        profileButton.setPrefWidth(BUTTON_WIDTH);
        profileButton.setPrefHeight(BUTTON_HEIGHT);
        profileButton.setOnAction(e -> {
            Log.d(TAG, "Profile button pressed");
            callback.onResultObtained(ResultObtainedCallback.RESULT_PROFILE);
        });
        
        settingsButton.setPrefWidth(BUTTON_WIDTH);
        settingsButton.setPrefHeight(BUTTON_HEIGHT);
        settingsButton.setOnAction(e -> {
        	Log.d(TAG, "Settings button pressed");
        	callback.onResultObtained(ResultObtainedCallback.RESULT_SETTINGS);
        });

        reportRatButton.setPrefWidth(BUTTON_WIDTH);
        reportRatButton.setPrefHeight(BUTTON_HEIGHT);
        reportRatButton.setOnAction(e -> {
            Log.d(TAG, "Report a Rat button pushed");
            callback.onResultObtained(ResultObtainedCallback.RESULT_REPORT);
        });

        logoutButton.setPrefWidth(BUTTON_WIDTH);
        logoutButton.setPrefHeight(BUTTON_HEIGHT);
        logoutButton.setOnAction(e -> {
            user.logout();
            Log.d(TAG, "Logout button pressed");
            callback.onResultObtained(ResultObtainedCallback.RESULT_LOGOUT);
        });

        filterButton.setPrefWidth(BUTTON_WIDTH);
        filterButton.setPrefHeight(BUTTON_HEIGHT);
        filterButton.setOnAction(e -> {
        	Log.d(TAG, "Filter button pressed");
        	callback.onResultObtained(ResultObtainedCallback.RESULT_FILTER);
        });
    }
}
