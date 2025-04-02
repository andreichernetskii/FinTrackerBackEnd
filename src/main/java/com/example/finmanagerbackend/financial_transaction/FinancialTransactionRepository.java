package com.example.finmanagerbackend.financial_transaction;


import org.springframework.data.jpa.repository.EntityGraph;
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
public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, Long> {

    // Custom query to find financial transactions based on specified criteria.
    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = "account")
    @Query( """
            SELECT f
            FROM FinancialTransaction f
            WHERE  f.account.id = :accountId
            AND ( :yearParam IS NULL OR YEAR( f.date ) = :yearParam )
            AND ( :monthParam IS NULL OR MONTH( f.date ) = :monthParam )
            AND ( :operationTypeParam IS NULL OR f.financialTransactionType = :operationTypeParam)
            AND ( :categoryParam IS NULL OR f.category = :categoryParam )
            ORDER BY f.date DESC
            """ )
    List<FinancialTransaction> findOperationsByCriteria( @Param( "accountId" ) Long accountId,
                                                         @Param( "yearParam" ) Integer year,
                                                         @Param( "monthParam" ) Integer month,
                                                         @Param( "operationTypeParam" ) FinancialTransactionType financialTransactionType,
                                                         @Param( "categoryParam" ) String category );

    // Custom query to calculate the annual balance based on specified criteria.
    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = "account")
    @Query( """
            SELECT SUM( financialTransaction.amount )
            FROM FinancialTransaction financialTransaction
            WHERE  financialTransaction.account.id = :accountId
            AND ( :yearParam IS NULL OR YEAR( financialTransaction.date ) = :yearParam )
            AND ( :monthParam IS NULL OR MONTH( financialTransaction.date ) = :monthParam )
            AND ( :operationTypeParam IS NULL OR financialTransaction.financialTransactionType = :operationTypeParam)
            AND ( :categoryParam IS NULL OR financialTransaction.category = :categoryParam )
            """ )
    Double calculateAnnualBalanceByCriteria( @Param( "accountId" ) Long accountId,
                                             @Param( "yearParam" ) Integer year,
                                             @Param( "monthParam" ) Integer month,
                                             @Param( "operationTypeParam" ) FinancialTransactionType financialTransactionType,
                                             @Param( "categoryParam" ) String category );

    // Default method to calculate the annual balance without specifying criteria.
    default Double calculateAnnualBalance( Long accountId ) {
        return calculateAnnualBalanceByCriteria( accountId, null, null, null, null );
    }

    // Custom query to retrieve a list of categories for a specific account.
    @Query( """
            SELECT financialTransaction.category
            FROM FinancialTransaction financialTransaction
            WHERE  financialTransaction.account.id = :accountId
            GROUP BY financialTransaction.category
            ORDER BY financialTransaction.category
            """ )
    List<String> getCategories( @Param( "accountId" ) Long accountId );

    // Custom query to calculate monthly expenses for a given month.
//    @Query( """
//            SELECT SUM( financialTransaction.amount )
//            FROM FinancialTransaction financialTransaction
//            WHERE financialTransaction.financialTransactionType = 'EXPENSE'
//            AND EXTRACT( YEAR FROM financialTransaction.date ) = EXTRACT( YEAR FROM CAST( :monthParam AS DATE) )
//            AND EXTRACT( MONTH FROM financialTransaction.date ) = EXTRACT( MONTH FROM CAST( :monthParam AS DATE) )
//            """ )
//    Double calculateMonthExpenses( @Param( "monthParam" ) LocalDate month );

    @Query(value = "SELECT SUM(tr.amount) " +
            "FROM financial_transaction tr " +
            "WHERE tr.financial_transaction_type = 'EXPENSE' " +
            "AND EXTRACT(YEAR FROM tr.date) = EXTRACT( YEAR FROM CAST( :monthParam AS DATE)) " +
            "AND EXTRACT( MONTH FROM tr.date ) = EXTRACT( MONTH FROM CAST( :monthParam AS DATE)) " +
            "AND tr.account_id = :accountId",
    nativeQuery = true)
    Double calculateMonthExpenses( @Param( "accountId" ) Long accountId, @Param( "monthParam" ) LocalDate month );

    // Custom query to calculate yearly expenses for a given year.
    @Query(value = "SELECT SUM( tr.amount ) " +
            "FROM financial_transaction  tr " +
            "WHERE tr.financial_transaction_type = 'EXPENSE' " +
            "AND EXTRACT( YEAR FROM tr.date ) = EXTRACT( YEAR FROM CAST( :yearParam AS DATE) ) " +
            "AND tr.account_id = :accountId",
    nativeQuery = true)
    Double calculateYearExpenses( @Param( "accountId" ) Long accountId, @Param( "yearParam" ) LocalDate year );


    // Custom query to calculate daily expenses for a given day.
    @Query(value = "SELECT SUM( tr.amount ) " +
            "FROM financial_transaction tr " +
            "WHERE tr.financial_transaction_type = 'EXPENSE' " +
            "AND CAST( tr.date AS DATE ) = CAST( :dayParam AS DATE ) " +
            "AND tr.account_id = :accountId",
    nativeQuery = true)
    Double calculateDayExpenses( @Param( "accountId" ) Long accountId, @Param( "dayParam" ) LocalDate day );

    // Custom query to calculate weekly expenses for a given week.
    @Query( value = "SELECT SUM(tr.amount) " +
            "FROM financial_transaction tr " +
            "WHERE tr.financial_transaction_type = 'EXPENSE' " +
            "AND ( tr.date >= CAST( :firstWeekDayParam AS DATE )) " +
            "AND ( tr.date <= CAST( :lastWeekDayParam AS DATE )) " +
            "AND tr.account_id = :accountId",
    nativeQuery = true)
    Double calculateWeekExpenses( @Param( "accountId" ) Long accountId,
                                  @Param( "firstWeekDayParam" ) LocalDate firstWeekDay,
                                  @Param( "lastWeekDayParam" ) LocalDate lastWeekDay );

    // Custom query to find a financial transaction by its ID and associated account.
    @Query( """
            SELECT operation
            FROM FinancialTransaction operation
            WHERE operation.id = :operationId
            AND operation.account.id = :accountId
            """ )
    Optional<FinancialTransaction> findByAccountIdPlusOperationId( @Param( "operationId" ) Long operationId,
                                                                   @Param( "accountId" ) Long accountId );

    @Query( """
            SELECT COUNT(*)
            FROM FinancialTransaction operation
            WHERE operation.account.id = :accountId
            """ )
    Integer countByAccountId( @Param( "accountId" ) Long accountId );

    @Query(value = "SELECT * " +
            "FROM financial_transaction " +
            "WHERE account_id = :accountId " +
            "ORDER BY date DESC",
    nativeQuery = true)
    List<FinancialTransaction> findAllTransactionsOfAccount(@Param( "accountId" ) Long accountId);
}