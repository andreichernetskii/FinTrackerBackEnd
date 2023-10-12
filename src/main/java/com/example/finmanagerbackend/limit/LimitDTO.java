package com.example.finmanagerbackend.limit;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LimitDTO {
    private LimitType limitType;
    private BigDecimal limitAmount;
    private String category;
    private LocalDate creationDate;

    public LimitDTO() {    }

    public LimitDTO( LimitType limitType, BigDecimal limitAmount, String category, LocalDate creationDate ) {
        this.limitType = limitType;
        this.limitAmount = limitAmount;
        this.category = category;
        this.creationDate = creationDate;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public LimitType getLimitType() {
        return limitType;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }
}
