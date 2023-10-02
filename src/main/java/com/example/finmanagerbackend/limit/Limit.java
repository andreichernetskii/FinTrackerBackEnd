package com.example.finmanagerbackend.limit;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table( name = "Limits" )
public class Limit {
    @Id
    @Enumerated( EnumType.STRING )
    private LimitType limitType; //todo skoro unikatowe to moze rowniez pelnic role id
    @Column( nullable = false )
    private BigDecimal limitAmount;

    // constructors

    public Limit() {
    }

    public Limit( BigDecimal limitAmount, LimitType limitType ) {
        this.limitAmount = limitAmount;
        this.limitType = limitType;
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
