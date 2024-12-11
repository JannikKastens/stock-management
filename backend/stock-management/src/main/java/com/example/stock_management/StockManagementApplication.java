package com.example.stock_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class StockManagementApplication {

  public static void main(String[] args) {
    SpringApplication.run(StockManagementApplication.class, args);
  }
}
