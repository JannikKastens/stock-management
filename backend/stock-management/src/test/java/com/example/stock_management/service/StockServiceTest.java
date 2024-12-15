package com.example.stock_management.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.stock_management.dto.StockDTO;
import com.example.stock_management.mapper.StockMapper;
import com.example.stock_management.model.Stock;
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
class StockServiceTest {

  @Mock private StockRepository stockRepository;

  @Spy private StockMapper stockMapper = new StockMapper();

  @InjectMocks private StockService stockService;

  private Stock testStock;
  private StockDTO testStockDTO;

  @BeforeEach
  void setUp() {
    testStock = new Stock();
    testStock.setId(1L);
    testStock.setTickerSymbol("AAPL");
    testStock.setName("Apple Inc.");
    testStock.setSector("Technology");
    testStock.setIsin("US0378331005");
    testStock.setPurchaseDate(LocalDate.now());
    testStock.setPurchasePrice(new BigDecimal("150.00"));
    testStock.setQuantity(10);

    testStockDTO = new StockDTO();
    testStockDTO.setTickerSymbol("AAPL");
    testStockDTO.setName("Apple Inc.");
    testStockDTO.setSector("Technology");
    testStockDTO.setIsin("US0378331005");
    testStockDTO.setPurchaseDate(LocalDate.now());
    testStockDTO.setPurchasePrice(new BigDecimal("150.00"));
    testStockDTO.setQuantity(10);
  }

  @Nested
  @DisplayName("getAllStocks Tests")
  class GetAllStocksTests {

    @Test
    @DisplayName("Should return list of stocks when stocks exist")
    void shouldReturnStocksList() {
      when(stockRepository.findAll()).thenReturn(Arrays.asList(testStock));

      List<StockDTO> result = stockService.getAllStocks();

      assertThat(result).isNotEmpty();
      assertThat(result).hasSize(1);
      assertThat(result.get(0).getTickerSymbol()).isEqualTo("AAPL");
      verify(stockRepository).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no stocks exist")
    void shouldReturnEmptyList() {
      when(stockRepository.findAll()).thenReturn(List.of());

      List<StockDTO> result = stockService.getAllStocks();

      assertThat(result).isEmpty();
      verify(stockRepository).findAll();
    }
  }

  @Nested
  @DisplayName("getStockById Tests")
  class GetStockByIdTests {

    @Test
    @DisplayName("Should return stock when found")
    void shouldReturnStock() {
      when(stockRepository.findById(1L)).thenReturn(Optional.of(testStock));

      StockDTO result = stockService.getStockById(1L);

      assertThat(result).isNotNull();
      assertThat(result.getTickerSymbol()).isEqualTo("AAPL");
      verify(stockRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when stock not found")
    void shouldThrowException() {
      when(stockRepository.findById(1L)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> stockService.getStockById(1L))
          .isInstanceOf(EntityNotFoundException.class)
          .hasMessageContaining("Stock not found with ID: 1");
    }
  }

  @Nested
  @DisplayName("createStock Tests")
  class CreateStockTests {

    @Test
    @DisplayName("Should create stock successfully")
    void shouldCreateStock() {
      when(stockRepository.save(any(Stock.class))).thenReturn(testStock);

      StockDTO result = stockService.createStock(testStockDTO);

      assertThat(result).isNotNull();
      assertThat(result.getTickerSymbol()).isEqualTo("AAPL");
      verify(stockRepository).save(any(Stock.class));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when ISIN already exists")
    void shouldThrowExceptionWhenIsinExists() {
      when(stockRepository.existsByIsin("US0378331005")).thenReturn(true);

      assertThatThrownBy(() -> stockService.createStock(testStockDTO))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("A stock with this ISIN already exists");
    }
  }

  @Nested
  @DisplayName("updateStock Tests")
  class UpdateStockTests {

    @Test
    @DisplayName("Should update stock successfully")
    void shouldUpdateStock() {
      when(stockRepository.findById(1L)).thenReturn(Optional.of(testStock));
      when(stockRepository.save(any(Stock.class))).thenReturn(testStock);

      StockDTO result = stockService.updateStock(1L, testStockDTO);

      assertThat(result).isNotNull();
      verify(stockRepository).findById(1L);
      verify(stockRepository).save(any(Stock.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when stock not found")
    void shouldThrowException() {
      when(stockRepository.findById(1L)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> stockService.updateStock(1L, testStockDTO))
          .isInstanceOf(EntityNotFoundException.class)
          .hasMessageContaining("Stock not found with ID: 1");
    }
  }

  @Nested
  @DisplayName("deleteStock Tests")
  class DeleteStockTests {

    @Test
    @DisplayName("Should delete stock successfully")
    void shouldDeleteStock() {
      when(stockRepository.existsById(1L)).thenReturn(true);
      doNothing().when(stockRepository).deleteById(1L);

      stockService.deleteStock(1L);

      verify(stockRepository).existsById(1L);
      verify(stockRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when stock not found")
    void shouldThrowException() {
      when(stockRepository.existsById(1L)).thenReturn(false);

      assertThatThrownBy(() -> stockService.deleteStock(1L))
          .isInstanceOf(EntityNotFoundException.class)
          .hasMessageContaining("Stock not found with ID: 1");
    }
  }
}
