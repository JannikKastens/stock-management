package com.example.stock_management.repository;

import com.example.stock_management.model.Dividend;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DividendRepository extends JpaRepository<Dividend, Long> {
  List<Dividend> findByStockId(Long stockId);

  List<Dividend> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
