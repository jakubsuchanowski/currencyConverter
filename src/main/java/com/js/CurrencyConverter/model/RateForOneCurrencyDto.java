package com.js.CurrencyConverter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RateForOneCurrencyDto {
    String no;
    String effectiveDate;
    Double mid;
}
