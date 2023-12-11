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

    public void addIncomeExpense( Account account, IncomeExpenseDTO incomeExpenseDTO ) {
        BigDecimal amount = ( incomeExpenseDTO.getOperationType() == OperationType.EXPENSE )
                ? incomeExpenseDTO.getAmount().negate()
                : incomeExpenseDTO.getAmount();


        IncomeExpense incomeExpense = new IncomeExpense(
                incomeExpenseDTO.getOperationType(),
                amount,
                incomeExpenseDTO.getCategory(),
                LocalDate.parse( incomeExpenseDTO.getDate() ) );

        incomeExpense.setAccount( account );
        incomeExpenseRepository.save( incomeExpense );
    }

    public List<IncomeExpense> getOperations() {
        return incomeExpenseRepository.findAll();
    }

    public void updateIncomeExpense( Account account, IncomeExpense incomeExpense ) {
        Optional<IncomeExpense> incomeExpenseOptional =
                incomeExpenseRepository.findByAccountIdPlusOperationId( incomeExpense.getId(), account.getId() );

        if ( !incomeExpenseOptional.isPresent() ) {
            throw new NotFoundException( "Operacji z id " + incomeExpense.getId() + " nie istnieje w bazie!" );
        }

        incomeExpense.setAccount( account ); // todo: inne jakieś rozwiązanie, żeby zabronić zmianę account'a
        incomeExpenseRepository.save( incomeExpense );
    }

    public void deleteIncomeExpense( Account account,
                                     Long operationId ) {

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

        return incomeExpenseRepository.calculateAnnualBalanceByCriteria( year, month, operationType, category );
    }

    public List<IncomeExpense> getOperationsByCriteria( Account account,
                                                        Integer year,
                                                        Integer month,
                                                        OperationType operationType,
                                                        String category ) {

        List<IncomeExpense> list = incomeExpenseRepository.findOperationsByCriteria(
                account.getId(),
                year,
                month,
                operationType,
                category );

        return list;
    }

    public List<String> getCategories() {
        List<String> categories = incomeExpenseRepository.getCategories();
        return categories;
    }
}
