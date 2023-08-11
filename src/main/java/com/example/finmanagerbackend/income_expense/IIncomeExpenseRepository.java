package com.example.finmanagerbackend.income_expense;


import jdk.dynalink.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IIncomeExpenseRepository extends JpaRepository<IncomeExpense, Long>, JpaSpecificationExecutor<IncomeExpense> {
    @Query("SELECT operation FROM IncomeExpense operation WHERE MONTH(operation.date) = ?1")
    List<IncomeExpense> findOperationByMonth(int month);

    @Query("SELECT operation FROM IncomeExpense operation WHERE YEAR(operation.date) = ?1")
    List<IncomeExpense> findOperationByYear(int year);

    @Query("SELECT operation FROM IncomeExpense operation WHERE MONTH(operation.date) = ?2 AND YEAR(operation.date) = ?1")
    List<IncomeExpense> findOperationByYearAndMonth(int year, int month);

    @Query("SELECT operation FROM IncomeExpense operation " +
            "WHERE (:yearParam IS NULL OR YEAR(operation.date) = :yearParam) " +
            "AND (:monthParam IS NULL OR MONTH(operation.date) = :monthParam)" +
            "AND (:operationTypeParam IS NULL OR operation.operationType = :operationTypeParam)"
    )
    List<IncomeExpense> findOperationsByCriteria(@Param("yearParam") Integer year,
                                                 @Param("monthParam") Integer month,
                                                 @Param("operationTypeParam") OperationType operationType);
}
