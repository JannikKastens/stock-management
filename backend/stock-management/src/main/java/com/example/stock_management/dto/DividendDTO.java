package com.example.stock_management.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class DividendDTO {
  private Long id;

  @NotNull(message = "Amount is required")
  @Positive(message = "Amount must be greater than 0")
  private BigDecimal amount;

  @NotNull(message = "Date is required")
  private LocalDate date;

  @NotNull(message = "Currency is required")
  private String currency;
}
