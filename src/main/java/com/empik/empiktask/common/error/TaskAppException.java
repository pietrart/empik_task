package com.empik.empiktask.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TaskAppException extends RuntimeException{
    private final String message;
    private final HttpStatus code;

    public TaskAppException(String message) {
        super(message);
        this.message = message;
        this.code = null;
    }

    public TaskAppException(HttpStatus code, String message) {
        super(message);
        this.message = message;
        this.code = code;
    }
}
