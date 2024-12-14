package com.example.stock_management.service;

import com.example.stock_management.dto.StockDTO;
import com.example.stock_management.mapper.StockMapper;
import com.example.stock_management.model.Stock;
import com.example.stock_management.repository.StockRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StockService {

  private final StockRepository stockRepository;
  private final StockMapper stockMapper;

  public List<StockDTO> getAllStocks() {
    return stockRepository.findAll().stream().map(stockMapper::toDto).toList();
  }

  public StockDTO getStockById(Long id) {
    return stockRepository
        .findById(id)
        .map(stockMapper::toDto)
        .orElseThrow(() -> new EntityNotFoundException("Stock not found with ID: " + id));
  }

  public StockDTO createStock(StockDTO stockDTO) {
    if (stockDTO.getIsin() != null && stockRepository.existsByIsin(stockDTO.getIsin())) {
      throw new IllegalArgumentException("A stock with this ISIN already exists");
    }
    Stock stock = stockMapper.toEntity(stockDTO);
    Stock savedStock = stockRepository.save(stock);
    return stockMapper.toDto(savedStock);
  }

  public StockDTO updateStock(Long id, StockDTO stockDTO) {
    Stock stock =
        stockRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Stock not found with ID: " + id));

    stockMapper.updateEntityFromDto(stockDTO, stock);
    Stock updatedStock = stockRepository.save(stock);
    return stockMapper.toDto(updatedStock);
  }

  public void deleteStock(Long id) {
    if (!stockRepository.existsById(id)) {
      throw new EntityNotFoundException("Stock not found with ID: " + id);
    }
    stockRepository.deleteById(id);
  }
}
