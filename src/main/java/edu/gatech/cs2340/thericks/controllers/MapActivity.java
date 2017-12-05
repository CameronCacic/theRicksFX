package edu.gatech.cs2340.thericks.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventHandler;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.InfoWindow;
import com.lynden.gmapsfx.javascript.object.InfoWindowOptions;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

import edu.gatech.cs2340.thericks.database.RatDatabase;
import edu.gatech.cs2340.thericks.models.RatData;
import edu.gatech.cs2340.thericks.models.RatFilter;
import edu.gatech.cs2340.thericks.utils.DataLoadedCallback;
import edu.gatech.cs2340.thericks.utils.Log;
import edu.gatech.cs2340.thericks.utils.NewFilterCallback;
import edu.gatech.cs2340.thericks.utils.RunningAverage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import netscape.javascript.JSObject;

/**
 * Map activity that displays a Google Map with rat data
 * @author Cameron
 *
 */
public class MapActivity extends AnchorPane implements MapComponentInitializedListener, NewFilterCallback {
	
	private static final String TAG = MapActivity.class.getSimpleName();
	private static final double LAT = 40.776278;
	private static final double LONG = -73.99086;
    private static final double ZOOM = 12;
    private static final double BEARING = 30;
    
    private GoogleMap map;
    
    private RatFilter filter;
    
    @FXML
    private GoogleMapView mapView;
    
    @FXML
    private ProgressIndicator progressIndicator;
    
    /**
     * Creates a new Map activity
     * @param f the filter to load data with
     */
    public MapActivity(RatFilter f) {
    	
    	Log.d(TAG, "Entered Map Activity");
    	
    	filter = f;
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/activity_map.fxml"));
    	loader.setController(this);
    	loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Initializes the map activity
     * SHOULD ONLY BE REFLECTIVELY CALLED BY AN FXMLLOADER
     */
    public void initialize() {
    	mapView.addMapInializedListener(this);
    }
    
    /**
     * Clears the current markers on the map then fetches rat data from the database,
     * applying the current filter, and adds a marker to the map for each one
     */
    private void loadFilteredMapMarkers() {
        progressIndicator.setVisible(true);
        map.clearMarkers();
        
		Log.d(TAG, "Attempting to load rat data and populate the map");
		List<RatData> filteredList = new ArrayList<RatData>();
		RatDatabase database = new RatDatabase();
		database.loadData(new DataLoadedCallback() {
			
			@Override
			public void notifyDataLoaded() {
				Log.d(TAG, "Populating the map");
				
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						RunningAverage latAverage = new RunningAverage();
						RunningAverage longAverage = new RunningAverage();
		    	        for (RatData r: filteredList) {
		    	            MarkerOptions markerOptions = new MarkerOptions();
		    	            LatLong position = new LatLong(r.getLatitude(), r.getLongitude());
		    	            markerOptions.position(position);
		    	            Marker marker = new Marker(markerOptions);
		    	            
		    	            latAverage.add(position.getLatitude());
		    	            longAverage.add(position.getLongitude());

		    	            map.addMarker(marker);
		    	            map.addUIEventHandler(marker, UIEventType.click, new UIEventHandler() {
								
								@Override
								public void handle(JSObject arg0) {
									InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
		    				        infoWindowOptions.content("<h2>" + r.getCity() + "</h2>"
		    				                                + r.getIncidentAddress() + "<br>"
		    				                                + r.getCreatedDateTime() );
		    				        InfoWindow infoWindow = new InfoWindow(infoWindowOptions);
		    				        infoWindow.open(map, marker);
		    				        map.panTo(position);
								}
								
							});
		    	        }
		    	        progressIndicator.setVisible(false);
		    	        if (latAverage.hasAverage() && longAverage.hasAverage()) {
		    	        	map.panTo(new LatLong(latAverage.getCurrentAverage(), longAverage.getCurrentAverage()));
		    	        }
					}
				});
				
			}
		}, filteredList, filter);;
    }

	@Override
	public void mapInitialized() {
		
		Log.d(TAG, "Google Map initialized");
		MapOptions engagedMapOptions = new MapOptions();
    	engagedMapOptions.center(new LatLong(LAT, LONG))
				.mapType(MapTypeIdEnum.ROADMAP)
				.overviewMapControl(true)
				.panControl(true)
				.rotateControl(true)
				.scaleControl(true)
				.streetViewControl(true)
				.zoomControl(true)
				.zoom(ZOOM);

		map = mapView.createMap(engagedMapOptions);
		map.setHeading(BEARING);
		loadFilteredMapMarkers();
	}

	@Override
	public void notifyFilterUpdated() {
		loadFilteredMapMarkers();
	}
}
