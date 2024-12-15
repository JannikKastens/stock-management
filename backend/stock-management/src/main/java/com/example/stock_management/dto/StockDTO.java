package com.example.stock_management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class StockDTO {
  private Long id;

  @NotBlank(message = "Ticker Symbol is required")
  private String tickerSymbol;

  @NotBlank(message = "Name is required")
  private String name;

  private String sector;
  private String isin;

  @NotNull(message = "Purchase date is required")
  private LocalDate purchaseDate;

  @NotNull(message = "Purchase price is required")
  @Positive(message = "Purchase price must be greater than 0")
  private BigDecimal purchasePrice;

  @NotNull(message = "Quantity is required")
  @Positive(message = "Quantity must be greater than 0")
  private Integer quantity;
}
