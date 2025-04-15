package com.example.finmanagerbackend.financial_transaction;


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

    /**
     * Custom query to find financial transactions based on specified criteria.
     */
    @Query("""
            SELECT ft
            FROM FinancialTransaction ft
            WHERE ft.account.id = :accountId
            AND ( :yearParam IS NULL OR YEAR(ft.date) = :yearParam )
            AND ( :monthParam IS NULL OR MONTH(ft.date) = :monthParam )
            AND ( :operationTypeParam IS NULL OR ft.financialTransactionType = :operationTypeParam )
            AND ( :categoryParam IS NULL OR ft.category = :categoryParam )
            ORDER BY ft.date DESC
            """)
    List<FinancialTransaction> findOperationsByCriteria(@Param("accountId") Long accountId,
                                                        @Param("yearParam") Integer year,
                                                        @Param("monthParam") Integer month,
                                                        @Param("operationTypeParam") FinancialTransactionType financialTransactionType, // Pass enum name as String for native query
                                                        @Param("categoryParam") String category);

    /**
     * Custom query to calculate the total balance (sum of amounts) based on specified criteria.
     */
    @Query("""
            SELECT SUM( ft.amount )
            FROM FinancialTransaction ft
            WHERE ft.account.id = :accountId
            AND ( :yearParam IS NULL OR YEAR(ft.date) = :yearParam )
            AND ( :monthParam IS NULL OR MONTH(ft.date) = :monthParam )
            AND ( :operationTypeParam IS NULL OR ft.financialTransactionType = :operationTypeParam )
            AND ( :categoryParam IS NULL OR ft.category = :categoryParam )
            """)
    Double calculateAnnualBalanceByCriteria(@Param("accountId") Long accountId,
                                            @Param("yearParam") Integer year,
                                            @Param("monthParam") Integer month,
                                            @Param("operationTypeParam") FinancialTransactionType financialTransactionType, // Pass enum name as String
                                            @Param("categoryParam") String category);

    default Double calculateAnnualBalance(Long accountId) {
        return calculateAnnualBalanceByCriteria(accountId, null, null, null, null);
    }

    /**
     * Custom query to retrieve a distinct list of categories for a specific account.
     */
    @Query("""
            SELECT DISTINCT ft.category
            FROM FinancialTransaction ft
            WHERE ft.account.id = :accountId
            AND ft.category IS NOT NULL
            ORDER BY ft.category
            """)
    List<String> getCategories(@Param("accountId") Long accountId);


    /**
     * Calculates monthly expenses for a given month and year for an account.
     */
    @Query("""
            SELECT SUM(ft.amount)
            FROM FinancialTransaction ft
            WHERE ft.financialTransactionType = 'EXPENSE'
            AND YEAR(ft.date) = :yearParam
            AND MONTH(ft.date) = :monthParam
            AND ft.account.id = :accountId
            """)
    Double calculateMonthExpenses(@Param("accountId") Long accountId, @Param("monthParam") LocalDate month);

    /**
     * Calculates yearly expenses for a given year and account.
     */
    @Query("""
            SELECT SUM( ft.amount )
            FROM FinancialTransaction ft
            WHERE ft.financialTransactionType = 'EXPENSE'
            AND YEAR(ft.date) = :yearParam
            AND ft.account.id = :accountId
            """)
    Double calculateYearExpenses(@Param("accountId") Long accountId, @Param("yearParam") LocalDate year);

    /**
     * Calculates daily expenses for a given day and account.
     */
    @Query("""
            SELECT SUM( ft.amount )
            FROM FinancialTransaction ft
            WHERE ft.financialTransactionType = 'EXPENSE'
            AND CAST( ft.date AS DATE ) = CAST( :dayParam AS DATE )
            AND ft.account.id = :accountId
            """)
    Double calculateDayExpenses(@Param("accountId") Long accountId, @Param("dayParam") LocalDate day);

    /**
     * Custom query to calculate weekly expenses for a given week range and account.
     */
    @Query("""
            SELECT SUM(ft.amount)
            FROM FinancialTransaction ft
            WHERE ft.financialTransactionType = 'EXPENSE'
            AND ft.date >= :firstWeekDayParam
            AND ft.date <= :lastWeekDayParam
            AND ft.account.id = :accountId
            """)
    Double calculateWeekExpenses(@Param("accountId") Long accountId,
                                 @Param("firstWeekDayParam") LocalDate firstWeekDay,
                                 @Param("lastWeekDayParam") LocalDate lastWeekDay);

    /**
     * Custom native query to find a financial transaction by its ID and associated account.
     */
    @Query("""
            SELECT ft
            FROM FinancialTransaction ft
            WHERE ft.id = :operationId
            AND ft.account.id = :accountId
            """)
    Optional<FinancialTransaction> findByAccountIdPlusOperationId(@Param("operationId") Long operationId,
                                                                  @Param("accountId") Long accountId);

    /**
     * Custom native query to count all transactions for a specific account.
     */
    @Query("""
            SELECT COUNT(ft)
            FROM FinancialTransaction ft
            WHERE ft.account.id = :accountId
            """)
    Long countByAccountId(@Param("accountId") Long accountId); // Changed return type to Long

    /**
     * Custom native query to find all transactions for a specific account, ordered by date descending.
     */
    @Query("""
            SELECT ft
            FROM FinancialTransaction ft
            WHERE ft.account.id = :accountId
            ORDER BY ft.date DESC
            """)
    List<FinancialTransaction> findAllTransactionsOfAccount(@Param("accountId") Long accountId);


    /**
     * Custom native query to find all distinct years present in transactions for a specific account.
     * Ordered descending (most recent year first).
     */
    @Query("""
            SELECT DISTINCT YEAR(ft.date)
            FROM FinancialTransaction ft
            WHERE ft.account.id = :accountId
            ORDER BY YEAR(ft.date) DESC
            """)
    List<Integer> findDistinctYearsByAccountId(@Param("accountId") Long accountId);

    /**
     * Custom native query to find all distinct month numbers (1-12) present in transactions
     * for a specific account across all years. Ordered ascending (Jan first).
     */
    @Query("""
            SELECT DISTINCT MONTH(ft.date)
            FROM FinancialTransaction ft
            WHERE ft.account.id = :accountId
            ORDER BY MONTH(ft.date) ASC
            """)
    List<Integer> findDistinctMonthsByAccountIdAllTime(@Param("accountId") Long accountId);
}