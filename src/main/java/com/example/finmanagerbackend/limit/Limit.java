package com.example.finmanagerbackend.limit;

import jakarta.persistence.*;

import java.math.BigDecimal;

// todo: rozwiąć, żeby pokazywał o ile i jaki limit był przekrocony
@Entity
@Table( name = "Limits" )
public class Limit {
    @Id
    @Enumerated( EnumType.STRING )
    private LimitType limitType;
    @Column( nullable = false )
    private BigDecimal limitAmount;

    // constructors

    public Limit() {
    }

    public Limit( LimitType limitType, BigDecimal limitAmount ) {
        this.limitType = limitType;
        this.limitAmount = limitAmount;
    }

// getters

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public LimitType getLimitType() {
        return limitType;
    }

    // setters

    public void setLimitAmount( BigDecimal amountLimit ) {
        this.limitAmount = amountLimit;
    }

    public void setLimitType( LimitType limitType ) {
        this.limitType = limitType;
    }
}
