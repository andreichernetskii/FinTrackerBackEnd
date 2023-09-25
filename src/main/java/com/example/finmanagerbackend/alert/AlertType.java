package com.example.finmanagerbackend.alert;

//todo: precezyjniej opisać komunikaty
public enum AlertType {
    NEGATIVE_BALANCE( "Jesteś na minusie!" ),
    MORE_THEN_PREV_MONTH( "Wydałeś więcej, niż w poprzednim miesięcu" ),
    BUDGET_LIMIT_EXCEEDING( "Przekrociłeś budżet!" ),
    YEAR_LIMIT_EXCEEDING( "Przekrociłeś roczny limit!" ),
    MONTH_LIMIT_EXCEEDING( "Przekrociłeś miesięczny limit!" ),
    WEEK_LIMIT_EXCEEDING( "Przekrociłeś tygodniowy limit!" ),
    DAY_LIMIT_EXCEEDING( "Przekrociłeś dzienny limit!" );

    public String label;

    private AlertType( String label ) {
        this.label = label;
    }
}
