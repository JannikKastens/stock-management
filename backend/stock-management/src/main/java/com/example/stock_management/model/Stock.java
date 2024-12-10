package com.example.stock_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Entity
@Table(name = "stocks")
@Data
public class Stock {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Ticker Symbol ist erforderlich")
  @Column(nullable = false)
  private String tickerSymbol;

  @NotBlank(message = "Name ist erforderlich")
  @Column(nullable = false)
  private String name;

  @Column private String sector;

  @Column(unique = true)
  private String isin;

  @NotNull(message = "Kaufdatum ist erforderlich")
  @Column(name = "purchase_date", nullable = false)
  private LocalDate purchaseDate;

  @NotNull(message = "Kaufpreis ist erforderlich")
  @Positive(message = "Kaufpreis muss größer als 0 sein")
  @Column(name = "purchase_price", nullable = false, precision = 19, scale = 2)
  private BigDecimal purchasePrice;

  @NotNull(message = "Anzahl ist erforderlich")
  @Positive(message = "Anzahl muss größer als 0 sein")
  @Column(nullable = false)
  private Integer amount;

  @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Dividend> dividends = new ArrayList<>();
}
