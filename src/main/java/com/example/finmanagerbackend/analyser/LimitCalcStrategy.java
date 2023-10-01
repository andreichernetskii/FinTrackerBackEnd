package com.example.finmanagerbackend.analyser;

import com.example.finmanagerbackend.limit.Limit;
import com.example.finmanagerbackend.limit.LimitType;

public interface LimitCalcStrategy {
    boolean isLimitExceeded( Limit limit);
}
