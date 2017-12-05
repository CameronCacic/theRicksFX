package edu.gatech.cs2340.thericks.controllers;

import edu.gatech.cs2340.thericks.database.DatabaseHandler;
import edu.gatech.cs2340.thericks.utils.Log;
import edu.gatech.cs2340.thericks.utils.ResultObtainedCallback;
import edu.gatech.cs2340.thericks.utils.Log.LogLevel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Class for launching the app
 * @author Cameron
 *
 */
public class Launcher extends Application {
	
	private static final String TAG = Launcher.class.getSimpleName();
	
	private Stage primaryStage;
	private Scene scene;
	
	/**
	 * Launches the app
	 * @param args any command line arguments, not used
	 */
	public static void main(String[] args) {
		Log.setLevel(LogLevel.DEBUG);
		Log.d(TAG, "Launcher started");
		launch(args);
    }

	@Override
	public void start(Stage pStage) throws Exception {
		
		primaryStage = pStage;
		
		primaryStage.setTitle("Rat Tracker");
		primaryStage.getIcons().clear();
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/drawable/ratbackgroundiconALPHA.png")));
//		primaryStage.getIcons().addAll(new Image(getClass().getResourceAsStream("/drawable/ratbackgroundicon16.jpg")),
//				new Image(getClass().getResourceAsStream("/drawable/ratbackgroundicon32.jpg")),
//				new Image(getClass().getResourceAsStream("/drawable/ratbackgroundicon64.jpg")));
		
		MainActivity mainActivity = new MainActivity(new ResultObtainedCallback<Integer>() {
			
			@Override
			public void onResultObtained(Integer result) {
				if (result == RESULT_RESTART) {
					setMainScene(new MainActivity(this));
				} else {
					primaryStage.close();
				}
			}
			
		});
		
		setMainScene(mainActivity);
	}
	
	/**
	 * Puts the main scene into the stage, or switches the root for the given MainActivity
	 * if the scene is already displaying to avoid a stage resizing bug
	 * @param m
	 */
	private void setMainScene(MainActivity m) {
		m.initialize();

		if (scene == null) {
			scene = new Scene(m);
			scene.getStylesheets().add(getClass().getResource("/styles/rat_tracker.css").toExternalForm());
		} else {
			scene.setRoot(m);
		}
		
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		if (!primaryStage.isShowing()) {
			primaryStage.show();
		}
	}
	
	@Override
	public void stop() {
		DatabaseHandler.closeInstance();
		Log.d(TAG, "Shutting down cleanly");
	}
}