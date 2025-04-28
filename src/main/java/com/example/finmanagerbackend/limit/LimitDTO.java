package com.example.finmanagerbackend.limit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) class for transferring information about limits.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class LimitDTO {

    private Long id;

    @NotNull(message = "Limit type cannot be null")
    private LimitType limitType;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private BigDecimal limitAmount;

    @NotBlank(message = "Category cannot be empty or blank")
    private String category;

    @NotNull(message = "Date cannot be null")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date must be in YYYY-MM-DD format")
    private LocalDate creationDate;
}
