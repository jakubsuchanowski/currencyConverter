package com.js.CurrencyConverter;


import com.js.CurrencyConverter.entity.ConvertHistory;
import com.js.CurrencyConverter.model.CurrencyExchangeRateDto;
import com.js.CurrencyConverter.model.CurrencySubsetDto;
import com.js.CurrencyConverter.model.RateForOneCurrencyDto;
import com.js.CurrencyConverter.repository.ConvertHistoryRepository;
import com.js.CurrencyConverter.service.ConverterService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class getConvertedValueTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ConvertHistoryRepository convertHistoryRepository;

    @InjectMocks
    private ConverterService converterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(converterService, "apiUrl2", "http://api.nbp.pl/api/exchangerates/rates/A");
        converterService = new ConverterService(restTemplate);
    }

    @Test
    public void testGetConvertedValue() {
        // Mock the external API responses
        CurrencyExchangeRateDto baseRateDto = new CurrencyExchangeRateDto();
        baseRateDto.setTable("A");
        baseRateDto.setCurrency("euro");
        baseRateDto.setCode("EUR");
        baseRateDto.setRates(Arrays.asList(new RateForOneCurrencyDto("232/A/NBP/2023","2023-12-01",4.3494)));
        when(restTemplate.getForObject(eq("http://api.nbp.pl/api/exchangerates/rates/A/EUR"), eq(CurrencyExchangeRateDto.class)))
                .thenReturn(baseRateDto);

        CurrencyExchangeRateDto targetRateDto = new CurrencyExchangeRateDto();
        targetRateDto.setTable("A");
        targetRateDto.setCurrency("dolar amerykański");
        targetRateDto.setCode("USD");
        targetRateDto.setRates(Arrays.asList(new RateForOneCurrencyDto("233/A/NBP/2023", "2023-12-01",3.9910)));
        when(restTemplate.getForObject(eq("http://api.nbp.pl/api/exchangerates/rates/A/USD"), eq(CurrencyExchangeRateDto.class)))
                .thenReturn(targetRateDto);

        // Test the method
        Double convertedValue = converterService.getConvertedValue("EUR", "USD", 100.0);

        // Assert the result
        assertEquals(50.0, convertedValue);

        // Verify that the save method is called with the expected parameters
//        ConvertHistory expectedHistory = new ConvertHistory(0L, "EUR", "USD", 100.0, 50.0, LocalDateTime.now());
//        verify(convertHistoryRepository).save(expectedHistory);

        verify(restTemplate).getForObject(eq("http://api.nbp.pl/api/exchangerates/rates/A/EUR"), eq(CurrencyExchangeRateDto.class));
        verify(restTemplate).getForObject(eq("http://api.nbp.pl/api/exchangerates/rates/A/USD"), eq(CurrencyExchangeRateDto.class));
    }
}
