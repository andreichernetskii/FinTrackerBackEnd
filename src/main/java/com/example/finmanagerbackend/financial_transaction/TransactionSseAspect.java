package com.example.finmanagerbackend.financial_transaction;

import com.example.finmanagerbackend.sse.SseEvent;
import com.example.finmanagerbackend.sse.SseService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Aspect
@Component
public class TransactionSseAspect {

    private final SseService sseService;
    private final FinancialTransactionService transactionService;

    @After("@annotation(com.example.finmanagerbackend.global.annotations.SendTransactions)")
    public void sendAllTransactionsOfAccount() {
        sseService.sendData(
                SseEvent.<List<FinancialTransaction>>builder()
                        .eventType("transactions")
                        .data(transactionService.getAllTransactionsOfAccount())
                        .build()
        );
    }
}
