package edu.gatech.cs2340.thericks.controllers;

import java.io.IOException;
import java.util.List;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

import edu.gatech.cs2340.thericks.database.RatDatabase;
import edu.gatech.cs2340.thericks.models.RatData;
import edu.gatech.cs2340.thericks.models.RatFilter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

public class MapActivity extends AnchorPane implements MapComponentInitializedListener {
	
	private static final LatLong POSITION = new LatLong(40.776278, -73.99086);
    private static final double ZOOM = 12;
    private static final double BEARING = 30;

    private MapOptions engagedMapOptions;
    private MapOptions staticMapOptions;
    
    private GoogleMap map;
    
    @FXML
    private GoogleMapView mapView;
    
    @FXML
    private ProgressBar progressBar;
    
    public MapActivity() {

    	staticMapOptions = new MapOptions();
    	staticMapOptions.center(POSITION)
    			.mapType(MapTypeIdEnum.ROADMAP)
    			.overviewMapControl(false)
    			.panControl(false)
    			.rotateControl(false)
    			.scaleControl(false)
    			.streetViewControl(false)
    			.zoomControl(false);
    	
    	engagedMapOptions.center(POSITION)
				.mapType(MapTypeIdEnum.ROADMAP)
				.overviewMapControl(true)
				.panControl(true)
				.rotateControl(true)
				.scaleControl(false)
				.streetViewControl(false)
				.zoomControl(true)
				.zoom(ZOOM);
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("activity_map.fxml"));
    	loader.setController(this);
    	loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Clears the current markers on the map, disables all map widgets,
     * then fetches rat data from the database, applying all current
     * filters, and adds a marker to the map for each one before
     * re-enabling all map widgets
     */
    private void loadFilteredMapMarkers(RatFilter filter) {
        progressBar.setVisible(true);
        map = mapView.createMap(staticMapOptions);
        
        List<RatData> filteredList = new RatDatabase().getFilteredRatData(filter);
        map.clearMarkers();
        for (RatData r: filteredList) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLong(r.getLatitude(), r.getLongitude()));
            markerOptions.title(r.getKey() + "");
            markerOptions.label(r.getIncidentAddress() + "\n" + r.getCreatedDateTime());
        }
        progressBar.setVisible(false);
    }

	@Override
	public void mapInitialized() {
		map = mapView.createMap(engagedMapOptions);
		map.setHeading(BEARING);
		loadFilteredMapMarkers();
	}
}
