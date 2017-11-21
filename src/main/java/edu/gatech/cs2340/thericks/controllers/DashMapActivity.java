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
import edu.gatech.cs2340.thericks.models.User;
import edu.gatech.cs2340.thericks.utils.Log;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

/**
 * Created by Cameron on 10/6/2017.
 * Dash-map activity in dash mode provides numerous activity options for a logged in user
 * to engage in. Defaults to dash mode, upon selecting map, dash-map switches to map mode,
 * where users can view rat data displayed on a Google Map, filtered by date
 */
public class DashMapActivity extends AnchorPane implements MapComponentInitializedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    //default position, zoom, and bearing to set the map camera to
    private static final LatLong POSITION = new LatLong(40.776278, -73.99086);
    private static final double ZOOM = 12;
    private static final double BEARING = 30;

    private MapOptions staticMapOptions;
    private MapOptions engagedMapOptions;
    
    private GoogleMap map;

    private RatFilter filter;
    
    private User user;
    
    private boolean onMap;

    @FXML
    private GoogleMapView mapView;
    
    @FXML
    private Button mapButton;
    
    @FXML
    private Button graphButton;
    
    @FXML
    private Button listRatDataButton;
    
    @FXML
    private Button profileButton;
    
    @FXML
    private Button settingsButton;
    
    @FXML
    private Button reportRatButton;
    
    @FXML
    private Button logoutButton;

    @FXML
    private Button returnToDashButton;
    
    @FXML
    private Button applyFiltersButton;

    @FXML
    private ProgressBar progressBar;

    public DashMapActivity(User u) {
    	user = u;
    	
    	staticMapOptions = new MapOptions();
    	staticMapOptions.center(POSITION)
    			.mapType(MapTypeIdEnum.ROADMAP)
    			.overviewMapControl(false)
    			.panControl(false)
    			.rotateControl(false)
    			.scaleControl(false)
    			.streetViewControl(false)
    			.zoomControl(false)
    			.zoom(ZOOM);
    	
    	engagedMapOptions.center(POSITION)
				.mapType(MapTypeIdEnum.ROADMAP)
				.overviewMapControl(true)
				.panControl(true)
				.rotateControl(true)
				.scaleControl(false)
				.streetViewControl(false)
				.zoomControl(true)
				.zoom(ZOOM);
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("activity_main.fxml"));
    	loader.setController(this);
    	loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void initialize() {

        Log.d(TAG, "Entered dashboard activity");

        filter = RatFilter.getDefaultInstance();

        onMap = false;

        assert user != null;
        user.login();
        Log.d(TAG, user.getUsername() + " is logged in = " + user.isLoggedIn());

        mapView.addMapInializedListener(this);
        
        mapButton.setOnAction(e -> {
            Log.d(TAG, "Rat Map Button pressed");
            if (map != null) {
                mapButton.setVisible(false);
                listRatDataButton.setVisible(false);
                profileButton.setVisible(false);
                settingsButton.setVisible(false);
                reportRatButton.setVisible(false);
                logoutButton.setVisible(false);

                returnToDashButton.setVisible(true);
                applyFiltersButton.setVisible(true);

                onMap = !onMap;
                map = mapView.createMap(engagedMapOptions);
                map.setHeading(BEARING);
                loadFilteredMapMarkers();
            }
        });

        graphButton.setOnAction(e ->  {
            Log.d(TAG, "Graph button pressed");
//            Context context = v.getContext();
//            Intent intent = new Intent(context, GraphActivity.class);
//            context.startActivity(intent);
        });

        listRatDataButton.setOnAction(e -> {
            Log.d(TAG, "Rat Data List button pressed");
//            Context context = v.getContext();
//            Intent intent = new Intent(context, RatDataListActivity.class);
//            context.startActivity(intent);
        });

        profileButton.setOnAction(e -> {
            Log.d(TAG, "Profile button pressed");
//            Context context = v.getContext();
//            Intent intent = new Intent(context, FilterActivity.class);
//            context.startActivity(intent);
        });

        reportRatButton.setOnAction(e -> {
            Log.d(TAG, "Report a Rat button pushed");
//            Context context = v.getContext();
//            Intent intent = new Intent(context, RatEntryActivity.class);
//            startActivityForResult(intent, ADD_RAT_DATA_REQUEST);
        });

        logoutButton.setOnAction(e -> {
            user.logout();
            Log.d(TAG, "Logout button pressed");
            Log.d(TAG, user.getLogin().getUsername() + " is logged in = "
                    + user.isLoggedIn());
//            Context context = v.getContext();
//            Intent intent = new Intent(context, WelcomeActivity.class);
//            context.startActivity(intent);
            //finish();
        });

        returnToDashButton.setOnAction(e -> {
            if (map != null) {
                mapButton.setVisible(true);
                listRatDataButton.setVisible(true);
                profileButton.setVisible(true);
                settingsButton.setVisible(true);
                reportRatButton.setVisible(true);
                logoutButton.setVisible(true);

                returnToDashButton.setVisible(false);
                applyFiltersButton.setVisible(false);

                onMap = !onMap;

                map = mapView.createMap(staticMapOptions);
                map.setHeading(BEARING);
            }
        });

        applyFiltersButton.setOnAction(e -> {
//            Intent intent = new Intent(v.getContext(), FilterActivity.class);
//            intent.putExtra(FilterActivity.FILTER, filter);
//            startActivityForResult(intent, FilterActivity.GET_FILTER);
        });
    }

    /**
     * Clears the current markers on the map, disables all map widgets,
     * then fetches rat data from the database, applying all current
     * filters, and adds a marker to the map for each one before
     * re-enabling all map widgets
     */
    private void loadFilteredMapMarkers() {
        progressBar.setVisible(true);
        returnToDashButton.setDisable(true);
        applyFiltersButton.setDisable(true);

        List<RatData> filteredList = new RatDatabase().getFilteredRatData(filter);
        map.clearMarkers();
        for (RatData r: filteredList) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLong(r.getLatitude(), r.getLongitude()));
            markerOptions.title(r.getKey() + "");
            markerOptions.label(r.getIncidentAddress() + "\n" + r.getCreatedDateTime());
            map.addMarker(new Marker(markerOptions));
        }
        progressBar.setVisible(false);
        returnToDashButton.setDisable(false);
        applyFiltersButton.setDisable(false);
    }

	@Override
	public void mapInitialized() {
		map = mapView.createMap(staticMapOptions);
		map.setHeading(BEARING);
		mapButton.setDisable(false);
	}
}
