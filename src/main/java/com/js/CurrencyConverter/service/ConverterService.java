package com.js.CurrencyConverter.service;


import com.js.CurrencyConverter.entity.ConvertHistory;
import com.js.CurrencyConverter.model.CurrencyExchangeRateDto;
import com.js.CurrencyConverter.model.CurrencySubsetDto;
import com.js.CurrencyConverter.model.ExchangeRateDto;
import com.js.CurrencyConverter.repository.ConvertHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConverterService {

    private final RestTemplate restTemplate;
    @Autowired
    private ConvertHistoryRepository convertHistoryRepository;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    public ConverterService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Value("${external.api.url}")
    private String apiUrl;

    @Value("${external.api.url2}")
    private String apiUrl2;

private String getUserIdentifierFromSession() {
    HttpSession httpSession = httpServletRequest.getSession();
    String userIdentifier = (String) httpSession.getAttribute("userIdentifier");
    if(userIdentifier == null){
        userIdentifier = generateUserIdentifier();
        httpSession.setAttribute("userIdentifier", userIdentifier);

    }
    return userIdentifier;

}

    private String generateUserIdentifier(){
        return "user_"+UUID.randomUUID().toString();
    }

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


    public Double getConvertedValue(String baseCurrency, String targetCurrency, Double amount) {
            Double response = null;
            Double rateBaseCurrency;
            Double rateTargetCurrency;
            CurrencyExchangeRateDto getBaseRate;
            CurrencyExchangeRateDto getTargetRate;
            if (!baseCurrency.equals("PLN") && !targetCurrency.equals("PLN")) {
                getBaseRate = restTemplate.getForObject(apiUrl2 + "/" + baseCurrency, CurrencyExchangeRateDto.class);
                if (getBaseRate != null) {
                    rateBaseCurrency = getBaseRate.getRates().get(0).getMid();

                    getTargetRate = restTemplate.getForObject(apiUrl2 + "/" + targetCurrency, CurrencyExchangeRateDto.class);

                    if (getTargetRate != null) {
                        rateTargetCurrency = getTargetRate.getRates().get(0).getMid();


                        Double responsePLN = amount * rateBaseCurrency;
                        response = responsePLN / rateTargetCurrency;
                    }
                }
            }
            if (baseCurrency.equals("PLN") && !targetCurrency.equals("PLN")) {
                getTargetRate = restTemplate.getForObject(apiUrl2 + "/" + targetCurrency, CurrencyExchangeRateDto.class);
                if (getTargetRate != null) {
                    rateTargetCurrency = getTargetRate.getRates().get(0).getMid();
                    response = amount / rateTargetCurrency;
                }
            }
            if (targetCurrency.equals("PLN") && !baseCurrency.equals("PLN")) {
                getBaseRate = restTemplate.getForObject(apiUrl2 + "/" + baseCurrency, CurrencyExchangeRateDto.class);
                if (getBaseRate != null) {
                    rateBaseCurrency = getBaseRate.getRates().get(0).getMid();
                    response = amount * rateBaseCurrency;
                }
            }
            if (baseCurrency.equals(targetCurrency)) {
                response = amount;
            }
            if (response != null) {
                BigDecimal roundedValue = BigDecimal.valueOf(response).setScale(2, RoundingMode.HALF_UP);
                response = roundedValue.doubleValue();
            }
            if (convertHistoryRepository != null) {
                String userToken = getUserIdentifierFromSession();

                convertHistoryRepository.save(new ConvertHistory(0L,baseCurrency, targetCurrency, amount, response,userToken, LocalDateTime.now()));
            }
            return response;
    }


    public List<ConvertHistory> getConvertHistory(){
        String userToken = getUserIdentifierFromSession();
        return convertHistoryRepository.findAllByUserToken(userToken);
    }

    public void clearHistory(){
        String userToken = getUserIdentifierFromSession();
        convertHistoryRepository.deleteAllByUserToken(userToken);
    }
}
