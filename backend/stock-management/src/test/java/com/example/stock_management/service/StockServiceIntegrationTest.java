package com.example.stock_management.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.stock_management.config.TestContainersConfig;
import com.example.stock_management.dto.StockDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class StockServiceIntegrationTest extends TestContainersConfig {

  @Autowired private StockService stockService;

  @Test
  void whenCreateStock_thenStockIsSaved() {
    // given
    StockDTO stockDTO = new StockDTO();
    stockDTO.setTickerSymbol("AAPL");
    stockDTO.setName("Apple Inc.");
    stockDTO.setKaufDatum(LocalDate.now());
    stockDTO.setKaufPreis(new BigDecimal("150.00"));
    stockDTO.setAnzahl(10);

    // when
    StockDTO savedStock = stockService.createStock(stockDTO);

    // then
    assertNotNull(savedStock.getId());
    assertEquals("AAPL", savedStock.getTickerSymbol());
    assertEquals("Apple Inc.", savedStock.getName());
  }
}
