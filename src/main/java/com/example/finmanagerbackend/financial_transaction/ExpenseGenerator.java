package com.example.finmanagerbackend.financial_transaction;

import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Service for generating a table with random financial statistics.
 */
//@Service
public class ExpenseGenerator {
    private final FinancialTransactionRepository financialTransactionRepository;
    private static final Faker faker = new Faker();

    public ExpenseGenerator( FinancialTransactionRepository financialTransactionRepository ) {
        this.financialTransactionRepository = financialTransactionRepository;
    }

    // PostConstruct method to create random expenses if the repository is empty.
    @PostConstruct
    public void createRandomExpenses() {
        if ( financialTransactionRepository.count() == 0 ) {
            financialTransactionRepository.saveAll( generateExpenses( 34 ) );
        }
    }

    // Method to generate a list of random expenses based on the specified count.
    public List<FinancialTransaction> generateExpenses( int count ) {

        List<FinancialTransaction> expenses = new ArrayList<>();

        // Generate 'count' number of random expenses
        for ( int i = 0; i < count; i++ ) {
            FinancialTransaction financialTransaction = new FinancialTransaction();
            // Set a random category using the commerce department from the Faker library
            financialTransaction.setCategory( faker.commerce().department() );
            // Set the operation type to EXPENSE
            financialTransaction.setOperationType( FinancialTransactionType.EXPENSE );
            // Set the amount to a random price multiplied by 3 for higher income
            financialTransaction.setAmount( new BigDecimal( faker.commerce().price() ).multiply( new BigDecimal( 3 ) ) );
            // Set the date to a random past date within the last 60 days
            financialTransaction.setDate( faker.date().past( 60, TimeUnit.DAYS ).toInstant()
                    .atZone( ZoneId.systemDefault() )
                    .toLocalDate() );
            // Add the generated expense to the list
            expenses.add( financialTransaction );
        }

        return expenses;
    }
}
