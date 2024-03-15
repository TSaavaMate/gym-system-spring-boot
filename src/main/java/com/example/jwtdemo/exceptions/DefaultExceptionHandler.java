package com.example.jwtdemo.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class DefaultExceptionHandler {

    private static final Map<Class<? extends Exception>, HttpStatus> EXCEPTION_STATUS_MAP = new HashMap<>();

    static {
        EXCEPTION_STATUS_MAP.put(ResourceNotFoundException.class, HttpStatus.NOT_FOUND);
        EXCEPTION_STATUS_MAP.put(InvalidCredentialException.class, HttpStatus.UNAUTHORIZED);
        EXCEPTION_STATUS_MAP.put(ExpiredJwtException.class, HttpStatus.UNAUTHORIZED);
        EXCEPTION_STATUS_MAP.put(InvalidUpdateRequestException.class, HttpStatus.BAD_REQUEST);
        EXCEPTION_STATUS_MAP.put(InvalidTrainingCreationRequest.class, HttpStatus.BAD_REQUEST);
        EXCEPTION_STATUS_MAP.put(SQLException.class, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({ ResourceNotFoundException.class,
                        InvalidCredentialException.class,
                        InvalidUpdateRequestException.class,
                        SQLException.class,
                        InvalidTrainingCreationRequest.class,
                        ExpiredJwtException.class
    })
    public ResponseEntity<ApiError> handleException(Exception ex, HttpServletRequest request) {
        var status = EXCEPTION_STATUS_MAP.getOrDefault(ex.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                ex.getMessage(),
                status.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, status);
    }
}
