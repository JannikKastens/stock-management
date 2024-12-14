package com.example.stock_management.service;

import com.example.stock_management.dto.DividendDTO;
import com.example.stock_management.mapper.DividendMapper;
import com.example.stock_management.model.Dividend;
import com.example.stock_management.model.Stock;
import com.example.stock_management.repository.DividendRepository;
import com.example.stock_management.repository.StockRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DividendService {

  private final DividendRepository dividendRepository;
  private final StockRepository stockRepository;
  private final DividendMapper dividendMapper;

  public List<DividendDTO> getDividendsByStockId(Long stockId) {
    return dividendRepository.findByStockId(stockId).stream().map(dividendMapper::toDto).toList();
  }

  public DividendDTO createDividend(Long stockId, DividendDTO dividendDTO) {
    Stock stock =
        stockRepository
            .findById(stockId)
            .orElseThrow(() -> new EntityNotFoundException("Stock not found with ID: " + stockId));

    Dividend dividend = dividendMapper.toEntity(dividendDTO);
    dividend.setStock(stock);

    Dividend savedDividend = dividendRepository.save(dividend);
    return dividendMapper.toDto(savedDividend);
  }

  public DividendDTO updateDividend(Long id, DividendDTO dividendDTO) {
    Dividend dividend =
        dividendRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Dividend not found with ID: " + id));

    dividendMapper.updateEntityFromDto(dividendDTO, dividend);
    Dividend updatedDividend = dividendRepository.save(dividend);
    return dividendMapper.toDto(updatedDividend);
  }

  public void deleteDividend(Long id) {
    if (!dividendRepository.existsById(id)) {
      throw new EntityNotFoundException("Dividend not found with ID: " + id);
    }
    dividendRepository.deleteById(id);
  }
}
