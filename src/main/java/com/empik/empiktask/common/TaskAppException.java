package com.empik.empiktask.common;

public class TaskAppException extends RuntimeException{
    private final String message;

    public TaskAppException(String message) {
        super(message);
        this.message = message;
    }
}
