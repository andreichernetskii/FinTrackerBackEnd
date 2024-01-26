package com.example.finmanagerbackend.limit;

public enum LimitType {
    ZERO( "You are in the negative overall!" ),
    BUDGET( "You exceeded the budget!" ),
    YEAR( "You exceeded the year limit!" ),
    MONTH( "You exceeded the monthly limit!" ),
    WEEK( "You exceeded the weekly limit!" ),
    DAY( "You exceeded the daily limit!" );

    private String alert;

    private LimitType( String alert ) {
        this.alert = alert;
    }

    public String getAlert() {
        return alert;
    }
}
