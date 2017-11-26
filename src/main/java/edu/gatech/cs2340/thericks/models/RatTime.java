package edu.gatech.cs2340.thericks.models;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Cameron on 10/31/2017.
 * Holds a time and can read in a properly formatted time
 * from a String
 */
public final class RatTime implements Comparable<RatTime> {

    private static final int HOURS_IN_HALF_DAY = 12;

    private final int seconds;
    private final int minutes;
    private final int hours;

    private final Period period;

    private RatTime(int hours, int minutes, int seconds, Period period) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.period = period;
    }

    private RatTime(String timeDef) {
        String[] splitDef = timeDef.split(":");
        hours = Integer.parseInt(splitDef[0]);
        minutes = Integer.parseInt(splitDef[1]);
        String[] periodSplit = splitDef[2].split("\\s");
        seconds = Integer.parseInt(periodSplit[0]);
        period = Period.valueOf(periodSplit[1]);
    }

    /**
     * Creates a new RatTime from the passed data
     * @param hours the hours
     * @param minutes the minutes
     * @param seconds the seconds
     * @param period the period (AM or PM)
     * @return the new RatTime
     */
    public static RatTime forTime(int hours, int minutes, int seconds, Period period) {
        return new RatTime(hours, minutes, seconds, period);
    }

    /**
     * Creates a new RatTime from the passed String, returns null
     * if the String doesn't contain a time
     * @param possibleTimeDef the String to pull a time from
     * @return the new RatTime
     */
    public static RatTime forTime(String possibleTimeDef) {
        Pattern p = Pattern.compile("\\d\\d:\\d\\d:\\d\\d\\s[AP]M");
        Matcher m = p.matcher(possibleTimeDef);
        if (m.find()) {
            return new RatTime(m.group());
        }
        return null;
    }

// --Commented out by Inspection START (11/20/2017 12:52 PM):
//    /**
//     * Builds a new RatTime from the passed RatTime, effectively cloning it
//     * @param t the RatTime to build a RatTime from
//     * @return the new RatTime
//     */
//    static RatTime forTime(RatTime t) {
//        if (t != null) {
//            return new RatTime(t.getHours(), t.getMinutes(), t.getSeconds(), t.getPeriod());
//        }
//        return null;
//    }
// --Commented out by Inspection STOP (11/20/2017 12:52 PM)

    /**
     * Checks if the passed String contains a time
     * @param possibleTimeDef the String to check
     * @return true if it contains a time, false otherwise
     */
    public static boolean isTime(String possibleTimeDef) {
        Pattern p = Pattern.compile("\\d\\d:\\d\\d:\\d\\d\\s[AP]M");
        Matcher m = p.matcher(possibleTimeDef);
        return m.find();
    }

    /**
     * Returns the hours
     * @return the hours
     */
    public int getHours() {
        return hours;
    }

    /**
     * Returns the hours as they would be represented
     * on a 24 hour clock (i.e. 3:00pm becomes 15:00)
     * @return the 24 hours
     */
    public int get24Hours() {
        if (period.equals(Period.PM)) {
            return hours + HOURS_IN_HALF_DAY;
        }
        return hours;
    }

    /**
     * Returns the minutes
     * @return the minutes
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * Returns the seconds
     * @return the seconds
     */
    public int getSeconds() {
        return seconds;
    }

    /**
     * Returns the period
     * @return the period
     */
    public Period getPeriod() {
        return period;
    }

// --Commented out by Inspection START (11/20/2017 12:52 PM):
//    /**
//     * Sets the hours to the specified value
//     * @param hours the hours to be set to
//     */
//    void setHours(int hours) {
//        this.hours = hours;
//    }
// --Commented out by Inspection STOP (11/20/2017 12:52 PM)

// --Commented out by Inspection START (11/20/2017 12:52 PM):
//    /**
//     * Sets the minutes to the specified value
//     * @param minutes the minutes to be set to
//     */
//    void setMinutes(int minutes) {
//        this.minutes = minutes;
//    }
// --Commented out by Inspection STOP (11/20/2017 12:52 PM)

// --Commented out by Inspection START (11/20/2017 12:52 PM):
//    /**
//     * Sets the seconds to the specified value
//     * @param seconds the seconds to be set to
//     */
//    void setSeconds(int seconds) {
//        this.seconds = seconds;
//    }
// --Commented out by Inspection STOP (11/20/2017 12:52 PM)

// --Commented out by Inspection START (11/20/2017 12:52 PM):
//    /**
//     * Sets the period to the specified value
//     * @param period the period to be set to
//     */
//    void setPeriod(Period period) {
//        this.period = period;
//    }
// --Commented out by Inspection STOP (11/20/2017 12:52 PM)

    @Override
    public int compareTo(RatTime ratTime) {
        if (get24Hours() < ratTime.get24Hours()) {
            return -1;
        } else if (get24Hours() > ratTime.get24Hours()) {
            return 1;
        } else {
            if (minutes < ratTime.minutes) {
                return -1;
            } else if (minutes > ratTime.minutes) {
                return 1;
            } else {
                if (seconds < ratTime.seconds) {
                    return -1;
                } else if (seconds > ratTime.seconds) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH,"%02d", hours) + ":"
                + String.format(Locale.ENGLISH,"%02d", minutes) + ":"
                + String.format(Locale.ENGLISH,"%02d", seconds) + " " + period.toString();
    }
}