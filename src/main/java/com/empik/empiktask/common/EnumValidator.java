package com.empik.empiktask.common;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

    private Class<? extends Enum<?>> enumClass;
    private boolean ignoreCase;

    @Override
    public void initialize(ValidEnum annotation) {
        this.enumClass = annotation.enumClass();
        this.ignoreCase = annotation.ignoreCase();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        for (Enum<?> e : enumClass.getEnumConstants()) {
            if (ignoreCase) {
                if (e.name().equalsIgnoreCase(value)) {
                    return true;
                }
            } else {
                if (e.name().equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }
}
