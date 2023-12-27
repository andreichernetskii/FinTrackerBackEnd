package com.example.finmanagerbackend.income_expense;

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
@Service
public class ExpenseGenerator {
    private final IncomeExpenseRepository incomeExpenseRepository;
    private static final Faker faker = new Faker();

    public ExpenseGenerator( IncomeExpenseRepository incomeExpenseRepository ) {
        this.incomeExpenseRepository = incomeExpenseRepository;
    }

    // PostConstruct method to create random expenses if the repository is empty.
    @PostConstruct
    public void createRandomExpenses() {
        if ( incomeExpenseRepository.count() == 0 ) {
            incomeExpenseRepository.saveAll( generateExpenses( 34 ) );
        }
    }

    // Method to generate a list of random expenses based on the specified count.
    public List<IncomeExpense> generateExpenses( int count ) {

        List<IncomeExpense> expenses = new ArrayList<>();

        // Generate 'count' number of random expenses
        for ( int i = 0; i < count; i++ ) {
            IncomeExpense incomeExpense = new IncomeExpense();
            // Set a random category using the commerce department from the Faker library
            incomeExpense.setCategory( faker.commerce().department() );
            // Set the operation type to EXPENSE
            incomeExpense.setOperationType( OperationType.EXPENSE );
            // Set the amount to a random price multiplied by 3 for higher income
            incomeExpense.setAmount( new BigDecimal( faker.commerce().price() ).multiply( new BigDecimal( 3 ) ) );
            // Set the date to a random past date within the last 60 days
            incomeExpense.setDate( faker.date().past( 60, TimeUnit.DAYS ).toInstant()
                    .atZone( ZoneId.systemDefault() )
                    .toLocalDate() );
            // Add the generated expense to the list
            expenses.add( incomeExpense );
        }

        return expenses;
    }
}
