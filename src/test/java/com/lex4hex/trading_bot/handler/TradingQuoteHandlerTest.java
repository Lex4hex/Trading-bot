package com.lex4hex.trading_bot.handler;

import com.lex4hex.trading_bot.model.feed.FeedJsonConstants;
import com.lex4hex.trading_bot.properties.ProductProperties;
import com.lex4hex.trading_bot.rules.BuyTradingRule;
import com.lex4hex.trading_bot.rules.ProfitSellTradingRule;
import com.lex4hex.trading_bot.rules.StopLossRule;
import com.lex4hex.trading_bot.rules.TradingRule;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.internal.util.io.IOUtil;

import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class TradingQuoteHandlerTest {
    @Mock
    private ProductProperties productProperties;

    @Spy
    private List<TradingRule> tradingRules = new ArrayList<>();

    @Mock
    private Session session;

    @Mock
    private BuyTradingRule buyTradingRule;

    @Mock
    private ProfitSellTradingRule profitSellTradingRule;

    @Mock
    private JsonProcessorService jsonProcessorService;

    @Mock
    private StopLossRule stopLossRule;

    @InjectMocks
    private TradingQuoteHandler uut;

    @BeforeEach
    private void init() {
        MockitoAnnotations.initMocks(this);

        tradingRules.add(buyTradingRule);
        tradingRules.add(profitSellTradingRule);
        tradingRules.add(stopLossRule);
    }

    @Test
    void handle() throws IOException {
        String message = "{\"t\":\"trading.quote\",\"id\":\"f91b3b01-83a3-11e9-bfdc-796b76535415\",\"v\":2," +
                "\"body\":{\"securityId\":\"27115\",\"currentPrice\":\"94.07\"}}";
        when(jsonProcessorService.isMessageShouldBeProcessed(any(), eq(FeedJsonConstants.TRADING_QUOTE)))
                .thenReturn(true);
        when(productProperties.getProductId()).thenReturn("27115");

        uut.handle(message, session);

        verify(productProperties).getProductId();

        verify(buyTradingRule).shouldBeApplied(any());
        verify(profitSellTradingRule).shouldBeApplied(any());
        verify(stopLossRule).shouldBeApplied(any());
    }

    @Test
    void handleSkipsOtherProduct() throws IOException {
        String message = "{\"t\":\"trading.quote\",\"id\":\"f91b3b01-83a3-11e9-bfdc-796b76535415\",\"v\":2," +
                "\"body\":{\"securityId\":\"otherProduct\",\"currentPrice\":\"94.07\"}}";
        when(jsonProcessorService.isMessageShouldBeProcessed(any(), eq(FeedJsonConstants.TRADING_QUOTE)))
                .thenReturn(true);
        when(productProperties.getProductId()).thenReturn("ourProduct");

        uut.handle(message, session);

        tradingRules.forEach(Mockito::verifyNoMoreInteractions);
    }

    @Test
    void handleSkips() {
        when(jsonProcessorService.isMessageShouldBeProcessed(any(), eq(FeedJsonConstants.TRADING_QUOTE)))
                .thenReturn(true);
        verifyNoMoreInteractions(productProperties);
        tradingRules.forEach(Mockito::verifyNoMoreInteractions);
    }
}
