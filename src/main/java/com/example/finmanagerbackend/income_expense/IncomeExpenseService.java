package com.example.finmanagerbackend.income_expense;

import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

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
        boolean exists =IIncomeExpenseRepository.existsById(operationId);
        if (!exists) {
            throw new IllegalStateException("Operations with id " + operationId + " is not exists!");
        }
        IIncomeExpenseRepository.deleteById(operationId);
    }

    // todo: dorobić
    public Map<String, BigDecimal> getAnnualBalance() {
        return new HashMap<String, BigDecimal>();
    }

    public void updateIncomeExpense(IncomeExpense incomeExpense) {
        Optional<IncomeExpense> incomeExpenseOptional = IIncomeExpenseRepository.findById(incomeExpense.getId());
        if (!incomeExpenseOptional.isPresent()) {
            throw new IllegalStateException("Operacji z id " + incomeExpense.getId() + " nie istnieje w bazie!");
        }
        IIncomeExpenseRepository.save(incomeExpense);
    }
}
