package com.empik.empiktask.common.error;

import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalApiErrorHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
        HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("", ex);

        String error = ex
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining(", "));

        if (StringUtils.isBlank(error)) {
            error = ex
                .getBindingResult()
                .getGlobalErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        }

        return new ResponseEntity<>(
            ApiErrorResponse.from(error, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedError(Exception e) {
        log.error("Unknown application exception", e);
        return new ResponseEntity<>(
            ApiErrorResponse.unknown(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TaskAppException.class)
    public ResponseEntity<ApiErrorResponse> handleApplicationException(TaskAppException ex) {
        log.error("", ex);
        HttpStatus errorCode = HttpStatus.BAD_REQUEST;
        if (ex.getCode() != null) {
            errorCode = ex.getCode();
        }
        return new ResponseEntity<>(
            ApiErrorResponse.from(ex.getMessage(), errorCode.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ApiErrorResponse> handleObjectOptimisticLockingFailureException(
        ObjectOptimisticLockingFailureException exception) {
        log.error("", exception);
        return new ResponseEntity<>(ApiErrorResponse.from(
            "Failed to perform requested action, please try again later",
            HttpStatus.CONFLICT.value()),
            HttpStatus.CONFLICT);
    }
}
