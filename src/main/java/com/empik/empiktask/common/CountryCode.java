package com.empik.empiktask.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CountryCode {
    US("United States"),
    PL("Poland"),
    DE("Germany"),
    FR("France"),
    GB("United Kingdom"),
    CA("Canada"),
    IT("Italy"),
    ES("Spain"),
    CN("China"),
    JP("Japan"),
    IN("India"),
    BR("Brazil"),
    AU("Australia");

    private final String countryName;
}
