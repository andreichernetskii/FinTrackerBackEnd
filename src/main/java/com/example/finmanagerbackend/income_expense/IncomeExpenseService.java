package com.example.finmanagerbackend.income_expense;

import com.example.finmanagerbackend.account.Account;
import com.example.finmanagerbackend.global.exceptions.NotFoundException;
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


        IncomeExpense incomeExpense = new IncomeExpense(
                incomeExpenseDTO.getOperationType(),
                amount,
                incomeExpenseDTO.getCategory(),
                LocalDate.parse( incomeExpenseDTO.getDate() ) );

        Account currentAccount = getCurrentAccount();
        currentAccount.addIncome(incomeExpense);
        accountService.save(currentAccount);

        //incomeExpense.setAccount( currentAccount );

       // incomeExpenseRepository.save( incomeExpense );
    }

    // todo: ważne!
    private Account getCurrentAccount(){
        //wyciagnie z AccountService, który wyciągnie z AuthService, który wyciągnie z security
        return null;
    }

    public List<IncomeExpense> getOperations() {
        return incomeExpenseRepository.findAll();
    }

    public void deleteIncomeExpense( Long operationId ) {
        boolean exists = incomeExpenseRepository.existsById( operationId );
        if ( !exists ) {
            throw new NotFoundException( "Operations with id " + operationId + " is not exists!" );
        }
        incomeExpenseRepository.deleteById( operationId );
    }

    public Double getAnnualBalance( Integer year, Integer month, OperationType operationType, String category ) {
        return incomeExpenseRepository.calculateAnnualBalanceByCriteria( year, month, operationType, category );
    }

    public void updateIncomeExpense( IncomeExpense incomeExpense ) {
        Optional<IncomeExpense> incomeExpenseOptional = incomeExpenseRepository.findById( incomeExpense.getId() );
        if ( !incomeExpenseOptional.isPresent() ) {
            throw new NotFoundException( "Operacji z id " + incomeExpense.getId() + " nie istnieje w bazie!" );
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
