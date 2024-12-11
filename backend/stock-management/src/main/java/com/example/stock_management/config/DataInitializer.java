package com.example.stock_management.config;

import com.example.stock_management.model.Dividend;
import com.example.stock_management.model.Stock;
import com.example.stock_management.repository.StockRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

  @Bean
  @Profile("!test")
  CommandLineRunner initDatabase(StockRepository stockRepository) {
    return args -> {
      // Apple
      Stock apple = new Stock();
      apple.setTickerSymbol("AAPL");
      apple.setName("Apple Inc.");
      apple.setSector("Technologie");
      apple.setIsin("US0378331005");
      apple.setPurchaseDate(LocalDate.of(2023, 1, 15));
      apple.setPurchasePrice(new BigDecimal("150.00"));
      apple.setAmount(10);

      // Add quarterly dividends for Apple
      Dividend appleDiv1 = new Dividend();
      appleDiv1.setStock(apple);
      appleDiv1.setAmount(new BigDecimal("0.24"));
      appleDiv1.setDate(LocalDate.now().minusMonths(3));
      appleDiv1.setCurrency("USD");
      apple.getDividends().add(appleDiv1);

      Dividend appleDiv2 = new Dividend();
      appleDiv2.setStock(apple);
      appleDiv2.setAmount(new BigDecimal("0.24"));
      appleDiv2.setDate(LocalDate.now().minusMonths(0));
      appleDiv2.setCurrency("USD");
      apple.getDividends().add(appleDiv2);

      // Microsoft
      Stock microsoft = new Stock();
      microsoft.setTickerSymbol("MSFT");
      microsoft.setName("Microsoft Corporation");
      microsoft.setSector("Technologie");
      microsoft.setIsin("US5949181045");
      microsoft.setPurchaseDate(LocalDate.of(2023, 3, 20));
      microsoft.setPurchasePrice(new BigDecimal("280.00"));
      microsoft.setAmount(5);

      // Add quarterly dividends for Microsoft
      Dividend msftDiv1 = new Dividend();
      msftDiv1.setStock(microsoft);
      msftDiv1.setAmount(new BigDecimal("0.68"));
      msftDiv1.setDate(LocalDate.now().minusMonths(2));
      msftDiv1.setCurrency("USD");
      microsoft.getDividends().add(msftDiv1);

      Dividend msftDiv2 = new Dividend();
      msftDiv2.setStock(microsoft);
      msftDiv2.setAmount(new BigDecimal("0.68"));
      msftDiv2.setDate(LocalDate.now().minusMonths(5));
      msftDiv2.setCurrency("USD");
      microsoft.getDividends().add(msftDiv2);

      // Coca-Cola
      Stock cocaCola = new Stock();
      cocaCola.setTickerSymbol("KO");
      cocaCola.setName("The Coca-Cola Company");
      cocaCola.setSector("Konsumgüter");
      cocaCola.setIsin("US1912161007");
      cocaCola.setPurchaseDate(LocalDate.of(2023, 2, 1));
      cocaCola.setPurchasePrice(new BigDecimal("60.00"));
      cocaCola.setAmount(20);

      // Add quarterly dividends for Coca-Cola
      Dividend koDiv1 = new Dividend();
      koDiv1.setStock(cocaCola);
      koDiv1.setAmount(new BigDecimal("0.46"));
      koDiv1.setDate(LocalDate.now().minusMonths(1));
      koDiv1.setCurrency("USD");
      cocaCola.getDividends().add(koDiv1);

      Dividend koDiv2 = new Dividend();
      koDiv2.setStock(cocaCola);
      koDiv2.setAmount(new BigDecimal("0.46"));
      koDiv2.setDate(LocalDate.now().minusMonths(4));
      koDiv2.setCurrency("USD");
      cocaCola.getDividends().add(koDiv2);

      stockRepository.save(apple);
      stockRepository.save(microsoft);
      stockRepository.save(cocaCola);
    };
  }
}
