package com.example.stock_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Entity
@Table(name = "dividends")
@Data
public class Dividend {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "stock_id", nullable = false)
  private Stock stock;

  @NotNull(message = "Betrag ist erforderlich")
  @Positive(message = "Betrag muss größer als 0 sein")
  @Column(name = "amount", nullable = false, precision = 19, scale = 2)
  private BigDecimal amount;

  @NotNull(message = "Datum ist erforderlich")
  @Column(name = "date", nullable = false)
  private LocalDate date;

  @NotNull(message = "Währung ist erforderlich")
  @Column(name = "currency", nullable = false)
  private String currency;
}
