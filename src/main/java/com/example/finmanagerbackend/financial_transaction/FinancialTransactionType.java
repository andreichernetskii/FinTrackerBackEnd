package com.example.finmanagerbackend.financial_transaction;

public enum FinancialTransactionType {
    INCOME("INCOME"),
    EXPENSE("EXPENSE");

    private final String type;

    FinancialTransactionType(String type) {
        this.type = type;
    }

    public static FinancialTransactionType fromString(String str) {
        for(FinancialTransactionType finType: FinancialTransactionType.values()) {
            if (finType.type.equals(str)) return finType;
        }

        return null;
    }
}
