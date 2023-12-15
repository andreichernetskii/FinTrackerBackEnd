package com.example.finmanagerbackend.income_expense;

import com.example.finmanagerbackend.account.Account;
import com.example.finmanagerbackend.account.AccountService;
import com.example.finmanagerbackend.global.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class IncomeExpenseService {
    private final IncomeExpenseRepository incomeExpenseRepository;
    private final AccountService accountService;

    public IncomeExpenseService( IncomeExpenseRepository incomeExpenseRepository, AccountService accountService ) {
        this.incomeExpenseRepository = incomeExpenseRepository;
        this.accountService = accountService;
    }

    public void addIncomeExpense( IncomeExpenseDTO incomeExpenseDTO ) {
        BigDecimal amount = ( incomeExpenseDTO.getOperationType() == OperationType.EXPENSE )
                ? incomeExpenseDTO.getAmount().negate()
                : incomeExpenseDTO.getAmount();


        IncomeExpense incomeExpense = new IncomeExpense(
                incomeExpenseDTO.getOperationType(),
                amount,
                incomeExpenseDTO.getCategory(),
                LocalDate.parse( incomeExpenseDTO.getDate() ) );

        Account account = accountService.getAccount();
        incomeExpense.setAccount( account );
        incomeExpenseRepository.save( incomeExpense );
    }

    public List<IncomeExpense> getOperations() {
        return incomeExpenseRepository.findAll();
    }

    public void updateIncomeExpense( IncomeExpense incomeExpense ) {
        Account account = accountService.getAccount();

        Optional<IncomeExpense> incomeExpenseOptional =
                incomeExpenseRepository.findByAccountIdPlusOperationId( incomeExpense.getId(), account.getId() );

        if ( !incomeExpenseOptional.isPresent() ) {
            throw new NotFoundException( "Operacji z id " + incomeExpense.getId() + " nie istnieje w bazie!" );
        }

        incomeExpense.setAccount( account );
        incomeExpenseRepository.save( incomeExpense );
    }

    public void deleteIncomeExpense( Long operationId ) {
        Account account = accountService.getAccount();

        Optional<IncomeExpense> incomeExpenseOptional =
                incomeExpenseRepository.findByAccountIdPlusOperationId( operationId, account.getId() );

        if ( !incomeExpenseOptional.isPresent() ) {
            throw new NotFoundException( "Operations with id " + operationId + " is not exists!" );
        }

        incomeExpenseRepository.deleteById( operationId );
    }

    public Double getAnnualBalance( Integer year,
                                    Integer month,
                                    OperationType operationType,
                                    String category ) {

        Account account = accountService.getAccount();
        return incomeExpenseRepository.calculateAnnualBalanceByCriteria( account.getId(), year, month, operationType, category );
    }

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

    public List<String> getCategories() {
        Account account = accountService.getAccount();
        return incomeExpenseRepository.getCategories( account.getId() );
    }
}
