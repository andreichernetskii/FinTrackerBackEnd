package com.example.finmanagerbackend.income_expense;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class IncomeExpense {
    @Id
    @GeneratedValue
    private Long id;
    @Enumerated( EnumType.STRING )
    private OperationType operationType;
    private BigDecimal amount;
    private String category;
    private LocalDate date; // todo: konwersia do odpowiedniego formatu: Date albo LocalDate

    public IncomeExpense() {
    }

    public IncomeExpense( OperationType operationType, BigDecimal amount, String category, LocalDate date ) {
        this.operationType = operationType;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public OperationType getOperationType() {
        return operationType;
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

    @Override
    public String toString() {
        return "IncomeExpenseManager {" +
                "id=" + id +
                ", operationType=" + operationType +
                ", amount=" + amount +
                ", category='" + category + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public void setAmount( BigDecimal amount ) {
        BigDecimal tempAmount = amount.abs();
        this.amount = ( operationType == OperationType.EXPENSE ) ? tempAmount.negate() : tempAmount;
    }

    public void setCategory( String category ) {
        this.category = category;
    }

    public void setDate( LocalDate date ) {
        this.date = date;
    }

    public void setOperationType( OperationType operationType ) {
        this.operationType = operationType;
    }
}
