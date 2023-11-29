package com.js.CurrencyConverter;

import com.js.CurrencyConverter.entity.ConvertHistory;
import com.js.CurrencyConverter.model.ExchangeRateDto;
import com.js.CurrencyConverter.model.RateDto;
import com.js.CurrencyConverter.repository.ConvertHistoryRepository;
import com.js.CurrencyConverter.service.ConverterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Parameterized;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class ConverterServiceTest {
    @Mock
    private ConvertHistoryRepository convertHistoryRepository;
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private ConverterService converterService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testGetConvertHistory() {
        // Mock the response from the repository
        List<ConvertHistory> histories = Arrays.asList(new ConvertHistory(0L, "USD", "PLN", 20.0, 	79.5, LocalDateTime.now()));

        given(convertHistoryRepository.findAll()).willReturn(histories);

        // Perform the test
        List<ConvertHistory> result = converterService.getConvertHistory();

        // Verify the result
        assertEquals(histories, result);
    }
    @Test
    void testClearHistory() {
        // Perform the test
        converterService.clearHistory();

        // Verify that the repository's deleteAll method is called
        verify(convertHistoryRepository).deleteAll();
    }
}
