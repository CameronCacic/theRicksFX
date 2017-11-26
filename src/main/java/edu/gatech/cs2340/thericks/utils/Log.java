package edu.gatech.cs2340.thericks.utils;

public class Log {

	/**
	 * Logs a debug level message
	 * @param tag the tag
	 * @param message the debug message
	 */
	public static void d(String tag, String message) {
		System.out.println("[DEBUG] " + tag + ": " + message);
	}
	
	/**
	 * Logs an error level message
	 * @param tag the tag
	 * @param message the error message
	 */
	public static void e(String tag, String message) {
		e(tag, message, null);
	}
	
	/**
	 * Logs an error level message
	 * @param tag the tag
	 * @param message the error message
	 * @param e the Exception
	 */
	public static void e(String tag, String message, Exception e) {
		System.err.println("[ERROR] " + tag + ": " + message);
		if (e != null) {
			e.printStackTrace();
		}
	}

}
