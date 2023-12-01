package com.js.CurrencyConverter;

import com.js.CurrencyConverter.entity.ConvertHistory;
import com.js.CurrencyConverter.repository.ConvertHistoryRepository;
import com.js.CurrencyConverter.service.ConverterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

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
        ReflectionTestUtils.setField(converterService, "apiUrl2", "http://api.nbp.pl/api/exchangerates/rates/A");
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

    @Test
    void testConversionWithDifferentCurrencies() {
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

        mockServer.expect(requestTo("http://api.nbp.pl/api/exchangerates/rates/A/USD"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(
                        "{" +
                                "\"table\": \"A\","+
                                "\"currency\": \"dolar amerykański\", " +
                                "\"code\": \"USD\", " +
                                "\"rates\": [ " +
                                "{" +
                                "\"no\": \"233/A/NBP/2023\"," +
                                "\"effectiveDate\": \"2023-12-01\"," +
                                "\"mid\": 3.9910" +
                                "}" +
                                "]" +
                                "}", MediaType.APPLICATION_JSON));
        mockServer.expect(requestTo("http://api.nbp.pl/api/exchangerates/rates/A/EUR"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(
                        "{" +
                                "\"table\": \"A\","+
                                "\"currency\": \"euro\", " +
                                "\"code\": \"EUR\", " +
                                "\"rates\": [ " +
                                "{" +
                                "\"no\": \"233/A/NBP/2023\"," +
                                "\"effectiveDate\": \"2023-12-01\"," +
                                "\"mid\": 4.3494" +
                                "}" +
                                "]" +
                                "}", MediaType.APPLICATION_JSON));

        Double result = converterService.getConvertedValue("USD", "EUR", 100.0);

        // Expected result: (100 * 1.5) / 2.0 = 75.0
        assertEquals(75.0, result);

        mockServer.verify();
    }

}
