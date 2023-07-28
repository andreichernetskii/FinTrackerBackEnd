package com.example.finmanagerbackend.income_expense;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class IncomeExpenseManager {
    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private OperationType operationType;
    private BigDecimal amount;
    private String category;
    private String date; // todo: konwersia do odpowiedniego formatu: Date albo LocalDate

    public IncomeExpenseManager() {
    }

    public IncomeExpenseManager(OperationType operationType, BigDecimal amount, String category, String date) {
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

    public String getDate() {
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
}
