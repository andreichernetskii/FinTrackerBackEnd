package com.example.finmanagerbackend.financial_transaction;


import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) representing financial transactions, used for communication between layers.
 */

public class FinancialTransactionDTO {
    private FinancialTransactionType financialTransactionType;
    private BigDecimal amount;
    private String category;
    private String date;

    public FinancialTransactionDTO() {
    }

    public FinancialTransactionDTO( FinancialTransactionType financialTransactionType, BigDecimal amount, String category, String date ) {
        this.financialTransactionType = financialTransactionType;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    // Getters are required by the Jackson library for JSON serialization/deserialization:

    public FinancialTransactionType getOperationType() {
        return financialTransactionType;
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

    // Override of the toString method for better representation
    @Override
    public String toString() {
        return "FinancialTransactionDTO {" +
                "financialTransactionType=" + financialTransactionType +
                ", amount=" + amount +
                ", category='" + category + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
