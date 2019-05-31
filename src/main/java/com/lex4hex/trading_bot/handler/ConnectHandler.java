package com.lex4hex.trading_bot.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.lex4hex.trading_bot.config.Mapper;
import com.lex4hex.trading_bot.exception.ProductFeedException;
import com.lex4hex.trading_bot.model.feed.FeedJsonConstants;
import com.lex4hex.trading_bot.service.ProductFeedSubscriptionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.IOException;

/**
 * Handles connection message
 */
@Service
@Order(1)
@Slf4j
@AllArgsConstructor
public class ConnectHandler implements MessageHandler {

    private ProductFeedSubscriptionService subscriptionService;

    private JsonProcessorService jsonProcessorService;

    /**
     * {@inheritDoc}
     * <br>
     * Handles connection message, which should contain message type "connect.connected" to proceed.
     * If connection is successful, sends a product subscription request to websocket.
     */
    @Override
    public void handle(String message, Session session) throws IOException {
        final JsonNode node = Mapper.instance.readTree(message);

        final boolean messageShouldBeProcessed =
                jsonProcessorService.isMessageShouldBeProcessed(node, FeedJsonConstants.CONNECTED);

        if (!messageShouldBeProcessed) {
            return;
        }

        // Subscribe to feed after successful connection
        try {
            subscriptionService.subscribe(session);
        } catch (Exception e) {
            throw new ProductFeedException("Error occurred during product feed subscription", e);
        }
    }
}
