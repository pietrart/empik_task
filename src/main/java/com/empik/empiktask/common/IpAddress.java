package com.empik.empiktask.common;

public record IpAddress(String address) {

    public static IpAddress from(String address) {
        return new IpAddress(address);
    }
}
