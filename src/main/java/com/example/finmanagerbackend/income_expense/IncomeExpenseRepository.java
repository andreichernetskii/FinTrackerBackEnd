package com.example.finmanagerbackend.income_expense;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing and querying financial transactions stored in the database.
 */
@Repository
public interface IncomeExpenseRepository extends JpaRepository<IncomeExpense, Long> {

    // Custom query to find financial transactions based on specified criteria.
    @Query( """
            SELECT incomeExpense 
            FROM IncomeExpense incomeExpense
            WHERE  incomeExpense.account.id = :accountId
            AND ( :yearParam IS NULL OR YEAR( incomeExpense.date ) = :yearParam ) 
            AND ( :monthParam IS NULL OR MONTH( incomeExpense.date ) = :monthParam ) 
            AND ( :operationTypeParam IS NULL OR incomeExpense.operationType = :operationTypeParam) 
            AND ( :categoryParam IS NULL OR incomeExpense.category = :categoryParam )
            """ )
    List<IncomeExpense> findOperationsByCriteria( @Param( "accountId" ) Long accountId,
                                                  @Param( "yearParam" ) Integer year,
                                                  @Param( "monthParam" ) Integer month,
                                                  @Param( "operationTypeParam" ) OperationType operationType,
                                                  @Param( "categoryParam" ) String category );

    // Custom query to calculate the annual balance based on specified criteria.
    @Query( """
            SELECT SUM( incomeExpense.amount ) 
            FROM IncomeExpense incomeExpense 
            WHERE  incomeExpense.account.id = :accountId 
            AND ( :yearParam IS NULL OR YEAR( incomeExpense.date ) = :yearParam ) 
            AND ( :monthParam IS NULL OR MONTH( incomeExpense.date ) = :monthParam ) 
            AND ( :operationTypeParam IS NULL OR incomeExpense.operationType = :operationTypeParam) 
            AND ( :categoryParam IS NULL OR incomeExpense.category = :categoryParam )
            """ )
    Double calculateAnnualBalanceByCriteria( @Param( "accountId" ) Long accountId,
                                             @Param( "yearParam" ) Integer year,
                                             @Param( "monthParam" ) Integer month,
                                             @Param( "operationTypeParam" ) OperationType operationType,
                                             @Param( "categoryParam" ) String category );

    // Default method to calculate the annual balance without specifying criteria.
    default Double calculateAnnualBalance( Long accountId) {
        return calculateAnnualBalanceByCriteria( accountId, null, null, null, null );
    }

    // Custom query to retrieve a list of categories for a specific account.
    @Query( """
            SELECT incomeExpense.category 
            FROM IncomeExpense incomeExpense
            WHERE  incomeExpense.account.id = :accountId 
            GROUP BY incomeExpense.category 
            ORDER BY incomeExpense.category
            """ )
    List<String> getCategories( @Param( "accountId" ) Long accountId );

    // Custom query to calculate monthly expenses for a given month.
    @Query( """
            SELECT SUM( incomeExpense.amount )
            FROM IncomeExpense incomeExpense
            WHERE incomeExpense.operationType = 'EXPENSE'
            AND YEAR( incomeExpense.date ) = YEAR( :monthParam )
            AND MONTH( incomeExpense.date ) = MONTH( :monthParam )
            """ )
    Double calculateMonthExpenses( @Param( "monthParam" ) LocalDate month );

    // Custom query to calculate yearly expenses for a given year.
    @Query( """
            SELECT SUM( incomeExpense.amount )
            FROM IncomeExpense  incomeExpense
            WHERE incomeExpense.operationType = 'EXPENSE'
            AND YEAR( incomeExpense.date ) = YEAR( :yearParam )
            """ )
    Double calculateYearExpenses( @Param( "yearParam" ) LocalDate year );

    // Custom query to calculate daily expenses for a given day.
    @Query( """
            SELECT SUM( incomeExpense.amount )
            FROM IncomeExpense incomeExpense
            WHERE incomeExpense.operationType = 'EXPENSE'
            AND YEAR( incomeExpense.date ) = YEAR( :dayParam )
            AND MONTH( incomeExpense.date ) = MONTH( :dayParam )
            AND DAY( incomeExpense.date ) = DAY( :dayParam )
            """ )
    Double calculateDayExpenses( @Param( "dayParam" ) LocalDate day );

    // Custom query to calculate weekly expenses for a given week.
    @Query( """
            SELECT SUM( incomeExpense.amount )
            FROM IncomeExpense incomeExpense
            WHERE incomeExpense.operationType = 'EXPENSE'
            AND ( :firstWeekDayParam IS NULL OR incomeExpense.date >= :firstWeekDayParam) 
            AND ( :lastWeekDayParam IS NULL OR incomeExpense.date <= :lastWeekDayParam)
            """ )
    Double calculateWeekExpenses( @Param( "firstWeekDayParam" ) LocalDate firstWeekDay,
                                  @Param( "lastWeekDayParam" ) LocalDate lastWeekDay );

    // Custom query to find a financial transaction by its ID and associated account.
    @Query( """
            SELECT operation
            FROM IncomeExpense operation
            WHERE operation.id = :operationId
            AND operation.account.id = :accountId
            """ )
    Optional<IncomeExpense> findByAccountIdPlusOperationId( @Param( "operationId" ) Long operationId,
                                                            @Param( "accountId" ) Long accountId );
}