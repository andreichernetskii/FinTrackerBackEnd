package com.example.finmanagerbackend.global;

import com.example.finmanagerbackend.global.exceptions.ForbiddenException;
import com.example.finmanagerbackend.global.exceptions.NotFoundException;
import com.example.finmanagerbackend.global.exceptions.UnprocessableEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class LimitExceptionHandler extends ResponseEntityExceptionHandler {
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
}
