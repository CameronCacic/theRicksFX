package edu.gatech.cs2340.thericks.controllers;

import edu.gatech.cs2340.thericks.models.RatFilter;
import edu.gatech.cs2340.thericks.models.User;
import edu.gatech.cs2340.thericks.utils.NewFilterCallback;
import edu.gatech.cs2340.thericks.utils.ResultObtainedCallback;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainActivity extends Application {
	
	private static final int POPUP_HEIGHT = 200;
	private static final int POPUP_WIDTH = 300;
	
	private static Stage primaryStage;
	private static BorderPane mainPane;

	@Override
	public void start(Stage pStage) {
		primaryStage = pStage;
		
		mainPane = new BorderPane();
		
		primaryStage.setScene(new Scene(mainPane));
		primaryStage.setMaximized(true);
		primaryStage.show();
    	
    	RatFilter filter = RatFilter.getDefaultInstance();
    	User[] user = new User[1];
    	
    	NewFilterCallback[] filterCallback = new NewFilterCallback[0];
    	
    	ResultObtainedCallback<Integer> filterResult = new ResultObtainedCallback<Integer>() {

			@Override
			public void onResultObtained(Integer result) {
				if (result == RESULT_OK) {
					if (filterCallback[0] != null) {
						filterCallback[0].notifyFilterUpdated();
					}
				}
			}
			
		};
		
    	ResultObtainedCallback<Integer> dashboardResult = new ResultObtainedCallback<Integer>() {

			@Override
			public void onResultObtained(Integer result) {
				switch (result) {
				case RESULT_MAP:
					MapActivity mapActivity = new MapActivity(filter);
					filterCallback[0] = mapActivity;
					mainPane.setCenter(mapActivity);
					break;
				case RESULT_GRAPH:
					GraphActivity graphActivity = new GraphActivity(filter);
					filterCallback[0] = graphActivity;
					mainPane.setCenter(graphActivity);
					break;
				case RESULT_DATA_LIST:
					break;
				case RESULT_PROFILE:
					break;
				case RESULT_SETTINGS:
					break;
				case RESULT_REPORT:
					break;
				case RESULT_LOGOUT:
					filterCallback[0] = null;
					user[0] = null;
					// filter.reset();
					mainPane = new BorderPane();
					user[0] = loginSequence();
					if (user[0] != null) {
						mainPane.setLeft(new DashboardActivity(user[0], dashboardResult));
					}
					break;
				case RESULT_FILTER:
					FilterActivity filterActivity = new FilterActivity(filter, user[0], filterResult);
					mainPane.setRight(filterActivity);
					break;
				}
			}
			
		};
		
		user[0] = loginSequence();
		if (user[0] != null) {
			mainPane.setLeft(new DashboardActivity(user[0], dashboardResult));
		}
	}
	
	public static User loginSequence() {
		Stage stage = new Stage();
    	stage.setHeight(POPUP_HEIGHT);
    	stage.setWidth(POPUP_WIDTH);
    	stage.initModality(Modality.APPLICATION_MODAL);
    	stage.initOwner(primaryStage);
    	
    	User[] user = new User[1];
    	
    	ResultObtainedCallback<User> getUserResult = new ResultObtainedCallback<User>() {

			@Override
			public void onResultObtained(User result) {
				user[0] = result;
				stage.close();
			}
			
		};
    	
		WelcomeActivity welcomeActivity = new WelcomeActivity(new ResultObtainedCallback<Integer>() {
			
			@Override
			public void onResultObtained(Integer result) {
				
				if (result == RESULT_LOGIN) {
					
					LoginActivity loginActivity = new LoginActivity(getUserResult);
					stage.setScene(new Scene(loginActivity));
					
				} else if (result == RESULT_REGISTER) {
					
					RegisterActivity registerActivity = new RegisterActivity(getUserResult);
					stage.setScene(new Scene(registerActivity));
					
				} else {
					
					primaryStage.close();
					
				}
			}
			
		});
		stage.setScene(new Scene(welcomeActivity));
		stage.showAndWait();
		return user[0];
	}
	
	public static void main(String[] args) {
    	launch(args);
    }
}
