package edu.gatech.cs2340.thericks;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


import edu.gatech.cs2340.thericks.database.RatDatabase;
import edu.gatech.cs2340.thericks.models.RatData;
import edu.gatech.cs2340.thericks.models.RatFilter;

//*
// * Created by Nylah Julmice on 11/3/17.



public class RatDataTest {

    private RatDatabase db;

    @Before
    public void setUp() {
        db = mock(RatDatabase.class);
    }

    @Test
    public void testPreConditions() {
        assertNotNull(db);
    }

    @Test
    public void testCreateRatData() {
        db.createRatData(31464020, "date and time", "location type", 10306, "address",
                "Atlanta", "borough", 40.892847, 34.847104);
        List<RatData> rats = db.getAllRatData();
        assertEquals(1, rats.size());
        assertEquals(31464020, rats.get(0).getKey());
    }

    @Test
    public void testDeleteAll() {
        List<RatData> rats = db.getAllRatData();
        for (RatData rat : rats) {
            db.deleteRatData(rat);
        }
        rats = db.getAllRatData();
        assertEquals(0, rats.size());
    }

    @Test
    public void testDeleteOneRatData() {
        List<RatData> rats = db.getAllRatData();
        int initialSize = rats.size();
        db.deleteRatData(rats.get(0));
        rats = db.getAllRatData();
        assertEquals(initialSize - 1, rats.size());
    }

    @Test
    public void testAddAndDelete() {
        // delete all
        List<RatData> rats = db.getAllRatData();
        for (RatData rat : rats) {
            db.deleteRatData(rat);
        }
        // add 3 RatData Objects
        db.createRatData(31464020, "date and time", "location type", 10306, "address",
                "Atlanta", "borough", 40.892847, 34.847104);
        db.createRatData(31464021, "date and time", "location type", 10306, "address",
                "Jamaica", "borough", 40.892847, 34.847104);
        db.createRatData(31464023, "date and time", "location type", 10306, "address",
                "Atlanta", "borough", 40.892847, 34.847104);
        rats = db.getAllRatData();
        assertEquals(3, rats.size());
        db.deleteRatData(rats.get(0));
        db.deleteRatData(rats.get(1));
        rats = db.getAllRatData();
        assertEquals(1, rats.size());
    }

    @Test
    public void testNullDataInLoadData() {
        ArrayAdapter a = mock(ArrayAdapter.class);
        ProgressBar p = mock(ProgressBar.class);
        List<RatData> rats = null;

        List<Predicate<RatData>> filters = new ArrayList<>();
        Predicate<RatData> inAtlanta = ratData -> ratData.getCity().equalsIgnoreCase("Atlanta");
        Predicate<RatData> commercialLocation = ratData -> ratData.getLocationType().equalsIgnoreCase("Commercial Building");
        if (filters.isEmpty()) {
            filters.add(inAtlanta);
            filters.add(commercialLocation);

            // ===== Loads in null data rat List ===== \\
            db.loadData(a, null, new RatFilter(filters));
            rats = db.getAllRatData();
            assertEquals(0, rats.size());
        }

    }
}
