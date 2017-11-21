package edu.gatech.cs2340.thericks.controllers;

import java.io.IOException;

import edu.gatech.cs2340.thericks.models.User;
import edu.gatech.cs2340.thericks.utils.ResultObtainedCallback;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Ben Lashley on 9/20/2017.
 *
 * Screen that appears when app starts. Allows user to login or register for an account.
 */

public class WelcomeActivity extends VBox {
	
	private static final int HEIGHT = 200;
	private static final int WIDTH = 300;
	
	@FXML
	Button login;
	
	@FXML
	Button register;
	
	private Stage mainStage;
	private ResultObtainedCallback<User> callback;
	
	public WelcomeActivity(Stage stage, ResultObtainedCallback<User> call) {
		mainStage = stage;
		callback = call;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("activity_welcome.fxml"));
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
    	Stage stage = new Stage();
    	stage.setHeight(HEIGHT);
    	stage.setWidth(WIDTH);
    	stage.initModality(Modality.APPLICATION_MODAL);
    	stage.initOwner(mainStage);
    	
    	login.setOnAction(e -> {
//          Context context = v.getContext();
//          Intent intent = new Intent(context, LoginActivity.class);
//          context.startActivity(intent);
	    });
	
	    register.setOnAction(e -> {
	    	RegisterActivity registerActivity = new RegisterActivity(new ResultObtainedCallback<User>() {

				@Override
				public void onResultObtained(User result) {
					callback.onResultObtained(result);
				}
				
			});
	    	stage.close();
	    });
	    
	    stage.setScene(new Scene(this));
	    stage.showAndWait();
    }
}
