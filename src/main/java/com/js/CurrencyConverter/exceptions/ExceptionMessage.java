package com.js.CurrencyConverter.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ExceptionMessage {
    CURRENCY_DOES_NOT_EXIST("Podana waluta nie istnieje"),
    CONVERSION_ERROR("Błąd podczas przeliczania");

    @Getter
    public final String code;

}
