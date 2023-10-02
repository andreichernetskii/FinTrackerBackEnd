package com.example.finmanagerbackend.limit;

import java.math.BigDecimal;

public class LimitDTO {
    private LimitType limitType;
    private BigDecimal limitAmount;

    public LimitDTO() {    }

    public LimitDTO( LimitType limitType, BigDecimal limitAmount ) {
        this.limitType = limitType;
        this.limitAmount = limitAmount;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public LimitType getLimitType() {
        return limitType;
    }
}
