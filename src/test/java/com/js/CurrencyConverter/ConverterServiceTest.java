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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
public class ConverterServiceTest {
    @Mock
    private ConvertHistoryRepository convertHistoryRepository;
    @Mock
    private HttpServletRequest httpServletRequest;
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
        List<ConvertHistory> histories = List.of(new ConvertHistory(0L, "USD", "PLN", 20.0, 79.5,
                "user_d343224b-65f5-4fb8-9c75-b00d0d6bd9bf", LocalDateTime.now()));

        HttpSession httpSession = mock(HttpSession.class);
        given(httpServletRequest.getSession()).willReturn(httpSession);
        given(httpSession.getAttribute("userIdentifier")).willReturn("user_d343224b-65f5-4fb8-9c75-b00d0d6bd9bf");

        given(convertHistoryRepository.findAllByUserToken("user_d343224b-65f5-4fb8-9c75-b00d0d6bd9bf")).willReturn(histories);
        List<ConvertHistory> result = converterService.getConvertHistory();

        assertEquals(histories, result);
    }
    @Test
    void testClearHistory() {

        HttpSession httpSession = mock(HttpSession.class);
        given(httpServletRequest.getSession()).willReturn(httpSession);
        given(httpSession.getAttribute("userIdentifier")).willReturn("user_d343224b-65f5-4fb8-9c75-b00d0d6bd9bf");
        converterService.clearHistory();

        verify(convertHistoryRepository).deleteAllByUserToken("user_d343224b-65f5-4fb8-9c75-b00d0d6bd9bf");
    }


}
