package com.example.stock_management.mapper;

import com.example.stock_management.dto.DividendDTO;
import com.example.stock_management.model.Dividend;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

@Component
public class DividendMapper {

  public DividendDTO toDto(Dividend dividend) {
    if (dividend == null) {
      return null;
    }

    DividendDTO dto = new DividendDTO();
    dto.setId(dividend.getId());
    dto.setAmount(dividend.getAmount().setScale(2, RoundingMode.HALF_UP));
    dto.setDate(dividend.getDate());
    dto.setCurrency(dividend.getCurrency());
    return dto;
  }

  public Dividend toEntity(DividendDTO dto) {
    if (dto == null) {
      return null;
    }

    Dividend dividend = new Dividend();
    updateEntityFromDto(dto, dividend);
    return dividend;
  }

  public void updateEntityFromDto(DividendDTO dto, Dividend dividend) {
    dividend.setAmount(dto.getAmount().setScale(2, RoundingMode.HALF_UP));
    dividend.setDate(dto.getDate());
    dividend.setCurrency(dto.getCurrency());
  }
}
