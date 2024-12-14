package com.example.stock_management.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.stock_management.dto.DividendDTO;
import com.example.stock_management.mapper.DividendMapper;
import com.example.stock_management.model.Dividend;
import com.example.stock_management.model.Stock;
import com.example.stock_management.repository.DividendRepository;
import com.example.stock_management.repository.StockRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DividendServiceTest {

  @Mock private DividendRepository dividendRepository;

  @Mock private StockRepository stockRepository;

  @Spy private DividendMapper dividendMapper = new DividendMapper();

  @InjectMocks private DividendService dividendService;

  private Stock testStock;
  private Dividend testDividend;
  private DividendDTO testDividendDTO;

  @BeforeEach
  void setUp() {
    testStock = new Stock();
    testStock.setId(1L);
    testStock.setTickerSymbol("AAPL");
    testStock.setName("Apple Inc.");
    testStock.setPurchaseDate(LocalDate.now());
    testStock.setPurchasePrice(new BigDecimal("150.00"));
    testStock.setAmount(10);

    testDividend = new Dividend();
    testDividend.setId(1L);
    testDividend.setStock(testStock);
    testDividend.setAmount(new BigDecimal("0.88"));
    testDividend.setDate(LocalDate.now());
    testDividend.setCurrency("USD");

    testDividendDTO = new DividendDTO();
    testDividendDTO.setAmount(new BigDecimal("0.88"));
    testDividendDTO.setDate(LocalDate.now());
    testDividendDTO.setCurrency("USD");
  }

  @Nested
  @DisplayName("getDividendsByStockId Tests")
  class GetDividendsByStockIdTests {

    @Test
    @DisplayName("Should return list of dividends when stock has dividends")
    void shouldReturnDividendsList() {
      when(dividendRepository.findByStockId(1L)).thenReturn(Arrays.asList(testDividend));

      List<DividendDTO> result = dividendService.getDividendsByStockId(1L);

      assertThat(result).isNotEmpty();
      assertThat(result).hasSize(1);
      assertThat(result.get(0).getAmount()).isEqualTo(new BigDecimal("0.88"));
      verify(dividendRepository).findByStockId(1L);
    }

    @Test
    @DisplayName("Should return empty list when stock has no dividends")
    void shouldReturnEmptyList() {
      when(dividendRepository.findByStockId(1L)).thenReturn(List.of());

      List<DividendDTO> result = dividendService.getDividendsByStockId(1L);

      assertThat(result).isEmpty();
      verify(dividendRepository).findByStockId(1L);
    }
  }

  @Nested
  @DisplayName("createDividend Tests")
  class CreateDividendTests {

    @Test
    @DisplayName("Should create dividend successfully")
    void shouldCreateDividend() {
      when(stockRepository.findById(1L)).thenReturn(Optional.of(testStock));
      when(dividendRepository.save(any(Dividend.class))).thenReturn(testDividend);

      DividendDTO result = dividendService.createDividend(1L, testDividendDTO);

      assertThat(result).isNotNull();
      assertThat(result.getAmount()).isEqualTo(new BigDecimal("0.88"));
      assertThat(result.getCurrency()).isEqualTo("USD");
      verify(stockRepository).findById(1L);
      verify(dividendRepository).save(any(Dividend.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when stock not found")
    void shouldThrowExceptionWhenStockNotFound() {
      when(stockRepository.findById(1L)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> dividendService.createDividend(1L, testDividendDTO))
          .isInstanceOf(EntityNotFoundException.class)
          .hasMessageContaining("Stock not found with ID: 1");
    }
  }

  @Nested
  @DisplayName("updateDividend Tests")
  class UpdateDividendTests {

    @Test
    @DisplayName("Should update dividend successfully")
    void shouldUpdateDividend() {
      DividendDTO updateDTO = new DividendDTO();
      updateDTO.setAmount(new BigDecimal("1.00"));
      updateDTO.setDate(LocalDate.now());
      updateDTO.setCurrency("EUR");

      when(dividendRepository.findById(1L)).thenReturn(Optional.of(testDividend));
      when(dividendRepository.save(any(Dividend.class))).thenReturn(testDividend);

      DividendDTO result = dividendService.updateDividend(1L, updateDTO);

      assertThat(result).isNotNull();
      verify(dividendRepository).findById(1L);
      verify(dividendRepository).save(any(Dividend.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when dividend not found")
    void shouldThrowExceptionWhenDividendNotFound() {
      when(dividendRepository.findById(1L)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> dividendService.updateDividend(1L, testDividendDTO))
          .isInstanceOf(EntityNotFoundException.class)
          .hasMessageContaining("Dividend not found with ID: 1");
    }
  }

  @Nested
  @DisplayName("deleteDividend Tests")
  class DeleteDividendTests {

    @Test
    @DisplayName("Should delete dividend successfully")
    void shouldDeleteDividend() {
      when(dividendRepository.existsById(1L)).thenReturn(true);
      doNothing().when(dividendRepository).deleteById(1L);

      dividendService.deleteDividend(1L);

      verify(dividendRepository).existsById(1L);
      verify(dividendRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when dividend not found")
    void shouldThrowExceptionWhenDividendNotFound() {
      when(dividendRepository.existsById(1L)).thenReturn(false);

      assertThatThrownBy(() -> dividendService.deleteDividend(1L))
          .isInstanceOf(EntityNotFoundException.class)
          .hasMessageContaining("Dividend not found with ID: 1");
    }
  }
}
