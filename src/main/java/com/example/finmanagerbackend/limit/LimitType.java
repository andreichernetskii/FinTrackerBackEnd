package com.example.finmanagerbackend.limit;

public enum LimitType {
    BUDGET( "Przekrociłeś budżet!" ),
    YEAR( "Przekrociłeś roczny limit!" ),
    MONTH( "Przekrociłeś miesięczny limit!" ),
    WEEK( "Przekrociłeś tygodniowy limit!" ),
    DAY( "Przekrociłeś dzienny limit!" );

    private String alert;

    private LimitType( String alert ) {
        this.alert = alert;
    }

    public String getAlert() {
        return alert;
    }
}
