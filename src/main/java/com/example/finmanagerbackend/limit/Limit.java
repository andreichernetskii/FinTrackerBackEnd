package com.example.finmanagerbackend.limit;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Limit {
    @Id
    @GeneratedValue
    private Long id;
    private BigDecimal amountLimit;
    @Enumerated( EnumType.STRING )
    private LimitType limitType;

    // constructors

    public Limit() {    }

    // budget
    public Limit( BigDecimal amountLimit ) {
        this.amountLimit = amountLimit;
    }

    // limit of special period of time
    public Limit( BigDecimal amountLimit, LimitType limitType ) {
        this.amountLimit = amountLimit;
        this.limitType = limitType;
    }

    // getters

    public Long getId() {
        return id;
    }

    public BigDecimal getAmountLimit() {
        return amountLimit;
    }

    public LimitType getLimitType() {
        return limitType;
    }

    // setters

    public void setAmountLimit( BigDecimal amountLimit ) {
        this.amountLimit = amountLimit;
    }

    public void setLimitType( LimitType limitType ) {
        this.limitType = limitType;
    }
}
