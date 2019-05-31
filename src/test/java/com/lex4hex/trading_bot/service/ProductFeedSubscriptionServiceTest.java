package com.lex4hex.trading_bot.service;

import com.lex4hex.trading_bot.model.feed.ProductSubscriptionRequest;
import com.lex4hex.trading_bot.properties.ProductProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.websocket.EncodeException;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductFeedSubscriptionServiceTest {

    @Mock
    private ProductProperties productProperties;

    @Mock
    private Session session;

    @Mock
    private RemoteEndpoint.Basic basic;

    @Captor
    private ArgumentCaptor<ProductSubscriptionRequest> subscriptionRequestArgumentCaptor;

    @InjectMocks
    private ProductFeedSubscriptionService uut;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void subscribe() throws IOException, EncodeException {
        when(productProperties.getProductId()).thenReturn("id");
        when(session.getBasicRemote()).thenReturn(basic);

        uut.subscribe(session);

        verify(basic).sendObject(subscriptionRequestArgumentCaptor.capture());
        assertEquals("trading.product.id", subscriptionRequestArgumentCaptor.getValue().getSubscribeTo().get(0));
    }
}
