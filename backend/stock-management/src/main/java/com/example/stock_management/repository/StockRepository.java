package com.example.stock_management.repository;

import com.example.stock_management.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
  boolean existsByIsin(String isin);
}
