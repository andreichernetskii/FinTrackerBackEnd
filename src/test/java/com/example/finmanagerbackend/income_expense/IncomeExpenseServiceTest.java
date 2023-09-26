package com.example.finmanagerbackend.income_expense;

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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith( MockitoExtension.class )
public class IncomeExpenseServiceTest {
    @Mock
    private IncomeExpenseRepository incomeExpenseRepository;
    @InjectMocks
    private IncomeExpenseService incomeExpenseService;

    @Test
    public void addTransactionTest_SuccessfulAddedTransaction() {
        IncomeExpenseDTO transactionDTO = new IncomeExpenseDTO(
                OperationType.INCOME,
                new BigDecimal( 100 ),
                new String( "Shoes" ),
                new String( "2005-12-12" )
        );

        incomeExpenseService.addIncomeExpense( transactionDTO );

        ArgumentCaptor<IncomeExpense> transactionCaptor = ArgumentCaptor.forClass( IncomeExpense.class );
        verify( incomeExpenseRepository ).save( transactionCaptor.capture() );

        IncomeExpense transaction = transactionCaptor.getValue();

        assertEquals( transactionDTO.getOperationType(), transaction.getOperationType() );
        assertEquals( transactionDTO.getAmount(), transaction.getAmount() );
        assertEquals( transactionDTO.getCategory(), transaction.getCategory() );
        assertEquals( transactionDTO.getDate(), transaction.getDate().toString() );
    }

    @Test
    public void getOperationsTest_SuccessfulShownOperations() {
        List<IncomeExpense> expectedOperations = new ArrayList<>();
        expectedOperations.add(
                new IncomeExpense(
                    OperationType.EXPENSE ,
                    new BigDecimal( 100 ),
                    "Shoes",
                    LocalDate.now() ) );
        when( incomeExpenseRepository.findAll() ).thenReturn( expectedOperations );

        List<IncomeExpense> actualOperations = incomeExpenseService.getOperations();

        verify( incomeExpenseRepository ).findAll();
        assertNotNull( actualOperations );
        assertEquals( expectedOperations, actualOperations );
    }

    @Test
    public void deleteOperationTest_SuccessfulDeletion() {
        Long id = 1L;
        when( incomeExpenseRepository.existsById( id ) ).thenReturn( true );
        incomeExpenseService.deleteIncomeExpense( id );

        verify( incomeExpenseRepository ).existsById( id );
        verify( incomeExpenseRepository ).deleteById( id );

        when( incomeExpenseRepository.existsById( id ) ).thenReturn( false );
        assertFalse( incomeExpenseRepository.existsById( id ) );
    }

    @Test
    public void deleteOperationTest_OperationNotExits() {
        Long id = 1L;
        when( incomeExpenseRepository.existsById( id ) ).thenReturn( false );

        assertThrows( IllegalStateException.class,
                () -> incomeExpenseService.deleteIncomeExpense( id ) );
        verify( incomeExpenseRepository ).existsById( id );
        verifyNoMoreInteractions( incomeExpenseRepository );
    }

    @Test
    public void getAnnualBalanceTest_CalculatedSuccessful() {
        OperationType operationType = OperationType.EXPENSE;
        String category = "Shoes";
        Double expectedBalance = 100.0;
        when(incomeExpenseRepository.calculateAnnualBalanceByCriteria( 2023, 9, operationType, category ) )
                .thenReturn( expectedBalance );

        Double result = incomeExpenseService.getAnnualBalance( 2023, 9, operationType, category );

        verify( incomeExpenseRepository ).calculateAnnualBalanceByCriteria( 2023, 9, operationType, category );
        assertEquals( expectedBalance, result );
    }
}
