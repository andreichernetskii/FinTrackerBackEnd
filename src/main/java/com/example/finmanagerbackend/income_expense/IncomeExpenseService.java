package com.example.finmanagerbackend.income_expense;

import com.example.finmanagerbackend.account.Account;
import com.example.finmanagerbackend.account.AccountService;
import com.example.finmanagerbackend.global.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * Service class for managing financial transactions.
 */
@Service
public class IncomeExpenseService {
    private final IncomeExpenseRepository incomeExpenseRepository;
    private final AccountService accountService;

    public IncomeExpenseService( IncomeExpenseRepository incomeExpenseRepository, AccountService accountService ) {
        this.incomeExpenseRepository = incomeExpenseRepository;
        this.accountService = accountService;
    }

    // Method to add a new financial transaction based on DTO information.
    public void addIncomeExpense( IncomeExpenseDTO incomeExpenseDTO ) {

        // Adjust the amount based on the operation type (expense or income)
        BigDecimal amount = ( incomeExpenseDTO.getOperationType() == OperationType.EXPENSE )
                ? incomeExpenseDTO.getAmount().negate()
                : incomeExpenseDTO.getAmount();

        IncomeExpense incomeExpense = new IncomeExpense(
                incomeExpenseDTO.getOperationType(),
                amount,
                incomeExpenseDTO.getCategory(),
                LocalDate.parse( incomeExpenseDTO.getDate() ) );

        // Get the current account and set it for the financial transaction
        Account account = accountService.getAccount();
        incomeExpense.setAccount( account );

        incomeExpenseRepository.save( incomeExpense );
    }

    // Method to retrieve all financial transactions.
    public List<IncomeExpense> getOperations() {
        return incomeExpenseRepository.findAll();
    }

    // Method to update an existing financial transaction.
    public void updateIncomeExpense( IncomeExpense incomeExpense ) {
        Account account = accountService.getAccount();

        Optional<IncomeExpense> incomeExpenseOptional =
                incomeExpenseRepository.findByAccountIdPlusOperationId( incomeExpense.getId(), account.getId() );

        if ( !incomeExpenseOptional.isPresent() ) {
            throw new NotFoundException( "Operation with ID: " + incomeExpense.getId() + " does not exist in the database!" );
        }

        incomeExpense.setAccount( account );
        incomeExpenseRepository.save( incomeExpense );
    }

    // Method to delete a financial transaction by its ID.
    public void deleteIncomeExpense( Long operationId ) {
        Account account = accountService.getAccount();

        Optional<IncomeExpense> incomeExpenseOptional =
                incomeExpenseRepository.findByAccountIdPlusOperationId( operationId, account.getId() );

        if ( !incomeExpenseOptional.isPresent() ) {
            throw new NotFoundException( "Operation with ID " + operationId + " does not exist in the database!" );
        }

        incomeExpenseRepository.deleteById( operationId );
    }

    // Method to get the annual balance based on specified criteria.
    public Double getAnnualBalance( Integer year,
                                    Integer month,
                                    OperationType operationType,
                                    String category ) {

        Account account = accountService.getAccount();
        return incomeExpenseRepository.calculateAnnualBalanceByCriteria( account.getId(), year, month, operationType, category );
    }

    // Method to retrieve financial transactions based on specified criteria.
    public List<IncomeExpense> getOperationsByCriteria( Integer year,
                                                        Integer month,
                                                        OperationType operationType,
                                                        String category ) {

        List<IncomeExpense> list = incomeExpenseRepository.findOperationsByCriteria(
                accountService.getAccount().getId(),
                year,
                month,
                operationType,
                category );

        return list;
    }

    // Method to retrieve a list of categories for the current account.
    public List<String> getCategories() {
        Account account = accountService.getAccount();
        return incomeExpenseRepository.getCategories( account.getId() );
    }
}
