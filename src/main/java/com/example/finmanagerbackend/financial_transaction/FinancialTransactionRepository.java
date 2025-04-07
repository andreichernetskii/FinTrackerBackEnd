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
     * Custom native query to find financial transactions based on specified criteria.
     */
    @Query(value = """
            SELECT ft.*
            FROM financial_transaction ft
            WHERE ft.account_id = :accountId
            AND ( :yearParam IS NULL OR EXTRACT(YEAR FROM ft.date) = :yearParam )
            AND ( :monthParam IS NULL OR EXTRACT(MONTH FROM ft.date) = :monthParam )
            AND ( CAST(:operationTypeParam AS VARCHAR) IS NULL OR ft.financial_transaction_type = :operationTypeParam )
            AND ( CAST(:categoryParam AS VARCHAR) IS NULL OR ft.category = :categoryParam )
            ORDER BY ft.date DESC
            """, nativeQuery = true)
    List<FinancialTransaction> findOperationsByCriteria( @Param("accountId") Long accountId,
                                                         @Param("yearParam") Integer year,
                                                         @Param("monthParam") Integer month,
                                                         @Param("operationTypeParam") FinancialTransactionType financialTransactionType, // Pass enum name as String for native query
                                                         @Param("categoryParam") String category );

    /**
     * Custom native query to calculate the total balance (sum of amounts) based on specified criteria.
     */
    @Query(value = """
            SELECT SUM( ft.amount )
            FROM financial_transaction ft
            WHERE ft.account_id = :accountId
            AND ( :yearParam IS NULL OR EXTRACT(YEAR FROM ft.date) = :yearParam )
            AND ( :monthParam IS NULL OR EXTRACT(MONTH FROM ft.date) = :monthParam )
            AND ( CAST(:operationTypeParam AS VARCHAR) IS NULL OR ft.financial_transaction_type = :operationTypeParam )
            AND ( CAST(:categoryParam AS VARCHAR) IS NULL OR ft.category = :categoryParam )
            """, nativeQuery = true)
    Double calculateAnnualBalanceByCriteria( @Param("accountId") Long accountId,
                                             @Param("yearParam") Integer year,
                                             @Param("monthParam") Integer month,
                                             @Param("operationTypeParam") FinancialTransactionType financialTransactionType, // Pass enum name as String
                                             @Param("categoryParam") String category );

    // Default method remains the same, utilizes the native query version now.
    default Double calculateAnnualBalance( Long accountId ) {
        // Assuming FinancialTransactionType enum has names like 'EXPENSE', 'INCOME' etc.
        // Pass null for String parameters if the enum parameter was null previously.
        return calculateAnnualBalanceByCriteria( accountId, null, null, null, null );
    }

    /**
     * Custom native query to retrieve a distinct list of categories for a specific account.
     */
    @Query(value = """
            SELECT DISTINCT ft.category
            FROM financial_transaction ft
            WHERE ft.account_id = :accountId
            AND ft.category IS NOT NULL -- Optional: exclude transactions without category
            ORDER BY ft.category
            """, nativeQuery = true)
    List<String> getCategories( @Param("accountId") Long accountId );


    /**
     * Custom native query to calculate monthly expenses for a given month and account.
     */
    @Query(value = """
            SELECT SUM(ft.amount)
            FROM financial_transaction ft
            WHERE ft.financial_transaction_type = 'EXPENSE' -- Assuming 'EXPENSE' is the enum string value
            AND EXTRACT(YEAR FROM ft.date) = EXTRACT( YEAR FROM CAST( :monthParam AS DATE))
            AND EXTRACT( MONTH FROM ft.date ) = EXTRACT( MONTH FROM CAST( :monthParam AS DATE))
            AND ft.account_id = :accountId
            """, nativeQuery = true)
    Double calculateMonthExpenses( @Param("accountId") Long accountId, @Param("monthParam") LocalDate month );

    /**
     * Custom native query to calculate yearly expenses for a given year and account.
     */
    @Query(value = """
            SELECT SUM( ft.amount )
            FROM financial_transaction ft
            WHERE ft.financial_transaction_type = 'EXPENSE' -- Assuming 'EXPENSE' is the enum string value
            AND EXTRACT( YEAR FROM ft.date ) = EXTRACT( YEAR FROM CAST( :yearParam AS DATE) )
            AND ft.account_id = :accountId
            """, nativeQuery = true)
    Double calculateYearExpenses( @Param("accountId") Long accountId, @Param("yearParam") LocalDate year );


    /**
     * Custom native query to calculate daily expenses for a given day and account.
     */
    @Query(value = """
            SELECT SUM( ft.amount )
            FROM financial_transaction ft
            WHERE ft.financial_transaction_type = 'EXPENSE' -- Assuming 'EXPENSE' is the enum string value
            AND CAST( ft.date AS DATE ) = CAST( :dayParam AS DATE )
            AND ft.account_id = :accountId
            """, nativeQuery = true)
    Double calculateDayExpenses( @Param("accountId") Long accountId, @Param("dayParam") LocalDate day );

    /**
     * Custom native query to calculate weekly expenses for a given week range and account.
     */
    @Query( value = """
            SELECT SUM(ft.amount)
            FROM financial_transaction ft
            WHERE ft.financial_transaction_type = 'EXPENSE' -- Assuming 'EXPENSE' is the enum string value
            AND ft.date >= CAST( :firstWeekDayParam AS DATE )
            AND ft.date <= CAST( :lastWeekDayParam AS DATE )
            AND ft.account_id = :accountId
            """, nativeQuery = true)
    Double calculateWeekExpenses( @Param("accountId") Long accountId,
                                  @Param("firstWeekDayParam") LocalDate firstWeekDay,
                                  @Param("lastWeekDayParam") LocalDate lastWeekDay );

    /**
     * Custom native query to find a financial transaction by its ID and associated account.
     */
    @Query(value = """
            SELECT ft.*
            FROM financial_transaction ft
            WHERE ft.id = :operationId
            AND ft.account_id = :accountId
            """, nativeQuery = true)
    Optional<FinancialTransaction> findByAccountIdPlusOperationId( @Param("operationId") Long operationId,
                                                                   @Param("accountId") Long accountId );

    /**
     * Custom native query to count all transactions for a specific account.
     */
    @Query(value = """
            SELECT COUNT(*)
            FROM financial_transaction ft
            WHERE ft.account_id = :accountId
            """, nativeQuery = true)
    Long countByAccountId( @Param("accountId") Long accountId ); // Changed return type to Long

    /**
     * Custom native query to find all transactions for a specific account, ordered by date descending.
     */
    @Query(value = """
            SELECT ft.*
            FROM financial_transaction ft
            WHERE ft.account_id = :accountId
            ORDER BY ft.date DESC
            """, nativeQuery = true)
    List<FinancialTransaction> findAllTransactionsOfAccount(@Param("accountId") Long accountId);

    // --- New Methods ---

    /**
     * Custom native query to find all distinct years present in transactions for a specific account.
     * Ordered descending (most recent year first).
     */
    @Query(value = """
            SELECT DISTINCT EXTRACT(YEAR FROM ft.date) AS transaction_year
            FROM financial_transaction ft
            WHERE ft.account_id = :accountId
            ORDER BY transaction_year DESC
            """, nativeQuery = true)
    List<Integer> findDistinctYearsByAccountId(@Param("accountId") Long accountId);

    /**
     * Custom native query to find all distinct month numbers (1-12) present in transactions
     * for a specific account across all years. Ordered ascending (Jan first).
     */
    @Query(value = """
            SELECT DISTINCT EXTRACT(MONTH FROM ft.date) AS transaction_month
            FROM financial_transaction ft
            WHERE ft.account_id = :accountId
            ORDER BY transaction_month ASC
            """, nativeQuery = true)
    List<Integer> findDistinctMonthsByAccountIdAllTime(@Param("accountId") Long accountId);

    // Old
//    // Custom query to find financial transactions based on specified criteria.
//    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = "account")
//    @Query( """
//            SELECT f
//            FROM FinancialTransaction f
//            WHERE  f.account.id = :accountId
//            AND ( :yearParam IS NULL OR YEAR( f.date ) = :yearParam )
//            AND ( :monthParam IS NULL OR MONTH( f.date ) = :monthParam )
//            AND ( :operationTypeParam IS NULL OR f.financialTransactionType = :operationTypeParam)
//            AND ( :categoryParam IS NULL OR f.category = :categoryParam )
//            ORDER BY f.date DESC
//            """ )
//    List<FinancialTransaction> findOperationsByCriteria( @Param( "accountId" ) Long accountId,
//                                                         @Param( "yearParam" ) Integer year,
//                                                         @Param( "monthParam" ) Integer month,
//                                                         @Param( "operationTypeParam" ) FinancialTransactionType financialTransactionType,
//                                                         @Param( "categoryParam" ) String category );
//
//    // Custom query to calculate the annual balance based on specified criteria.
//    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = "account")
//    @Query( """
//            SELECT SUM( financialTransaction.amount )
//            FROM FinancialTransaction financialTransaction
//            WHERE  financialTransaction.account.id = :accountId
//            AND ( :yearParam IS NULL OR YEAR( financialTransaction.date ) = :yearParam )
//            AND ( :monthParam IS NULL OR MONTH( financialTransaction.date ) = :monthParam )
//            AND ( :operationTypeParam IS NULL OR financialTransaction.financialTransactionType = :operationTypeParam)
//            AND ( :categoryParam IS NULL OR financialTransaction.category = :categoryParam )
//            """ )
//    Double calculateAnnualBalanceByCriteria( @Param( "accountId" ) Long accountId,
//                                             @Param( "yearParam" ) Integer year,
//                                             @Param( "monthParam" ) Integer month,
//                                             @Param( "operationTypeParam" ) FinancialTransactionType financialTransactionType,
//                                             @Param( "categoryParam" ) String category );
//
//    // Default method to calculate the annual balance without specifying criteria.
//    default Double calculateAnnualBalance( Long accountId ) {
//        return calculateAnnualBalanceByCriteria( accountId, null, null, null, null );
//    }
//
//    // Custom query to retrieve a list of categories for a specific account.
//    @Query( """
//            SELECT financialTransaction.category
//            FROM FinancialTransaction financialTransaction
//            WHERE  financialTransaction.account.id = :accountId
//            GROUP BY financialTransaction.category
//            ORDER BY financialTransaction.category
//            """ )
//    List<String> getCategories( @Param( "accountId" ) Long accountId );
//
//    // Custom query to calculate monthly expenses for a given month.
////    @Query( """
////            SELECT SUM( financialTransaction.amount )
////            FROM FinancialTransaction financialTransaction
////            WHERE financialTransaction.financialTransactionType = 'EXPENSE'
////            AND EXTRACT( YEAR FROM financialTransaction.date ) = EXTRACT( YEAR FROM CAST( :monthParam AS DATE) )
////            AND EXTRACT( MONTH FROM financialTransaction.date ) = EXTRACT( MONTH FROM CAST( :monthParam AS DATE) )
////            """ )
////    Double calculateMonthExpenses( @Param( "monthParam" ) LocalDate month );
//
//    @Query(value = "SELECT SUM(tr.amount) " +
//            "FROM financial_transaction tr " +
//            "WHERE tr.financial_transaction_type = 'EXPENSE' " +
//            "AND EXTRACT(YEAR FROM tr.date) = EXTRACT( YEAR FROM CAST( :monthParam AS DATE)) " +
//            "AND EXTRACT( MONTH FROM tr.date ) = EXTRACT( MONTH FROM CAST( :monthParam AS DATE)) " +
//            "AND tr.account_id = :accountId",
//    nativeQuery = true)
//    Double calculateMonthExpenses( @Param( "accountId" ) Long accountId, @Param( "monthParam" ) LocalDate month );
//
//    // Custom query to calculate yearly expenses for a given year.
//    @Query(value = "SELECT SUM( tr.amount ) " +
//            "FROM financial_transaction  tr " +
//            "WHERE tr.financial_transaction_type = 'EXPENSE' " +
//            "AND EXTRACT( YEAR FROM tr.date ) = EXTRACT( YEAR FROM CAST( :yearParam AS DATE) ) " +
//            "AND tr.account_id = :accountId",
//    nativeQuery = true)
//    Double calculateYearExpenses( @Param( "accountId" ) Long accountId, @Param( "yearParam" ) LocalDate year );
//
//
//    // Custom query to calculate daily expenses for a given day.
//    @Query(value = "SELECT SUM( tr.amount ) " +
//            "FROM financial_transaction tr " +
//            "WHERE tr.financial_transaction_type = 'EXPENSE' " +
//            "AND CAST( tr.date AS DATE ) = CAST( :dayParam AS DATE ) " +
//            "AND tr.account_id = :accountId",
//    nativeQuery = true)
//    Double calculateDayExpenses( @Param( "accountId" ) Long accountId, @Param( "dayParam" ) LocalDate day );
//
//    // Custom query to calculate weekly expenses for a given week.
//    @Query( value = "SELECT SUM(tr.amount) " +
//            "FROM financial_transaction tr " +
//            "WHERE tr.financial_transaction_type = 'EXPENSE' " +
//            "AND ( tr.date >= CAST( :firstWeekDayParam AS DATE )) " +
//            "AND ( tr.date <= CAST( :lastWeekDayParam AS DATE )) " +
//            "AND tr.account_id = :accountId",
//    nativeQuery = true)
//    Double calculateWeekExpenses( @Param( "accountId" ) Long accountId,
//                                  @Param( "firstWeekDayParam" ) LocalDate firstWeekDay,
//                                  @Param( "lastWeekDayParam" ) LocalDate lastWeekDay );
//
//    // Custom query to find a financial transaction by its ID and associated account.
//    @Query( """
//            SELECT operation
//            FROM FinancialTransaction operation
//            WHERE operation.id = :operationId
//            AND operation.account.id = :accountId
//            """ )
//    Optional<FinancialTransaction> findByAccountIdPlusOperationId( @Param( "operationId" ) Long operationId,
//                                                                   @Param( "accountId" ) Long accountId );
//
//    @Query( """
//            SELECT COUNT(*)
//            FROM FinancialTransaction operation
//            WHERE operation.account.id = :accountId
//            """ )
//    Integer countByAccountId( @Param( "accountId" ) Long accountId );
//
//    @Query(value = "SELECT * " +
//            "FROM financial_transaction " +
//            "WHERE account_id = :accountId " +
//            "ORDER BY date DESC",
//    nativeQuery = true)
//    List<FinancialTransaction> findAllTransactionsOfAccount(@Param( "accountId" ) Long accountId);
}