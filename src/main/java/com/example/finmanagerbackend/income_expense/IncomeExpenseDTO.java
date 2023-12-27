package com.example.finmanagerbackend.income_expense;


import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) representing financial transactions, used for communication between layers.
 */

public class IncomeExpenseDTO {
    private OperationType operationType;
    private BigDecimal amount;
        private String category;
    private String date;

    public IncomeExpenseDTO() {
    }

    public IncomeExpenseDTO( OperationType operationType, BigDecimal amount, String category, String date ) {
        this.operationType = operationType;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    // Getters are required by the Jackson library for JSON serialization/deserialization:

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

    // Override of the toString method for better representation
    @Override
    public String toString() {
        return "IncomeExpenseDTO {" +
                "operationType=" + operationType +
                ", amount=" + amount +
                ", category='" + category + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
