package com.example.finmanagerbackend.financial_transaction;

import com.example.finmanagerbackend.account.Account;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity representing financial transactions, including both incomes and expenses.
 */
@Entity
public class FinancialTransaction {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn( name = "account_id" ) // connection with account entity
    private Account account;
    @Enumerated( EnumType.STRING )
    private FinancialTransactionType financialTransactionType;
    private BigDecimal amount;
    private String category;
    private LocalDate date;

    // constructors

    public FinancialTransaction() {
    }

    public FinancialTransaction( FinancialTransactionType financialTransactionType, BigDecimal amount, String category, LocalDate date ) {
        this.financialTransactionType = financialTransactionType;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    // getters

    public Long getId() {
        return id;
    }

    public FinancialTransactionType getFinancialTransactionType() {
        return financialTransactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }

    // toString method for better representation
    @Override
    public String toString() {
        return "FinancialTransactionManager {" +
                "id=" + id +
                ", financialTransactionType=" + financialTransactionType +
                ", amount=" + amount +
                ", category='" + category + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    // setters

    public void setAmount( BigDecimal amount ) {
        BigDecimal tempAmount = amount.abs();
        this.amount = ( financialTransactionType == FinancialTransactionType.EXPENSE ) ? tempAmount.negate() : tempAmount;
    }

    public void setCategory( String category ) {
        this.category = category;
    }

    public void setDate( LocalDate date ) {
        this.date = date;
    }

    public void setFinancialTransactionType( FinancialTransactionType financialTransactionType ) {
        this.financialTransactionType = financialTransactionType;
    }

    public void setAccount( Account account ) {
        this.account = account;
    }
}
