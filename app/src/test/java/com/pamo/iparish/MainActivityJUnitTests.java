package com.pamo.iparish;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MainActivityJUnitTests {

    @Test
    public void isValidEmail_correctEmailSimple_ReturnsTrue() {
        assertTrue(MainActivity.isValidEmail("user@test.com"));
    }
    @Test
    public void emailValidator_CorrectEmailSubDomain_ReturnsTrue() {
        assertTrue(MainActivity.isValidEmail("user@test.com.pl"));
    }
    @Test
    public void emailValidator_InvalidEmailNoTld_ReturnsFalse() {
        assertFalse(MainActivity.isValidEmail("user@test"));
    }
    @Test
    public void emailValidator_InvalidEmailDoubleDot_ReturnsFalse() {
        assertFalse(MainActivity.isValidEmail("user@test..com"));
    }
    @Test
    public void emailValidator_InvalidEmailNoUsername_ReturnsFalse() {
        assertFalse(MainActivity.isValidEmail("@email.com"));
    }
    @Test
    public void emailValidator_EmptyString_ReturnsFalse() {
        assertFalse(MainActivity.isValidEmail(""));
    }
    @Test
    public void emailValidator_NullEmail_ReturnsFalse() {
        assertFalse(MainActivity.isValidEmail(null));
    }
}