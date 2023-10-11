package com.example.finmanagerbackend.analyser.limit_calcs;

import com.example.finmanagerbackend.limit.Limit;
import com.example.finmanagerbackend.limit.LimitType;

import java.math.BigDecimal;

public interface LimitCalcStrategy {
    boolean isLimitExceeded( Limit limit);
}
