package com.empik.empiktask.common.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApiErrorResponse implements Serializable {

    private int status;
    private String message;
    @JsonProperty("timestamp")
    private LocalDateTime timeStamp;

    public static ApiErrorResponse from(String error, int status) {
        return ApiErrorResponse.builder()
            .status(status)
            .message(error)
            .timeStamp(LocalDateTime.now())
            .build();
    }

    public static ApiErrorResponse unknown() {
        return ApiErrorResponse.builder()
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .message("Server error")
            .timeStamp(LocalDateTime.now())
            .build();
    }
}
