package edu.gatech.cs2340.thericks.utils;

/**
 * Callback for notifying when a filte object has been updated
 * @author Cameron
 *
 */
public interface NewFilterCallback {
	
	/**
	 * Called when a new filter is ready to be worked with
	 */
	void notifyFilterUpdated();
}
