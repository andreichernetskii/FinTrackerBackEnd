package com.example.finmanagerbackend.analyser.actual_balance_of_period_calc_strategy;

import com.example.finmanagerbackend.limit.Limit;

public interface ActualBalanceCalcStrategy {
    Double calcActualBalanceOfPeriod( Limit limit );
}
