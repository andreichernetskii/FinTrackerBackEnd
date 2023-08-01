package com.example.finmanagerbackend.income_expense;
import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ExpenseGenerator {
    private IIncomeExpenseRepository iIncomeExpenseRepository;
    private static final Faker faker = new Faker();

    public ExpenseGenerator(IIncomeExpenseRepository iIncomeExpenseRepository) {
        this.iIncomeExpenseRepository = iIncomeExpenseRepository;
    }

    @PostConstruct
    public void createRandomExpenses(){
        if (iIncomeExpenseRepository.count() == 0) {
            iIncomeExpenseRepository.saveAll(generateExpenses(34));
        }
    }

    public List<IncomeExpense> generateExpenses(int count) { // todo: wybalansować, żeby były incomy i expens'y
        List<IncomeExpense> expenses = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            IncomeExpense incomeExpense = new IncomeExpense();
            incomeExpense.setCategory(faker.commerce().department());
            incomeExpense.setOperationType(OperationType.EXPENSE);
            incomeExpense.setAmount(new BigDecimal(faker.commerce().price()).multiply(new BigDecimal(3))); // income większy
            incomeExpense.setDate(faker.date().past(60, TimeUnit.DAYS).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
            expenses.add(incomeExpense);
        }
        return expenses;
    }
}
