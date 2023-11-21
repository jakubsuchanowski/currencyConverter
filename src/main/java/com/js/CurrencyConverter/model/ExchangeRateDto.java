package com.js.CurrencyConverter.model;

import lombok.Getter;

import java.util.List;

@Getter
public class ExchangeRateDto {
    String table;
    String no;
    String effectiveDate;
    List<Rate> rates;
}
