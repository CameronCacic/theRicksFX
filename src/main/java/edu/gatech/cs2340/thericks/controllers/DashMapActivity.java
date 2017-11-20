package edu.gatech.cs2340.thericks.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import edu.gatech.cs2340.thericks.R;
import edu.gatech.cs2340.thericks.database.RatDatabase;
import edu.gatech.cs2340.thericks.models.RatData;
import edu.gatech.cs2340.thericks.models.RatFilter;
import edu.gatech.cs2340.thericks.models.User;

/**
 * Created by Cameron on 10/6/2017.
 * Dash-map activity in dash mode provides numerous activity options for a logged in user
 * to engage in. Defaults to dash mode, upon selecting map, dash-map switches to map mode,
 * where users can view rat data displayed on a Google Map, filtered by date
 */
public class DashMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = DashMapActivity.class.getSimpleName();
    private static final int ADD_RAT_DATA_REQUEST = 2;

    //default position, zoom, and bearing to set the map camera to
    private static final CameraPosition position = new CameraPosition(new LatLng(40.776278,
            -73.99086), 12, 0, 30);

    private GoogleMap map;

    private boolean onMap;

    private Button mapButton;
    private Button listRatDataButton;
    private Button profileButton;
    private Button settingsButton;
    private Button reportRatButton;
    private Button logoutButton;

    private Button returnToDashButton;
    private Button applyFiltersButton;

    private RatFilter filter;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_map);

        Log.d(TAG, "Entered dashboard activity");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapButton = findViewById(R.id.map_button);
        Button graphButton = findViewById(R.id.rat_graph_button);
        listRatDataButton = findViewById(R.id.rat_data_list_button);
        profileButton = findViewById(R.id.profile_button);
        settingsButton = findViewById(R.id.settings_button);
        reportRatButton = findViewById(R.id.report_rat_button);
        logoutButton = findViewById(R.id.logout_button);

        returnToDashButton = findViewById(R.id.return_to_dashboard_button);
        returnToDashButton.setVisibility(View.GONE);

        applyFiltersButton = findViewById(R.id.apply_filters_button);
        applyFiltersButton.setVisibility(View.GONE);

        filter = RatFilter.getDefaultInstance();

        onMap = false;

        progressBar = findViewById(R.id.progress_bar_dash_map);
        progressBar.setVisibility(View.GONE);

        Bundle b = getIntent().getExtras();
        assert b != null;
        User user = b.getParcelable("edu.gatech.cs2340.thericks.User");
        assert user != null;
        user.login();
        Log.d(TAG, user.getLogin().getUsername() + " is logged in = " + user.isLoggedIn());

        mapButton.setOnClickListener((View v) -> {
            Log.d(TAG, "Rat Map Button pressed");
            if (map != null) {
                mapButton.setVisibility(View.GONE);
                listRatDataButton.setVisibility(View.GONE);
                profileButton.setVisibility(View.GONE);
                settingsButton.setVisibility(View.GONE);
                reportRatButton.setVisibility(View.GONE);
                logoutButton.setVisibility(View.GONE);

                loadFilteredMapMarkers();

                returnToDashButton.setVisibility(View.VISIBLE);
                applyFiltersButton.setVisibility(View.VISIBLE);

                onMap = !onMap;
                map.getUiSettings().setAllGesturesEnabled(onMap);
            }
        });

        graphButton.setOnClickListener((View v) ->  {
            Log.d(TAG, "Graph button pressed");
            Context context = v.getContext();
            Intent intent = new Intent(context, GraphActivity.class);
            context.startActivity(intent);
        });

        listRatDataButton.setOnClickListener((View v) -> {
            Log.d(TAG, "Rat Data List button pressed");
            Context context = v.getContext();
            Intent intent = new Intent(context, RatDataListActivity.class);
            context.startActivity(intent);
        });

        profileButton.setOnClickListener((View v) -> {
            Log.d(TAG, "Profile button pressed");
            Context context = v.getContext();
            Intent intent = new Intent(context, FilterActivity.class);
            context.startActivity(intent);
        });

        reportRatButton.setOnClickListener((View v) -> {
            Log.d(TAG, "Report a Rat button pushed");
            Context context = v.getContext();
            Intent intent = new Intent(context, RatEntryActivity.class);
            startActivityForResult(intent, ADD_RAT_DATA_REQUEST);
        });

        logoutButton.setOnClickListener(v -> {
            user.logout();
            Log.d(TAG, "Logout button pressed");
            Log.d(TAG, user.getLogin().getUsername() + " is logged in = "
                    + user.isLoggedIn());
            Context context = v.getContext();
            Intent intent = new Intent(context, WelcomeActivity.class);
            context.startActivity(intent);
            finish();
        });

        returnToDashButton.setOnClickListener(v -> {
            if (map != null) {
                mapButton.setVisibility(View.VISIBLE);
                listRatDataButton.setVisibility(View.VISIBLE);
                profileButton.setVisibility(View.VISIBLE);
                settingsButton.setVisibility(View.VISIBLE);
                reportRatButton.setVisibility(View.VISIBLE);
                logoutButton.setVisibility(View.VISIBLE);

                returnToDashButton.setVisibility(View.GONE);
                applyFiltersButton.setVisibility(View.GONE);

                onMap = !onMap;
                map.getUiSettings().setAllGesturesEnabled(onMap);

                map.clear();

                map.animateCamera(CameraUpdateFactory.newCameraPosition(position));
            }
        });

        applyFiltersButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), FilterActivity.class);
            intent.putExtra(FilterActivity.FILTER, filter);
            startActivityForResult(intent, FilterActivity.GET_FILTER);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "Received result from RatEntryActivity");
        if (requestCode == FilterActivity.GET_FILTER) {
            if (resultCode == RESULT_OK) {
                filter = data.getParcelableExtra(FilterActivity.FILTER);
                loadFilteredMapMarkers();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setAllGesturesEnabled(onMap);
        map.animateCamera(CameraUpdateFactory.newCameraPosition(position));
        map.setInfoWindowAdapter(new CustomInfoWindowAdapter());
    }

    /**
     * Clears the current markers on the map, disables all map widgets,
     * then fetches rat data from the database, applying all current
     * filters, and adds a marker to the map for each one before
     * re-enabling all map widgets
     */
    private void loadFilteredMapMarkers() {
        progressBar.setVisibility(View.VISIBLE);
        returnToDashButton.setEnabled(false);
        applyFiltersButton.setEnabled(false);

        List<RatData> filteredList = new RatDatabase().getFilteredRatData(filter);
        map.clear();
        for (RatData r: filteredList) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(r.getLatitude(), r.getLongitude()));
            markerOptions.title(r.getKey() + "");
            markerOptions.snippet(r.getIncidentAddress() + ";" + r.getCreatedDateTime());
            map.addMarker(markerOptions);
        }
        progressBar.setVisibility(View.GONE);
        returnToDashButton.setEnabled(true);
        applyFiltersButton.setEnabled(true);
    }

    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        CustomInfoWindowAdapter(){
            myContentsView = getLayoutInflater().inflate(R.layout.rat_marker_view, null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            TextView titleText = myContentsView.findViewById(R.id.rat_marker_view_title_text);
            titleText.setText(marker.getTitle());
            TextView snippetText1 = myContentsView.findViewById(R.id.rat_marker_view_snippet1_text);
            TextView snippetText2 = myContentsView.findViewById(R.id.rat_marker_view_snippet2_text);
            String[] splitSnippet = marker.getSnippet().split(";");
            snippetText1.setText(splitSnippet[0]);
            snippetText2.setText(splitSnippet[1]);

            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

    }
}
