package com.example.finmanagerbackend.global.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( HttpStatus.NOT_FOUND)
public class NotExistException extends RuntimeException {
    public NotExistException( String message ) {
        super( message );
    }
}
