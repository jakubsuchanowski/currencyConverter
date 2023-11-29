package com.js.CurrencyConverter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExchangeRateDto {
    String table;
    String no;
    String effectiveDate;
    List<RateDto> rates;
}
