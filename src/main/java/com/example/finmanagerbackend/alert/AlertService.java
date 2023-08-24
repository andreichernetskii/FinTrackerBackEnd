package com.example.finmanagerbackend.alert;

import com.example.finmanagerbackend.income_expense.IIncomeExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class AlertService {
    private IIncomeExpenseRepository iIncomeExpenseRepository;

    public AlertService( IIncomeExpenseRepository iIncomeExpenseRepository ) {
        this.iIncomeExpenseRepository = iIncomeExpenseRepository;
    }

    public List<AlertDTO> showAllAlerts() {
        BigDecimal actualBalance = getActualBalance();
        List<AlertDTO> alerts = new ArrayList<>();
        if (actualBalance.doubleValue() < 0) {
            alerts.add( new AlertDTO( "Jesteś na minusie!", false ) );
        }

        return alerts;
    }

    public BigDecimal getActualBalance() {
        return new BigDecimal( iIncomeExpenseRepository.calculateAnnualBalance() );
    }
}
