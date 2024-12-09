package com.example.stock_management.controller;

import com.example.stock_management.dto.DividendDTO;
import com.example.stock_management.service.DividendService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dividends")
@RequiredArgsConstructor
public class DividendController {

  private final DividendService dividendService;

  @GetMapping("/stock/{stockId}")
  public ResponseEntity<List<DividendDTO>> getDividendsByStockId(@PathVariable Long stockId) {
    return ResponseEntity.ok(dividendService.getDividendsByStockId(stockId));
  }

  @PostMapping("/stock/{stockId}")
  public ResponseEntity<DividendDTO> createDividend(
      @PathVariable Long stockId, @Valid @RequestBody DividendDTO dividendDTO) {
    return new ResponseEntity<>(
        dividendService.createDividend(stockId, dividendDTO), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<DividendDTO> updateDividend(
      @PathVariable Long id, @Valid @RequestBody DividendDTO dividendDTO) {
    return ResponseEntity.ok(dividendService.updateDividend(id, dividendDTO));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteDividend(@PathVariable Long id) {
    dividendService.deleteDividend(id);
    return ResponseEntity.noContent().build();
  }
}
