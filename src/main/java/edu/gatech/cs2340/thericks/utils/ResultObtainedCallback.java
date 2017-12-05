package edu.gatech.cs2340.thericks.utils;

/**
 * Callback for obtaining a result and passing it to the callback owner.
 * For state results, Integer should be used, as there are some predefined
 * Integer states avalible to use
 * @author Cameron
 *
 * @param <T> the data type of the result
 */
public interface ResultObtainedCallback<T> {
	
	static final int RESULT_CANCELED = -1;
	static final int RESULT_OK = 0;
	static final int RESULT_LOGIN = 1;
	static final int RESULT_REGISTER = 2;
	static final int RESULT_MAP = 3;
	static final int RESULT_GRAPH = 4;
	static final int RESULT_DATA_LIST = 5;
	static final int RESULT_PROFILE = 6;
	static final int RESULT_SETTINGS = 7;
	static final int RESULT_REPORT = 8;
	static final int RESULT_LOGOUT = 9;
	static final int RESULT_FILTER = 10;
	static final int RESULT_RESTART = 11;
	static final int RESULT_STOP = 12;
	
	/**
	 * Called when a result has been obtained
	 * @param result the result
	 */
	void onResultObtained(T result);
}
