package com.example.finmanagerbackend.financial_transaction;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) representing financial transactions, used for communication between layers.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class FinancialTransactionDTO {

    private Long id;

    @NotNull(message = "Financial transaction type cannot be null")
    private FinancialTransactionType financialTransactionType;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotBlank(message = "Category cannot be empty or blank")
    private String category;

    @NotNull(message = "Date cannot be null")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date must be in YYYY-MM-DD format")
    private String date;
}
