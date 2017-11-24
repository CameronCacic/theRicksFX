package edu.gatech.cs2340.thericks.controllers;

import java.awt.Checkbox;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;

import edu.gatech.cs2340.thericks.models.RatFilter;
import edu.gatech.cs2340.thericks.models.User;
import edu.gatech.cs2340.thericks.utils.DateUtility;
import edu.gatech.cs2340.thericks.utils.Log;
import edu.gatech.cs2340.thericks.utils.ResultObtainedCallback;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import tornadofx.control.DateTimePicker;

/**
 * Created by Cameron on 11/15/2017.
 * Activity to be started for result to obtain a single predicate to
 * filter with
 */
public class FilterActivity extends VBox {

    private static final String TAG = FilterActivity.class.getSimpleName();

    public static final int GET_FILTER = 700;
    public static final String FILTER = "FILTER";

    @FXML
    private Checkbox dateAndTimeCheck;
    
    @FXML
    private Checkbox locationTypeCheck;
    
    @FXML
    private Checkbox zipCheck;
    
    @FXML
    private Checkbox addressCheck;
    
    @FXML
    private Checkbox cityCheck;
    
    @FXML
    private Checkbox boroughCheck;
    
    @FXML
    private Checkbox latitudeCheck;
    
    @FXML
    private Checkbox longitudeCheck;

    @FXML
    private DateTimePicker dateTimePickerBegin;
    
    @FXML
    private DateTimePicker dateTimePickerEnd;

    @FXML
    private TextField locationTypeEdit;
    
    @FXML
    private TextField zipEdit;
    
    @FXML
    private TextField addressEdit;
    
    @FXML
    private TextField cityEdit;
    
    @FXML
    private TextField boroughEdit;
    
    @FXML
    private TextField minLatitudeEdit;
    
    @FXML
    private TextField maxLatitudeEdit;
    
    @FXML
    private TextField minLongitudeEdit;
    
    @FXML
    private TextField maxLongitudeEdit;

    @FXML
    private Text latSeparator;
    
    @FXML
    private Text longSeparator;

    private RatFilter filter;
    private User user;
    private ResultObtainedCallback<Integer> callback;

    public FilterActivity(RatFilter f, User u, ResultObtainedCallback<Integer> call) {
    	
    	assert f != null;
    	filter = f;
    	
    	assert call != null;
    	callback = call;
    	
    	assert u != null;
    	user = u;
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("activity_filter.fxml"));
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
        Log.d(TAG, "Entered Filter Activity");

        if (filter.hasPredicate(RatFilter.DATE)) {
        	LocalDateTime dateTimeBegin = LocalDateTime.parse(filter.getBeginDateStr()
        			+ " " + filter.getBeginTimeStr(), DateUtility.DATE_TIME_FORMAT);
            dateTimePickerBegin.setDateTimeValue(dateTimeBegin);
            if (filter.isPredicateEnabled(RatFilter.DATE)) {
                dateAndTimeCheck.setState(true);
            }
        }

        if (filter.hasPredicate(RatFilter.LOCATION_TYPE)) {
            locationTypeEdit.setText(filter.getLocationType());
            if (filter.isPredicateEnabled(RatFilter.LOCATION_TYPE)) {
                locationTypeCheck.setState(true);
            }
        }

        if (filter.hasPredicate(RatFilter.ZIP)) {
            zipEdit.setText(String.format(Locale.ENGLISH, "%d", filter.getZip()));
            if (filter.isPredicateEnabled(RatFilter.ZIP)) {
                zipCheck.setState(true);
            }
        }

        if (filter.hasPredicate(RatFilter.ADDRESS)) {
            addressEdit.setText(filter.getAddress());
            if (filter.isPredicateEnabled(RatFilter.ADDRESS)) {
                addressCheck.setState(true);
            }
        }

        if (filter.hasPredicate(RatFilter.CITY)) {
            cityEdit.setText(filter.getCity());
            if (filter.isPredicateEnabled(RatFilter.CITY)) {
                cityCheck.setState(true);
            }
        }

        if (filter.hasPredicate(RatFilter.BOROUGH)) {
            boroughEdit.setText(filter.getBorough());
            if (filter.isPredicateEnabled(RatFilter.BOROUGH)) {
                boroughCheck.setState(true);
            }
        }

        if (filter.hasPredicate(RatFilter.LATITUDE)) {
            minLatitudeEdit.setText(
            		String.format(Locale.ENGLISH, "%f", filter.getMinLatitude()));
            maxLatitudeEdit.setText(
            		String.format(Locale.ENGLISH, "%f", filter.getMaxLatitude()));
            if (filter.isPredicateEnabled(RatFilter.LATITUDE)) {
                latitudeCheck.setState(true);
            }
        }

        if (filter.hasPredicate(RatFilter.LONGITUDE)) {
            minLongitudeEdit.setText(
                    String.format(Locale.ENGLISH, "%f", filter.getMinLongitude()));
            maxLongitudeEdit.setText(
                    String.format(Locale.ENGLISH, "%f", filter.getMaxLongitude()));
            if (filter.isPredicateEnabled(RatFilter.LONGITUDE)) {
                longitudeCheck.setState(true);
            }
        }
    }

    /**
     * Handler method for when the apply filter button is pressed. Consolidates the data in the
     * activity into the RatFilter object
     * @param v the clicked view
     */
    public void onApplyButtonClicked() {
        filter.setBeginDate(dateTimePickerBegin.getDateTimeValue());
        filter.setEndDate(dateTimePickerEnd.getDateTimeValue());
        filter.setPredicateEnabled(RatFilter.DATE, dateAndTimeCheck.getState());

        filter.setLocationType(locationTypeEdit.getText().toString());
        filter.setPredicateEnabled(RatFilter.LOCATION_TYPE, locationTypeCheck.getState());

        try {
            filter.setZip(Integer.parseInt(zipEdit.getText().toString()));
        } catch (NumberFormatException e) {
            filter.setZip(null);
        }
        filter.setPredicateEnabled(RatFilter.ZIP, zipCheck.getState());

        filter.setAddress(addressEdit.getText().toString());
        filter.setPredicateEnabled(RatFilter.ADDRESS, addressCheck.getState());

        filter.setCity(cityEdit.getText().toString());
        filter.setPredicateEnabled(RatFilter.CITY, cityCheck.getState());

        filter.setBorough(boroughEdit.getText().toString());
        filter.setPredicateEnabled(RatFilter.BOROUGH, boroughCheck.getState());

        try {
            filter.setMinLatitude(Double.parseDouble(minLatitudeEdit.getText().toString()));
        } catch (NumberFormatException e) {
            filter.setMinLatitude(null);
        }
        try {
            filter.setMaxLatitude(Double.parseDouble(maxLatitudeEdit.getText().toString()));
        } catch (NumberFormatException e) {
            filter.setMaxLatitude(null);
        }
        filter.setPredicateEnabled(RatFilter.LATITUDE, latitudeCheck.getState());

        try {
            filter.setMinLongitude(Double.parseDouble(minLongitudeEdit.getText().toString()));
        } catch (NumberFormatException e) {
            filter.setMinLongitude(null);
        }
        try {
            filter.setMaxLongitude(Double.parseDouble(maxLongitudeEdit.getText().toString()));
        } catch (NumberFormatException e) {
            filter.setMaxLongitude(null);
        }
        filter.setPredicateEnabled(RatFilter.LONGITUDE, longitudeCheck.getState());

        callback.onResultObtained(ResultObtainedCallback.RESULT_OK);
    }

    /**
     * Handles the cancel button being clicked. Cancels the result
     * @param v the clicked view
     */
    public void onCancelButtonClicked() {
    	callback.onResultObtained(ResultObtainedCallback.RESULT_CANCELED);
    }

    /**
     * Handles the clear button being pressed. Un-checks all filters
     * @param v the clicked view
     */
    public void onClearButtonClicked() {
        dateAndTimeCheck.setState(false);
        locationTypeCheck.setState(false);
        zipCheck.setState(false);
        addressCheck.setState(false);
        cityCheck.setState(false);
        boroughCheck.setState(false);
        latitudeCheck.setState(false);
        longitudeCheck.setState(false);
        filter.disableAllPredicates();
    }

    /**
     * Toggles the date and time filter views
     * @param v the clicked view
     */
    public void onDateAndTimeCheckClicked() {
        if (dateAndTimeCheck.getState()) {
            dateTimePickerBegin.setVisible(true);
            dateTimePickerEnd.setVisible(true);
        } else {
            dateTimePickerBegin.setVisible(false);
            dateTimePickerEnd.setVisible(false);
        }
    }

    /**
     * Toggles location type filter view
     * @param v the clicked view
     */
    public void onLocationTypeCheckClicked() {
        if (locationTypeCheck.getState()) {
            locationTypeEdit.setVisible(false);
        } else {
            locationTypeCheck.setState(true);
            locationTypeEdit.setVisible(true);
        }
    }

    /**
     * Toggles zip code filter views
     * @param v the clicked view
     */
    public void onZipCheckClicked() {
        if (zipCheck.getState()) {
            zipEdit.setVisible(false);
        } else {
            zipEdit.setVisible(true);
        }
    }

    /**
     * Toggles the address filter views
     * @param v the clicked view
     */
    public void onAddressCheckClicked() {
        if (addressCheck.getState()) {
            addressEdit.setVisible(false);
        } else {
            addressEdit.setVisible(true);
        }
    }

    /**
     * Toggles the city filter views
     * @param v the clicked view
     */
    public void onCityCheckClicked() {
        if (cityCheck.getState()) {
            cityEdit.setVisible(false);
        } else {
            cityEdit.setVisible(true);
        }
    }

    /**
     * Toggles the borough filter views
     * @param v the clicked view
     */
    public void onBoroughCheckClicked() {
        if (boroughCheck.getState()) {
            boroughEdit.setVisible(false);
        } else {
            boroughEdit.setVisible(true);
        }
    }

    /**
     * Toggles the latitude filter views
     * @param v the clicked view
     */
    public void onLatitudeCheckClicked() {
        if (latitudeCheck.getState()) {
            minLatitudeEdit.setVisible(false);
            maxLatitudeEdit.setVisible(false);
            latSeparator.setVisible(false);
        } else {
            minLatitudeEdit.setVisible(true);
            maxLatitudeEdit.setVisible(true);
            latSeparator.setVisible(true);
        }
    }

    /**
     * Toggles the longitude filter views
     * @param v the clicked view
     */
    public void onLongitudeCheckClicked() {
        if (longitudeCheck.getState()) {
            minLongitudeEdit.setVisible(false);
            maxLongitudeEdit.setVisible(false);
            longSeparator.setVisible(false);
        } else {
            minLongitudeEdit.setVisible(true);
            maxLongitudeEdit.setVisible(true);
            longSeparator.setVisible(true);
        }
    }
}
