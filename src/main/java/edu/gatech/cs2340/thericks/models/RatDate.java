package edu.gatech.cs2340.thericks.models;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Cameron on 10/31/2017.
 * Holds a date and can read properly formatted Strings
 * as dates
 */
public final class RatDate implements Comparable<RatDate>, Serializable{

    private final int year;
    private final int month;
    private final int day;

    private RatDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    private RatDate(String timeDef) {
        String[] splitDef = timeDef.split("/");
        month = Integer.parseInt(splitDef[0]);
        day = Integer.parseInt(splitDef[1]);
        year = Integer.parseInt(splitDef[2]);
    }

    /**
     * Creates a new RatDate from the passed data
     * @param year the year
     * @param month the month
     * @param day the day
     * @return the new RatDate
     */
    public static RatDate forDate(int year, int month, int day) {
        return new RatDate(year, month, day);
    }

    /**
     * Creates a new RatDate from the passed String, returns
     * null if the String doesn't contain a date
     * @param possibleDateDef the String to pull a date from
     * @return the new RatDate
     */
    public static RatDate forDate(@SuppressWarnings("TypeMayBeWeakened") String possibleDateDef) {
        Pattern p = Pattern.compile("\\d\\d/\\d\\d/\\d\\d\\d\\d");
        Matcher m = p.matcher(possibleDateDef);
        if (m.find()) {
            return new RatDate(m.group());
        }
        return null;
    }

// --Commented out by Inspection START (11/20/2017 12:52 PM):
//    /**
//     * Builds a new RatDate from the passed RatDate, effectively cloning it
//     * @param d the RatDate to build a RatDate from
//     * @return the new RatDate
//     */
//    public static RatDate forDate(RatDate d) {
//        if (d != null) {
//            return new RatDate(d.getYear(), d.getMonth(), d.getDay());
//        }
//        return null;
//    }
// --Commented out by Inspection STOP (11/20/2017 12:52 PM)

    /**
     * Checks if the passed String contains a date
     * @param possibleDateDef the String to check
     * @return true if it contains a date, false otherwise
     */
    public static boolean isDate(@SuppressWarnings("TypeMayBeWeakened") String possibleDateDef) {
        Pattern p = Pattern.compile("\\d\\d/\\d\\d/\\d\\d\\d\\d");
        Matcher m = p.matcher(possibleDateDef);
        return m.find();
    }

    /**
     * Returns the year
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * Returns the month
     * @return the month
     */
    public int getMonth() {
        return month;
    }

    /**
     * Returns the day
     * @return the day
     */
    public int getDay() {
        return day;
    }

// --Commented out by Inspection START (11/20/2017 12:52 PM):
//    /**
//     * Sets the year to the specified value
//     * @param year the year to be set to
//     */
//    public void setYear(int year) {
//        this.year = year;
//    }
// --Commented out by Inspection STOP (11/20/2017 12:52 PM)

// --Commented out by Inspection START (11/20/2017 12:52 PM):
//    /**
//     * Sets the month to the specified value
//     * @param month the month to be set to
//     */
//    public void setMonth(int month) {
//        this.month = month;
//    }
// --Commented out by Inspection STOP (11/20/2017 12:52 PM)

// --Commented out by Inspection START (11/20/2017 12:52 PM):
//    /**
//     * Sets the day to the specified value
//     * @param day the day to be set to
//     */
//    public void setDay(int day) {
//        this.day = day;
//    }
// --Commented out by Inspection STOP (11/20/2017 12:52 PM)

    @Override
    public int compareTo(@Nullable RatDate ratDate) {
        if (ratDate == null) {
            return 1;
        }

        if (year < ratDate.year) {
            return -1;
        } else if (year > ratDate.year) {
            return 1;
        } else {
            if (month < ratDate.month) {
                return -1;
            } else if (month > ratDate.month) {
                return 1;
            } else {
                if (day < ratDate.day) {
                    return -1;
                } else if (day > ratDate.day) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%02d", month)
                + "/" + String.format(Locale.ENGLISH,"%02d", day) + "/"
                + String.format(Locale.ENGLISH,"%04d", year);
    }
}