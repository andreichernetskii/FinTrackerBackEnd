package com.example.finmanagerbackend.income_expense;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    // todo: dorobić
    public Map<String, BigDecimal> getAnnualBalance() {
        return new HashMap<String, BigDecimal>();
    }

    public void updateIncomeExpense(Long id, String date, BigDecimal amount, String category) {
        IncomeExpense incomeExpense = IIncomeExpenseRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Operacji z id " + id + " nie istnieje w bazie!"));

        if (date != null && date.length() > 0 && !Objects.equals(incomeExpense.getDate(), date)) {
            incomeExpense.setDate(LocalDate.parse(date));
        }

        if (amount != null && !amount.equals(new BigDecimal(0)) && !Objects.equals(incomeExpense.getAmount(), amount)) {
            incomeExpense.setAmount(amount);
        }

        if (category != null && category.length() > 0 && !Objects.equals(incomeExpense.getCategory(), category)) {
            incomeExpense.setCategory(category);
        }
    }
}
