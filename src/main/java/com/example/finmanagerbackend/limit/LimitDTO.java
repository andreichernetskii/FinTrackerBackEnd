package com.example.finmanagerbackend.limit;

import org.hibernate.query.spi.Limit;

import java.math.BigDecimal;

public class LimitDTO {
    private BigDecimal amountLimit;
    private LimitType limitType;

    public LimitDTO() {    }

    // budget
    public LimitDTO( BigDecimal amountLimit ) {
        this.amountLimit = amountLimit;
    }

    // limit of special period of time
    public LimitDTO( BigDecimal amountLimit, LimitType limitType ) {
        this.amountLimit = amountLimit;
        this.limitType = limitType;
    }

    public BigDecimal getAmountLimit() {
        return amountLimit;
    }

    public LimitType getLimitType() {
        return limitType;
    }
}
