package com.example.finmanagerbackend.income_expense;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IIncomeExpenseRepository extends JpaRepository<IncomeExpense, Long> {
    @Query( "SELECT operation FROM IncomeExpense operation " +
            "WHERE ( :yearParam IS NULL OR YEAR( operation.date ) = :yearParam ) " +
            "AND ( :monthParam IS NULL OR MONTH( operation.date ) = :monthParam )" +
            "AND ( :operationTypeParam IS NULL OR operation.operationType = :operationTypeParam)" +
            "AND ( :categoryParam IS NULL OR operation.category = :categoryParam )" )
    List<IncomeExpense> findOperationsByCriteria( @Param( "yearParam" ) Integer year,
                                                  @Param( "monthParam" ) Integer month,
                                                  @Param( "operationTypeParam" ) OperationType operationType,
                                                  @Param( "categoryParam" ) String category );

    @Query( "SELECT SUM( operation.amount ) FROM IncomeExpense operation " +
            "WHERE ( :yearParam IS NULL OR YEAR( operation.date ) = :yearParam ) " +
            "AND ( :monthParam IS NULL OR MONTH( operation.date ) = :monthParam )" +
            "AND ( :operationTypeParam IS NULL OR operation.operationType = :operationTypeParam)" +
            "AND ( :categoryParam IS NULL OR operation.category = :categoryParam )" )
    Double calculateAnnualBalanceByCriteria( @Param( "yearParam" ) Integer year,
                                             @Param( "monthParam" ) Integer month,
                                             @Param( "operationTypeParam" ) OperationType operationType,
                                             @Param( "categoryParam" ) String category );

    default Double calculateAnnualBalance() {
        return calculateAnnualBalanceByCriteria( null, null, null, null );
    }

    @Query( "SELECT category FROM IncomeExpense GROUP BY category ORDER BY category" )
    List<String> getCategories();

    @Query( "SELECT SUM(operation.amount) FROM IncomeExpense operation " +
            "WHERE operation.operationType = 'EXPENSE'" +
            "AND ( :yearParam IS NULL OR YEAR( operation.date ) = YEAR( :localDateParam ) )" +
            "AND ( :monthParam IS NULL OR MONTH( operation.date ) = MONTH( :localDateParam ) )" +
            "AND ( :weekParam IS NULL OR " +
                "( YEAR( operation.date ) = YEAR( :localDateParam ) AND WEEK( operation.date ) = WEEK( :localDateParam ) ) )" +
            "AND ( :dayParam IS NULL OR operation.date = :localDateParam )" )
    Double calculateExpensesFromActualDate( @Param( "localDateParam" ) LocalDate localDate,
                                            @Param( "yearParam" ) Boolean year,
                                            @Param( "monthParam" ) Boolean month,
                                            @Param( "weekParam" ) Boolean week,
                                            @Param( "dayParam" ) Boolean day );
}
