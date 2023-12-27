package com.example.finmanagerbackend.alert;

/**
 * Data Transfer Object (DTO) class containing alert information in text form.
 */
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
