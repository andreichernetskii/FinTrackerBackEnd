package com.example.finmanagerbackend.financial_transaction;

import com.example.finmanagerbackend.account.Account;
import com.example.finmanagerbackend.account.AccountService;
import com.example.finmanagerbackend.global.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith( MockitoExtension.class )
public class FinancialTransactionServiceTest {

    @Mock
    private FinancialTransactionRepository financialTransactionRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private FinTransactionGenerator finTransactionGenerator;

    @InjectMocks
    private FinancialTransactionService financialTransactionService;

    private Account mockAccount;
    private FinancialTransactionDTO sampleFinancialTransactionDTO;
    private FinancialTransaction sampleTransaction;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {

        mockAccount = Account.builder()
                .id(1L)
                .isDemo(false)
                .build();

        sampleFinancialTransactionDTO = FinancialTransactionDTO.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(100.00))
                .category("Cars")
                .financialTransactionType(FinancialTransactionType.EXPENSE)
                .date(LocalDate.now().toString())
                .build();

        sampleTransaction = new FinancialTransaction(
                FinancialTransactionType.EXPENSE,
                BigDecimal.valueOf(-100.00),
                "Cars",
                LocalDate.now()
        );

        Field idFieldTransaction = FinancialTransaction.class.getDeclaredField("id");
        idFieldTransaction.setAccessible(true);
        idFieldTransaction.set(sampleTransaction, 1L);

        sampleTransaction.setAccount(mockAccount);
    }

    @Test
    void addFinancialTransaction_whenExpense_thenAmountIsNegatedAndSaved() {

        FinancialTransactionDTO expenseDto = FinancialTransactionDTO.builder()
                .amount(BigDecimal.valueOf(50.00))
                .category("Food")
                .financialTransactionType(FinancialTransactionType.EXPENSE)
                .date(LocalDate.now().toString())
                .build();

        when(accountService.getAccount()).thenReturn(mockAccount);

        when(financialTransactionRepository.save(any(FinancialTransaction.class)))
                .thenAnswer(invocation -> {
                    FinancialTransaction transactionToSave = invocation.getArgument(0);

                    Field idField = FinancialTransaction.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(transactionToSave, 2L);

                    return transactionToSave;
                });

        FinancialTransactionDTO resultDto = financialTransactionService.addFinancialTransaction(expenseDto);

        assertNotNull(resultDto);
        assertEquals(expenseDto.getCategory(), resultDto.getCategory());
        assertEquals(expenseDto.getFinancialTransactionType(), resultDto.getFinancialTransactionType());
        assertEquals(expenseDto.getDate(), resultDto.getDate());

        assertEquals(BigDecimal.valueOf(-50.00), resultDto.getAmount());
        assertNotNull(resultDto.getId());

        verify(accountService, times(1)).getAccount();
        verify(financialTransactionRepository, times(1)).save(any(FinancialTransaction.class));
    }

    @Test
    void addFinancialTransaction_whenIncome_thenAmountIsPositiveAndSaved() {

        FinancialTransactionDTO incomeDto = FinancialTransactionDTO.builder()
                .amount(BigDecimal.valueOf(200.00))
                .category("Salary")
                .financialTransactionType(FinancialTransactionType.INCOME)
                .date(LocalDate.now().toString())
                .build();

        when(accountService.getAccount()).thenReturn(mockAccount);
        when(financialTransactionRepository.save(any(FinancialTransaction.class)))
                .thenAnswer(invocation -> {
                    FinancialTransaction transactionToSave = invocation.getArgument(0);

                    Field idField = FinancialTransaction.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(transactionToSave, 3L);

                    return transactionToSave;
                });

        FinancialTransactionDTO resultDto = financialTransactionService.addFinancialTransaction(incomeDto);

        assertNotNull(resultDto);
        assertEquals(incomeDto.getCategory(), resultDto.getCategory());
        assertEquals(incomeDto.getFinancialTransactionType(), resultDto.getFinancialTransactionType());
        assertEquals(incomeDto.getDate(), resultDto.getDate());

        assertEquals(BigDecimal.valueOf(200.00), resultDto.getAmount());
        assertNotNull(resultDto.getId());

        verify(accountService, times(1)).getAccount();
        verify(financialTransactionRepository, times(1)).save(any(FinancialTransaction.class));
    }

    @Test
    void getAllTransactionsOfAccount_shouldReturnListOfTransactions() throws NoSuchFieldException, IllegalAccessException {

        List<FinancialTransaction> transactionsFromRepo = Arrays.asList(
                sampleTransaction, // Используем sampleTransaction из setUp
                new FinancialTransaction(FinancialTransactionType.INCOME, BigDecimal.valueOf(200), "Bonus", LocalDate.now().minusDays(1))
        );

        transactionsFromRepo.get(1).setAccount(mockAccount);

        Field idFieldTransaction = FinancialTransaction.class.getDeclaredField("id");
        idFieldTransaction.setAccessible(true);
        idFieldTransaction.set(transactionsFromRepo.get(1), 2L);

        when(accountService.getAccount()).thenReturn(mockAccount);
        when(financialTransactionRepository.findAllTransactionsOfAccount(mockAccount.getId()))
                .thenReturn(transactionsFromRepo);

        List<FinancialTransaction> resultList = financialTransactionService.getAllTransactionsOfAccount();

        assertNotNull(resultList);
        assertEquals(2, resultList.size());
        assertEquals(transactionsFromRepo.get(0).getCategory(), resultList.get(0).getCategory());
        assertEquals(transactionsFromRepo.get(1).getAmount(), resultList.get(1).getAmount());

        verify(accountService, times(1)).getAccount();
        verify(financialTransactionRepository, times(1)).findAllTransactionsOfAccount(mockAccount.getId());
    }

    @Test
    void getAllTransactionsOfAccount_whenNoTransactions_shouldReturnEmptyList() {

        when(accountService.getAccount()).thenReturn(mockAccount);
        when(financialTransactionRepository.findAllTransactionsOfAccount(mockAccount.getId()))
                .thenReturn(Collections.emptyList());

        List<FinancialTransaction> resultList = financialTransactionService.getAllTransactionsOfAccount();

        assertNotNull(resultList);
        assertTrue(resultList.isEmpty());

        verify(accountService, times(1)).getAccount();
        verify(financialTransactionRepository, times(1)).findAllTransactionsOfAccount(mockAccount.getId());
    }

    @Test
    void updateFinancialTransaction_whenTransactionExists_shouldUpdateAndReturnDTO() throws NoSuchFieldException, IllegalAccessException {

        Long transactionIdToUpdate = 1L;

        FinancialTransactionDTO updateDto = FinancialTransactionDTO.builder()
                .amount(BigDecimal.valueOf(150.00))
                .category("Updated Category")
                .financialTransactionType(FinancialTransactionType.INCOME)
                .date(LocalDate.now().minusDays(5).toString())
                .build();

        FinancialTransaction existingTransaction = new FinancialTransaction(
                FinancialTransactionType.EXPENSE, BigDecimal.valueOf(-50), "Old Category", LocalDate.now().minusDays(10)
        );

        Field idField = FinancialTransaction.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(existingTransaction, transactionIdToUpdate);

        when(accountService.getAccount()).thenReturn(mockAccount);
        when(financialTransactionRepository.findByAccountIdPlusOperationId(transactionIdToUpdate, mockAccount.getId()))
                .thenReturn(Optional.of(existingTransaction));

        when(financialTransactionRepository.save(any(FinancialTransaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        FinancialTransactionDTO resultDto = financialTransactionService.updateFinancialTransaction(transactionIdToUpdate, updateDto);

        assertNotNull(resultDto);
        assertEquals(transactionIdToUpdate, resultDto.getId());
        assertEquals(updateDto.getAmount(), resultDto.getAmount());
        assertEquals(updateDto.getCategory(), resultDto.getCategory());
        assertEquals(updateDto.getFinancialTransactionType(), resultDto.getFinancialTransactionType());
        assertEquals(updateDto.getDate(), resultDto.getDate());

        assertEquals(updateDto.getAmount(), existingTransaction.getAmount());
        assertEquals(updateDto.getCategory(), existingTransaction.getCategory());
        assertEquals(LocalDate.parse(updateDto.getDate()), existingTransaction.getDate());

        verify(accountService, times(1)).getAccount();
        verify(financialTransactionRepository, times(1)).findByAccountIdPlusOperationId(transactionIdToUpdate, mockAccount.getId());
        verify(financialTransactionRepository, times(1)).save(existingTransaction);
    }

    @Test
    void updateFinancialTransaction_whenTransactionNotFound_shouldThrowNotFoundException() {

        Long nonExistentTransactionId = 99L;
        FinancialTransactionDTO updateDto = sampleFinancialTransactionDTO;

        when(accountService.getAccount()).thenReturn(mockAccount);
        when(financialTransactionRepository.findByAccountIdPlusOperationId(nonExistentTransactionId, mockAccount.getId()))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            financialTransactionService.updateFinancialTransaction(nonExistentTransactionId, updateDto);
        });

        assertEquals("Operation with ID: " + nonExistentTransactionId + " does not exist in the database!", exception.getMessage());

        verify(accountService, times(1)).getAccount();
        verify(financialTransactionRepository, times(1)).findByAccountIdPlusOperationId(nonExistentTransactionId, mockAccount.getId());
        verify(financialTransactionRepository, never()).save(any(FinancialTransaction.class));
    }

    @Test
    void deleteFinancialTransaction_whenTransactionExists_shouldDelete() {

        Long transactionIdToDelete = 1L;

        when(accountService.getAccount()).thenReturn(mockAccount);
        when(financialTransactionRepository.findByAccountIdPlusOperationId(transactionIdToDelete, mockAccount.getId()))
                .thenReturn(Optional.of(sampleTransaction));

        doNothing().when(financialTransactionRepository).deleteById(transactionIdToDelete);

        assertDoesNotThrow(() -> financialTransactionService.deleteFinancialTransaction(transactionIdToDelete));

        verify(accountService, times(1)).getAccount();
        verify(financialTransactionRepository, times(1)).findByAccountIdPlusOperationId(transactionIdToDelete, mockAccount.getId());
        verify(financialTransactionRepository, times(1)).deleteById(transactionIdToDelete);
    }

    @Test
    void deleteFinancialTransaction_whenTransactionNotFound_shouldThrowNotFoundException() {

        Long nonExistentTransactionId = 99L;

        when(accountService.getAccount()).thenReturn(mockAccount);
        when(financialTransactionRepository.findByAccountIdPlusOperationId(nonExistentTransactionId, mockAccount.getId()))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            financialTransactionService.deleteFinancialTransaction(nonExistentTransactionId);
        });

        assertEquals("Operation with ID " + nonExistentTransactionId + " does not exist in the database!", exception.getMessage());

        verify(accountService, times(1)).getAccount();
        verify(financialTransactionRepository, times(1)).findByAccountIdPlusOperationId(nonExistentTransactionId, mockAccount.getId());
        verify(financialTransactionRepository, never()).deleteById(anyLong());
    }

    @Test
    void getAnnualBalance_shouldReturnCalculatedBalance() {

        Integer year = 2023;
        Integer month = 12;
        FinancialTransactionType type = FinancialTransactionType.EXPENSE;
        String category = "Food";
        Double expectedBalance = -150.75;

        when(accountService.getAccount()).thenReturn(mockAccount);
        when(financialTransactionRepository.calculateAnnualBalanceByCriteria(
                mockAccount.getId(), year, month, type, category))
                .thenReturn(expectedBalance);

        Double actualBalance = financialTransactionService.getAnnualBalance(year, month, type, category);

        assertEquals(expectedBalance, actualBalance);

        verify(accountService, times(1)).getAccount();
        verify(financialTransactionRepository, times(1))
                .calculateAnnualBalanceByCriteria(mockAccount.getId(), year, month, type, category);
    }

    @Test
    void getOperationsByCriteria_whenNotDemoOrHasTransactions_shouldNotGenerateAndReturnDTOs() {

        Integer year = 2023;
        Integer month = 12;
        FinancialTransactionType type = FinancialTransactionType.EXPENSE;
        String category = "Utilities";

        List<FinancialTransaction> entitiesFromRepo = Collections.singletonList(sampleTransaction);

        when(accountService.getAccount()).thenReturn(mockAccount);
        when(financialTransactionRepository.findOperationsByCriteria(
                mockAccount.getId(), year, month, type, category))
                .thenReturn(entitiesFromRepo);

        List<FinancialTransactionDTO> resultDTOs = financialTransactionService.getOperationsByCriteria(year, month, type, category);

        assertNotNull(resultDTOs);
        assertEquals(1, resultDTOs.size());

        FinancialTransactionDTO firstDto = resultDTOs.get(0);

        assertEquals(sampleTransaction.getId(), firstDto.getId());
        assertEquals(sampleTransaction.getAmount(), firstDto.getAmount());
        assertEquals(sampleTransaction.getCategory(), firstDto.getCategory());

        verify(accountService, times(1)).getAccount();
        verify(financialTransactionRepository, never()).countByAccountId(anyLong());
        verify(finTransactionGenerator, never()).createRandomExpenses(any(Account.class));
        verify(financialTransactionRepository, times(1)).findOperationsByCriteria(mockAccount.getId(), year, month, type, category);
    }

    @Test
    void getOperationsByCriteria_whenDemoAndNoTransactions_shouldGenerateAndReturnDTOs() throws NoSuchFieldException, IllegalAccessException {

        Integer year = 2023;
        Integer month = 12;
        FinancialTransactionType type = FinancialTransactionType.EXPENSE;
        String category = "Entertainment";

        mockAccount.setDemo(true);

        List<FinancialTransaction> generatedEntities = Arrays.asList(
                new FinancialTransaction(FinancialTransactionType.EXPENSE, BigDecimal.valueOf(-20), "Cinema", LocalDate.now()),
                new FinancialTransaction(FinancialTransactionType.EXPENSE, BigDecimal.valueOf(-30), "Games", LocalDate.now().minusDays(1))
        );

        Field idFieldTransaction = FinancialTransaction.class.getDeclaredField("id");
        idFieldTransaction.setAccessible(true);
        idFieldTransaction.set(generatedEntities.get(0), 10L);
        idFieldTransaction.set(generatedEntities.get(1), 11L);

        when(accountService.getAccount()).thenReturn(mockAccount);
        when(financialTransactionRepository.countByAccountId(mockAccount.getId())).thenReturn(0L);

        doNothing().when(finTransactionGenerator).createRandomExpenses(mockAccount);

        when(financialTransactionRepository.findOperationsByCriteria(
                mockAccount.getId(), year, month, type, category))
                .thenReturn(generatedEntities);

        List<FinancialTransactionDTO> resultDTOs = financialTransactionService.getOperationsByCriteria(year, month, type, category);

        assertNotNull(resultDTOs);
        assertEquals(2, resultDTOs.size());
        assertEquals(generatedEntities.get(0).getCategory(), resultDTOs.get(0).getCategory());

        verify(accountService, times(1)).getAccount();
        verify(financialTransactionRepository, times(1)).countByAccountId(mockAccount.getId());
        verify(finTransactionGenerator, times(1)).createRandomExpenses(mockAccount);
        verify(financialTransactionRepository, times(1))
                .findOperationsByCriteria(mockAccount.getId(), year, month, type, category);
    }

    @Test
    void getCategories_shouldReturnListOfCategories() {

        List<String> categoriesFromRepo = Arrays.asList("Food", "Transport", "Salary");

        when(accountService.getAccount()).thenReturn(mockAccount);
        when(financialTransactionRepository.getCategories(mockAccount.getId())).thenReturn(categoriesFromRepo);

        List<String> actualCategories = financialTransactionService.getCategories();

        assertEquals(categoriesFromRepo, actualCategories);

        verify(accountService, times(1)).getAccount();
        verify(financialTransactionRepository, times(1)).getCategories(mockAccount.getId());
    }

    @Test
    void getTransactionTypes_shouldReturnAllEnumValuesAsString() {

        List<String> transactionTypes = financialTransactionService.getTransactionTypes();

        assertNotNull(transactionTypes);

        List<String> expectedTypes = Arrays.stream(FinancialTransactionType.values())
                .map(Enum::toString)
                .toList();

        assertEquals(expectedTypes.size(), transactionTypes.size());
        assertTrue(transactionTypes.containsAll(expectedTypes));
    }

    @Test
    void getYears_shouldReturnListOfYears() {

        List<Integer> yearsFromRepo = Arrays.asList(2022, 2023);

        when(accountService.getAccount()).thenReturn(mockAccount);
        when(financialTransactionRepository.findDistinctYearsByAccountId(mockAccount.getId())).thenReturn(yearsFromRepo);

        List<Integer> actualYears = financialTransactionService.getYears();

        assertEquals(yearsFromRepo, actualYears);

        verify(accountService, times(1)).getAccount();
        verify(financialTransactionRepository, times(1)).findDistinctYearsByAccountId(mockAccount.getId());
    }

    @Test
    void getMonths_shouldReturnListOfMonths() {

        List<Integer> monthsFromRepo = Arrays.asList(1, 2, 11, 12);

        when(accountService.getAccount()).thenReturn(mockAccount);
        when(financialTransactionRepository.findDistinctMonthsByAccountIdAllTime(mockAccount.getId())).thenReturn(monthsFromRepo);

        List<Integer> actualMonths = financialTransactionService.getMonths();

        assertEquals(monthsFromRepo, actualMonths);

        verify(accountService, times(1)).getAccount();
        verify(financialTransactionRepository, times(1)).findDistinctMonthsByAccountIdAllTime(mockAccount.getId());
    }

    @Test
    void getYearExpenses_shouldReturnCalculatedYearExpenses() {

        LocalDate now = LocalDate.of(2023, 10, 26);
        Double expectedExpenses = -1200.50;

        when(accountService.getAccount()).thenReturn(mockAccount);
        when(financialTransactionRepository.calculateYearExpenses(mockAccount.getId(), now.getYear()))
                .thenReturn(expectedExpenses);

        Double actualExpenses = financialTransactionService.getYearExpenses(now);

        assertEquals(expectedExpenses, actualExpenses);

        verify(accountService, times(1)).getAccount();
        verify(financialTransactionRepository, times(1)).calculateYearExpenses(mockAccount.getId(), now.getYear());
    }

    @Test
    void getMonthExpenses_shouldReturnCalculatedMonthExpenses() {

        LocalDate now = LocalDate.of(2023, 10, 26);
        Double expectedExpenses = -300.25;

        when(accountService.getAccount()).thenReturn(mockAccount);
        when(financialTransactionRepository.calculateMonthExpenses(mockAccount.getId(), now.getMonthValue(), now.getYear()))
                .thenReturn(expectedExpenses);

        Double actualExpenses = financialTransactionService.getMonthExpenses(now);

        assertEquals(expectedExpenses, actualExpenses);

        verify(accountService, times(1)).getAccount();
        verify(financialTransactionRepository, times(1)).calculateMonthExpenses(mockAccount.getId(), now.getMonthValue(), now.getYear());
    }

    @Test
    void getWeekExpenses_shouldReturnCalculatedWeekExpenses() {

        LocalDate firstDayOfWeek = LocalDate.of(2023, 10, 23);
        LocalDate lastDayOfWeek = LocalDate.of(2023, 10, 29);
        Double expectedExpenses = -150.0;

        when(accountService.getAccount()).thenReturn(mockAccount);
        when(financialTransactionRepository.calculateWeekExpenses(mockAccount.getId(), firstDayOfWeek, lastDayOfWeek))
                .thenReturn(expectedExpenses);

        Double actualExpenses = financialTransactionService.getWeekExpenses(firstDayOfWeek, lastDayOfWeek);

        assertEquals(expectedExpenses, actualExpenses);

        verify(accountService, times(1)).getAccount();
        verify(financialTransactionRepository, times(1)).calculateWeekExpenses(mockAccount.getId(), firstDayOfWeek, lastDayOfWeek);
    }

    @Test
    void getDayExpenses_shouldReturnCalculatedDayExpenses() {

        LocalDate today = LocalDate.of(2023, 10, 26);
        Double expectedExpenses = -50.75;

        when(accountService.getAccount()).thenReturn(mockAccount);
        when(financialTransactionRepository.calculateDayExpenses(mockAccount.getId(), today))
                .thenReturn(expectedExpenses);

        Double actualExpenses = financialTransactionService.getDayExpenses(today);

        assertEquals(expectedExpenses, actualExpenses);

        verify(accountService, times(1)).getAccount();
        verify(financialTransactionRepository, times(1)).calculateDayExpenses(mockAccount.getId(), today);
    }
}
