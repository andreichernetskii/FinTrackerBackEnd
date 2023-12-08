package com.example.finmanagerbackend.account;

import com.example.finmanagerbackend.application_user.ApplicationUser;
import com.example.finmanagerbackend.income_expense.IncomeExpense;
import com.example.finmanagerbackend.limit.Limit;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
    @OneToMany( mappedBy = "account" )  // tą relację bedzie mapować polę o nazwie account w IncomeExpence
                                        // oby uniknąc tworzenia nowej tabeli przez Hibernate
    @Cascade( CascadeType.ALL )
    private List<IncomeExpense> operations = new ArrayList<>();
    @OneToMany( mappedBy = "account" ) // todo: sprawdzić, czy skuma, że to już inny object
    private List<Limit> limits;

    public void addIncome( IncomeExpense incomeExpense ) {
        operations.add( incomeExpense );
        incomeExpense.setAccount( this );
    }

    public Long getId() {
        return id;
    }
}
