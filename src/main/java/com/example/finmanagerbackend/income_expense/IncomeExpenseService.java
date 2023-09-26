package com.example.finmanagerbackend.income_expense;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class IncomeExpenseService {
    private final IncomeExpenseRepository incomeExpenseRepository;

    public IncomeExpenseService( IncomeExpenseRepository incomeExpenseRepository ) {
        this.incomeExpenseRepository = incomeExpenseRepository;
    }

    public void addIncomeExpense( IncomeExpenseDTO incomeExpenseDTO ) {
        BigDecimal amount = ( incomeExpenseDTO.getOperationType() == OperationType.EXPENSE )
                ? incomeExpenseDTO.getAmount().negate()
                : incomeExpenseDTO.getAmount();

        incomeExpenseRepository.save( new IncomeExpense(
                incomeExpenseDTO.getOperationType(),
                amount,
                incomeExpenseDTO.getCategory(),
                LocalDate.parse( incomeExpenseDTO.getDate() ) )
        );
    }

    public List<IncomeExpense> getOperations() {
        return incomeExpenseRepository.findAll();
    }

    public void deleteIncomeExpense( Long operationId ) {
        boolean exists = incomeExpenseRepository.existsById( operationId );
        if ( !exists ) {
            throw new IllegalStateException( "Operations with id " + operationId + " is not exists!" );
        }
        incomeExpenseRepository.deleteById( operationId );
    }

    public Double getAnnualBalance( Integer year, Integer month, OperationType operationType, String category ) {
        return incomeExpenseRepository.calculateAnnualBalanceByCriteria( year, month, operationType, category );
    }

    public void updateIncomeExpense( IncomeExpense incomeExpense ) {
        Optional<IncomeExpense> incomeExpenseOptional = incomeExpenseRepository.findById( incomeExpense.getId() );
        if ( !incomeExpenseOptional.isPresent() ) {
            throw new IllegalStateException( "Operacji z id " + incomeExpense.getId() + " nie istnieje w bazie!" );
        }
        incomeExpenseRepository.save( incomeExpense );
    }

    public List<IncomeExpense> getOperationsByCriteria( Integer year, Integer month, OperationType operationType, String category ) {
        List<IncomeExpense> list = incomeExpenseRepository.findOperationsByCriteria( year, month, operationType, category );
        return list;
    }

    public List<String> getCategories() {
        List<String> categories = incomeExpenseRepository.getCategories();
        return categories;
    }
}
