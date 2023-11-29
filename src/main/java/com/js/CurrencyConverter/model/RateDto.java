package com.js.CurrencyConverter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RateDto {
    String currency;
    String code;
    Double mid;
}
