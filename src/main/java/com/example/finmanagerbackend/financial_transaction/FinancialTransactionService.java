package com.example.finmanagerbackend.financial_transaction;

import com.example.finmanagerbackend.account.Account;
import com.example.finmanagerbackend.account.AccountService;
import com.example.finmanagerbackend.global.annotations.SendAlerts;
import com.example.finmanagerbackend.global.annotations.SendTransactions;
import com.example.finmanagerbackend.global.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
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
    @SendTransactions
    @SendAlerts
    public FinancialTransactionDTO addFinancialTransaction( FinancialTransactionDTO financialTransactionDTO ) {

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

        FinancialTransaction savedTransaction = financialTransactionRepository.save( financialTransaction );

        return FinancialTransactionDTO.builder()
                .id(savedTransaction.getId())
                .amount(savedTransaction.getAmount())
                .category(savedTransaction.getCategory())
                .financialTransactionType(savedTransaction.getFinancialTransactionType())
                .date(savedTransaction.getDate().toString())
                .build();
    }

    // Method to retrieve all financial transactions.
    public List<FinancialTransaction> getAllTransactionsOfAccount() {

        return financialTransactionRepository.findAllTransactionsOfAccount(accountService.getAccount().getId());
    }

    // Method to update an existing financial transaction.
    @SendTransactions
    public FinancialTransactionDTO updateFinancialTransaction( Long transactionId, FinancialTransactionDTO dto ) {

        Account account = accountService.getAccount();

        FinancialTransaction existingTransaction =
                financialTransactionRepository
                        .findByAccountIdPlusOperationId( transactionId, account.getId() )
                        .orElseThrow(() -> new NotFoundException( "Operation with ID: " + transactionId + " does not exist in the database!" ));

        existingTransaction.setFinancialTransactionType(dto.getFinancialTransactionType());
        existingTransaction.setAmount(dto.getAmount());
        existingTransaction.setDate(LocalDate.parse(dto.getDate()));
        existingTransaction.setCategory(dto.getCategory());

        FinancialTransaction savedTransaction =  financialTransactionRepository.save( existingTransaction );

        return FinancialTransactionDTO.builder()
                .id(savedTransaction.getId())
                .amount(savedTransaction.getAmount())
                .category(savedTransaction.getCategory())
                .financialTransactionType(savedTransaction.getFinancialTransactionType())
                .date(savedTransaction.getDate().toString())
                .build();
    }

    // Method to delete a financial transaction by its ID.
    @SendTransactions
    public void deleteFinancialTransaction( Long operationId ) {

        Account account = accountService.getAccount();

        Optional<FinancialTransaction> incomeExpenseOptional =
                financialTransactionRepository.findByAccountIdPlusOperationId( operationId, account.getId() );

        if ( incomeExpenseOptional.isEmpty() ) {
            throw new NotFoundException( "Operation with ID " + operationId + " does not exist in the database!" );
        }

        financialTransactionRepository.deleteById( operationId );
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
    public List<FinancialTransactionDTO> getOperationsByCriteria( Integer year,
                                                               Integer month,
                                                               FinancialTransactionType financialTransactionType,
                                                               String category ) {

        Account account = accountService.getAccount();

        // generate table of random transactions for showcase if user is demo and his transactions table is empty
        if ( account.isDemo() && financialTransactionRepository.countByAccountId( account.getId() ) == 0 ) {
            finTransactionGenerator.createRandomExpenses( account );
        }

        List<FinancialTransaction> listEntity =  financialTransactionRepository
                .findOperationsByCriteria(
                        account.getId(),
                        year,
                        month,
                        financialTransactionType,
                        category );

        return listEntity.stream()
                .map(entity -> FinancialTransactionDTO.builder()
                        .id(entity.getId())
                        .amount(entity.getAmount())
                        .financialTransactionType(entity.getFinancialTransactionType())
                        .category(entity.getCategory())
                        .date(entity.getDate().toString())
                        .build())
                .toList();
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
