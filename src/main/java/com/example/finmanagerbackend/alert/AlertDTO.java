package com.example.finmanagerbackend.alert;

import com.example.finmanagerbackend.limit.Limit;

// contains alert info inside the text
public class AlertDTO {
    private String message;

    public AlertDTO() {    }

    public AlertDTO( String message ) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage( String str ) { this.message = str; }
}
