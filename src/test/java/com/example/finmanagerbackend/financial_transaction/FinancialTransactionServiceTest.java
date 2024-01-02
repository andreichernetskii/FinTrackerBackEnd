package com.example.finmanagerbackend.financial_transaction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith( MockitoExtension.class )
public class FinancialTransactionServiceTest {
    @Mock
    private FinancialTransactionRepository financialTransactionRepository;
    @InjectMocks
    private FinancialTransactionService financialTransactionService;

    @Test
    public void addTransactionTest_SuccessfulAddedTransaction() {
        FinancialTransactionDTO transactionDTO = new FinancialTransactionDTO(
                FinancialTransactionType.INCOME,
                new BigDecimal( 100 ),
                new String( "Shoes" ),
                new String( "2005-12-12" )
        );

        financialTransactionService.addIncomeExpense( transactionDTO );

        ArgumentCaptor<FinancialTransaction> transactionCaptor = ArgumentCaptor.forClass( FinancialTransaction.class );
        verify( financialTransactionRepository ).save( transactionCaptor.capture() );

        FinancialTransaction transaction = transactionCaptor.getValue();

        assertEquals( transactionDTO.getFinancialTransactionType(), transaction.getOperationType() );
        assertEquals( transactionDTO.getAmount(), transaction.getAmount() );
        assertEquals( transactionDTO.getCategory(), transaction.getCategory() );
        assertEquals( transactionDTO.getDate(), transaction.getDate().toString() );
    }

    @Test
    public void getOperationsTest_SuccessfulShownOperations() {
        List<FinancialTransaction> expectedIncomeExpens = new ArrayList<>();
        expectedIncomeExpens.add(
                new FinancialTransaction(
                    FinancialTransactionType.EXPENSE ,
                    new BigDecimal( 100 ),
                    "Shoes",
                    LocalDate.now() ) );
        when( financialTransactionRepository.findAll() ).thenReturn( expectedIncomeExpens );

        List<FinancialTransaction> actualIncomeExpens = financialTransactionService.getOperations();

        verify( financialTransactionRepository ).findAll();
        assertNotNull( actualIncomeExpens );
        assertEquals( expectedIncomeExpens, actualIncomeExpens );
    }

    @Test
    public void deleteOperationTest_SuccessfulDeletion() {
        Long id = 1L;
        when( financialTransactionRepository.existsById( id ) ).thenReturn( true );
        financialTransactionService.deleteIncomeExpense( id );

        verify( financialTransactionRepository ).existsById( id );
        verify( financialTransactionRepository ).deleteById( id );

        when( financialTransactionRepository.existsById( id ) ).thenReturn( false );
        assertFalse( financialTransactionRepository.existsById( id ) );
    }

    @Test
    public void deleteOperationTest_OperationNotExits() {
        Long id = 1L;
        when( financialTransactionRepository.existsById( id ) ).thenReturn( false );

        assertThrows( IllegalStateException.class,
                () -> financialTransactionService.deleteIncomeExpense( id ) );
        verify( financialTransactionRepository ).existsById( id );
        verifyNoMoreInteractions( financialTransactionRepository );
    }

//    @Test
//    public void getAnnualBalanceTest_CalculatedSuccessful() {
//        OperationType operationType = OperationType.EXPENSE;
//        String category = "Shoes";
//        Double expectedBalance = 100.0;
//        when( incomeExpenseRepository.calculateAnnualBalanceByCriteria( 2023, 9, operationType, category ) )
//                .thenReturn( expectedBalance );
//
//        Double result = incomeExpenseService.getAnnualBalance( 2023, 9, operationType, category );
//
//        verify( incomeExpenseRepository ).calculateAnnualBalanceByCriteria( 2023, 9, operationType, category );
//        assertEquals( expectedBalance, result );
//    }
}
