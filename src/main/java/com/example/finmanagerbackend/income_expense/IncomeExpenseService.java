package com.example.finmanagerbackend.income_expense;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomeExpenseService {
    private final IIncomeExpenseRepository IIncomeExpenseRepository;

    public IncomeExpenseService(IIncomeExpenseRepository IIncomeExpenseRepository) {
        this.IIncomeExpenseRepository = IIncomeExpenseRepository;
    }

    public void addIncomeExpense(IncomeExpenseDTO incomeExpenseDTO) {
        IIncomeExpenseRepository.save(new IncomeExpenseManager(
                incomeExpenseDTO.getOperationType(),
                (incomeExpenseDTO.getOperationType() == OperationType.INCOME) ? incomeExpenseDTO.getAmount() : incomeExpenseDTO.getAmount().negate(),
                incomeExpenseDTO.getCategory(),
                incomeExpenseDTO.getDate())
        );
    }

    public List<IncomeExpenseManager> getOperations() {
        System.out.println("Request delivered. Processing...");
        return IIncomeExpenseRepository.findAll();
    }
}
