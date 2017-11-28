package edu.gatech.cs2340.thericks.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.gatech.cs2340.thericks.database.RatDatabase;
import edu.gatech.cs2340.thericks.models.Months;
import edu.gatech.cs2340.thericks.models.RatData;
import edu.gatech.cs2340.thericks.models.RatFilter;
import edu.gatech.cs2340.thericks.utils.DataLoadedCallback;
import edu.gatech.cs2340.thericks.utils.DateUtility;
import edu.gatech.cs2340.thericks.utils.Log;
import edu.gatech.cs2340.thericks.utils.NewFilterCallback;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * Created by Cameron on 11/3/2017.
 * Holds multiple different graphs for displaying rat data
 */

public class GraphActivity extends AnchorPane implements NewFilterCallback {

    private static final String TAG = GraphActivity.class.getSimpleName();

    private static final float X_LABEL_ROTATION = 60f;

    private List<RatData> loadedData;
    
    private RatFilter filter;
    
    private DataLoadedCallback dataCallback;
    
    private LineChart<String, Number> chart;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private Text noDataText;

    public GraphActivity(RatFilter f) {
    	
    	assert f != null;
		filter = f;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("activity_graph.fxml"));
    	loader.setController(this);
    	loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    public void initialize() {

        Log.d(TAG, "Entered Graph Activity");

        noDataText.setVisible(false);

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");
        xAxis.setTickLabelRotation(X_LABEL_ROTATION);
        NumberAxis yAxis = new NumberAxis();
        chart = new LineChart<>(xAxis, yAxis);
        getChildren().add(chart);
        setTopAnchor(chart, 0d);
        setBottomAnchor(chart, 0d);
        setRightAnchor(chart, 0d);
        setLeftAnchor(chart, 0d);
        
        chart.setVisible(false);

        loadedData = new ArrayList<>();
        
        dataCallback = new DataLoadedCallback() {
			
			@Override
			public void notifyDataLoaded() {
				Log.d(TAG, "Notified that the data finished loading");
                displayGraph();
			}
		};

        RatDatabase db = new RatDatabase();
        db.loadData(dataCallback, loadedData, filter);
    }
    
    @Override
	public void notifyFilterUpdated() {
        progressIndicator.setVisible(true);
        RatDatabase db = new RatDatabase();
        db.loadData(dataCallback, loadedData, filter);
	}

    /**
     * Displays the graph, calculates the bounds for the specified date range,
     * and gets the needed data from the database
     */
    private void displayGraph() {
        progressIndicator.setVisible(true);
        chart.setVisible(false);
        noDataText.setVisible(false);

        Log.d(TAG, "Sorting data");
        loadedData.sort((ratData1, ratData2) -> {
            LocalDateTime date1 = DateUtility.parse(ratData1.getCreatedDateTime());
            LocalDateTime date2 = DateUtility.parse(ratData2.getCreatedDateTime());
            if ((date1 == null) && (date2 == null)) {
                return 0;
            }
            if (date1 == null) {
                return -1;
            }
            if (date2 == null) {
                return 1;
            }
            return date1.compareTo(date2);
        });

        Log.d(TAG, "Displaying graph");

        if (!loadedData.isEmpty()) {

            LocalDateTime beginDateTime = DateUtility.parse(loadedData.get(0).getCreatedDateTime());
            int beginMonth = beginDateTime.getMonthValue();
            int beginYear = beginDateTime.getYear();
            
            LocalDateTime endDateTime = DateUtility.parse(
            		loadedData.get(loadedData.size() - 1).getCreatedDateTime());
            int endMonth = endDateTime.getMonthValue();
            int endYear = endDateTime.getYear();

            int monthDif = (endMonth + (endYear * Months.values().length))
                    - (beginMonth + (beginYear * Months.values().length));

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Rat Data");
            for (int i = 0; i < monthDif; i++) {
            	LocalDateTime oneMoreMonth = beginDateTime.plusMonths(1);
                series.getData().add(
                		new XYChart.Data<String, Number>(formatLocalDateTime(oneMoreMonth),
                				DateUtility.filterByDate(beginDateTime, oneMoreMonth, loadedData).size()));
                beginDateTime = beginDateTime.plusMonths(1);
            }

            chart.getData().add(series);

            chart.setVisible(true);
        } else {
            noDataText.setVisible(true);
        }

        progressIndicator.setVisible(false);
    }
    
    private String formatLocalDateTime(LocalDateTime dateTime) {
    	return Months.values()[dateTime.getMonthValue() - 1] + " " + dateTime.getYear();
    }
}