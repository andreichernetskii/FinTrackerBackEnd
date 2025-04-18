package com.example.finmanagerbackend.financial_transaction;

import com.example.finmanagerbackend.account.Account;
import com.example.finmanagerbackend.account.AccountService;
import com.example.finmanagerbackend.global.annotations.SendAlerts;
import com.example.finmanagerbackend.global.annotations.SendTransactions;
import com.example.finmanagerbackend.global.exceptions.NotFoundException;
import com.example.finmanagerbackend.security.application_user.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing financial transactions.
 */

@RequiredArgsConstructor
@Service
public class FinancialTransactionService {

    private final FinancialTransactionRepository financialTransactionRepository;
    private final AccountService accountService;
    private final FinTransactionGenerator finTransactionGenerator;

    // Method to add a new financial transaction based on DTO information.
    // todo: service shouldn't send a some response entities
    @SendTransactions
    @SendAlerts
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
    public List<FinancialTransaction> getAllTransactionsOfAccount() {

        return financialTransactionRepository.findAllTransactionsOfAccount(accountService.getAccount().getId());
    }

    // Method to update an existing financial transaction.
    // todo: service shouldn't send a some response entities
    @SendTransactions
    public ResponseEntity<?> updateFinancialTransaction( FinancialTransaction financialTransaction ) {

        Account account = accountService.getAccount();

        Optional<FinancialTransaction> incomeExpenseOptional =
                financialTransactionRepository.findByAccountIdPlusOperationId( financialTransaction.getId(), account.getId() );

        if (incomeExpenseOptional.isEmpty()) {
            throw new NotFoundException( "Operation with ID: " + financialTransaction.getId() + " does not exist in the database!" );
        }

        financialTransaction.setAccount( account );
        financialTransactionRepository.save( financialTransaction );

        return ResponseEntity.ok( new MessageResponse( "Financial transaction successfully updated" ) );
    }

    // todo: service shouldn't send a some response entities
    // Method to delete a financial transaction by its ID.
    @SendTransactions
    public ResponseEntity<?> deleteFinancialTransaction( Long operationId ) {

        Account account = accountService.getAccount();

        Optional<FinancialTransaction> incomeExpenseOptional =
                financialTransactionRepository.findByAccountIdPlusOperationId( operationId, account.getId() );

        if ( incomeExpenseOptional.isEmpty() ) {
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

        return financialTransactionRepository
                .calculateAnnualBalanceByCriteria(
                        accountService.getAccount().getId(),
                        year,
                        month,
                        financialTransactionType,
                        category );
    }

    // Method to retrieve financial transactions based on specified criteria.
    public List<FinancialTransaction> getOperationsByCriteria( Integer year,
                                                               Integer month,
                                                               FinancialTransactionType financialTransactionType,
                                                               String category ) {

        Account account = accountService.getAccount();

        // generate table of random transactions for showcase if user is demo and his transactions table is empty
        if ( account.isDemo() && financialTransactionRepository.countByAccountId( account.getId() ) == 0 ) {
            finTransactionGenerator.createRandomExpenses( account );
        }

        return financialTransactionRepository
                .findOperationsByCriteria(
                        account.getId(),
                        year,
                        month,
                        financialTransactionType,
                        category );
    }

    public List<String> getCategories() {

        return financialTransactionRepository.getCategories( accountService.getAccount().getId() );
    }

    public List<String> getTransactionTypes() {

        return Arrays.stream( FinancialTransactionType.values() )
                .map( Enum::toString )
                .toList();
    }

    public Double getYearExpenses(LocalDate now) {
        return financialTransactionRepository.calculateYearExpenses(accountService.getAccount().getId(), now);
    }

    public Double getMonthExpenses(LocalDate now) {
        return financialTransactionRepository.calculateMonthExpenses(accountService.getAccount().getId(), now);
    }

    public Double getWeekExpenses(LocalDate firstWeekDay, LocalDate lastWeekDay) {
        return financialTransactionRepository.calculateWeekExpenses(accountService.getAccount().getId(), firstWeekDay, lastWeekDay);
    }

    public Double getDayExpenses(LocalDate now) {
        return financialTransactionRepository.calculateDayExpenses(accountService.getAccount().getId(), now);
    }

    public List<Integer> getYears() {
        return financialTransactionRepository.findDistinctYearsByAccountId(accountService.getAccount().getId());
    }

    public List<Integer> getMonths() {
        return financialTransactionRepository.findDistinctMonthsByAccountIdAllTime(accountService.getAccount().getId());
    }
}
