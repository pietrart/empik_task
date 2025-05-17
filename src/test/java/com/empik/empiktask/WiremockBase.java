package com.empik.empiktask;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

@EnableWireMock({
    @ConfigureWireMock(
        name = "test-wiremock",
        port = 9999)
})
public class WiremockBase extends BaseTestIT {

    @Autowired
    protected MockMvc mvc;

    public static void geoApiMockReturnCountryCodePL(String ip) {
        stubFor(get(String.format("/json/%s?fields=countryCode", ip))
            .willReturn(aResponse()
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("""
                    {
                        "countryCode": "PL"
                    }
                    """)));
    }
}
