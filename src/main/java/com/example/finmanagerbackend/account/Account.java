package com.example.finmanagerbackend.account;

import com.example.finmanagerbackend.financial_transaction.FinancialTransaction;
import com.example.finmanagerbackend.limit.Limit;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing all financial tracking instruments, including user, financials, limits, etc.
 */
@Entity
public class Account {
    @Id
    @GeneratedValue
    private Long id;

    // Relationship mapping: One Account has many IncomeExpense records (Lazy loading for efficiency).
    // This avoids creating a new table for the relationship.
    @OneToMany( mappedBy = "account")
    // Cascade option is used to apply the same operation (e.g., save, update, delete) to the associated entities.
    @Cascade( CascadeType.ALL )
    private List<FinancialTransaction> operations = new ArrayList<>();

    // Relationship mapping: One Account has many Limit records (Lazy loading for efficiency).
    @OneToMany( mappedBy = "account" )
    private List<Limit> limits = new ArrayList<>();
    private boolean isDemo;

    public Long getId() {
        return id;
    }

    public boolean isDemo() {
        return isDemo;
    }

    public void setDemo( boolean demo ) {
        isDemo = demo;
    }
}
