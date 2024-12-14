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

  @NotBlank(message = "Ticker Symbol is required")
  @Column(nullable = false)
  private String tickerSymbol;

  @NotBlank(message = "Name is required")
  @Column(nullable = false)
  private String name;

  @Column private String sector;

  @Column(unique = true)
  private String isin;

  @NotNull(message = "Purchase date is required")
  @Column(name = "purchase_date", nullable = false)
  private LocalDate purchaseDate;

  @NotNull(message = "Purchase price is required")
  @Positive(message = "Purchase price must be greater than 0")
  @Column(name = "purchase_price", nullable = false, precision = 19, scale = 2)
  private BigDecimal purchasePrice;

  @NotNull(message = "Amount is required")
  @Positive(message = "Amount must be greater than 0")
  @Column(nullable = false)
  private Integer amount;

  @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Dividend> dividends = new ArrayList<>();
}
