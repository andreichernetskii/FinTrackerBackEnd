package com.example.finmanagerbackend.limit;

import jakarta.persistence.*;
import lombok.NonNull;

import java.math.BigDecimal;

@Entity
@Table( name = "Limits",
        uniqueConstraints = @UniqueConstraint( columnNames = {"limitType"} ) )
public class Limit {
    @Id
    @GeneratedValue
    private Long id;
    @Column( nullable = false )
    private BigDecimal amountLimit;
    @Enumerated( EnumType.STRING )
    @Column( nullable = false )
    private LimitType limitType;

    // constructors

    public Limit() {
    }

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
