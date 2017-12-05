package edu.gatech.cs2340.thericks.utils;

/**
 * Class to mimic Android logging for easier library conversion
 * @author Cameron
 *
 */
public class Log {
	
	/**
	 * Enum representing the different levels of logging
	 * @author Cameron
	 *
	 */
	public static enum LogLevel {
		VERBOSE,
		DEBUG,
		ERROR
	}
	
	private static LogLevel level = LogLevel.VERBOSE;
	
	/**
	 * Sets the logging level to the specified value
	 * @param lvl
	 */
	public static void setLevel(LogLevel lvl) {
		level = lvl;
	}
	
	/**
	 * Logs a verbose level message
	 * @param tag the tag
	 * @param message the message
	 */
	public static void v(String tag, String message) {
		if (level.compareTo(LogLevel.VERBOSE) <= 0) {
			System.out.println("[VERBOSE] " + tag + ": " + message);
		}
	}

	/**
	 * Logs a debug level message
	 * @param tag the tag
	 * @param message the debug message
	 */
	public static void d(String tag, String message) {
		if (level.compareTo(LogLevel.DEBUG) <= 0) {
			System.out.println("[DEBUG] " + tag + ": " + message);
		}
	}
	
	/**
	 * Logs an error level message
	 * @param tag the tag
	 * @param message the error message
	 */
	public static void e(String tag, String message) {
		if (level.compareTo(LogLevel.ERROR) <= 0) {
			e(tag, message, null);
		}
	}
	
	/**
	 * Logs an error level message
	 * @param tag the tag
	 * @param message the error message
	 * @param e the Exception
	 */
	public static void e(String tag, String message, Exception e) {
		if (level.compareTo(LogLevel.ERROR) <= 0) {
			System.err.println("[ERROR] " + tag + ": " + message);
			if (e != null) {
				e.printStackTrace();
			}
		}
	}

}
