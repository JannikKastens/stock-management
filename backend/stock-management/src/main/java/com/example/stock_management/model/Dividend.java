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
  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal betrag;

  @NotNull(message = "Datum ist erforderlich")
  @Column(nullable = false)
  private LocalDate datum;

  @NotNull(message = "Währung ist erforderlich")
  @Column(nullable = false)
  private String waehrung;
}
