package com.example.stock_management.controller;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.stock_management.config.TestContainersConfig;
import com.example.stock_management.dto.DividendDTO;
import com.example.stock_management.dto.StockDTO;
import com.example.stock_management.service.StockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class DividendControllerIntegrationTest extends TestContainersConfig {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private StockService stockService;

  private Long stockId;

  @BeforeEach
  void setUpTestData() {
    try {
      StockDTO stockDTO = new StockDTO();
      stockDTO.setTickerSymbol("AAPL");
      stockDTO.setName("Apple Inc.");
      stockDTO.setPurchaseDate(LocalDate.now());
      stockDTO.setPurchasePrice(new BigDecimal("150.00"));
      stockDTO.setAmount(10);

      StockDTO savedStock = stockService.createStock(stockDTO);
      stockId = savedStock.getId();
    } catch (Exception e) {
      fail("Failed to set up test data: " + e.getMessage());
    }
  }

  @Test
  void whenCreateValidDividend_thenReturns201() throws Exception {
    DividendDTO dividendDTO = new DividendDTO();
    dividendDTO.setAmount(new BigDecimal("0.88"));
    dividendDTO.setDate(LocalDate.now());
    dividendDTO.setCurrency("USD");

    mockMvc
        .perform(
            post("/api/v1/dividends/stock/" + stockId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dividendDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.amount").value("0.88"))
        .andExpect(jsonPath("$.currency").value("USD"))
        .andExpect(jsonPath("$.date").value(LocalDate.now().toString()));
  }

  @Test
  void whenGetDividendsByStockId_thenReturns200() throws Exception {
    // Create a test dividend first
    DividendDTO dividendDTO = new DividendDTO();
    dividendDTO.setAmount(new BigDecimal("0.88"));
    dividendDTO.setDate(LocalDate.now());
    dividendDTO.setCurrency("USD");

    mockMvc.perform(
        post("/api/v1/dividends/stock/" + stockId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dividendDTO)));

    // Then test getting the dividends
    mockMvc
        .perform(get("/api/v1/dividends/stock/" + stockId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0]").exists());
  }

  @Test
  void whenUpdateDividend_thenReturns200() throws Exception {
    // First create a dividend
    DividendDTO dividendDTO = new DividendDTO();
    dividendDTO.setAmount(new BigDecimal("0.88"));
    dividendDTO.setDate(LocalDate.now());
    dividendDTO.setCurrency("USD");

    String response =
        mockMvc
            .perform(
                post("/api/v1/dividends/stock/" + stockId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dividendDTO)))
            .andReturn()
            .getResponse()
            .getContentAsString();

    DividendDTO createdDividend = objectMapper.readValue(response, DividendDTO.class);

    // Then update it
    dividendDTO.setId(createdDividend.getId());
    dividendDTO.setAmount(new BigDecimal("1.11"));
    dividendDTO.setDate(createdDividend.getDate());
    dividendDTO.setCurrency(createdDividend.getCurrency());

    mockMvc
        .perform(
            put("/api/v1/dividends/" + createdDividend.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dividendDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.amount").value("1.11"))
        .andExpect(jsonPath("$.date").exists())
        .andExpect(jsonPath("$.currency").exists());
  }

  @Test
  void whenDeleteDividend_thenReturns204() throws Exception {
    // First create a dividend
    DividendDTO dividendDTO = new DividendDTO();
    dividendDTO.setAmount(new BigDecimal("0.88"));
    dividendDTO.setDate(LocalDate.now());
    dividendDTO.setCurrency("USD");

    String response =
        mockMvc
            .perform(
                post("/api/v1/dividends/stock/" + stockId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dividendDTO)))
            .andReturn()
            .getResponse()
            .getContentAsString();

    DividendDTO createdDividend = objectMapper.readValue(response, DividendDTO.class);

    // Then delete it
    mockMvc
        .perform(delete("/api/v1/dividends/" + createdDividend.getId()))
        .andExpect(status().isNoContent());
  }
}
