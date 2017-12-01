package edu.gatech.cs2340.thericks.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import edu.gatech.cs2340.thericks.utils.DateUtility;

/**
 * Created by Cameron on 11/18/2017.
 * Holds the raw data and Predicates needed for filtering RatData. Its
 * primary reason for existence is to be passed between FilterActivity
 * and whatever activity starts FilterActivity for a result, so that
 * filters can be obtained from the FilterActivity and so that the
 * FilterActivity can rebuild itself from the raw data inside this
 * class. Any custom Predicates added to this class (custom meaning
 * any Predicate that will be put into the map with a procedurally
 * generated key)
 */
public class RatFilter {

    public static final String DATE = "DATE";
    public static final String LOCATION_TYPE = "LOCATION_TYPE";
    public static final String ZIP = "ZIP";
    public static final String ADDRESS = "ADDRESS";
    public static final String CITY = "CITY";
    public static final String BOROUGH = "BOROUGH";
    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";

    private final Map<String, Predicate<RatData>> predicates;
    private final Map<String, Boolean> enabledMap;

    // raw data
    private String beginDateStr;
    private String beginTimeStr;
    private String endDateStr;
    private String endTimeStr;
    private String locationType;
    private Integer zip;
    private String address;
    private String city;
    private String borough;
    private Double minLatitude;
    private Double maxLatitude;
    private Double minLongitude;
    private Double maxLongitude;

    private RatFilter() {
        predicates = new HashMap<>();
        enabledMap = new HashMap<>();
    }

    /**
     * Builds a new RatFilter with all the passed Predicates included as filters. Allows for
     * custom filtering outside of the normal filters
     * @param set the srt of Predicates to add as custom filters
     */
    public RatFilter(Iterable<Predicate<RatData>> set) {
        predicates = new HashMap<>();
        enabledMap = new HashMap<>();
        if (set != null) {
            int i = 0;
            for (Predicate<RatData> p : set) {
                if (p != null) {
                    predicates.put(i + p.toString(), p);
                    enabledMap.put(i + p.toString(), true);
                    i++;
                }
            }
        }
    }

    private void buildAllPredicates() {
        if ((beginDateStr != null)
                && (beginTimeStr != null)
                && (endDateStr != null)
                && (endTimeStr != null)) {
            buildDatePredicate();
        }
        if (locationType != null) {
            buildLocationTypePredicate();
        }
        if (zip != null) {
            buildZipPredicate();
        }
        if (address != null) {
            buildAddressPredicate();
        }
        if (city != null) {
            buildCityPredicate();
        }
        if (borough != null ) {
            buildBoroughPredicate();
        }
        if ((minLatitude != null) && (maxLatitude != null)) {
            buildLatitudePredicate();
        }
        if ((minLongitude != null) && (maxLongitude != null)) {
            buildLongitudePredicate();
        }
    }

    private void buildDatePredicate() {
        predicates.put(DATE, DateUtility.createDateRangeFilter(
                DateUtility.parse(beginDateStr + " " + beginTimeStr),
                DateUtility.parse(endDateStr + " " + endTimeStr)));
    }

    private void buildLocationTypePredicate() {
        predicates.put(LOCATION_TYPE, ratData ->
                ratData.getLocationType().equalsIgnoreCase(locationType));
    }

    private void buildZipPredicate() {
        predicates.put(ZIP, ratData -> ratData.getIncidentZip() == zip);
    }

    private void buildAddressPredicate() {
        predicates.put(ADDRESS, ratData ->
                ratData.getIncidentAddress().toLowerCase().contains(address.toLowerCase()));
    }

    private void buildCityPredicate() {
        predicates.put(CITY, ratData -> ratData.getCity().equalsIgnoreCase(city));
    }

    private void buildBoroughPredicate() {
        predicates.put(BOROUGH, ratData -> ratData.getBorough().equalsIgnoreCase(borough));
    }

    private void buildLatitudePredicate() {
        predicates.put(LATITUDE, ratData -> (ratData.getLatitude() >= minLatitude)
                && (ratData.getLatitude() <= maxLatitude));
    }

    private void buildLongitudePredicate() {
        predicates.put(LONGITUDE, ratData -> (ratData.getLongitude() >= minLongitude)
                && (ratData.getLongitude() <= maxLongitude));
    }

    /**
     * Enables the specified predicate to be included in the final Predicate List
     * @param key the Predicate's key
     * @param value the boolean to enable or disable the predicate
     */
    public void setPredicateEnabled(String key, boolean value) {
        enabledMap.put(key, value);
    }

    /**
     * Disables all predicates
     */
    public void disableAllPredicates() {
        for (String key: enabledMap.keySet()) {
            setPredicateEnabled(key, false);
        }
    }

    /**
     * Sets the beginning date for the date filter
     * @param beginDate the beginning date
     */
    public void setBeginDate(LocalDateTime beginDate) {
        beginDateStr = DateUtility.DATE_FORMAT.format(beginDate);
        beginTimeStr = DateUtility.TIME_FORMAT.format(beginDate);
        if ((endDateStr != null) && (endTimeStr != null)) {
            buildDatePredicate();
        }
    }

    /**
     * Sets the ending date for the date filter
     * @param endDate the ending date
     */
    public void setEndDate(LocalDateTime endDate) {
        endDateStr = DateUtility.DATE_FORMAT.format(endDate);
        endTimeStr = DateUtility.TIME_FORMAT.format(endDate);
        if ((beginDateStr != null) && (beginTimeStr != null)) {
            buildDatePredicate();
        }
    }

    /**
     * Sets the location type for the location type filter
     * @param locationType the location type
     */
    public void setLocationType(String locationType) {
        this.locationType = locationType;
        buildLocationTypePredicate();
    }

    /**
     * Sets the zip code for the zip code filter
     * @param zip the zip code
     */
    public void setZip(Integer zip) {
        this.zip = zip;
        if (zip != null ) {
            buildZipPredicate();
        }
    }

    /**
     * Sets the address for the address filter
     * @param address the address
     */
    public void setAddress(String address) {
        this.address = address;
        buildAddressPredicate();
    }

    /**
     * Sets the city for the city filter
     * @param city the city
     */
    public void setCity(String city) {
        this.city = city;
        buildCityPredicate();
    }

    /**
     * Sets the borough for the borough filter
     * @param borough the borough
     */
    public void setBorough(String borough) {
        this.borough = borough;
        buildBoroughPredicate();
    }

    /**
     * Sets the minimum latitude for the latitude filter
     * @param minLatitude the minimum latitude
     */
    public void setMinLatitude(Double minLatitude) {
        this.minLatitude = minLatitude;
        if ((minLatitude != null)
                && (maxLatitude != null)) {
            buildLatitudePredicate();
        }
    }

    /**
     * Sets the maximum latitude for the latitude filter
     * @param maxLatitude the maximum latitude
     */
    public void setMaxLatitude(Double maxLatitude) {
        this.maxLatitude = maxLatitude;
        if ((minLatitude != null)
                && (maxLatitude != null)) {
            buildLatitudePredicate();
        }
    }

    /**
     * Sets the minimum longitude for the longitude filter
     * @param minLongitude the minimum longitude
     */
    public void setMinLongitude(Double minLongitude) {
        this.minLongitude = minLongitude;
        if ((maxLongitude != null)
                && (minLongitude != null)) {
            buildLongitudePredicate();
        }
    }

    /**
     * Sets the maximum longitude for the longitude filter
     * @param maxLongitude the maximum longitude
     */
    public void setMaxLongitude(Double maxLongitude) {
        this.maxLongitude = maxLongitude;
        if ((minLongitude != null)
                && (maxLongitude != null)) {
            buildLongitudePredicate();
        }
    }

    /**
     * Returns if a predicate exists for the given key. A true value would indicate that a
     * predicate exists and all the raw data for that predicate are present and valid
     * @param key the key
     * @return if the predicate exists
     */
    public boolean hasPredicate(String key) {
        return predicates.get(key) != null;
    }

    /**
     * Returns if the predicate for the passed key is enabled
     * @param key the key
     * @return if the predicate is enabled
     */
    public boolean isPredicateEnabled(String key) {
        return enabledMap.get(key);
    }

    /**
     * Returns the beginning date raw data
     * @return the beginning date
     */
    public CharSequence getBeginDateStr() {
        return beginDateStr;
    }

    /**
     * Returns the beginning time raw data
     * @return the beginning time
     */
    public CharSequence getBeginTimeStr() {
        return beginTimeStr;
    }

    /**
     * Returns the ending date raw data
     * @return the ending date
     */
    public CharSequence getEndDateStr() {
        return endDateStr;
    }

    /**
     * Returns the ending time raw data
     * @return the ending time
     */
    public CharSequence getEndTimeStr() {
        return endTimeStr;
    }

    /**
     * Returns the location type
     * @return the location type
     */
    public String getLocationType() {
        return locationType;
    }

    /**
     * Returns the zip code
     * @return the zip code
     */
    public Integer getZip() {
        return zip;
    }

    /**
     * Returns the address
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns the city
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Returns the borough
     * @return the borough
     */
    public String getBorough() {
        return borough;
    }

    /**
     * Returns the minimum latitude
     * @return the minimum latitude
     */
    public Double getMinLatitude() {
        return minLatitude;
    }

    /**
     * Returns the maximum latitude
     * @return the maximum latitude
     */
    public Double getMaxLatitude() {
        return maxLatitude;
    }

    /**
     * Returns the minimum longitude
     * @return the minimum longitude
     */
    public Double getMinLongitude() {
        return minLongitude;
    }

    /**
     * Returns the maximum longitude
     * @return the maximum longitude
     */
    public Double getMaxLongitude() {
        return maxLongitude;
    }

    /**
     * Returns a List of all the enabled predicates
     * @return all enabled predicates
     */
    public List<Predicate<RatData>> getPredicates() {
        List<Predicate<RatData>> list = new ArrayList<>();
        for (String key: predicates.keySet()) {
            Boolean enabled = enabledMap.get(key);
            if ((enabled != null) && enabled) {
                list.add(predicates.get(key));
            }
        }
        return list;
    }

    /**
     * Returns a default instance of a RatFilter
     * @return a default RatFilter
     */
    public static RatFilter getDefaultInstance() {
        RatFilter filter = new RatFilter();
        filter.setBeginDate(DateUtility.getYesterday());
        filter.setEndDate(LocalDateTime.now());
        filter.setPredicateEnabled(DATE, true);
        return filter;
    }
}
