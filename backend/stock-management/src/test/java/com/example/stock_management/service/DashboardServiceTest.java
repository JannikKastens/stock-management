package com.example.stock_management.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.stock_management.dto.DashboardStatsDTO;
import com.example.stock_management.dto.MonthlyDividendDTO;
import com.example.stock_management.model.Dividend;
import com.example.stock_management.model.Stock;
import com.example.stock_management.repository.DividendRepository;
import com.example.stock_management.repository.StockRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

  @Mock private StockRepository stockRepository;

  @Mock private DividendRepository dividendRepository;

  @InjectMocks private DashboardService dashboardService;

  private Stock testStock;
  private Dividend testDividend;
  private LocalDate now;

  @BeforeEach
  void setUp() {
    now = LocalDate.now();

    testStock = new Stock();
    testStock.setId(1L);
    testStock.setTickerSymbol("AAPL");
    testStock.setPurchasePrice(new BigDecimal("150.00"));
    testStock.setQuantity(10);

    testDividend = new Dividend();
    testDividend.setId(1L);
    testDividend.setStock(testStock);
    testDividend.setAmount(new BigDecimal("0.88"));
    testDividend.setDate(now);
    testDividend.setCurrency("USD");
  }

  @Nested
  @DisplayName("getDashboardStats Tests")
  class GetDashboardStatsTests {

    @Test
    @DisplayName("Should calculate dashboard stats correctly")
    void shouldCalculateStats() {
      // Arrange
      when(stockRepository.findAll()).thenReturn(Arrays.asList(testStock));
      when(stockRepository.count()).thenReturn(1L);
      when(dividendRepository.findAll()).thenReturn(Arrays.asList(testDividend));

      LocalDate endDate = now;
      LocalDate startDate = endDate.minusMonths(11);
      when(dividendRepository.findByDateBetween(startDate, endDate))
          .thenReturn(Arrays.asList(testDividend));

      // Act
      DashboardStatsDTO result = dashboardService.getDashboardStats();

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getTotalValue()).isEqualTo(new BigDecimal("1500.00")); // 150.00 * 10
      assertThat(result.getTotalDividends()).isEqualTo(new BigDecimal("0.88"));
      assertThat(result.getStockCount()).isEqualTo(1);
      assertThat(result.getMonthlyDividends()).isNotEmpty();
    }

    @Test
    @DisplayName("Should return zero values when no data exists")
    void shouldReturnZeroValues() {
      // Arrange
      when(stockRepository.findAll()).thenReturn(List.of());
      when(stockRepository.count()).thenReturn(0L);
      when(dividendRepository.findAll()).thenReturn(List.of());

      LocalDate endDate = now;
      LocalDate startDate = endDate.minusMonths(11);
      when(dividendRepository.findByDateBetween(startDate, endDate)).thenReturn(List.of());

      // Act
      DashboardStatsDTO result = dashboardService.getDashboardStats();

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getTotalValue()).isEqualTo(BigDecimal.ZERO);
      assertThat(result.getTotalDividends()).isEqualTo(BigDecimal.ZERO);
      assertThat(result.getStockCount()).isEqualTo(0);
      assertThat(result.getMonthlyDividends())
          .isNotEmpty(); // Should still have 12 months with zero values
    }
  }

  @Nested
  @DisplayName("getMonthlyDividends Tests")
  class GetMonthlyDividendsTests {

    @Test
    @DisplayName("Should calculate monthly dividends correctly")
    void shouldCalculateMonthlyDividends() {
      // Arrange
      LocalDate endDate = now;
      LocalDate startDate = endDate.minusMonths(11);
      when(dividendRepository.findByDateBetween(startDate, endDate))
          .thenReturn(Arrays.asList(testDividend));

      // Act
      List<MonthlyDividendDTO> result = dashboardService.getMonthlyDividends();

      // Assert
      assertThat(result).isNotNull();
      assertThat(result).hasSize(12); // Should have 12 months

      // Find current month's dividend
      String currentMonth = now.format(DateTimeFormatter.ofPattern("MMM"));
      MonthlyDividendDTO currentMonthDividend =
          result.stream()
              .filter(dto -> dto.getMonth().equals(currentMonth))
              .findFirst()
              .orElseThrow();

      assertThat(currentMonthDividend.getAmount()).isEqualTo(new BigDecimal("0.88"));
    }

    @Test
    @DisplayName("Should return zero values for months without dividends")
    void shouldReturnZeroValuesForEmptyMonths() {
      // Arrange
      LocalDate endDate = now;
      LocalDate startDate = endDate.minusMonths(11);
      when(dividendRepository.findByDateBetween(startDate, endDate)).thenReturn(List.of());

      // Act
      List<MonthlyDividendDTO> result = dashboardService.getMonthlyDividends();

      // Assert
      assertThat(result).isNotNull();
      assertThat(result).hasSize(12); // Should have 12 months
      assertThat(result).allMatch(dto -> dto.getAmount().compareTo(BigDecimal.ZERO) == 0);
    }
  }
}
