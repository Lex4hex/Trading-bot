package com.lex4hex.trading_bot.handler;

import com.lex4hex.trading_bot.exception.TradingBotException;
import com.lex4hex.trading_bot.model.feed.FeedJsonConstants;
import com.lex4hex.trading_bot.service.ProductFeedSubscriptionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ConnectHandlerTest {

    @Mock
    private ProductFeedSubscriptionService subscriptionService;

    @Mock
    private Session session;

    @Mock
    private JsonProcessorService jsonProcessorService;

    @InjectMocks
    private ConnectHandler uut;

    @BeforeEach
    private void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void handle() throws IOException, EncodeException {
        when(jsonProcessorService.isMessageShouldBeProcessed(any(), eq(FeedJsonConstants.CONNECTED))).thenReturn(true);

        uut.handle("", session);

        verify(subscriptionService).subscribe(eq(session));
    }

    @Test
    void handleThrows() throws IOException, EncodeException {
        when(jsonProcessorService.isMessageShouldBeProcessed(any(), eq(FeedJsonConstants.CONNECTED))).thenReturn(true);
        doThrow(TradingBotException.class).when(subscriptionService).subscribe(session);

        Assertions.assertThrows(TradingBotException.class, () -> uut.handle("", session));
    }

    @Test
    void handleSkips() throws IOException, EncodeException {
        when(jsonProcessorService.isMessageShouldBeProcessed(any(), eq(FeedJsonConstants.CONNECTED))).thenReturn(false);

        uut.handle("", session);

        verifyNoMoreInteractions(subscriptionService);
    }
}
