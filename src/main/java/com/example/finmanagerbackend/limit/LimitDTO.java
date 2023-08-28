package com.example.finmanagerbackend.limit;

import java.math.BigDecimal;

public class LimitDTO {
    private BigDecimal limitAmount;
    private LimitType limitType;

    public LimitDTO() {    }

    public LimitDTO( BigDecimal limitAmount, LimitType limitType ) {
        this.limitAmount = limitAmount;
        this.limitType = limitType;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public LimitType getLimitType() {
        return limitType;
    }
}
