package com.lex4hex.trading_bot.service;

import com.lex4hex.trading_bot.exception.TradingBotException;
import com.lex4hex.trading_bot.model.feed.Quote;
import com.lex4hex.trading_bot.model.trading_api.Amount;
import com.lex4hex.trading_bot.model.trading_api.Product;
import com.lex4hex.trading_bot.model.trading_api.SourceType;
import com.lex4hex.trading_bot.model.trading_api.TradeDirection;
import com.lex4hex.trading_bot.model.trading_api.TradeRequest;
import com.lex4hex.trading_bot.model.trading_api.TradeResponse;
import com.lex4hex.trading_bot.properties.ConnectionProperties;
import com.lex4hex.trading_bot.properties.ProductProperties;
import com.lex4hex.trading_bot.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PositionServiceTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ConnectionProperties connectionProperties;
    @Mock
    private ProductProperties productProperties;
    @Mock
    private ResponseEntity<TradeResponse> tradeResponseResponseEntity;
    @InjectMocks
    private PositionService uut;
    @Captor
    private ArgumentCaptor<TradeRequest> tradeRequestArgumentCaptor;
    private String productId = "123";
    private Quote quote = new Quote(productId, BigDecimal.ONE, null, null, null);

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void openPosition() {
        initiateCommonOpenStabs();

        when(productProperties.getInvestingAmount()).thenReturn(BigDecimal.TEN);
        final int leverage = 2;
        when(productProperties.getLeverage()).thenReturn(leverage);
        final TradeResponse tradeResponse = new TradeResponse();
        tradeResponse.setProduct(new Product());
        tradeResponse.setPrice(new Amount());

        when(tradeResponseResponseEntity.getBody()).thenReturn(tradeResponse);

        uut.openPosition(quote);

        assertEquals(productId, tradeRequestArgumentCaptor.getValue().getProductId());
        assertEquals(leverage, (int) tradeRequestArgumentCaptor.getValue().getLeverage());
        assertEquals(TradeDirection.BUY, tradeRequestArgumentCaptor.getValue().getDirection());
        assertEquals(SourceType.OTHER, tradeRequestArgumentCaptor.getValue().getSourceType());

        verify(productRepository).save(any());
    }

    @Test
    void openPositionThrows() {
        initiateCommonOpenStabs();

        assertThrows(TradingBotException.class, () -> uut.openPosition(quote));
    }

    @Test
    void closePosition() {
        final com.lex4hex.trading_bot.model.Product product = initiateCommonClosingStabs();
        final TradeResponse tradeResponse = new TradeResponse();
        tradeResponse.setProduct(new Product());
        tradeResponse.setPrice(new Amount());

        when(tradeResponseResponseEntity.getBody()).thenReturn(tradeResponse);

        uut.closePosition(quote);

        assertFalse(product.isBought());
    }

    @Test
    void closePositionThrows() {
        initiateCommonClosingStabs();

        assertThrows(TradingBotException.class, () -> uut.closePosition(quote));
    }

    private void initiateCommonOpenStabs() {
        String buyUrl = "buyUrl";
        when(connectionProperties.getBuyApiUrl()).thenReturn(buyUrl);
        when(restTemplate.postForEntity(eq(buyUrl), tradeRequestArgumentCaptor.capture(), eq(TradeResponse.class)))
                .thenReturn(tradeResponseResponseEntity);
    }

    private com.lex4hex.trading_bot.model.Product initiateCommonClosingStabs() {
        final com.lex4hex.trading_bot.model.Product product = new com.lex4hex.trading_bot.model.Product(productId,
                "position", BigDecimal.TEN);
        when(productRepository.findById(quote.getProductId())).thenReturn(product);
        String closeUrl = "https://test.test/core/21/users/me/portfolio/positions/{position}";
        when(connectionProperties.getCloseApiUrl()).thenReturn(closeUrl);
        when(restTemplate.exchange(any(), eq(HttpMethod.DELETE), isNull(),
                eq(TradeResponse.class))).thenReturn(tradeResponseResponseEntity);

        return product;
    }
}
