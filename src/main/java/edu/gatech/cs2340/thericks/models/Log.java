package edu.gatech.cs2340.thericks.models;

public class Log {

	/**
	 * Logs a debug level message
	 * @param tag
	 * @param message
	 */
	public static void d(String tag, String message) {
		System.out.println(tag + ": " + message);
	}

}
