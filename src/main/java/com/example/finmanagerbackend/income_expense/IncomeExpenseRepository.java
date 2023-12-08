package com.example.finmanagerbackend.income_expense;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeExpenseRepository extends JpaRepository<IncomeExpense, Long> {
    @Query( """
            SELECT incomeExpense 
            FROM IncomeExpense incomeExpense 
            WHERE ( :yearParam IS NULL OR YEAR( incomeExpense.date ) = :yearParam ) 
            AND ( :monthParam IS NULL OR MONTH( incomeExpense.date ) = :monthParam ) 
            AND ( :operationTypeParam IS NULL OR incomeExpense.operationType = :operationTypeParam) 
            AND ( :categoryParam IS NULL OR incomeExpense.category = :categoryParam )
            """ )
    List<IncomeExpense> findOperationsByCriteria( @Param( "yearParam" ) Integer year,
                                                  @Param( "monthParam" ) Integer month,
                                                  @Param( "operationTypeParam" ) OperationType operationType,
                                                  @Param( "categoryParam" ) String category );

    @Query( """
            SELECT SUM( incomeExpense.amount ) 
            FROM IncomeExpense incomeExpense 
            WHERE ( :yearParam IS NULL OR YEAR( incomeExpense.date ) = :yearParam ) 
            AND ( :monthParam IS NULL OR MONTH( incomeExpense.date ) = :monthParam ) 
            AND ( :operationTypeParam IS NULL OR incomeExpense.operationType = :operationTypeParam) 
            AND ( :categoryParam IS NULL OR incomeExpense.category = :categoryParam )
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
            SELECT SUM( incomeExpense.amount )
            FROM IncomeExpense incomeExpense
            WHERE incomeExpense.operationType = 'EXPENSE'
            AND YEAR( incomeExpense.date ) = YEAR( :monthParam )
            AND MONTH( incomeExpense.date ) = MONTH( :monthParam )
            """ )
    Double calculateMonthExpenses( @Param( "monthParam" ) LocalDate month );

    @Query( """
            SELECT SUM( incomeExpense.amount )
            FROM IncomeExpense  incomeExpense
            WHERE incomeExpense.operationType = 'EXPENSE'
            AND YEAR( incomeExpense.date ) = YEAR( :yearParam )
            """ )
    Double calculateYearExpenses( @Param( "yearParam" ) LocalDate year );

    @Query( """
            SELECT SUM( incomeExpense.amount )
            FROM IncomeExpense incomeExpense
            WHERE incomeExpense.operationType = 'EXPENSE'
            AND YEAR( incomeExpense.date ) = YEAR( :dayParam )
            AND MONTH( incomeExpense.date ) = MONTH( :dayParam )
            AND DAY( incomeExpense.date ) = DAY( :dayParam )
            """ )
    Double calculateDayExpenses( @Param( "dayParam" ) LocalDate day );

    @Query( """
            SELECT SUM( incomeExpense.amount )
            FROM IncomeExpense incomeExpense
            WHERE incomeExpense.operationType = 'EXPENSE'
            AND ( :firstWeekDayParam IS NULL OR incomeExpense.date >= :firstWeekDayParam) 
            AND ( :lastWeekDayParam IS NULL OR incomeExpense.date <= :lastWeekDayParam)
            """ )
    Double calculateWeekExpenses( @Param( "firstWeekDayParam" ) LocalDate firstWeekDay,
                                  @Param( "lastWeekDayParam" ) LocalDate lastWeekDay );

    @Query( """
            SELECT operation
            FROM IncomeExpense operation
            WHERE operation.id = :operationId
            AND operation.account.id = :accountId
            """ )
    Optional<IncomeExpense> findByAccountIdPlusOperationId( @Param( "operationId" ) Long operationId,
                                                            @Param( "accountId" ) Long accountId );
}