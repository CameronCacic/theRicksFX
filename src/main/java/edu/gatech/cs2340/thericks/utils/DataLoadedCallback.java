package edu.gatech.cs2340.thericks.utils;

/**
 * Callback for notifying when a data loading task has finished
 * @author Cameron
 *
 */
public interface DataLoadedCallback {
	
	/**
	 * Called when data has finished loading
	 */
	void notifyDataLoaded();
}
