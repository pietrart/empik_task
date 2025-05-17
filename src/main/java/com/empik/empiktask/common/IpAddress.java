package com.empik.empiktask.common;

import com.empik.empiktask.common.error.TaskAppException;
import org.apache.commons.lang3.StringUtils;

public record IpAddress(String address) {

    private static final String IPV4_REGEX = "^((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.|$)){4}$";

    public static IpAddress from(String address) {
        if (StringUtils.isBlank(address) || !isValidIPv4(address)) {
            throw new TaskAppException("Invalid IP address: " + address);
        }
        return new IpAddress(address);
    }

    private static boolean isValidIPv4(String ip) {
        return ip != null && ip.matches(IPV4_REGEX);
    }
}
