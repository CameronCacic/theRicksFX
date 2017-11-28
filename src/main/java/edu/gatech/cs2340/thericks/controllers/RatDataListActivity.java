package edu.gatech.cs2340.thericks.controllers;

import java.io.IOException;

import edu.gatech.cs2340.thericks.database.RatDatabase;
import edu.gatech.cs2340.thericks.models.RatData;
import edu.gatech.cs2340.thericks.models.RatFilter;
import edu.gatech.cs2340.thericks.utils.DataLoadedCallback;
import edu.gatech.cs2340.thericks.utils.Log;
import edu.gatech.cs2340.thericks.utils.NewFilterCallback;
import edu.gatech.cs2340.thericks.utils.ResultObtainedCallback;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 * Created by Cameron on 10/5/2017.
 * Displays the rat data from the database in a list format
 */
public class RatDataListActivity extends AnchorPane implements NewFilterCallback {

    private static final String TAG = RatDataListActivity.class.getSimpleName();

    private ObservableList<RatData> ratDataList;
    
    private RatFilter filter;
    
    private ResultObtainedCallback<RatData> callback;

    @FXML
    private ListView<RatData> ratDataListView;
    
    @FXML
    private ProgressIndicator progressIndicator;
    
    public RatDataListActivity(RatFilter f, ResultObtainedCallback<RatData> call) {
    	filter = f;
    	callback = call;
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/activity_rat_data_list.fxml"));
    	loader.setController(this);
    	loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
    }
    }

    public void initialize() {

        ratDataListView.setCellFactory(new Callback<ListView<RatData>, ListCell<RatData>>() {
			
			@Override
			public ListCell<RatData> call(ListView<RatData> param) {
				return new RatDataListCell();
			}
			
		});
        
        ratDataList = FXCollections.observableArrayList();
        ratDataListView.setItems(ratDataList);
        
        ratDataListView.setOnMouseClicked(e -> {
        	callback.onResultObtained(ratDataListView.getSelectionModel().getSelectedItem());
        });
        
        notifyFilterUpdated();
    }

    private class RatDataListCell extends ListCell<RatData> {
    	
    	@Override
    	protected void updateItem(RatData r, boolean empty) {
    		super.updateItem(r, empty);
    		if (r != null && !empty) {
    			VBox mainBox = new VBox();
    			mainBox.setPadding(new Insets(5));
    			mainBox.setSpacing(5);
    			mainBox.setAlignment(Pos.TOP_LEFT);
    			
    			Text city = new Text(r.getCity());
    			
    			HBox subBox = new HBox();
    			Text address = new Text(r.getIncidentAddress());
    			Text dateTime = new Text(r.getCreatedDateTime());
    			Pane spacer = new Pane();
    			HBox.setHgrow(spacer, Priority.ALWAYS);
    			subBox.getChildren().addAll(address, spacer, dateTime);
    			
    			mainBox.getChildren().addAll(city, subBox);
    			
    			setGraphic(mainBox);
    		}
    	}
    }

	@Override
	public void notifyFilterUpdated() {
		Log.d(TAG, "Calling the RatDatabase to load the data");
		
		RatDatabase database = new RatDatabase();
        progressIndicator.setVisible(true);
        database.loadData(new DataLoadedCallback() {
			
			@Override
			public void notifyDataLoaded() {
				progressIndicator.setVisible(false);
				Log.d(TAG, "Data loaded");
			}
			
		}, ratDataList, filter);
	}
}