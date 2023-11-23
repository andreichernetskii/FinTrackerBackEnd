package com.example.finmanagerbackend.global;

import com.example.finmanagerbackend.global.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// main class what will handle with unchecked app exceptions
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    // for some forbidden actions with data
    @ExceptionHandler( ForbiddenException.class )
    protected ResponseEntity<?> handleForbiddenConflict( ForbiddenException exception ) {
        String message = exception.getMessage();
        return ResponseEntity.status( HttpStatus.FORBIDDEN ).body( message );
    }

    @ExceptionHandler( NotFoundException.class )
    protected ResponseEntity<?> handleNotFoundConflict ( NotFoundException exception ) {
        String message = exception.getMessage();
        return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( message );
    }

    @ExceptionHandler( UnprocessableEntityException.class )
    protected ResponseEntity<?> handleAlreadyExistConflict ( UnprocessableEntityException exception ) {
        String message = exception.getMessage();
        return ResponseEntity.status( HttpStatus.UNPROCESSABLE_ENTITY ).body( message );
    }

    @ExceptionHandler( ExpiredTokenException.class )
    protected ResponseEntity<?> handleAlreadyExistConflict ( ExpiredTokenException exception ) {
        String message = exception.getMessage();
        return ResponseEntity.status( HttpStatus.FORBIDDEN ).body( message );
    }

    @ExceptionHandler( AuthenticationException.class )
    protected ResponseEntity<?> handleAlreadyExistConflict ( AuthenticationException exception ) {
        String message = exception.getMessage();
        return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( message );
    }
}
