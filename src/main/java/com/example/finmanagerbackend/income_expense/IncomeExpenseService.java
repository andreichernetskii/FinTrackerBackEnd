package com.example.finmanagerbackend.income_expense;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class IncomeExpenseService {
    private final IIncomeExpenseRepository iIncomeExpenseRepository;

    public IncomeExpenseService(IIncomeExpenseRepository iIncomeExpenseRepository) {
        this.iIncomeExpenseRepository = iIncomeExpenseRepository;
    }

    public void addIncomeExpense(IncomeExpenseDTO incomeExpenseDTO) {
        iIncomeExpenseRepository.save(new IncomeExpense(
                incomeExpenseDTO.getOperationType(),
                incomeExpenseDTO.getAmount(),
                incomeExpenseDTO.getCategory(),
                LocalDate.parse(incomeExpenseDTO.getDate()))
        );
    }

    public List<IncomeExpense> getOperations() {
        return iIncomeExpenseRepository.findAll();
    }

    public List<IncomeExpense> getOperationsYearAndMonth(int year, int month) {
        return iIncomeExpenseRepository.findOperationByYearAndMonth(year, month);
    }

    public List<IncomeExpense> getOperationsMonth(int month) {
        return iIncomeExpenseRepository.findOperationByMonth(month);
    }

    public List<IncomeExpense> getOperationsYear(int year) {
        return iIncomeExpenseRepository.findOperationByYear(year);
    }

    public void deleteIncomeExpense(Long operationId) {
        boolean exists = iIncomeExpenseRepository.existsById(operationId);
        if (!exists) {
            throw new IllegalStateException("Operations with id " + operationId + " is not exists!");
        }
        iIncomeExpenseRepository.deleteById(operationId);
    }

    // todo: dorobić
    public Map<String, BigDecimal> getAnnualBalance() {
        return new HashMap<String, BigDecimal>();
    }

    public void updateIncomeExpense(IncomeExpense incomeExpense) {
        Optional<IncomeExpense> incomeExpenseOptional = iIncomeExpenseRepository.findById(incomeExpense.getId());
        if (!incomeExpenseOptional.isPresent()) {
            throw new IllegalStateException("Operacji z id " + incomeExpense.getId() + " nie istnieje w bazie!");
        }
        iIncomeExpenseRepository.save(incomeExpense);
    }



}
