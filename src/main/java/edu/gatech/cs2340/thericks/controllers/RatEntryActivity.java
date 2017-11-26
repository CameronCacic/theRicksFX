package edu.gatech.cs2340.thericks.controllers;

import java.util.Locale;
import java.util.Scanner;

import edu.gatech.cs2340.thericks.database.RatDatabase;
import edu.gatech.cs2340.thericks.models.RatData;
import edu.gatech.cs2340.thericks.models.RatDataSource;
import edu.gatech.cs2340.thericks.models.User;
import edu.gatech.cs2340.thericks.utils.DateUtility;
import edu.gatech.cs2340.thericks.utils.Log;
import edu.gatech.cs2340.thericks.utils.ResultObtainedCallback;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import tornadofx.control.DateTimePicker;

/**
 * Created by Cameron on 10/6/2017.
 * Displays the data in a passed RatData through
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
    
    public RatEntryActivity(User u, RatData r, ResultObtainedCallback<Integer> call) {
    	user = u;
    	ratData = r;
    	callback = call;
    }

    public void initialize() {

        if (ratData != null) {
            key.setText(String.valueOf(ratData.getKey()));
            date.setDateTimeValue(DateUtility.parse(ratData.getCreatedDateTime()));
            locationType.setText(ratData.getLocationType());
            address.setText(ratData.getIncidentAddress());
            zip.setText(String.valueOf(ratData.getIncidentZip()));
            borough.setText(ratData.getBorough());
            city.setText(ratData.getCity());
            latitude.setText(String.format(Locale.ENGLISH, "%8f", ratData.getLatitude()));
            longitude.setText(String.format(Locale.ENGLISH, "%8f", ratData.getLongitude()));
        } else {
            Log.d(TAG, "No rat data passed in.");
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
