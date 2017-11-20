package edu.gatech.cs2340.thericks.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
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
    private static final DateFormat DATE_TIME_FORMAT
            = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.ENGLISH);
    public static final DateFormat TIME_FORMAT
            = new SimpleDateFormat("hh:mm:ss a", Locale.ENGLISH);
    public static final DateFormat DATE_FORMAT
            = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

    /**
     * Returns a Date object containing the date exactly one month from today
     * @return the Date one month ago
     */
    public static Date getLastMonth() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        if (month > 0) {
            cal.set(Calendar.MONTH, month - 1);
        } else {
            cal.set(Calendar.MONTH, Calendar.DECEMBER);
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
        }
        return cal.getTime();
    }

    /**
     * Parses the input String using the static DATE_TIME_FORMAT DateFormat object
     * @param input the String to parse
     * @return the Date resulting from the String
     */
    public static Date parse(String input) {
        try {
            return DATE_TIME_FORMAT.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns a Predicate that filters Dates in the specified range
     * @param begin the start date
     * @param end the end date
     * @return the predicate
     */
    public static Predicate<RatData> createDateRangeFilter(Date begin, Date end) {
        return ratData -> {
            Date d = parse(ratData.getCreatedDateTime());
            return (d != null) && ((begin == null) || !d.before(begin))
                    && ((end == null) || !d.after(end));
        };
    }

    /**
     * Filters the given List by the given date
     * @param begin the start date
     * @param end the end date
     * @param data the list
     * @return a list containing the RatData between the specified dates
     */
    public static Collection<RatData> filterByDate(Date begin, Date end, Collection<RatData> data) {
        Predicate<RatData> predicate = createDateRangeFilter(begin, end);
        return data.stream().filter(predicate).collect(Collectors.toList());
    }
}
