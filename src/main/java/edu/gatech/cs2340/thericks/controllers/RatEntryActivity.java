package edu.gatech.cs2340.thericks.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import edu.gatech.cs2340.thericks.database.RatDatabase;
import edu.gatech.cs2340.thericks.models.Privilege;
import edu.gatech.cs2340.thericks.models.RatData;
import edu.gatech.cs2340.thericks.models.RatDataSource;
import edu.gatech.cs2340.thericks.models.User;
import edu.gatech.cs2340.thericks.utils.DateUtility;
import edu.gatech.cs2340.thericks.utils.Log;
import edu.gatech.cs2340.thericks.utils.ResultObtainedCallback;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import tornadofx.control.DateTimePicker;

/**
 * Created by Cameron on 10/6/2017.
 * Displays the data in a passed RatData
 */
public class RatEntryActivity extends VBox {

    private static final String TAG = RatEntryActivity.class.getSimpleName();

    @FXML
    private TextField key;
    
    @FXML
    private DateTimePicker date;
    
    @FXML
    private TextField locationType;
    
    @FXML
    private TextField address;
    
    @FXML
    private TextField zip;
    
    @FXML
    private TextField borough;
    
    @FXML
    private TextField city;
    
    @FXML
    private TextField latitude;
    
    @FXML
    private TextField longitude;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button cancelButton;
    
    private User user;
    private RatData ratData;
    private ResultObtainedCallback<Integer> callback;
    
    /**
     * Creates a new RatEntry activity
     * @param u the user to determine editing privileges from
     * @param r the RatData to populate field with, or create new RatData if null
     * @param call callback to notify if ratdata was changed or canceled
     */
    public RatEntryActivity(User u, RatData r, ResultObtainedCallback<Integer> call) {
    	user = u;
    	ratData = r;
    	callback = call;
    	
    	setSpacing(5);
    	setPadding(new Insets(5, 5, 5, 20));
    	setAlignment(Pos.TOP_LEFT);
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/activity_rat_entry.fxml"));
    	loader.setController(this);
    	loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    /**
     * Initializes the ratentry activity
     * SHOULD ONLY BE REFLECTIVELY CALLED BY AN FXMLLOADER
     */
    public void initialize() {

    	boolean privilege = user != null && user.getPrivilege().equals(Privilege.ADMIN);
        if (ratData != null) {
        	Log.d(TAG, "Rat data passed in, populating fields with its data");
        	
            key.setText(String.valueOf(ratData.getKey()));
            key.setEditable(privilege);
            
            date.setDateTimeValue(DateUtility.parse(ratData.getCreatedDateTime()));
            date.setEditable(privilege);
            
            locationType.setText(ratData.getLocationType());
            locationType.setEditable(privilege);
            
            address.setText(ratData.getIncidentAddress());
            address.setEditable(privilege);
            
            zip.setText(String.valueOf(ratData.getIncidentZip()));
            zip.setEditable(privilege);
            
            borough.setText(ratData.getBorough());
            borough.setEditable(privilege);
            
            city.setText(ratData.getCity());
            city.setEditable(privilege);
            
            latitude.setText(String.format(Locale.ENGLISH, "%8f", ratData.getLatitude()));
            latitude.setEditable(privilege);
            
            longitude.setText(String.format(Locale.ENGLISH, "%8f", ratData.getLongitude()));
            longitude.setEditable(privilege);
        } else {
            Log.d(TAG, "No rat data passed in, populating with current default data");
            
            RatDatabase db = new RatDatabase();
            int randKey;
            do {
            	randKey = ThreadLocalRandom.current().nextInt(0, 100000000);
            } while (db.findRatDataByKey(randKey) != null);
            key.setText(String.format("%08d", randKey));
            key.setEditable(privilege);
            
            date.setDateTimeValue(LocalDateTime.now());
            saveButton.setText("Create");
            //set location but don't know how to access windows location services
        }

        key.textProperty().addListener((observable, oldValue, newValue) -> {
            Scanner intTest = new Scanner(newValue);
            if (intTest.hasNextInt()) {
            	key.setStyle("-fx-text-inner-color: black;");
            } else {
            	key.setStyle("-fx-text-inner-color: red;");
            }
            intTest.close();
        });

        zip.textProperty().addListener((observable, oldValue, newValue) -> {
            Scanner intTest = new Scanner(newValue);
            if (intTest.hasNextInt()) {
            	zip.setStyle("-fx-text-inner-color: black;");
            } else {
            	zip.setStyle("-fx-text-inner-color: red;");
            }
            intTest.close();
        });

        latitude.textProperty().addListener((observable, oldValue, newValue) -> {
            Scanner doubleTest = new Scanner(newValue);
            if (doubleTest.hasNextDouble()) {
            	latitude.setStyle("-fx-text-inner-color: black;");
            } else {
            	latitude.setStyle("-fx-text-inner-color: red;");
            }
            doubleTest.close();
        });

        longitude.textProperty().addListener((observable, oldValue, newValue) -> {
            Scanner doubleTest = new Scanner(newValue);
            if (doubleTest.hasNextDouble()) {
            	longitude.setStyle("-fx-text-inner-color: black;");
            } else {
            	longitude.setStyle("-fx-text-inner-color: red;");
            }
            doubleTest.close();
        });

        cancelButton.setOnAction(e -> {
            callback.onResultObtained(ResultObtainedCallback.RESULT_CANCELED);
        });

        saveButton.setOnAction(e -> {
            int iKey;
            int iZip;
            double dLatitude;
            double dLongitude;

            Log.d(TAG, "Confirming rat data is valid");
            try {
                iKey = Integer.parseInt(key.getText().toString());
            } catch (NumberFormatException ex) {
                Log.d(TAG, "Improperly formatted input detected in the key");
                return;
            }
            try {
                iZip = Integer.parseInt(zip.getText().toString());
            } catch (NumberFormatException ex) {
                Log.d(TAG, "Improperly formatted input detected in the zip");
                return;
            }
            try {
                dLatitude = Double.parseDouble(latitude.getText().toString());
            } catch (NumberFormatException ex) {
                Log.d(TAG, "Improperly formatted input detected in the latitude");
                return;
            }
            try {
                dLongitude = Double.parseDouble(longitude.getText().toString());
            } catch (NumberFormatException ex) {
                Log.d(TAG, "Improperly formatted input detected in the longitude");
                return;
            }

            Log.d(TAG, "Valid rat data entered, passing rat meta data to the database");
            RatDataSource database = new RatDatabase();

            database.createRatData(iKey,
                    DateUtility.DATE_TIME_FORMAT.format(date.getDateTimeValue()),
                    locationType.getText().toString(),
                    iZip,
                    address.getText().toString(),
                    city.getText().toString(),
                    borough.getText().toString(),
                    dLatitude,
                    dLongitude);
            callback.onResultObtained(ResultObtainedCallback.RESULT_OK);
        });
    }
}