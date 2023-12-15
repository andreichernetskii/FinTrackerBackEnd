package com.example.finmanagerbackend.account;

import com.example.finmanagerbackend.application_user.ApplicationUser;
import com.example.finmanagerbackend.income_expense.IncomeExpense;
import com.example.finmanagerbackend.limit.Limit;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.ArrayList;
import java.util.List;

// this is all financial tracking instruments
// user + financials + limits, etc.

// todo: muszę zrozumieć, jak buduje się account w BD
@Entity
public class Account {
    @Id
    @GeneratedValue
    private Long id;

    // todo: relacje hibernatowe poczytać - Typ'y LAZY i EAGER + wzorzec projektowy Proxy + cykl życia Hibernate
    @OneToMany( mappedBy = "account", fetch = FetchType.LAZY )
    // tą relację bedzie mapować polę o nazwie account w IncomeExpence
    // oby uniknąc tworzenia nowej tabeli przez Hibernate
    @Cascade( CascadeType.ALL )
    private List<IncomeExpense> operations = new ArrayList<>();

    @OneToMany( mappedBy = "account", fetch = FetchType.LAZY ) // todo: sprawdzić, czy skuma, że to już inny object
    private List<Limit> limits = new ArrayList<>();

    public void addIncome( IncomeExpense incomeExpense ) {
        operations.add( incomeExpense );
        incomeExpense.setAccount( this );
    }

    public Long getId() {
        return id;
    }
}
