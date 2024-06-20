package com.example.finmanagerbackend.financial_transaction;

import com.example.finmanagerbackend.account.Account;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Service for generating a table with random financial statistics.
 */

@Service
public class FinTransactionGenerator {
    private final FinancialTransactionRepository financialTransactionRepository;
    private static final Faker faker = new Faker();
    private Random random = new Random();

    public FinTransactionGenerator( FinancialTransactionRepository financialTransactionRepository ) {
        this.financialTransactionRepository = financialTransactionRepository;
    }

    // PostConstruct method to create random expenses if the repository is empty.
//    @PostConstruct
    public void createRandomExpenses( Account account) {
            financialTransactionRepository.saveAll( generateTransactions( 34, account ) );
    }

    // Method to generate a list of random expenses based on the specified count.
    public List<FinancialTransaction> generateTransactions( int count, Account account ) {

        List<FinancialTransaction> expenses = new ArrayList<>();

        // Generate 'count' number of random expenses
        for ( int i = 0; i < count; i++ ) {
            FinancialTransaction financialTransaction = new FinancialTransaction();
            // Set a random category using the commerce department from the Faker library
            financialTransaction.setCategory( faker.commerce().department().split( "," )[0].split( " " )[0] );
            // Set the operation type to EXPENSE
            financialTransaction.setFinancialTransactionType(
                    random.nextBoolean() ? FinancialTransactionType.EXPENSE : FinancialTransactionType.INCOME
            );
            // Set the amount to a random price multiplied by 3 for higher income
            financialTransaction.setAmount(
                    new BigDecimal( faker.commerce().price().replace( ",", "." ) )
                            .multiply( new BigDecimal( 3 ) )
            );
            // Set the date to a random past date within the last 60 days
            financialTransaction.setDate(
                    faker.date()
                            .past( 60, TimeUnit.DAYS )
                            .toInstant()
                            .atZone( ZoneId.systemDefault() )
                            .toLocalDate()
            );
            financialTransaction.setAccount( account );
            // Add the generated expense to the list
            expenses.add( financialTransaction );
        }
        return expenses;
    }
}
