package com.example.finmanagerbackend.global.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( HttpStatus.FORBIDDEN )
public class ExpiredTokenException extends RuntimeException {
    public ExpiredTokenException( String message ) {
        super( message );
    }
}
