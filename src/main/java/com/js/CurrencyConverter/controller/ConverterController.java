package com.js.CurrencyConverter.controller;

import com.js.CurrencyConverter.entity.ConvertHistory;
import com.js.CurrencyConverter.service.ConverterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

@RestController
@AllArgsConstructor
public class ConverterController {

    private ConverterService converterService;
    @CrossOrigin(origins = "http://127.0.0.1:5500", allowCredentials = "true")
    @GetMapping("/exchangerate")
    public ResponseEntity showExchangeRate(){
        return ResponseEntity.ok().body(converterService.getExchangeRates());
    }


    @CrossOrigin(origins = "http://127.0.0.1:5500", allowCredentials = "true")
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

    @CrossOrigin(origins = "http://127.0.0.1:5500", allowCredentials = "true")
    @GetMapping("/currencyconvert/history")
    public ResponseEntity<List<ConvertHistory>> showAllConversions(){
        try {
            return ResponseEntity.ok().body(converterService.getConvertHistory());
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500", allowCredentials = "true")
    @DeleteMapping("/currencyconvert/history/clear")
    public ResponseEntity deleteHistory(){
        try {
            converterService.clearHistory();
            return ResponseEntity.ok().body("Historia usunięta!");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Historia nie została usunięta!");
        }
    }
}
