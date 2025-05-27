package com.example.finmanagerbackend.financial_transaction;

import com.example.finmanagerbackend.account.Account;
import com.example.finmanagerbackend.account.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;

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

        mockAccount = new Account();

        // making private field accessible with reflection
        Field idField = Account.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(mockAccount, 1L);

        mockAccount.setDemo(false);

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
}
