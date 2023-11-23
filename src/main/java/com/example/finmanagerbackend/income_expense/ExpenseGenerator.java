package com.example.finmanagerbackend.income_expense;

import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

// for generation a table with random financial statistics
@Service
public class ExpenseGenerator {
    private final IncomeExpenseRepository incomeExpenseRepository;
    private static final Faker faker = new Faker();

    public ExpenseGenerator( IncomeExpenseRepository incomeExpenseRepository ) {
        this.incomeExpenseRepository = incomeExpenseRepository;
    }

    @PostConstruct
    public void createRandomExpenses() {
        if ( incomeExpenseRepository.count() == 0 ) {
            incomeExpenseRepository.saveAll( generateExpenses( 34 ) );
        }
    }

    public List<IncomeExpense> generateExpenses( int count ) {
        List<IncomeExpense> expenses = new ArrayList<>();

        for ( int i = 0; i < count; i++ ) {
            IncomeExpense incomeExpense = new IncomeExpense();
            incomeExpense.setCategory( faker.commerce().department() );
            incomeExpense.setOperationType( OperationType.EXPENSE );
            incomeExpense.setAmount( new BigDecimal( faker.commerce().price() ).multiply( new BigDecimal( 3 ) ) ); // income większy
            incomeExpense.setDate( faker.date().past( 60, TimeUnit.DAYS ).toInstant()
                    .atZone( ZoneId.systemDefault() )
                    .toLocalDate() );
            expenses.add( incomeExpense );
        }

        return expenses;
    }
}
