package com.example.finmanagerbackend.analyser;

import com.example.finmanagerbackend.alert.AlertDTO;
import com.example.finmanagerbackend.alert.AlertType;
import com.example.finmanagerbackend.income_expense.IncomeExpenseRepository;
import com.example.finmanagerbackend.limit.Limit;
import com.example.finmanagerbackend.limit.LimitRepository;
import com.example.finmanagerbackend.limit.LimitType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FinAnalyser {
    private IncomeExpenseRepository incomeExpenseRepository;
    private LimitRepository limitRepository;
    private LimitCalcStrategy strategy;

    public FinAnalyser( IncomeExpenseRepository incomeExpenseRepository, LimitRepository limitRepository ) {
        this.incomeExpenseRepository = incomeExpenseRepository;
        this.limitRepository = limitRepository;
    }

    public List<AlertDTO> calcActualLimits() {
      /*  Map<LimitType, BigDecimal> limits = new HashMap<>();
        Arrays.stream( LimitType.values() ).forEach( type ->
            limits.put( type, getLimit( type ) )
         );
*/

//        Map<LimitType, BigDecimal> limits = Arrays.stream( LimitType.values() )
//                .collect( Collectors.toMap( limitType -> limitType, limitType -> getLimit( limitType ) ) );

        List<Limit> limitsList = limitRepository.findAll();
        List<AlertDTO> alerts = new ArrayList<>();

        for ( Limit limit : limitsList ) {
            setStrategy( limit.getLimitType() );
            if ( isLimitExceeded( limit ) )
                alerts.add( new AlertDTO( "limit przekrocony" + limit.getLimitType(), false ) );
        }


//        limits.put( "budget", getLimit( LimitType.BUDGET ) );
//        limits.put( "day", getLimit( LimitType.DAY ) );
//        limits.put( "week", getLimit( LimitType.WEEK ) );
//        limits.put( "month", getLimit( LimitType.MONTH ) );
//        limits.put( "year", getLimit( LimitType.YEAR ) );


        // todo przekrocenia limita 0
      /*  if ( isNegativeConditionOfAccount() ) {
            alerts.add( new AlertDTO( AlertType.NEGATIVE_BALANCE.label, false ) );
        }

        if ( isBudgetExceeded( limits.get( "budget" ) ) ) {
            alerts.add( new AlertDTO( AlertType.BUDGET_LIMIT_EXCEEDING.label, false ) );
        }

        if ( isYearLimitExceeded( limits.get( "year" ) ) ) {
            alerts.add( new AlertDTO( AlertType.YEAR_LIMIT_EXCEEDING.label, false ) );
        }

        if ( isMonthLimitExceeded( limits.get( "month" ) ) ) {
            alerts.add( new AlertDTO( AlertType.MONTH_LIMIT_EXCEEDING.label, false ) );
        }

        if ( isWeekLimitExceeded( limits.get( "week" ) ) ) {
            alerts.add( new AlertDTO( AlertType.WEEK_LIMIT_EXCEEDING.label, false ) );
        }

        if ( isDayLimitExceeded( limits.get( "day" ) ) ) {
            alerts.add( new AlertDTO( AlertType.DAY_LIMIT_EXCEEDING.label, false ) );
        }*/


        return alerts;
    }

    private boolean isLimitExceeded( Limit limit ) {
        return strategy.isLimitExceeded(limit);
    }

    private void setStrategy( LimitType limitType ) {
      strategy =  switch ( limitType ) {
            case YEAR -> new YearLimitCalcStrategy( incomeExpenseRepository );
        case MONTH -> new MonthLimitCalcStrategy( incomeExpenseRepository );
          case WEEK -> new WeekLimitCalcStrategy( incomeExpenseRepository );
          case DAY -> new DayLimitCalcStrategy( incomeExpenseRepository );
            default -> throw new IllegalStateException();
        };
    }

    private BigDecimal getLimit( LimitType limitType ) {
        Double val = limitRepository.getLimitAmountByLimitType( limitType );
        return ( val != null ) ? BigDecimal.valueOf( val ) : null;
    }

//    private BigDecimal getBudget() {
//        Double val = limitRepository.getLimitAmountByLimitType( LimitType.BUDGET );
//        return (val != null) ? BigDecimal.valueOf( val ) : null;
//    }
//
//    private BigDecimal getYearLimit() {
//        Double val = limitRepository.getLimitAmountByLimitType( LimitType.YEAR );
//        return (val != null) ? BigDecimal.valueOf( val ) : null;
//    }
//
//    private BigDecimal getMonthLimit() {
//        Double val = limitRepository.getLimitAmountByLimitType( LimitType.MONTH );
//        return (val != null) ? BigDecimal.valueOf( val ) : null;
//    }
//
//    private BigDecimal getWeekLimit() {
//        Double val = limitRepository.getLimitAmountByLimitType( LimitType.WEEK );
//        return (val != null) ? BigDecimal.valueOf( val ) : null;
//    }
//
//    private BigDecimal getDayLimit() {
//        Double val = limitRepository.getLimitAmountByLimitType( LimitType.DAY );
//        return (val != null) ? BigDecimal.valueOf( val ) : null;
//    }

    // checking is limits are exceeded
        //todo: private
//    public boolean isNegativeConditionOfAccount() {
//        return incomeExpenseRepository.calculateAnnualBalance() <0;
//    }
//
//    public Boolean isBudgetExceeded( BigDecimal budget ) {
//        if ( budget == null ) return false;
//
//        BigDecimal actualBalance = BigDecimal.valueOf( incomeExpenseRepository.calculateAnnualBalance() ).abs(); //todo annual = year
//        return actualBalance.compareTo( budget ) >= 0;
//    }
//
//    public Boolean isYearLimitExceeded( BigDecimal yearLimit ) {
//        if ( yearLimit == null ) return false;
//
//        Double actualBalanceAsDouble = incomeExpenseRepository.calculateYearExpenses( LocalDate.now() );
//        if ( actualBalanceAsDouble == null ) return false;
//
//        BigDecimal actualBalance = BigDecimal.valueOf( actualBalanceAsDouble ).abs();
//        return actualBalance.compareTo( yearLimit ) >= 0;
//    }
//
//    public Boolean isMonthLimitExceeded( BigDecimal monthLimit ) {
//        if ( monthLimit == null ) return false;
//
//        Double actualBalanceAsDouble = incomeExpenseRepository.calculateMonthExpenses( LocalDate.now() );
//        if ( actualBalanceAsDouble == null ) return false;
//
//        BigDecimal actualBalance = BigDecimal.valueOf( actualBalanceAsDouble ).abs();
//        return actualBalance.compareTo( monthLimit ) >= 0;
//    }
//
//    public boolean isWeekLimitExceeded( BigDecimal weekLimit ) {
//        if ( weekLimit == null ) return false;
//        List<LocalDate> firstLastWeekDay = getStartAndEndOfWeekDates();
//
//        Double actualBalanceAsDouble = incomeExpenseRepository
//                .calculateWeekExpenses( firstLastWeekDay.get( 0 ), firstLastWeekDay.get( 1 ) );
//        if ( actualBalanceAsDouble == null ) return false;
//
//        BigDecimal actualBalance = BigDecimal.valueOf( actualBalanceAsDouble ).abs();
//        return actualBalance.compareTo( weekLimit ) >= 0;
//    }
//
//
//    public boolean isDayLimitExceeded( BigDecimal dayLimit ) {
//        if ( dayLimit == null ) return false;
//
//        Double actualBalanceAsDouble = incomeExpenseRepository.calculateDayExpenses( LocalDate.now() );
//        if ( actualBalanceAsDouble == null ) return false;
//
//        BigDecimal actualBalance = BigDecimal.valueOf( actualBalanceAsDouble ).abs();
//        return actualBalance.compareTo( dayLimit ) >= 0;
//    }

    // todo sprobowac dodac klasy strAT jako klasy wewnt
}
