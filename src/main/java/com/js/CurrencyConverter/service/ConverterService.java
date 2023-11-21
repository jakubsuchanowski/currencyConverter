package com.js.CurrencyConverter.service;


import com.js.CurrencyConverter.entity.ConvertHistory;
import com.js.CurrencyConverter.exceptions.ExceptionMessage;
import com.js.CurrencyConverter.model.ExchangeRateDto;
import com.js.CurrencyConverter.repository.ConvertHistoryRepository;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

    public List<ExchangeRateDto> getExchangeRates(){
        ExchangeRateDto[] response = restTemplate.getForObject(apiUrl, ExchangeRateDto[].class);
        return Arrays.asList(response);
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
            if (baseCurrency.equals("PLN")) {
                ExchangeRateDto getTargetRate = restTemplate.getForObject(apiUrl2 + "/" + targetCurrency, ExchangeRateDto.class);
                Double rateTargetCurrency = getTargetRate.getRates().get(0).getMid();
                response = amount / rateTargetCurrency;
            }
            if (targetCurrency.equals("PLN")) {
                ExchangeRateDto getBaseRate = restTemplate.getForObject(apiUrl2 + "/" + baseCurrency, ExchangeRateDto.class);
                Double rateBaseCurrency = getBaseRate.getRates().get(0).getMid();
                response = amount * rateBaseCurrency;
            }
            convertHistoryRepository.save(new ConvertHistory(0L, baseCurrency,targetCurrency, amount,response, LocalDateTime.now()));
            return response;

    }
}
