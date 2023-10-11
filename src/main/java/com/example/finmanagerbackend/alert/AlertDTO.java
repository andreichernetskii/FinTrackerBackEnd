package com.example.finmanagerbackend.alert;

import com.example.finmanagerbackend.limit.Limit;

public class AlertDTO {
    private String message;
    private boolean positive;
    //todo client moglby potrzebować informacji ile wynosil alert i jaka jest faktyczna kwota np. gdyby chcial zaprezentowac alerty jako

    public AlertDTO() {    }

    public AlertDTO( String message, boolean positive ) {
        this.message = message;
        this.positive = positive;
    }

    public String getMessage() {
        return message;
    }

    public boolean isPositive() {
        return positive;
    }

    public void setMessage( String str ) { this.message = str; }
}
