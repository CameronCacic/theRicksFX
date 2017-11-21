package edu.gatech.cs2340.thericks.utils;

public interface ResultObtainedCallback<T> {
	
	static final int RESULT_CANCELED = -1;
	static final int RESULT_OK = 0;
	static final int RESULT_LOGIN = 1;
	static final int RESULT_REGISTER = 2;
	
	void onResultObtained(T result);
}
