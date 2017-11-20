package edu.gatech.cs2340.thericks.models;

/**
 * Created by Cameron on 10/31/2017.
 * Holds a date and time and can read dates and times in from
 * properly formatted Strings and raw data
 */
public final class RatDateTime implements Comparable<RatDateTime> {

    private final RatDate date;
    private final RatTime time;

// --Commented out by Inspection START (11/20/2017 12:52 PM):
//    private RatDateTime(RatDate date, RatTime time) {
//        this.date = date;
//        this.time = time;
//    }
// --Commented out by Inspection STOP (11/20/2017 12:52 PM)

    private RatDateTime(String dateTimeDef) {
        date = RatDate.forDate(dateTimeDef);
        time = RatTime.forTime(dateTimeDef);
    }

//    private RatDateTime(int year, int month, int day, int hour, int minute, int second,
//                        Period period) {
//        date = RatDate.forDate(year, month, day);
//        time = RatTime.forTime(hour, minute, second, period);
//    }

    /**
     * Creates a new RatDateTime from the passed String, returns
     * null if no date and time were found in the String
     * @param possibleDateTimeDef the String to pull the date and
     *                            time from
     * @return the new RatDateTime
     */
    public static RatDateTime forDateTime(String possibleDateTimeDef) {
        if (RatDate.isDate(possibleDateTimeDef) && RatTime.isTime(possibleDateTimeDef)) {
            return new RatDateTime(possibleDateTimeDef);
        }
        return null;
    }

// --Commented out by Inspection START (11/20/2017 12:51 PM):
//    /**
//     * Builds a new RatDateTime from the passed RatDateTime, effectively cloning it
//     * @param r the RatDateTime to build a new RatDateTime from
//     * @return the new RatDateTime
//     */
//    public static RatDateTime forDateTime(RatDateTime r) {
//        if (r != null) {
//            return new RatDateTime(RatDate.forDate(r.getDate()), RatTime.forTime(r.getTime()));
//        }
//        return null;
//    }
// --Commented out by Inspection STOP (11/20/2017 12:51 PM)

    /**
     * Checks if the passed String contains a date and time
     * @return true if it contains a date and time, false otherwise
     */
    public static boolean isDateTime() {
        return RatDate.isDate("09/22/1992 04:12:19 AM") && RatTime.isTime("09/22/1992 04:12:19 AM");
    }

    /**
     * Getter for date
     * @return the date
     */
    public RatDate getDate() {
        return date;
    }

    /**
     * Getter for time
     * @return the time
     */
    public RatTime getTime() {
        return time;
    }

    /**
     * Getter for year
     * @return the year
     */
    public int getYear() {
        return date.getYear();
    }

    /**
     * Getter for month
     * @return the month
     */
    public int getMonth() {
        return date.getMonth();
    }

    /**
     * Getter for day
     * @return the day
     */
    public int getDay() {
        return date.getDay();
    }

    /**
     * Getter for hours
     * @return the hours
     */
    public int getHours() {
        return time.getHours();
    }

// --Commented out by Inspection START (11/20/2017 12:51 PM):
//    /**
//     * Getter for military time
//     * @return the 24-hour
//     */
//    public int get24Hours() {
//        return time.get24Hours();
//    }
// --Commented out by Inspection STOP (11/20/2017 12:51 PM)

    /**
     * Getter for minutes
     * @return the minutes
     */
    public int getMinutes() {
        return time.getMinutes();
    }

    /**
     * Getter for seconds
     * @return the seconds
     */
    public int getSeconds() {
        return time.getSeconds();
    }

// --Commented out by Inspection START (11/20/2017 12:51 PM):
//    /**
//     * Getter for period
//     * @return the period
//     */
//    public Period getPeriod() {
//        return time.getPeriod();
//    }
// --Commented out by Inspection STOP (11/20/2017 12:51 PM)

// --Commented out by Inspection START (11/20/2017 12:51 PM):
//    /**
//     * Setter for date
//     * @param date new date
//     */
//    public void setDate(RatDate date) {
//        this.date = date;
//    }
// --Commented out by Inspection STOP (11/20/2017 12:51 PM)

// --Commented out by Inspection START (11/20/2017 12:51 PM):
//    /**
//     * Setter for time
//     * @param time new time
//     */
//    public void setTime(RatTime time) {
//        this.time = time;
//    }
// --Commented out by Inspection STOP (11/20/2017 12:51 PM)

// --Commented out by Inspection START (11/20/2017 12:51 PM):
//    /**
//     * Setter for year
//     * @param year new year
//     */
//    public void setYear(int year) {
//        date.setYear(year);
//    }
// --Commented out by Inspection STOP (11/20/2017 12:51 PM)

// --Commented out by Inspection START (11/20/2017 12:51 PM):
//    /**
//     * Setter for month
//     * @param month new month
//     */
//    public void setMonth(int month) {
//        date.setMonth(month);
//    }
// --Commented out by Inspection STOP (11/20/2017 12:51 PM)

// --Commented out by Inspection START (11/20/2017 12:51 PM):
//    /**
//     * Setter for day
//     * @param day new day
//     */
//    public void setDay(int day) {
//        date.setDay(day);
//    }
// --Commented out by Inspection STOP (11/20/2017 12:51 PM)

// --Commented out by Inspection START (11/20/2017 12:51 PM):
//    /**
//     * Setter for hours
//     * @param hours new hours
//     */
//    public void setHours(int hours) {
//        time.setHours(hours);
//    }
// --Commented out by Inspection STOP (11/20/2017 12:51 PM)

// --Commented out by Inspection START (11/20/2017 12:51 PM):
//    /**
//     * Setter for minutes
//     * @param minutes new minutes
//     */
//    public void setMinutes(int minutes) {
//        time.setMinutes(minutes);
//    }
// --Commented out by Inspection STOP (11/20/2017 12:51 PM)

// --Commented out by Inspection START (11/20/2017 12:51 PM):
//    /**
//     * Setter for seconds
//     * @param seconds new seconds
//     */
//    public void setSeconds(int seconds) {
//        time.setSeconds(seconds);
//    }
// --Commented out by Inspection STOP (11/20/2017 12:51 PM)

// --Commented out by Inspection START (11/20/2017 12:51 PM):
//    /**
//     * Setter for period
//     * @param period new period
//     */
//    public void setPeriod(Period period) {
//        time.setPeriod(period);
//    }
// --Commented out by Inspection STOP (11/20/2017 12:51 PM)

    @Override
    public int compareTo(RatDateTime ratDateTime) {
        int dateComp = date.compareTo(ratDateTime.getDate());
        if (dateComp == 0) {
            return time.compareTo(ratDateTime.getTime());
        }
        return dateComp;
    }

    @Override
    public String toString() {
        return date.toString() + " " + time.toString();
    }
}