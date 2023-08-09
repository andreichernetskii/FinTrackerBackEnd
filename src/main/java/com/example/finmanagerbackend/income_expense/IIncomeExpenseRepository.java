package com.example.finmanagerbackend.income_expense;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IIncomeExpenseRepository extends JpaRepository<IncomeExpense, Long> {
    @Query("SELECT operation FROM IncomeExpense operation WHERE MONTH(operation.date) = ?1")
    List<IncomeExpense> findOperationByMonth(int month);

    @Query("SELECT operation FROM IncomeExpense operation WHERE YEAR(operation.date) = ?1")
    List<IncomeExpense> findOperationByYear(int year);

    @Query("SELECT operation FROM IncomeExpense operation WHERE MONTH(operation.date) = ?2 AND YEAR(operation.date) = ?1")
    List<IncomeExpense> findOperationByYearAndMonth(int year, int month);
}
