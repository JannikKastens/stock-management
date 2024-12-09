package com.example.stock_management.controller;

import com.example.stock_management.dto.StockDTO;
import com.example.stock_management.service.StockService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stocks")
@RequiredArgsConstructor
public class StockController {

  private final StockService stockService;

  @GetMapping
  public ResponseEntity<List<StockDTO>> getAllStocks() {
    return ResponseEntity.ok(stockService.getAllStocks());
  }

  @GetMapping("/{id}")
  public ResponseEntity<StockDTO> getStockById(@PathVariable Long id) {
    return ResponseEntity.ok(stockService.getStockById(id));
  }

  @PostMapping
  public ResponseEntity<StockDTO> createStock(@Valid @RequestBody StockDTO stockDTO) {
    return new ResponseEntity<>(stockService.createStock(stockDTO), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<StockDTO> updateStock(
      @PathVariable Long id, @Valid @RequestBody StockDTO stockDTO) {
    return ResponseEntity.ok(stockService.updateStock(id, stockDTO));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
    stockService.deleteStock(id);
    return ResponseEntity.noContent().build();
  }
}
