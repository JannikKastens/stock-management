package com.example.stock_management.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.stock_management.config.TestContainersConfig;
import com.example.stock_management.dto.StockDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class StockControllerIntegrationTest extends TestContainersConfig {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void whenCreateValidStock_thenReturns201() throws Exception {
    StockDTO stockDTO = new StockDTO();
    stockDTO.setTickerSymbol("AAPL");
    stockDTO.setName("Apple Inc.");
    stockDTO.setPurchaseDate(LocalDate.now());
    stockDTO.setPurchasePrice(new BigDecimal("150.00"));
    stockDTO.setAmount(10);

    mockMvc
        .perform(
            post("/api/v1/stocks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stockDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.tickerSymbol").value("AAPL"));
  }
}
