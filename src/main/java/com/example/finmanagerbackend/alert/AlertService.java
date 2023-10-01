package com.example.finmanagerbackend.alert;

import com.example.finmanagerbackend.analyser.FinAnalyser;
import com.example.finmanagerbackend.income_expense.IncomeExpenseRepository;
import com.example.finmanagerbackend.limit.LimitRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AlertService {
    private final IncomeExpenseRepository incomeExpenseRepository;
    private final LimitRepository limitRepository;

    public AlertService( IncomeExpenseRepository incomeExpenseRepository, LimitRepository limitRepository ) {
        this.incomeExpenseRepository = incomeExpenseRepository;
        this.limitRepository = limitRepository;
    }

    public List<AlertDTO> showAllAlerts() {
        FinAnalyser finAnalyser = new FinAnalyser( incomeExpenseRepository, limitRepository );
        return finAnalyser.calcActualLimits(); //todo zamist key-String - LimitType



        //wstawiamy ten kod jako czesc metody getActualLimits ktora w takim razie bedzie musiala zwracac List<AlertDTO>


    }
}
