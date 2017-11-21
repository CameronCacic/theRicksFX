package edu.gatech.cs2340.thericks.controllers;

import edu.gatech.cs2340.thericks.models.User;
import edu.gatech.cs2340.thericks.utils.ResultObtainedCallback;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainActivity extends Application {
	
	private static final int POPUP_HEIGHT = 200;
	private static final int POPUP_WIDTH = 300;

	@Override
	public void start(Stage primaryStage) {
		BorderPane mainPane = new BorderPane();
		
		primaryStage.setScene(new Scene(mainPane));
		primaryStage.setMaximized(true);
		primaryStage.show();
		
		Stage stage = new Stage();
    	stage.setHeight(POPUP_HEIGHT);
    	stage.setWidth(POPUP_WIDTH);
    	stage.initModality(Modality.APPLICATION_MODAL);
    	stage.initOwner(primaryStage);
		
		WelcomeActivity welcomeActivity = new WelcomeActivity(new ResultObtainedCallback<Integer>() {
			
			@Override
			public void onResultObtained(Integer result) {
				
				ResultObtainedCallback<User> launchDashMap = new ResultObtainedCallback<User>() {

					@Override
					public void onResultObtained(User result) {
						mainPane.setCenter(new DashMapActivity(result));
						stage.close();
					}
					
				};
				
				if (result == RESULT_LOGIN) {
					
					LoginActivity loginActivity = new LoginActivity(launchDashMap);
					stage.setScene(new Scene(loginActivity));
					
				} else if (result == RESULT_REGISTER) {
					
					RegisterActivity registerActivity = new RegisterActivity(launchDashMap);
					stage.setScene(new Scene(registerActivity));
					
				} else {
					
					primaryStage.close();
					
				}
			}
			
		});
		stage.setScene(new Scene(welcomeActivity));
		stage.showAndWait();
	}
	
	public static void main(String[] args) {
    	launch(args);
    }
}
