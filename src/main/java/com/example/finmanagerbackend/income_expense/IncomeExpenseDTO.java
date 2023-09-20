package com.example.finmanagerbackend.income_expense;


import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    // getter'y są wymagane przez bibliotekę Jackson:

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
        return "IncomeExpenseDTO {" +
                "operationType=" + operationType +
                ", amount=" + amount +
                ", category='" + category + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
