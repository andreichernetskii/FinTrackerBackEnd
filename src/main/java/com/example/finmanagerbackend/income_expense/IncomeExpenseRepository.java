package com.example.finmanagerbackend.income_expense;


import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IncomeExpenseRepository extends JpaRepository<IncomeExpense, Long> {
    @Query( """
            SELECT operation 
            FROM IncomeExpense operation 
            WHERE ( :yearParam IS NULL OR YEAR( operation.date ) = :yearParam ) 
            AND ( :monthParam IS NULL OR MONTH( operation.date ) = :monthParam ) 
            AND ( :operationTypeParam IS NULL OR operation.operationType = :operationTypeParam) 
            AND ( :categoryParam IS NULL OR operation.category = :categoryParam )
            """ )
    List<IncomeExpense> findOperationsByCriteria( @Param( "yearParam" ) Integer year,
                                                  @Param( "monthParam" ) Integer month,
                                                  @Param( "operationTypeParam" ) OperationType operationType,
                                                  @Param( "categoryParam" ) String category );

    @Query( """
            SELECT SUM( operation.amount ) 
            FROM IncomeExpense operation 
            WHERE ( :yearParam IS NULL OR YEAR( operation.date ) = :yearParam ) 
            AND ( :monthParam IS NULL OR MONTH( operation.date ) = :monthParam ) 
            AND ( :operationTypeParam IS NULL OR operation.operationType = :operationTypeParam) 
            AND ( :categoryParam IS NULL OR operation.category = :categoryParam )
            """ )
    Double calculateAnnualBalanceByCriteria( @Param( "yearParam" ) Integer year,
                                             @Param( "monthParam" ) Integer month,
                                             @Param( "operationTypeParam" ) OperationType operationType,
                                             @Param( "categoryParam" ) String category );

    default Double calculateAnnualBalance() {
        return calculateAnnualBalanceByCriteria( null, null, null, null );
    }

    @Query( """
            SELECT category 
            FROM IncomeExpense 
            GROUP BY category 
            ORDER BY category
            """ )
    List<String> getCategories();

    @Query( """
            SELECT SUM( operation.amount )
            FROM IncomeExpense operation
            WHERE operation.operationType = 'EXPENSE'
            AND YEAR( operation.date ) = YEAR( :monthParam )
            AND MONTH( operation.date ) = MONTH( :monthParam )
            """ )
    Double calculateMonthExpenses( @Param( "monthParam" ) LocalDate month );

    @Query( """
            SELECT SUM( operation.amount )
            FROM IncomeExpense  operation
            WHERE operation.operationType = 'EXPENSE'
            AND YEAR( operation.date ) = YEAR( :yearParam )
            """)
    Double calculateYearExpenses( @Param( "yearParam" ) LocalDate year );

    @Query( """
            SELECT SUM( operation.amount )
            FROM IncomeExpense operation
            WHERE operation.operationType = 'EXPENSE'
            AND YEAR( operation.date ) = YEAR( :dayParam )
            AND MONTH( operation.date ) = MONTH( :dayParam )
            AND DAY( operation.date ) = DAY( :dayParam )
            """)
    Double calculateDayExpenses( @Param( "dayParam" ) LocalDate day );

    @Query( """
            SELECT SUM( operation.amount )
            FROM IncomeExpense operation
            WHERE operation.operationType = 'EXPENSE'
            AND ( :firstWeekDayParam IS NULL OR operation.date >= :firstWeekDayParam) 
            AND ( :lastWeekDayParam IS NULL OR operation.date <= :lastWeekDayParam)
            """)
    Double calculateWeekExpenses( @Param( "firstWeekDayParam" ) LocalDate firstWeekDay,
                                  @Param( "lastWeekDayParam" ) LocalDate lastWeekDay );
}