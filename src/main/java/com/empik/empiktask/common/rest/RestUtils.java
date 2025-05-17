package com.empik.empiktask.common.rest;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class RestUtils {

    private RestUtils() {
    }

    private static final List<String> possibleHeadersWithIp = List.of(
        "X-Forwarded-For",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP"
    );

    public static String extractIpFromRequest(HttpServletRequest request) {
        for (String header : possibleHeadersWithIp) {
            String ip = request.getHeader(header);
            if (StringUtils.isNotBlank(ip)) {
                if (ip.contains(",")) {
                    ip = ip.split(",")[0];
                }
                return ip.trim();
            }
        }

        return request.getRemoteAddr();
    }
}
