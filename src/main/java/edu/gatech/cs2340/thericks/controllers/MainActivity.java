package edu.gatech.cs2340.thericks.controllers;

import edu.gatech.cs2340.thericks.models.RatData;
import edu.gatech.cs2340.thericks.models.RatFilter;
import edu.gatech.cs2340.thericks.models.User;
import edu.gatech.cs2340.thericks.utils.NewFilterCallback;
import edu.gatech.cs2340.thericks.utils.ResultObtainedCallback;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Main activity that coordinates the displaying of other activities within its children
 * @author Cameron
 *
 */
public class MainActivity extends BorderPane {
	
	private static final double MAX_LOGIN_WIDTH = 200;
	private static final double MAX_REGISTER_WIDTH = 400;
	
	private static final HBox TOP_BORDER = new HBox();
	static {
		TOP_BORDER.setPrefHeight(20);
		TOP_BORDER.setMinHeight(TOP_BORDER.getPrefHeight());
		TOP_BORDER.setMaxHeight(TOP_BORDER.getPrefHeight());
	}
	private static final HBox BOTTOM_BORDER = new HBox();
	static {
		BOTTOM_BORDER.setPrefHeight(20);
		BOTTOM_BORDER.setMinHeight(BOTTOM_BORDER.getPrefHeight());
		BOTTOM_BORDER.setMaxHeight(BOTTOM_BORDER.getPrefHeight());
	}
	private static final HBox SIDE_BORDER = new HBox();
	static {
		SIDE_BORDER.setPrefWidth(20);
		SIDE_BORDER.setMinWidth(SIDE_BORDER.getPrefHeight());
		SIDE_BORDER.setMaxWidth(SIDE_BORDER.getPrefHeight());
	}
	
	private ResultObtainedCallback<Integer> callback;
	
	/**
	 * Creates a new Main activity
	 * @param call the callback to notify when the user logs out or
	 * cancels loggin in/registering
	 */
	public MainActivity(ResultObtainedCallback<Integer> call) {
		callback = call;
	}

	 /**
     * Initializes the main activity
     * SHOULD ONLY BE REFLECTIVELY CALLED BY AN FXMLLOADER
     */
	public void initialize() {
		
		setRight(SIDE_BORDER);
		setTop(TOP_BORDER);
		setBottom(BOTTOM_BORDER);
    	
    	RatFilter filter = RatFilter.getDefaultInstance();
    	User[] user = new User[1];
    	
    	NewFilterCallback[] filterCallback = new NewFilterCallback[1];
    	
    	ResultObtainedCallback<Integer> filterResult = new ResultObtainedCallback<Integer>() {

			@Override
			public void onResultObtained(Integer result) {
				if (result == RESULT_OK) {
					if (filterCallback[0] != null) {
						filterCallback[0].notifyFilterUpdated();
					}
				} else {
					setRight(SIDE_BORDER);
				}
			}
			
		};
		
		ResultObtainedCallback<Integer> ratReportResult = new ResultObtainedCallback<Integer>() {

			@Override
			public void onResultObtained(Integer result) {
				if (result == RESULT_OK) {
					if (filterCallback[0] != null) {
						filterCallback[0].notifyFilterUpdated();
					}
				}
				setRight(SIDE_BORDER);
			}
			
		};
		
		ResultObtainedCallback<RatData> ratDataListResult = new ResultObtainedCallback<RatData>() {

			@Override
			public void onResultObtained(RatData result) {
				if (result != null) {
					RatEntryActivity ratEntryActivity = new RatEntryActivity(user[0], result, ratReportResult);
					setRight(ratEntryActivity);
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
					setCenter(mapActivity);
					break;
				case RESULT_GRAPH:
					GraphActivity graphActivity = new GraphActivity(filter);
					filterCallback[0] = graphActivity;
					setCenter(graphActivity);
					break;
				case RESULT_DATA_LIST:
					RatDataListActivity ratDataListActivity = new RatDataListActivity(filter, ratDataListResult);
					filterCallback[0] = ratDataListActivity;
					setCenter(ratDataListActivity);
					break;
				case RESULT_PROFILE:
					break;
				case RESULT_SETTINGS:
					break;
				case RESULT_REPORT:
					RatEntryActivity ratEntryActivity = new RatEntryActivity(user[0], null, ratReportResult);
					setRight(ratEntryActivity);
					break;
				case RESULT_LOGOUT:
					callback.onResultObtained(RESULT_RESTART);
					break;
				case RESULT_FILTER:
					FilterActivity filterActivity = new FilterActivity(filter, filterResult);
					setRight(filterActivity);
					break;
				}
			}
			
		};
		
    	ResultObtainedCallback<User> getUserResult = new ResultObtainedCallback<User>() {

			@Override
			public void onResultObtained(User result) {
				if (result != null) {
					user[0] = result;
					setLeft(new DashboardActivity(user[0], dashboardResult));
					VBox centerBox = new VBox();
					centerBox.setAlignment(Pos.BOTTOM_CENTER);
					centerBox.setPadding(new Insets(100));
					Image centerImg = new Image(getClass().getResourceAsStream("/drawable/ratbackgroundiconALPHA.png"));
					ImageView imageView = new ImageView(centerImg);
					imageView.setSmooth(true);
					imageView.setPreserveRatio(true);
					imageView.setFitHeight(200);
					centerBox.getChildren().add(imageView);
					setCenter(centerBox);
				} else {
					callback.onResultObtained(RESULT_RESTART);
				}
			}
			
		};
    	
		WelcomeActivity welcomeActivity = new WelcomeActivity(new ResultObtainedCallback<Integer>() {
			
			@Override
			public void onResultObtained(Integer result) {
				
				if (result == RESULT_LOGIN) {
					
					LoginActivity loginActivity = new LoginActivity(getUserResult);
					loginActivity.setMaxWidth(MAX_LOGIN_WIDTH);
					setCenter(loginActivity);
					
				} else if (result == RESULT_REGISTER) {
					
					RegisterActivity registerActivity = new RegisterActivity(getUserResult);
					registerActivity.setMaxWidth(MAX_REGISTER_WIDTH);
					setCenter(registerActivity);
					
				} else {
					
					callback.onResultObtained(RESULT_STOP);
					
				}
			}
			
		});
		welcomeActivity.setMaxWidth(MAX_LOGIN_WIDTH);
		setCenter(welcomeActivity);
	}
}
