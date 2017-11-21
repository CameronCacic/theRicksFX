package edu.gatech.cs2340.thericks.controllers;

import edu.gatech.cs2340.thericks.models.User;
import edu.gatech.cs2340.thericks.utils.ResultObtainedCallback;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainActivity extends Application {

	@Override
	public void start(Stage primaryStage) {
		BorderPane mainPane = new BorderPane();
		
		primaryStage.setScene(new Scene(mainPane));
		primaryStage.setMaximized(true);
		primaryStage.show();
		
		WelcomeActivity welcomeActivity = new WelcomeActivity(primaryStage, new ResultObtainedCallback<User>() {
			
			@Override
			public void onResultObtained(User result) {
				mainPane.setCenter(new DashMapActivity(result));
			}
			
		});
	}
	
	public static void main(String[] args) {
    	launch(args);
    }
}
