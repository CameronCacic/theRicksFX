package edu.gatech.cs2340.thericks.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;

import edu.gatech.cs2340.thericks.models.RatFilter;
import edu.gatech.cs2340.thericks.utils.DateUtility;
import edu.gatech.cs2340.thericks.utils.Log;
import edu.gatech.cs2340.thericks.utils.ResultObtainedCallback;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import tornadofx.control.DateTimePicker;

/**
 * Created by Cameron on 11/15/2017.
 * Edits the passed in filter with user selected conditions
 */
public class FilterActivity extends VBox {

    private static final String TAG = FilterActivity.class.getSimpleName();

    private static final double WIDTH = 480;

    @FXML
    private CheckBox dateAndTimeCheck;
    
    @FXML
    private CheckBox locationTypeCheck;
    
    @FXML
    private CheckBox zipCheck;
    
    @FXML
    private CheckBox addressCheck;
    
    @FXML
    private CheckBox cityCheck;
    
    @FXML
    private CheckBox boroughCheck;
    
    @FXML
    private CheckBox latitudeCheck;
    
    @FXML
    private CheckBox longitudeCheck;

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
    private Text dateSeparator;

    @FXML
    private Text latSeparator;
    
    @FXML
    private Text longSeparator;

    private RatFilter filter;
    private ResultObtainedCallback<Integer> callback;

    /**
     * Creates a new Filter activity
     * @param f the filter to edit
     * @param call the callback to notify when the filter has been edited or canceled
     */
    public FilterActivity(RatFilter f, ResultObtainedCallback<Integer> call) {
    	
    	assert f != null;
    	filter = f;
    	
    	assert call != null;
    	callback = call;
    	
    	setPrefWidth(WIDTH);
    	setMaxWidth(WIDTH);
    	setPadding(new Insets(0, 0, 0, 15));
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/activity_filter.fxml"));
    	loader.setController(this);
    	loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    /**
     * Initializes the filter activity
     * SHOULD ONLY BE REFLECTIVELY CALLED BY AN FXMLLOADER
     */
    public void initialize() {
        Log.d(TAG, "Entered Filter Activity");

        // enforce numeric values
        zipEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    zipEdit.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        minLatitudeEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("(-{0,1}(?!0))\\d{0,2}([\\.]\\d{0,7})?") && !"".equals(newValue)) {
                    minLatitudeEdit.setText(oldValue);
                }
            }
        });
        maxLatitudeEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("(-{0,1}(?!0))\\d{0,2}([\\.]\\d{0,7})?") && !"".equals(newValue)) {
                    minLatitudeEdit.setText(oldValue);
                }
            }
        });
        minLongitudeEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("(-{0,1}(?!0))\\d{0,3}([\\.]\\d{0,7})?") && !"".equals(newValue)) {
                    minLatitudeEdit.setText(oldValue);
                }
            }
        });
        maxLongitudeEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("(-{0,1}(?!0))\\d{0,2}([\\.]\\d{0,7})?") && !"".equals(newValue)) {
                    minLatitudeEdit.setText(oldValue);
                }
            }
        });
        
        if (filter.hasPredicate(RatFilter.DATE)) {
        	LocalDateTime dateTimeBegin = LocalDateTime.parse(filter.getBeginDateStr()
        			+ " " + filter.getBeginTimeStr(), DateUtility.DATE_TIME_FORMAT);
        	LocalDateTime dateTimeEnd = LocalDateTime.parse(filter.getEndDateStr()
        			+ " " + filter.getEndTimeStr(), DateUtility.DATE_TIME_FORMAT);
            dateTimePickerBegin.setDateTimeValue(dateTimeBegin);
            dateTimePickerEnd.setDateTimeValue(dateTimeEnd);
            dateTimePickerBegin.setFormat(DateUtility.DATE_TIME_PATTERN);
            dateTimePickerEnd.setFormat(DateUtility.DATE_TIME_PATTERN);
            if (filter.isPredicateEnabled(RatFilter.DATE)) {
            	dateAndTimeCheck.setSelected(true);
                onDateAndTimeCheckClicked(null);
            }
        }

        if (filter.hasPredicate(RatFilter.LOCATION_TYPE)) {
            locationTypeEdit.setText(filter.getLocationType());
            if (filter.isPredicateEnabled(RatFilter.LOCATION_TYPE)) {
            	locationTypeCheck.setSelected(true);
                onLocationTypeCheckClicked(null);
            }
        }

        if (filter.hasPredicate(RatFilter.ZIP)) {
            zipEdit.setText(String.format(Locale.ENGLISH, "%d", filter.getZip()));
            if (filter.isPredicateEnabled(RatFilter.ZIP)) {
            	zipCheck.setSelected(true);
                onZipCheckClicked(null);
            }
        }

        if (filter.hasPredicate(RatFilter.ADDRESS)) {
            addressEdit.setText(filter.getAddress());
            if (filter.isPredicateEnabled(RatFilter.ADDRESS)) {
            	addressCheck.setSelected(true);
                onAddressCheckClicked(null);
            }
        }

        if (filter.hasPredicate(RatFilter.CITY)) {
            cityEdit.setText(filter.getCity());
            if (filter.isPredicateEnabled(RatFilter.CITY)) {
            	cityCheck.setSelected(true);
                onCityCheckClicked(null);
            }
        }

        if (filter.hasPredicate(RatFilter.BOROUGH)) {
            boroughEdit.setText(filter.getBorough());
            if (filter.isPredicateEnabled(RatFilter.BOROUGH)) {
            	boroughCheck.setSelected(true);
                onBoroughCheckClicked(null);
            }
        }

        if (filter.hasPredicate(RatFilter.LATITUDE)) {
            minLatitudeEdit.setText(
            		String.format(Locale.ENGLISH, "%f", filter.getMinLatitude()));
            maxLatitudeEdit.setText(
            		String.format(Locale.ENGLISH, "%f", filter.getMaxLatitude()));
            if (filter.isPredicateEnabled(RatFilter.LATITUDE)) {
            	latitudeCheck.setSelected(true);
                onLatitudeCheckClicked(null);
            }
        }

        if (filter.hasPredicate(RatFilter.LONGITUDE)) {
            minLongitudeEdit.setText(
                    String.format(Locale.ENGLISH, "%f", filter.getMinLongitude()));
            maxLongitudeEdit.setText(
                    String.format(Locale.ENGLISH, "%f", filter.getMaxLongitude()));
            if (filter.isPredicateEnabled(RatFilter.LONGITUDE)) {
            	longitudeCheck.setSelected(true);
                onLongitudeCheckClicked(null);
            }
        }
    }

    /**
     * Handler method for when the apply filter button is pressed. Consolidates the data in the
     * activity into the RatFilter object
     * @param e the action event
     */
    @FXML
    public void onApplyButtonClicked(ActionEvent e) {
        filter.setBeginDate(dateTimePickerBegin.getDateTimeValue());
        filter.setEndDate(dateTimePickerEnd.getDateTimeValue());
        filter.setPredicateEnabled(RatFilter.DATE, dateAndTimeCheck.isSelected());

        filter.setLocationType(locationTypeEdit.getText().toString());
        filter.setPredicateEnabled(RatFilter.LOCATION_TYPE, locationTypeCheck.isSelected());

        try {
            filter.setZip(Integer.parseInt(zipEdit.getText().toString()));
        } catch (NumberFormatException e1) {
            filter.setZip(null);
        }
        filter.setPredicateEnabled(RatFilter.ZIP, zipCheck.isSelected());

        filter.setAddress(addressEdit.getText().toString());
        filter.setPredicateEnabled(RatFilter.ADDRESS, addressCheck.isSelected());

        filter.setCity(cityEdit.getText().toString());
        filter.setPredicateEnabled(RatFilter.CITY, cityCheck.isSelected());

        filter.setBorough(boroughEdit.getText().toString());
        filter.setPredicateEnabled(RatFilter.BOROUGH, boroughCheck.isSelected());

        try {
            filter.setMinLatitude(Double.parseDouble(minLatitudeEdit.getText().toString()));
        } catch (NumberFormatException e1) {
            filter.setMinLatitude(null);
        }
        try {
            filter.setMaxLatitude(Double.parseDouble(maxLatitudeEdit.getText().toString()));
        } catch (NumberFormatException e1) {
            filter.setMaxLatitude(null);
        }
        filter.setPredicateEnabled(RatFilter.LATITUDE, latitudeCheck.isSelected());

        try {
            filter.setMinLongitude(Double.parseDouble(minLongitudeEdit.getText().toString()));
        } catch (NumberFormatException e1) {
            filter.setMinLongitude(null);
        }
        try {
            filter.setMaxLongitude(Double.parseDouble(maxLongitudeEdit.getText().toString()));
        } catch (NumberFormatException e1) {
            filter.setMaxLongitude(null);
        }
        filter.setPredicateEnabled(RatFilter.LONGITUDE, longitudeCheck.isSelected());

        callback.onResultObtained(ResultObtainedCallback.RESULT_OK);
    }

    /**
     * Handles the cancel button being clicked. Cancels the result
     * @param e the action event
     */
    @FXML
    public void onCancelButtonClicked(ActionEvent e) {
    	callback.onResultObtained(ResultObtainedCallback.RESULT_CANCELED);
    }

    /**
     * Handles the clear button being pressed. Un-checks all filters
     * @param e the action event
     */
    @FXML
    public void onClearButtonClicked(ActionEvent e) {
        dateAndTimeCheck.setSelected(false);
        onDateAndTimeCheckClicked(null);
        locationTypeCheck.setSelected(false);
        onLocationTypeCheckClicked(null);
        zipCheck.setSelected(false);
        onZipCheckClicked(null);
        addressCheck.setSelected(false);
        onAddressCheckClicked(null);
        cityCheck.setSelected(false);
        onCityCheckClicked(null);
        boroughCheck.setSelected(false);
        onBoroughCheckClicked(null);
        latitudeCheck.setSelected(false);
        onLatitudeCheckClicked(null);
        longitudeCheck.setSelected(false);
        onLongitudeCheckClicked(null);
        filter.disableAllPredicates();
    }

    /**
     * Toggles the date and time filter views
     * @param e the action event
     */
    @FXML
    public void onDateAndTimeCheckClicked(ActionEvent e) {
        if (dateAndTimeCheck.isSelected()) {
            dateTimePickerBegin.setVisible(true);
            dateTimePickerBegin.setManaged(true);
            dateSeparator.setVisible(true);
            dateSeparator.setManaged(true);
            dateTimePickerEnd.setVisible(true);
            dateTimePickerEnd.setManaged(true);
        } else {
            dateTimePickerBegin.setVisible(false);
            dateTimePickerBegin.setManaged(false);
            dateSeparator.setVisible(false);
            dateSeparator.setManaged(false);
            dateTimePickerEnd.setVisible(false);
            dateTimePickerEnd.setManaged(false);
        }
    }

    /**
     * Toggles location type filter view
     * @param e the action event
     */
    @FXML
    public void onLocationTypeCheckClicked(ActionEvent e) {
        if (!locationTypeCheck.isSelected()) {
            locationTypeEdit.setVisible(false);
            locationTypeEdit.setManaged(false);
        } else {
            locationTypeEdit.setVisible(true);
            locationTypeEdit.setManaged(true);
        }
    }

    /**
     * Toggles zip code filter views
     * @param e the action event
     */
    @FXML
    public void onZipCheckClicked(ActionEvent e) {
        if (!zipCheck.isSelected()) {
            zipEdit.setVisible(false);
            zipEdit.setManaged(false);
        } else {
            zipEdit.setVisible(true);
            zipEdit.setManaged(true);
        }
    }

    /**
     * Toggles the address filter views
     * @param e the action event
     */
    @FXML
    public void onAddressCheckClicked(ActionEvent e) {
        if (!addressCheck.isSelected()) {
            addressEdit.setVisible(false);
            addressEdit.setManaged(false);
        } else {
            addressEdit.setVisible(true);
            addressEdit.setManaged(true);
        }
    }

    /**
     * Toggles the city filter views
     * @param e the action event
     */
    @FXML
    public void onCityCheckClicked(ActionEvent e) {
        if (!cityCheck.isSelected()) {
            cityEdit.setVisible(false);
            cityEdit.setManaged(false);
        } else {
            cityEdit.setVisible(true);
            cityEdit.setManaged(true);
        }
    }

    /**
     * Toggles the borough filter views
     * @param e the action event
     */
    @FXML
    public void onBoroughCheckClicked(ActionEvent e) {
        if (!boroughCheck.isSelected()) {
            boroughEdit.setVisible(false);
            boroughEdit.setManaged(false);
        } else {
            boroughEdit.setVisible(true);
            boroughEdit.setManaged(true);
        }
    }

    /**
     * Toggles the latitude filter views
     * @param e the action event
     */
    @FXML
    public void onLatitudeCheckClicked(ActionEvent e) {
        if (!latitudeCheck.isSelected()) {
            minLatitudeEdit.setVisible(false);
            minLatitudeEdit.setManaged(false);
            maxLatitudeEdit.setVisible(false);
            maxLatitudeEdit.setManaged(false);
            latSeparator.setVisible(false);
            latSeparator.setManaged(false);
        } else {
            minLatitudeEdit.setVisible(true);
            minLatitudeEdit.setManaged(true);
            maxLatitudeEdit.setVisible(true);
            maxLatitudeEdit.setManaged(true);
            latSeparator.setVisible(true);
            latSeparator.setManaged(true);
        }
    }

    /**
     * Toggles the longitude filter views
     * @param e the action event
     */
    @FXML
    public void onLongitudeCheckClicked(ActionEvent e) {
        if (!longitudeCheck.isSelected()) {
            minLongitudeEdit.setVisible(false);
            minLongitudeEdit.setManaged(false);
            maxLongitudeEdit.setVisible(false);
            maxLongitudeEdit.setManaged(false);
            longSeparator.setVisible(false);
            longSeparator.setManaged(false);
        } else {
            minLongitudeEdit.setVisible(true);
            minLongitudeEdit.setManaged(true);
            maxLongitudeEdit.setVisible(true);
            maxLongitudeEdit.setManaged(true);
            longSeparator.setVisible(true);
            longSeparator.setManaged(true);
        }
    }
}