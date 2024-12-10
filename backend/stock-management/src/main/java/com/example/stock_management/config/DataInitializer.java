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
  @Profile("!test") // Nicht im Test-Profil ausführen
  CommandLineRunner initDatabase(StockRepository stockRepository) {
    return args -> {
      // Apple Aktie
      Stock apple = new Stock();
      apple.setTickerSymbol("AAPL");
      apple.setName("Apple Inc.");
      apple.setSektor("Technologie");
      apple.setIsin("US0378331005");
      apple.setKaufDatum(LocalDate.of(2023, 1, 15));
      apple.setKaufPreis(new BigDecimal("150.00"));
      apple.setAnzahl(10);

      Dividend appleDiv1 = new Dividend();
      appleDiv1.setStock(apple);
      appleDiv1.setBetrag(new BigDecimal("0.24"));
      appleDiv1.setDatum(LocalDate.of(2023, 2, 16));
      appleDiv1.setWaehrung("USD");
      apple.getDividenden().add(appleDiv1);

      // Microsoft Aktie
      Stock microsoft = new Stock();
      microsoft.setTickerSymbol("MSFT");
      microsoft.setName("Microsoft Corporation");
      microsoft.setSektor("Technologie");
      microsoft.setIsin("US5949181045");
      microsoft.setKaufDatum(LocalDate.of(2023, 3, 20));
      microsoft.setKaufPreis(new BigDecimal("280.00"));
      microsoft.setAnzahl(5);

      Dividend msftDiv1 = new Dividend();
      msftDiv1.setStock(microsoft);
      msftDiv1.setBetrag(new BigDecimal("0.68"));
      msftDiv1.setDatum(LocalDate.of(2023, 3, 9));
      msftDiv1.setWaehrung("USD");
      microsoft.getDividenden().add(msftDiv1);

      // Coca-Cola Aktie
      Stock cocaCola = new Stock();
      cocaCola.setTickerSymbol("KO");
      cocaCola.setName("The Coca-Cola Company");
      cocaCola.setSektor("Konsumgüter");
      cocaCola.setIsin("US1912161007");
      cocaCola.setKaufDatum(LocalDate.of(2023, 2, 1));
      cocaCola.setKaufPreis(new BigDecimal("60.00"));
      cocaCola.setAnzahl(20);

      Dividend koDiv1 = new Dividend();
      koDiv1.setStock(cocaCola);
      koDiv1.setBetrag(new BigDecimal("0.46"));
      koDiv1.setDatum(LocalDate.of(2023, 4, 3));
      koDiv1.setWaehrung("USD");
      cocaCola.getDividenden().add(koDiv1);

      // Speichern der Aktien (Dividenden werden automatisch durch CascadeType.ALL gespeichert)
      stockRepository.save(apple);
      stockRepository.save(microsoft);
      stockRepository.save(cocaCola);
    };
  }
}
