package com.empik.empiktask.common.validators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CountryCodeValidatorTest {

    private CountryCodeValidator validator;

    @BeforeEach
    void setUp() {
        validator = new CountryCodeValidator();
    }

    @Test
    void isValid_should_return_true() {
        //when/then
        assertTrue(validator.isValid("US", null));
        assertTrue(validator.isValid("CA", null));
        assertTrue(validator.isValid("us", null));
        assertTrue(validator.isValid("gb", null));
    }

    @Test
    void isValid_should_return_false() {
        assertFalse(validator.isValid("XYZ", null));
        assertFalse(validator.isValid("usa", null));
        assertFalse(validator.isValid("123", null));
        assertFalse(validator.isValid(null, null));
        assertFalse(validator.isValid("  ", null));
    }
}