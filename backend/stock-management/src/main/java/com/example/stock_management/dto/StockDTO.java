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

  @NotBlank(message = "Ticker Symbol ist erforderlich")
  private String tickerSymbol;

  @NotBlank(message = "Name ist erforderlich")
  private String name;

  private String sektor;
  private String isin;

  @NotNull(message = "Kaufdatum ist erforderlich")
  private LocalDate kaufDatum;

  @NotNull(message = "Kaufpreis ist erforderlich")
  @Positive(message = "Kaufpreis muss größer als 0 sein")
  private BigDecimal kaufPreis;

  @NotNull(message = "Anzahl ist erforderlich")
  @Positive(message = "Anzahl muss größer als 0 sein")
  private Integer anzahl;
}
