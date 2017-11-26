package edu.gatech.cs2340.thericks.database;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import edu.gatech.cs2340.thericks.models.Privilege;
import edu.gatech.cs2340.thericks.models.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * Created by Daisha Braxton on 11/12/2017.
 */
public class UserDataTest {

    private UserDatabase udb;

    @Before
    public void setUp() {
        udb = mock(UserDatabase.class);
    }

    @Test
    public void testPreConditions() {
        assertNotNull(udb);
    }

    @Test
    public void testCreateUser() {
        udb.createUser("dbrax6", "passWord17", Privilege.ADMIN);
        List<User> users = udb.getAllUsers();
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("dbrax6", users.get(0).getUsername());
    }

    @Test
    public void deleteUser() {
        List<User> users = udb.getAllUsers();
        for (User user : users) {
            udb.deleteUser(String.valueOf(user));
        }
        users = udb.getAllUsers();
        assertEquals(0, users.size());
    }

    @Test
    public void testAddAndDelete() {
        // delete all
        List<User> users = udb.getAllUsers();
        for (User user : users) {
            udb.deleteUser(String.valueOf(user));
        }
        // add 3 RatData Objects
        udb.createUser("user411", "123iLuvdogZ", Privilege.NORMAL);
        udb.createUser("dbraxton3", "gatech4L", Privilege.ADMIN);
        udb.createUser("ratDude1", "iTrackRats4000", Privilege.NORMAL);

        users = udb.getAllUsers();
        assertEquals(3, users.size());

        //delete user 1 and 2
        udb.deleteUser(String.valueOf(users.get(0)));
        udb.deleteUser(String.valueOf(users.get(1)));

        users = udb.getAllUsers();
        assertEquals(1, users.size());
    }

}