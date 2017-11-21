package edu.gatech.cs2340.thericks.utils;

public interface ResultObtainedCallback<T> {
	
	static final int RESULT_CANCELED = -1;
	static final int RESULT_LOGIN = 0;
	static final int RESULT_REGISTER = 1;
	
	void onResultObtained(T result);
}
