package com.js.CurrencyConverter.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CurrencySubsetDto {
    String effectiveDate;
    List<RatesDto> rates;
}
