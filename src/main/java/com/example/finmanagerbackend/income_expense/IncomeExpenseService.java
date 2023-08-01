package com.example.finmanagerbackend.income_expense;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IncomeExpenseService {
    private final IIncomeExpenseRepository IIncomeExpenseRepository;

    public IncomeExpenseService(IIncomeExpenseRepository IIncomeExpenseRepository) {
        this.IIncomeExpenseRepository = IIncomeExpenseRepository;
    }

    public void addIncomeExpense(IncomeExpenseDTO incomeExpenseDTO) {
        IIncomeExpenseRepository.save(new IncomeExpense(
                incomeExpenseDTO.getOperationType(),
                incomeExpenseDTO.getAmount(),
                incomeExpenseDTO.getCategory(),
                LocalDate.parse(incomeExpenseDTO.getDate()))
        );
    }

    public List<IncomeExpense> getOperations() {
        return IIncomeExpenseRepository.findAll();
    }

    public void deleteIncomeExpense(Long operationId) {
        System.out.println("Deleting");
        boolean exists =IIncomeExpenseRepository.existsById(operationId);
        if (!exists) {
            throw new IllegalStateException("Operations with id " + operationId + " is not exists!");
        }
        IIncomeExpenseRepository.deleteById(operationId);
    }

    public Map<String, BigDecimal> getAnnualBalance() {
        return new HashMap<String, BigDecimal>();
    }
}
