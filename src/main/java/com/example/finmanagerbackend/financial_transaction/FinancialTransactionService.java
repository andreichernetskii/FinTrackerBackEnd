package com.example.finmanagerbackend.financial_transaction;

import com.example.finmanagerbackend.account.Account;
import com.example.finmanagerbackend.account.AccountService;
import com.example.finmanagerbackend.dto.FilterParameters;
import com.example.finmanagerbackend.global.exceptions.NotFoundException;
import com.example.finmanagerbackend.security.application_user.response.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Service class for managing financial transactions.
 */

// todo: maybe will be better to add id_account to the repo's method?
@Service
public class FinancialTransactionService {
    private final FinancialTransactionRepository financialTransactionRepository;
    private final AccountService accountService;
    private final FinTransactionGenerator finTransactionGenerator;

    public FinancialTransactionService( FinancialTransactionRepository financialTransactionRepository,
                                        AccountService accountService,
                                        FinTransactionGenerator finTransactionGenerator ) {
        this.financialTransactionRepository = financialTransactionRepository;
        this.accountService = accountService;
        this.finTransactionGenerator = finTransactionGenerator;
    }

    // Method to add a new financial transaction based on DTO information.

    public ResponseEntity<?> addFinancialTransaction( FinancialTransactionDTO financialTransactionDTO ) {

        // Adjust the amount based on the operation type (expense or income)
        BigDecimal amount =
                ( financialTransactionDTO.getFinancialTransactionType() == FinancialTransactionType.EXPENSE )
                        ? financialTransactionDTO.getAmount().negate()
                        : financialTransactionDTO.getAmount();

        FinancialTransaction financialTransaction = new FinancialTransaction(
                financialTransactionDTO.getFinancialTransactionType(),
                amount,
                financialTransactionDTO.getCategory(),
                LocalDate.parse( financialTransactionDTO.getDate() ) );

        // Get the current account and set it for the financial transaction
        Account account = accountService.getAccount();
        financialTransaction.setAccount( account );

        financialTransactionRepository.save( financialTransaction );

        return ResponseEntity.ok( new MessageResponse( "Financial transaction successfully added." ) );
    }

    // Method to retrieve all financial transactions.
    public List<FinancialTransaction> getOperations() {
        return financialTransactionRepository.findAll();
    }

    // Method to update an existing financial transaction.
    public ResponseEntity<?> updateFinancialTransaction( FinancialTransaction financialTransaction ) {
        Account account = accountService.getAccount();

        Optional<FinancialTransaction> incomeExpenseOptional =
                financialTransactionRepository.findByAccountIdPlusOperationId( financialTransaction.getId(), account.getId() );

        if ( !incomeExpenseOptional.isPresent() ) {
            throw new NotFoundException( "Operation with ID: " + financialTransaction.getId() + " does not exist in the database!" );
        }

        financialTransaction.setAccount( account );
        financialTransactionRepository.save( financialTransaction );

        return ResponseEntity.ok( new MessageResponse( "Financial transaction successfully updated" ) );
    }

    // Method to delete a financial transaction by its ID.
    public ResponseEntity<?> deleteFinancialTransaction( Long operationId ) {
        Account account = accountService.getAccount();

        Optional<FinancialTransaction> incomeExpenseOptional =
                financialTransactionRepository.findByAccountIdPlusOperationId( operationId, account.getId() );

        if ( !incomeExpenseOptional.isPresent() ) {
            throw new NotFoundException( "Operation with ID " + operationId + " does not exist in the database!" );
        }

        financialTransactionRepository.deleteById( operationId );

        return ResponseEntity.ok( new MessageResponse( "Financial transaction successfully deleted" ) );
    }

    // Method to get the annual balance based on specified criteria.
    public Double getAnnualBalance( Integer year,
                                    Integer month,
                                    FinancialTransactionType financialTransactionType,
                                    String category ) {

        Account account = accountService.getAccount();

        return financialTransactionRepository.calculateAnnualBalanceByCriteria( account.getId(), year, month, financialTransactionType, category );
    }

    public Double getAnnualBalance(FilterParameters filter) {

        return getAnnualBalance(filter, accountService.getAccount().getId()).join();
    }

    @Async
    public CompletableFuture<Double> getAnnualBalance(FilterParameters filter, Long accountId) {

        System.out.println("!!!!!!!!!!!!!!!getAnnualBalance method in " + Thread.currentThread());


        return CompletableFuture.completedFuture(financialTransactionRepository.calculateAnnualBalanceByCriteria(
                accountId,
                (isEmpty(filter.getYear())) ? null : Integer.parseInt(filter.getYear()),
                (isEmpty(filter.getMonth())) ? null : Integer.parseInt(filter.getMonth()),
                (isEmpty(filter.getFinancialTransactionType())) ? null : FinancialTransactionType.fromString(filter.getFinancialTransactionType()),
                (isEmpty(filter.getCategory())) ? null : filter.getCategory()
                ));
    }

    // Method to retrieve financial transactions based on specified criteria.
    @Async
    public CompletableFuture<List<FinancialTransaction>> getOperationsByCriteriaInner( Integer year,
                                                               Integer month,
                                                               FinancialTransactionType financialTransactionType,
                                                               String category,
                                                               Long accountId) {

        return CompletableFuture.completedFuture(financialTransactionRepository.findOperationsByCriteria(
                accountId,
                year,
                month,
                financialTransactionType,
                category ));
    }
    // Method to retrieve a list of categories for the current account.

    public List<FinancialTransaction> getOperationsByCriteria(Integer year,
                                                              Integer month,
                                                              FinancialTransactionType financialTransactionType,
                                                              String category) {

        Account account = accountService.getAccount();

        if ( account.isDemo() && financialTransactionRepository.countByAccountId( account.getId() ) == 0 ) {
            finTransactionGenerator.createRandomExpenses( account );
        }

        return getOperationsByCriteriaInner(year, month, financialTransactionType, category, account.getId()).join();
    }

    @Async
    public CompletableFuture<List<FinancialTransaction>> getOperationsByCriteria(FilterParameters filter, Long accountId) {

        System.out.println("!!!!!!!!!!!!!! getOperationsByCriteria method in " + Thread.currentThread());


        return CompletableFuture.completedFuture(financialTransactionRepository.findOperationsByCriteria(
                accountId,
                (isEmpty(filter.getYear())) ? null : Integer.parseInt(filter.getYear()),
                (isEmpty(filter.getMonth())) ? null : Integer.parseInt(filter.getMonth()),
                (isEmpty(filter.getFinancialTransactionType())) ? null : FinancialTransactionType.fromString(filter.getFinancialTransactionType()),
                (isEmpty(filter.getCategory())) ? null : filter.getCategory() ));

    }

    public List<String> getCategories() {

        return getCategories(accountService.getAccount().getId()).join();
    }


    @Async
    public CompletableFuture<List<String>> getCategories(Long accountId) {

        return CompletableFuture.completedFuture(financialTransactionRepository.getCategories( accountId ));
    }

    @Async
    public CompletableFuture<List<String>> getTransactionTypes() {

        return CompletableFuture.completedFuture(Arrays.stream( FinancialTransactionType.values() )
                .map( Enum::toString )
                .toList());
    }

    private boolean isEmpty(Object obj) {
        return obj == null || obj == "";
    }
}
