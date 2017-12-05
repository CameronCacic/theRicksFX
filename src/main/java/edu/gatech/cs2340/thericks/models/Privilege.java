package edu.gatech.cs2340.thericks.models;

/**
 * Created by Cameron on 9/29/2017.
 * Enumerated class for different levels of user privilege
 */
public enum Privilege {
    NORMAL("Normal"),
    ADMIN("Admin");

    private final String label;

    /**
     * Creates a privilege with the specified label
     * @param label the label
     */
    Privilege(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
