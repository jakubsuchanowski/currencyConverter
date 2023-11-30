package com.js.CurrencyConverter.service;


import com.js.CurrencyConverter.entity.ConvertHistory;
import com.js.CurrencyConverter.model.CurrencySubsetDto;
import com.js.CurrencyConverter.model.ExchangeRateDto;
import com.js.CurrencyConverter.repository.ConvertHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConverterService {

    private final RestTemplate restTemplate;
    @Autowired
    private ConvertHistoryRepository convertHistoryRepository;

    @Autowired
    public ConverterService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Value("${external.api.url}")
    private String apiUrl;

    @Value("${external.api.url2}")
    private String apiUrl2;

    public List<CurrencySubsetDto> getExchangeRates(){
        ExchangeRateDto[] exchangeRates = restTemplate.getForObject(apiUrl, ExchangeRateDto[].class);

        List<CurrencySubsetDto> responseList = Arrays.stream(exchangeRates)
                .map(exchangeRateDto -> {
                    CurrencySubsetDto currencySubsetDto = new CurrencySubsetDto();
                    currencySubsetDto.setEffectiveDate(exchangeRateDto.getEffectiveDate());
                    currencySubsetDto.setRates(exchangeRateDto.getRates());
                    return currencySubsetDto;
                })
                .collect(Collectors.toList());

        return responseList;
    }


    public Double getConvertedValue(String baseCurrency, String targetCurrency, Double amount){
            Double response = null;
            if (!baseCurrency.equals("PLN") && !targetCurrency.equals("PLN")) {
                ExchangeRateDto getBaseRate = restTemplate.getForObject(apiUrl2 + "/" + baseCurrency, ExchangeRateDto.class);
                Double rateBaseCurrency = getBaseRate.getRates().get(0).getMid();
                Double responsePLN = amount * rateBaseCurrency;
                ExchangeRateDto getTargetRate = restTemplate.getForObject(apiUrl2 + "/" + targetCurrency, ExchangeRateDto.class);
                Double rateTargetCurrency = getTargetRate.getRates().get(0).getMid();
                response = responsePLN / rateTargetCurrency;
            }
            if (baseCurrency.equals("PLN") && !targetCurrency.equals("PLN")) {
                ExchangeRateDto getTargetRate = restTemplate.getForObject(apiUrl2 + "/" + targetCurrency, ExchangeRateDto.class);
                Double rateTargetCurrency = getTargetRate.getRates().get(0).getMid();
                response = amount / rateTargetCurrency;
            }
            if (targetCurrency.equals("PLN") && !baseCurrency.equals("PLN")) {
                ExchangeRateDto getBaseRate = restTemplate.getForObject(apiUrl2 + "/" + baseCurrency, ExchangeRateDto.class);
                Double rateBaseCurrency = getBaseRate.getRates().get(0).getMid();
                response = amount * rateBaseCurrency;
            }
            if(baseCurrency.equals("PLN") && targetCurrency.equals("PLN")){
                response=amount;
            }
            if(baseCurrency.equals(targetCurrency)) {
                response = amount;
            }
            if(response !=0){
                BigDecimal roundedValue = BigDecimal.valueOf(response).setScale(2, RoundingMode.HALF_UP);
                response = roundedValue.doubleValue();
            }
            convertHistoryRepository.save(new ConvertHistory(0L, baseCurrency,targetCurrency, amount,response, LocalDateTime.now()));
            return response;

    }
    public List<ConvertHistory> getConvertHistory(){
        return convertHistoryRepository.findAll();
    }

    public void clearHistory(){
        convertHistoryRepository.deleteAll();
    }
}
