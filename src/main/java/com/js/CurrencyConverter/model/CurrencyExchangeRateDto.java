package com.js.CurrencyConverter.model;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CurrencyExchangeRateDto {
    String table;
    String currency;
    String code;
    List<RateForOneCurrencyDto> rates;
}
