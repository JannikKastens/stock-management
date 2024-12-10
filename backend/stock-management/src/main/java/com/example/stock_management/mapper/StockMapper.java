package com.example.stock_management.mapper;

import com.example.stock_management.dto.StockDTO;
import com.example.stock_management.model.Stock;
import org.springframework.stereotype.Component;

@Component
public class StockMapper {

  public StockDTO toDto(Stock stock) {
    if (stock == null) {
      return null;
    }

    StockDTO dto = new StockDTO();
    dto.setId(stock.getId());
    dto.setTickerSymbol(stock.getTickerSymbol());
    dto.setName(stock.getName());
    dto.setSector(stock.getSector());
    dto.setIsin(stock.getIsin());
    dto.setPurchaseDate(stock.getPurchaseDate());
    dto.setPurchasePrice(stock.getPurchasePrice());
    dto.setAmount(stock.getAmount());
    return dto;
  }

  public Stock toEntity(StockDTO dto) {
    if (dto == null) {
      return null;
    }

    Stock stock = new Stock();
    updateEntityFromDto(dto, stock);
    return stock;
  }

  public void updateEntityFromDto(StockDTO dto, Stock stock) {
    stock.setTickerSymbol(dto.getTickerSymbol());
    stock.setName(dto.getName());
    stock.setSector(dto.getSector());
    stock.setIsin(dto.getIsin());
    stock.setPurchaseDate(dto.getPurchaseDate());
    stock.setPurchasePrice(dto.getPurchasePrice());
    stock.setAmount(dto.getAmount());
  }
}
