package com.example.finmanagerbackend.limit;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( HttpStatus.FORBIDDEN)  // przyklado
public class LimitException extends RuntimeException {
    public LimitException( String message ) {
        super( message );
    }
}
