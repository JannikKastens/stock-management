package com.example.stock_management.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.stock_management.dto.StockDTO;
import com.example.stock_management.service.StockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.math.BigDecimal;
import java.time.LocalDate;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class StockControllerTest {

  @Mock private StockService stockService;

  @InjectMocks private StockController stockController;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;
  private StockDTO testStockDTO;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(stockController).build();
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    testStockDTO = new StockDTO();
    testStockDTO.setId(1L);
    testStockDTO.setTickerSymbol("AAPL");
    testStockDTO.setName("Apple Inc.");
    testStockDTO.setSector("Technology");
    testStockDTO.setIsin("US0378331005");
    testStockDTO.setPurchaseDate(LocalDate.now());
    testStockDTO.setPurchasePrice(new BigDecimal("150.00"));
    testStockDTO.setAmount(10);
  }

  @Nested
  @DisplayName("GET /api/v1/stocks Tests")
  class GetAllStocksTests {

    @Test
    @DisplayName("Should return list of stocks")
    void shouldReturnStocksList() throws Exception {
      List<StockDTO> stocks = Arrays.asList(testStockDTO);
      when(stockService.getAllStocks()).thenReturn(stocks);

      mockMvc
          .perform(get("/api/v1/stocks"))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$[0].id").value(1))
          .andExpect(jsonPath("$[0].tickerSymbol").value("AAPL"))
          .andExpect(jsonPath("$[0].name").value("Apple Inc."));
    }
  }

  @Nested
  @DisplayName("GET /api/v1/stocks/{id} Tests")
  class GetStockByIdTests {

    @Test
    @DisplayName("Should return stock when found")
    void shouldReturnStock() throws Exception {
      when(stockService.getStockById(1L)).thenReturn(testStockDTO);

      mockMvc
          .perform(get("/api/v1/stocks/1"))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.id").value(1))
          .andExpect(jsonPath("$.tickerSymbol").value("AAPL"));
    }
  }

  @Nested
  @DisplayName("POST /api/v1/stocks Tests")
  class CreateStockTests {

    @Test
    @DisplayName("Should create stock successfully")
    void shouldCreateStock() throws Exception {
      when(stockService.createStock(any(StockDTO.class))).thenReturn(testStockDTO);

      mockMvc
          .perform(
              post("/api/v1/stocks")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(testStockDTO)))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.tickerSymbol").value("AAPL"))
          .andExpect(jsonPath("$.name").value("Apple Inc."));
    }

    @Test
    @DisplayName("Should return 400 when validation fails")
    void shouldReturn400WhenValidationFails() throws Exception {
      testStockDTO.setTickerSymbol(null); // Violates @NotBlank constraint

      mockMvc
          .perform(
              post("/api/v1/stocks")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(testStockDTO)))
          .andExpect(status().isBadRequest());
    }
  }

  @Nested
  @DisplayName("PUT /api/v1/stocks/{id} Tests")
  class UpdateStockTests {

    @Test
    @DisplayName("Should update stock successfully")
    void shouldUpdateStock() throws Exception {
      when(stockService.updateStock(eq(1L), any(StockDTO.class))).thenReturn(testStockDTO);

      mockMvc
          .perform(
              put("/api/v1/stocks/1")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(testStockDTO)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.tickerSymbol").value("AAPL"));
    }
  }

  @Nested
  @DisplayName("DELETE /api/v1/stocks/{id} Tests")
  class DeleteStockTests {

    @Test
    @DisplayName("Should delete stock successfully")
    void shouldDeleteStock() throws Exception {
      doNothing().when(stockService).deleteStock(1L);

      mockMvc.perform(delete("/api/v1/stocks/1")).andExpect(status().isNoContent());
    }
  }
}
