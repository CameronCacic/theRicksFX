package edu.gatech.cs2340.thericks.controllers;

import java.io.IOException;

import edu.gatech.cs2340.thericks.utils.ResultObtainedCallback;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * Created by Ben Lashley on 9/20/2017.
 *
 * Screen that appears when app starts. Allows user to login or register for an account.
 */
public class WelcomeActivity extends VBox {
	
	@FXML
	Button login;
	
	@FXML
	Button register;
	
	private ResultObtainedCallback<Integer> callback;
	
	public WelcomeActivity(ResultObtainedCallback<Integer> call) {
		callback = call;
		
		setSpacing(15);
		setAlignment(Pos.CENTER);
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/activity_welcome.fxml"));
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
    	
    	login.setOnAction(e -> {
    		callback.onResultObtained(ResultObtainedCallback.RESULT_LOGIN);
	    });
	
	    register.setOnAction(e -> {
	    	callback.onResultObtained(ResultObtainedCallback.RESULT_REGISTER);
	    });
    }
}
