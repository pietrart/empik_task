package com.empik.empiktask.geo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.empik.empiktask.common.CountryCode;
import com.empik.empiktask.common.IpAddress;
import com.empik.empiktask.geo.GeoApiClient.GeoDataBasedOnIp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class GeoApiClientTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private GeoApiConfig config;

    @InjectMocks
    private GeoApiClient client;

    private String geoApiUrl = "http://test-api";

    @BeforeEach
    void setUp() {
        when(config.getUrl()).thenReturn(geoApiUrl);
    }

    @Test
    void getCountryCodeFromIp_should_return_null() {
        //given
        String testIp = "127.0.0.1";
        when(restTemplate.getForObject(eq(geoApiUrl + String.format("/json/%s?fields=countryCode", testIp)), any()))
            .thenReturn(null);
        //when
        CountryCode result = client.getCountryCodeFromIp(IpAddress.from(testIp));
        //then
        assertNull(result);
    }

    @Test
    void getCountryCodeFromIp_should_code() {
        //given
        String testIp = "127.0.0.1";
        when(restTemplate.getForObject(eq(geoApiUrl + String.format("/json/%s?fields=countryCode", testIp)), any()))
            .thenReturn(new GeoDataBasedOnIp("PL"));
        //when
        CountryCode result = client.getCountryCodeFromIp(IpAddress.from(testIp));
        //then
        assertEquals(CountryCode.PL, result);
    }
}