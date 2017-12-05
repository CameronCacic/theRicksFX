package edu.gatech.cs2340.thericks.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import edu.gatech.cs2340.thericks.models.RatData;

/**
 * Utility class for filtering by date and time.
 *
 * Created by Ben Lashley on 11/1/2017.
 */

public class DateUtility {
	public static final String DATE_TIME_PATTERN = "MM/dd/yyyy hh:mm:ss a";
    public static final DateTimeFormatter DATE_TIME_FORMAT 
    		= DateTimeFormatter.ofPattern(DATE_TIME_PATTERN, Locale.ENGLISH);
    public static final String TIME_PATTERN = "hh:mm:ss a";
    public static final DateTimeFormatter TIME_FORMAT
    		= DateTimeFormatter.ofPattern(TIME_PATTERN, Locale.ENGLISH);
    public static final String DATE_PATTERN = "MM/dd/yyyy";
    public static final DateTimeFormatter DATE_FORMAT
    		= DateTimeFormatter.ofPattern(DATE_PATTERN, Locale.ENGLISH);

    /**
     * Returns a Date object containing the date exactly one month from today
     * @return the Date one month ago
     */
    public static LocalDateTime getLastMonth() {
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.minusMonths(1);
    }
    
    /**
     * Returns a Date object containing the date yesterday
     * @return the Date yesterday
     */
    public static LocalDateTime getYesterday() {
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.minusDays(1);
    }

    /**
     * Parses the input String using the static DATE_TIME_FORMAT DateFormat object
     * @param input the String to parse
     * @return the Date resulting from the String
     */
    public static LocalDateTime parse(String input) {
        return LocalDateTime.parse(input, DATE_TIME_FORMAT);
    }

    /**
     * Returns a Predicate that filters Dates in the specified range
     * @param begin the start date
     * @param end the end date
     * @return the predicate
     */
    public static Predicate<RatData> createDateRangeFilter(LocalDateTime begin, LocalDateTime end) {
        return ratData -> {
        	LocalDateTime dateTime = LocalDateTime.parse(ratData.getCreatedDateTime(), DATE_TIME_FORMAT);
            return (dateTime.compareTo(begin) >= 0) && (dateTime.compareTo(end) <= 0);
        };
    }

    /**
     * Filters the given List by the given date
     * @param begin the start date
     * @param end the end date
     * @param data the list
     * @return a list containing the RatData between the specified dates
     */
    public static Collection<RatData> filterByDate(LocalDateTime begin, LocalDateTime end, Collection<RatData> data) {
        Predicate<RatData> predicate = createDateRangeFilter(begin, end);
        return data.stream().filter(predicate).collect(Collectors.toList());
    }
}
