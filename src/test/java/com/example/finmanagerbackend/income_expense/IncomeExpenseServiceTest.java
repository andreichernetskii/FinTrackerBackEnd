package com.example.finmanagerbackend.income_expense;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith( MockitoExtension.class )
public class IncomeExpenseServiceTest {
    @Mock
    IncomeExpenseRepository incomeExpenseRepository;

    @InjectMocks
    IncomeExpenseService incomeExpenseService;

    @Test
    public void addTransactionTest_SuccessfulAdding() {
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
}
