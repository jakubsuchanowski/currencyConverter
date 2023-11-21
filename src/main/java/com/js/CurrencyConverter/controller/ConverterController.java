package com.js.CurrencyConverter.controller;

import com.js.CurrencyConverter.exceptions.ExceptionMessage;
import com.js.CurrencyConverter.model.ExchangeRateDto;
import com.js.CurrencyConverter.service.ConverterService;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class ConverterController {

    private ConverterService converterService;
    @GetMapping("/exchangerate")
    public ResponseEntity showExchangeRate(){
        return ResponseEntity.ok().body(converterService.getExchangeRates());
    }

    @GetMapping("/currencyconvert/{baseCurrency}/{targetCurrency}")
    public ResponseEntity convertCurrency(
            @PathVariable(value = "baseCurrency") String baseCurrency,
            @PathVariable(value = "targetCurrency") String targetCurrency,
            @RequestParam(defaultValue = "1") Double amount){
        try {
            return ResponseEntity.ok().body(converterService.getConvertedValue(baseCurrency, targetCurrency, amount));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
