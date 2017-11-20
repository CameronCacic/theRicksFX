package edu.gatech.cs2340.thericks;

import org.junit.Test;
import static org.junit.Assert.*;

import edu.gatech.cs2340.thericks.utils.Security;

/**
 * Unit tests for validating passwords.
 *
 * Criteria for valid password (must meet all):
 *  - at least one digit
 *  - at least one lowercase letter
 *  - at least one uppercase letter
 *  - no invalid chars; i.e. not alphanumeric or a member of SPECIAL_CHARACTERS !@#$%&?_
 *  - no whitespace
 *  - length between min (8) and max (32), inclusive
 *
 * Created by Ben Lashley on 11/3/2017.
 */

public class PasswordValidationTest {

    // Helper method to shorten test method calls
    private boolean test(String pw) {
        return Security.validatePassword(pw);
    }

    @Test
    public void testNullPassword() {
        assertFalse("Null password is considered valid, should be invalid.", test(null));
    }

    @Test
    public void testTooShortPassword() {
        String pw1 = "";
        String pw2 = "aA1";
        String pw3 = "aaaAAA1"; // size = 7

        String failMessage = "Too short password is considered valid, should be invalid";
        assertFalse(failMessage, test(pw1));
        assertFalse(failMessage, test(pw2));
        assertFalse(failMessage, test(pw3));
    }

    @Test
    public void testTooLongPassword() {
        String pw1 = "aA1111111111111111111111111111111"; // size = 33
        String pw2 = pw1 + pw1; // size = 66

        String failMessage = "Too long password is considered valid, should be invalid";
        assertFalse(failMessage, test(pw1));
        assertFalse(failMessage, test(pw2));
    }

    @Test
    public void testMaxAllowedLength() {
        String pw1 = "aA111111111111111111111111111111"; // size = 32
        assertTrue("Valid password of max length is considered invalid", test(pw1));
    }

    @Test
    public void testMinAllowedLength() {
        String pw1 = "aA111111"; // size = 8
        assertTrue("Valid password of min length is considered invalid", test(pw1));
    }

    @Test
    public void testPasswordHasWhitespace() {
        String pw1 = "hiTh3re World";
        String pw2 = "h3resATab\t";
        String pw3 = "AndAN3wLine\n";
        String pw4 = "\n\t l0tsOfWhitespace";

        String failMessage = "A password containing whitespace is considered valid, should be invalid";
        assertFalse(failMessage, test(pw1));
        assertFalse(failMessage, test(pw2));
        assertFalse(failMessage, test(pw3));
        assertFalse(failMessage, test(pw4));
    }

    @Test
    public void testNoUppercase() {
        String pw1 = "alllower3";
        String pw2 = "its2low4me";
        String pw3 = "aaaaaaaaaah2";

        String failMessage = "A password containing no uppercase letter is considered valid; should be invalid";
        assertFalse(failMessage, test(pw1));
        assertFalse(failMessage, test(pw2));
        assertFalse(failMessage, test(pw3));
    }

    @Test
    public void testNoLowercase() {
        String pw1 = "ALLUPPER3";
        String pw2 = "ITSLOOKINGUP4ME";
        String pw3 = "AAAAAAAAAAAAAAH2";

        String failMessage = "A password containing no lowercase letter is considered valid; should be invalid";
        assertFalse(failMessage, test(pw1));
        assertFalse(failMessage, test(pw2));
        assertFalse(failMessage, test(pw3));
    }

    @Test
    public void testNoDigit() {
        String pw1 = "IHateNumbers";
        String pw2 = "QwertyAsdf";
        String pw3 = "MyNumPadIsBroken";

        String failMessage = "A password containing no digit is considered valid; should be invalid";
        assertFalse(failMessage, test(pw1));
        assertFalse(failMessage, test(pw2));
        assertFalse(failMessage, test(pw3));
    }

    @Test
    public void testValidChars() {
        String pw1 = "myP@ssw0rd";
        String pw2 = "#Password1";
        String pw3 = "SIEFJOeijofaijf489w5&&";
        String pw4 = "DJFoie8439wEJO$jifoa$IJF";
        String pw5 = "350jtgijfIEjf%jsid";
        String pw6 = "i4hjg9?o4jiDjg";
        String pw7 = "afji_eDF349";
        String pw8 = "!fia8Dgaqzio";
        String pw9 = "1Aa#?$&&%_!@";

        String failMessage = "A password with valid special characters is considered invalid; should be valid";
        assertTrue(failMessage, test(pw1));
        assertTrue(failMessage, test(pw2));
        assertTrue(failMessage, test(pw3));
        assertTrue(failMessage, test(pw4));
        assertTrue(failMessage, test(pw5));
        assertTrue(failMessage, test(pw6));
        assertTrue(failMessage, test(pw7));
        assertTrue(failMessage, test(pw8));
        assertTrue(failMessage, test(pw9));
    }

    @Test
    public void testInvalidChars() {
        String pw1 = "EFijfa3d++";    // + is invalid
        String pw2 = "Fzeta*efj3t4";  // * is invalid
        String pw3 = "E#Fazjfiz^dj}"; // ^ is invalid
        String pw4 = "iejFEj#$?(";    // ( is invalid

        String failMessage = "A password containing invalid character(s) is considered valid; should be invalid";
        assertFalse(failMessage, test(pw1));
        assertFalse(failMessage, test(pw2));
        assertFalse(failMessage, test(pw3));
        assertFalse(failMessage, test(pw4));
    }

    @Test
    public void testValidPassword() {
        String pw1 = "V@lidPassw0rd!";
        String pw2 = "DJfirej38502&%@!$8";
        String pw3 = "F84jiajia";
        String pw4 = "Aa1!!!!!!!!!!!!!!!!";

        String failMessage = "A password meeting all criteria is considered invalid; should be valid";
        assertTrue(failMessage, test(pw1));
        assertTrue(failMessage, test(pw2));
        assertTrue(failMessage, test(pw3));
        assertTrue(failMessage, test(pw4));
    }

    @Test
    public void testMultipleProblems() {
        String pw1 = "S0 Wr^ng";
        String pw2 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa&1A";
        String pw3 = "\nLol";
        String pw4 = "^Uea^";
        String pw5 = "all_lowercase";
        String pw6 = "ALL_UPPERCASE";
        String pw7 = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA1";
        String pw8 = "all lower1";
        String pw9 = "all upper1";
        String pw10 = "\t";

        String failMessage = "A password containing multiple problems is considered valid; should be invalid";
        assertFalse(failMessage, test(pw1));
        assertFalse(failMessage, test(pw2));
        assertFalse(failMessage, test(pw3));
        assertFalse(failMessage, test(pw4));
        assertFalse(failMessage, test(pw5));
        assertFalse(failMessage, test(pw6));
        assertFalse(failMessage, test(pw7));
        assertFalse(failMessage, test(pw8));
        assertFalse(failMessage, test(pw9));
    }
}
