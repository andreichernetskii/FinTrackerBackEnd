package com.example.finmanagerbackend.limit;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table( name = "Limits",
        uniqueConstraints = @UniqueConstraint( columnNames = {"limitType"} ) )
public class Limit {
    @Id
    @GeneratedValue
    private Long id;
    @Column( nullable = false )
    private BigDecimal limitAmount;
    @Enumerated( EnumType.STRING )
    @Column( nullable = false )
    private LimitType limitType; //todo skoro unikatowe to moze rowniez pelnic role id

    // constructors

    public Limit() {
    }

    public Limit( BigDecimal limitAmount, LimitType limitType ) {
        this.limitAmount = limitAmount;
        this.limitType = limitType;
    }

    // getters

    public Long getId() {
        return id;
    }

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
