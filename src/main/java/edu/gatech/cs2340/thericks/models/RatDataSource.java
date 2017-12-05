package edu.gatech.cs2340.thericks.models;

import java.util.List;

/**
 * Interface for a source of rat data with basic operations for managing a collection of rat data.
 *
 * Created by Ben Lashley on 10/18/2017.
 */

public interface RatDataSource {
    /**
     * Creates a rat entry
     * @param key unique rat data key
     * @param createdDateTime date and time of creation
     * @param locationType location
     * @param incidentZip zip code
     * @param incidentAddress address
     * @param city city
     * @param borough borough
     * @param latitude latitude
     * @param longitude longitude
     */
    void createRatData(int key, String createdDateTime, String locationType,
                       int incidentZip, String incidentAddress, String city,
                       String borough, double latitude, double longitude);

    /**
     * Deletes a ratData object
     * @param data the rat to delete
     */
    void deleteRatData(RatData data);

    /**
     * Getter for ratData List
     * @return list of all ratData objects
     */
    List<RatData> getAllRatData();

    /**
     * Gets a ratData Object by its unique key
     * @param key the ratData Object's key
     * @return the ratData Object
     */
    RatData findRatDataByKey(int key);
}
