package com.empik.empiktask.common.rest;

import com.empik.empiktask.common.error.TaskAppException;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;


@Configuration
@RequiredArgsConstructor
@Slf4j
class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
            .readTimeout(Duration.of(10, ChronoUnit.SECONDS))
            .connectTimeout(Duration.of(10, ChronoUnit.SECONDS))
            .errorHandler(new CustomResponseErrorHandler())
            .build();
    }


    static class CustomResponseErrorHandler implements ResponseErrorHandler {

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return !response.getStatusCode().is2xxSuccessful();
        }

        @Override
        public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
            String body = StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
            log.error("Rest client error, method: {}, url: {}, status: {}, body: {}", method, url, response.getStatusCode().value(), body);
            if (response.getStatusCode().is4xxClientError()) {
                throw new TaskAppException(HttpStatus.BAD_REQUEST, "Client error");
            }
            throw new TaskAppException(HttpStatus.INTERNAL_SERVER_ERROR, "Client error");
        }
    }
}
