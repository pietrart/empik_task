package com.empik.empiktask.geo;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ToString
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "geo-api")
@Getter
@Setter
class GeoApiConfig {

    private String url;

    @PostConstruct
    void postConstruct() {
        log.info("Geo api configuration: {}", this);
    }
}
