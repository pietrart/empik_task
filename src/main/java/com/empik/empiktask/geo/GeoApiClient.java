package com.empik.empiktask.geo;

import com.empik.empiktask.common.CountryCode;
import com.empik.empiktask.common.IpAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
class GeoApiClient {

    private final RestTemplate restTemplate;
    private final GeoApiConfig config;
    private static final String mapping = "/json/%s?fields=countryCode";

    public CountryCode getCountryCodeFromIp(IpAddress ipAddress) {
        log.debug("Fetch geo data for ip: {}", ipAddress);
        StringBuilder url = new StringBuilder(config.getUrl());
        url.append(String.format(mapping, ipAddress.address()));
        log.info("Fetch geo data url: {}", url);
        GeoDataBasedOnIp response = restTemplate.getForObject(url.toString(), GeoDataBasedOnIp.class);
        CountryCode result = null;
        try {
            result = CountryCode.valueOf(response.countryCode.toUpperCase());
        } catch (Exception e) {
            log.error("Failed to parse country code. Response {} Exception: ", response, e);
        }
        return result;
    }

    record GeoDataBasedOnIp(String countryCode) {

    }
}
