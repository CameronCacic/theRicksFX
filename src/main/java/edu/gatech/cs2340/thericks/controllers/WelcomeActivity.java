package edu.gatech.cs2340.thericks.controllers;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Created by Ben Lashley on 9/20/2017.
 *
 * Screen that appears when app starts. Allows user to login or register for an account.
 */

public class WelcomeActivity extends Application {
	
	@FXML
	Button login;
	
	@FXML
	Button register;
	
    @Override
    public void start(Stage stage) {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("activity_welcome.xml"));
    	loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    public void initialize() {
    	login.setOnAction(e -> {
//          Context context = v.getContext();
//          Intent intent = new Intent(context, LoginActivity.class);
//          context.startActivity(intent);
      });

      register.setOnAction(v -> {
//          Context context = v.getContext();
//          Intent intent = new Intent(context, RegisterActivity.class);
//          context.startActivity(intent);
      });
    }
    
    public static void main(String[] args) {
    	launch(args);
    }
}
