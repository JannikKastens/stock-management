package com.example.stock_management.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class DividendDTO {
  private Long id;

  @NotNull(message = "Betrag ist erforderlich")
  @Positive(message = "Betrag muss größer als 0 sein")
  private BigDecimal betrag;

  @NotNull(message = "Datum ist erforderlich")
  private LocalDate datum;

  @NotNull(message = "Währung ist erforderlich")
  private String waehrung;
}
