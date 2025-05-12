package com.example.finmanagerbackend.alert.analyser.strategy;

import com.example.finmanagerbackend.limit.LimitDTO;

/**
 * Interface defining the strategy for calculating the actual balance of a specific period.
 */
public interface ActualBalanceCalcStrategy {
    Double calcActualBalanceOfPeriod( LimitDTO limit );
}
