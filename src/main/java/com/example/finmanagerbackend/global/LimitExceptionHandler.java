package com.example.finmanagerbackend.global;

import com.example.finmanagerbackend.limit.LimitException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class LimitExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler( LimitException.class )
    protected ResponseEntity<?> handleConflict( RuntimeException exception ) {
        String message = "Not allowed";
        return ResponseEntity.status( HttpStatus.FORBIDDEN ).body( message );
    }
}
