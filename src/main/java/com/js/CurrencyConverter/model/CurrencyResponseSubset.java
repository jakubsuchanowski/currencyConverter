package com.js.CurrencyConverter.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CurrencyResponseSubset {
    String effectiveDate;
    List<RateDto> rates;
}
