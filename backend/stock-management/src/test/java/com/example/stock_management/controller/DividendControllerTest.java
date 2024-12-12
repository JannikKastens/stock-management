package com.example.stock_management.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.stock_management.dto.DividendDTO;
import com.example.stock_management.service.DividendService;
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
class DividendControllerTest {

  @Mock private DividendService dividendService;

  @InjectMocks private DividendController dividendController;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;
  private DividendDTO testDividendDTO;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(dividendController).build();
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    testDividendDTO = new DividendDTO();
    testDividendDTO.setId(1L);
    testDividendDTO.setAmount(new BigDecimal("0.88"));
    testDividendDTO.setDate(LocalDate.now());
    testDividendDTO.setCurrency("USD");
  }

  @Nested
  @DisplayName("GET /api/v1/dividends/stock/{stockId} Tests")
  class GetDividendsByStockIdTests {

    @Test
    @DisplayName("Should return list of dividends")
    void shouldReturnDividendsList() throws Exception {
      List<DividendDTO> dividends = Arrays.asList(testDividendDTO);
      when(dividendService.getDividendsByStockId(1L)).thenReturn(dividends);

      mockMvc
          .perform(get("/api/v1/dividends/stock/1"))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$[0].id").value(1))
          .andExpect(jsonPath("$[0].amount").value("0.88"))
          .andExpect(jsonPath("$[0].currency").value("USD"));
    }
  }

  @Nested
  @DisplayName("POST /api/v1/dividends/stock/{stockId} Tests")
  class CreateDividendTests {

    @Test
    @DisplayName("Should create dividend successfully")
    void shouldCreateDividend() throws Exception {
      when(dividendService.createDividend(eq(1L), any(DividendDTO.class)))
          .thenReturn(testDividendDTO);

      mockMvc
          .perform(
              post("/api/v1/dividends/stock/1")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(testDividendDTO)))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.amount").value("0.88"))
          .andExpect(jsonPath("$.currency").value("USD"));
    }

    @Test
    @DisplayName("Should return 400 when validation fails")
    void shouldReturn400WhenValidationFails() throws Exception {
      testDividendDTO.setAmount(null); // Violates @NotNull constraint

      mockMvc
          .perform(
              post("/api/v1/dividends/stock/1")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(testDividendDTO)))
          .andExpect(status().isBadRequest());
    }
  }

  @Nested
  @DisplayName("PUT /api/v1/dividends/{id} Tests")
  class UpdateDividendTests {

    @Test
    @DisplayName("Should update dividend successfully")
    void shouldUpdateDividend() throws Exception {
      when(dividendService.updateDividend(eq(1L), any(DividendDTO.class)))
          .thenReturn(testDividendDTO);

      mockMvc
          .perform(
              put("/api/v1/dividends/1")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(testDividendDTO)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.amount").value("0.88"));
    }
  }

  @Nested
  @DisplayName("DELETE /api/v1/dividends/{id} Tests")
  class DeleteDividendTests {

    @Test
    @DisplayName("Should delete dividend successfully")
    void shouldDeleteDividend() throws Exception {
      doNothing().when(dividendService).deleteDividend(1L);

      mockMvc.perform(delete("/api/v1/dividends/1")).andExpect(status().isNoContent());
    }
  }
}
