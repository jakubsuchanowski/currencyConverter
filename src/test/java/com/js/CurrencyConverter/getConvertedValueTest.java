package com.js.CurrencyConverter;


import com.js.CurrencyConverter.model.ExchangeRateDto;
import com.js.CurrencyConverter.model.RateDto;
import com.js.CurrencyConverter.service.ConverterService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
public class getConvertedValueTest {

    @Before
    public void init(){
        Double expectedResult=91.53;
    }
    @Test
    public void getConvertedValueTest_expectedConvertedValue(){
        ExchangeRateDto mockBaseRate = new ExchangeRateDto();
        mockBaseRate.setRates(Collections.singletonList(new RateDto("dolar amerykański","USD",3.981)));
        ExchangeRateDto mockTargetRate = new ExchangeRateDto();
        mockTargetRate.setRates(Collections.singletonList(new RateDto("euro","EUR",4.3492)));

        RestTemplate mockRestTemplate = Mockito.mock(RestTemplate.class);
        Mockito.when(mockRestTemplate.getForObject(eq("http://api.nbp.pl/api/exchangerates/rates/A/USD"), eq(ExchangeRateDto.class)))
                .thenReturn(mockBaseRate);
        Mockito.when(mockRestTemplate.getForObject(eq("http://api.nbp.pl/api/exchangerates/rates/A/EUR"), eq(ExchangeRateDto.class)))
                .thenReturn(mockTargetRate);

        ConverterService converterService= new ConverterService(mockRestTemplate);
        Double result = converterService.getConvertedValue("USD","EUR",100.0);

        assertEquals(91.53, result);

    }


}
