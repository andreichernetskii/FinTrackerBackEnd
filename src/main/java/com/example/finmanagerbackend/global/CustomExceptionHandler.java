package com.example.finmanagerbackend.global;

import com.example.finmanagerbackend.dto.ApiResponse;
import com.example.finmanagerbackend.dto.ErrorDto;
import com.example.finmanagerbackend.global.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Global exception handler for handling unchecked application exceptions.
 */
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiResponse<?>> handleValidationConflict(MethodArgumentNotValidException exception) {

        ApiResponse<?> serviceResponse = new ApiResponse<>();
        List<ErrorDto> errors = new ArrayList<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> {
                    ErrorDto errorDTO = new ErrorDto(error.getField(), error.getDefaultMessage());
                    errors.add(errorDTO);
                });
        serviceResponse.setStatus("FAILED");
        serviceResponse.setErrors(errors);

        return new ResponseEntity<>(serviceResponse, HttpStatus.BAD_REQUEST);
    }

    // Handles ForbiddenException, returns a response with HTTP status FORBIDDEN and the exception message.
    @ExceptionHandler( ForbiddenException.class )
    protected ResponseEntity<?> handleForbiddenConflict( ForbiddenException exception ) {
        String message = exception.getMessage();
        return ResponseEntity.status( HttpStatus.FORBIDDEN ).body( message );
    }

    // Handles NotFoundException, returns a response with HTTP status NOT_FOUND and the exception message.
    @ExceptionHandler( NotFoundException.class )
    protected ResponseEntity<?> handleNotFoundConflict ( NotFoundException exception ) {
        String message = exception.getMessage();
        return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( message );
    }

    // Handles UnprocessableEntityException, returns a response with HTTP status UNPROCESSABLE_ENTITY and the exception message.
    @ExceptionHandler( UnprocessableEntityException.class )
    protected ResponseEntity<?> handleUnauthorizedConflict( UnprocessableEntityException exception ) {
        String message = exception.getMessage();
        return ResponseEntity.status( HttpStatus.UNPROCESSABLE_ENTITY ).body( message );
    }

    // Handles ExpiredTokenException, returns a response with HTTP status FORBIDDEN and the exception message.
    @ExceptionHandler( ExpiredTokenException.class )
    protected ResponseEntity<?> handleUnauthorizedConflict( ExpiredTokenException exception ) {
        String message = exception.getMessage();
        return ResponseEntity.status( HttpStatus.FORBIDDEN ).body( message );
    }

    // Handles AuthenticationException, returns a response with HTTP status UNAUTHORIZED and the exception message.
    @ExceptionHandler( AuthenticationException.class )
    protected ResponseEntity<?> handleUnauthorizedConflict( AuthenticationException exception ) {
        String message = exception.getMessage();
        return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( message );
    }

    // Handles NotExistException, returns a response with HTTP status NOT_FOUND and the exception message.
    @ExceptionHandler( NotExistException.class )
    protected ResponseEntity<?> handleNotExistException ( NotExistException exception ) {
        String message = exception.getMessage();
        return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( message );
    }
}
