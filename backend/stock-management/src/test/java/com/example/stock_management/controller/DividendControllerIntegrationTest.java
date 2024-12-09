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
import org.junit.jupiter.api.BeforeAll;
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

  private static Long stockId;

  @BeforeAll
  static void setUpTestData(@Autowired StockService stockService) {
    try {
      StockDTO stockDTO = new StockDTO();
      stockDTO.setTickerSymbol("AAPL");
      stockDTO.setName("Apple Inc.");
      stockDTO.setKaufDatum(LocalDate.now());
      stockDTO.setKaufPreis(new BigDecimal("150.00"));
      stockDTO.setAnzahl(10);

      StockDTO savedStock = stockService.createStock(stockDTO);
      stockId = savedStock.getId();
    } catch (Exception e) {
      fail("Failed to set up test data: " + e.getMessage());
    }
  }

  @Test
  void whenCreateValidDividend_thenReturns201() throws Exception {
    DividendDTO dividendDTO = new DividendDTO();
    dividendDTO.setBetrag(new BigDecimal("0.88"));
    dividendDTO.setDatum(LocalDate.now());
    dividendDTO.setWaehrung("USD");

    mockMvc
        .perform(
            post("/api/v1/dividends/stock/" + stockId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dividendDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.betrag").value("0.88"))
        .andExpect(jsonPath("$.waehrung").value("USD"))
        .andExpect(jsonPath("$.datum").value(LocalDate.now().toString()));
  }

  @Test
  void whenGetDividendsByStockId_thenReturns200() throws Exception {
    mockMvc
        .perform(get("/api/v1/dividends/stock/" + stockId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray());
  }

  @Test
  void whenUpdateDividend_thenReturns200() throws Exception {
    // First create a dividend
    DividendDTO dividendDTO = new DividendDTO();
    dividendDTO.setBetrag(new BigDecimal("0.88"));
    dividendDTO.setDatum(LocalDate.now());
    dividendDTO.setWaehrung("USD");

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
    dividendDTO.setBetrag(new BigDecimal("1.11"));
    dividendDTO.setDatum(createdDividend.getDatum());
    dividendDTO.setWaehrung(createdDividend.getWaehrung());

    mockMvc
        .perform(
            put("/api/v1/dividends/" + createdDividend.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dividendDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.betrag").value("1.11"))
        .andExpect(jsonPath("$.datum").exists())
        .andExpect(jsonPath("$.waehrung").exists());
  }

  @Test
  void whenDeleteDividend_thenReturns204() throws Exception {
    // First create a dividend
    DividendDTO dividendDTO = new DividendDTO();
    dividendDTO.setBetrag(new BigDecimal("0.88"));
    dividendDTO.setDatum(LocalDate.now());
    dividendDTO.setWaehrung("USD");

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
