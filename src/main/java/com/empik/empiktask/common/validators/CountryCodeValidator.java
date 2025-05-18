package com.empik.empiktask.common.validators;

import com.empik.empiktask.common.CountryCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;

class CountryCodeValidator implements ConstraintValidator<CountryCodeValid, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) return false;
        return Arrays.stream(CountryCode.values())
            .anyMatch(code -> code.name().equalsIgnoreCase(value));
    }
}