package com.js.CurrencyConverter;
import com.js.CurrencyConverter.service.ConverterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class GetConvertedValueTest {
    @Autowired
    private ConverterService converterService;
    @Test
    void testGetConvertedValue_WhenPLNIsNotCurrency() {

        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        Double amount = 100.0;


        Double result = converterService.getConvertedValue(baseCurrency, targetCurrency, amount);

        Double rateBaseCurrency = 4.0202;
        Double rateTargetCurrency = 4.3382;

        Double expectedValue = amount * rateBaseCurrency / rateTargetCurrency;
        double tolerance = 0.01;


        assertEquals(expectedValue, result, tolerance);
    }


    @Test
    void testGetConvertedValue_WhenPLNIsTargetCurrency() {

        String baseCurrency = "USD";
        String targetCurrency = "PLN";
        Double amount = 100.0;


        Double result = converterService.getConvertedValue(baseCurrency, targetCurrency, amount);

        Double rateBaseCurrency = 4.0202;
        Double expectedValue = amount * rateBaseCurrency;
        double tolerance = 0.01;

        assertEquals(expectedValue, result, tolerance);
    }


    @Test
    void testGetConvertedValue_WhenPLNIsBaseCurrency() {

        String baseCurrency = "PLN";
        String targetCurrency = "EUR";
        Double amount = 100.0;


        Double result = converterService.getConvertedValue(baseCurrency, targetCurrency, amount);
        Double rateTargetCurrency = 4.3382;

        Double expectedValue = amount / rateTargetCurrency;
        double tolerance = 0.01;

        assertEquals(expectedValue, result, tolerance);
    }
}

