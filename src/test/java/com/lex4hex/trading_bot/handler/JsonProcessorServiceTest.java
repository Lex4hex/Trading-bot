package com.lex4hex.trading_bot.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.lex4hex.trading_bot.config.Mapper;
import com.lex4hex.trading_bot.model.feed.FeedJsonConstants;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonProcessorServiceTest {
    private JsonProcessorService uut = new JsonProcessorService();

    private String message = "{\"t\":\""+FeedJsonConstants.TRADING_QUOTE+ "\"}";

    @Test
    void isMessageShouldBeProcessedFalse() throws IOException {
        final JsonNode node = Mapper.instance.readTree(message);

        final boolean messageShouldBeProcessed = uut.isMessageShouldBeProcessed(node, FeedJsonConstants.CONNECTED);

        assertFalse(messageShouldBeProcessed);
    }

    @Test
    void isMessageShouldBeProcessedTrueQuote() throws IOException {
        final JsonNode node = Mapper.instance.readTree(message);

        final boolean messageShouldBeProcessed = uut.isMessageShouldBeProcessed(node, FeedJsonConstants.TRADING_QUOTE);

        assertTrue(messageShouldBeProcessed);
    }

    @Test
    void isMessageShouldBeProcessedTrueConnected() throws IOException {
        String message = "{\"t\":\""+FeedJsonConstants.CONNECTED+ "\"}";

        final JsonNode node = Mapper.instance.readTree(message);

        final boolean messageShouldBeProcessed = uut.isMessageShouldBeProcessed(node, FeedJsonConstants.CONNECTED);

        assertTrue(messageShouldBeProcessed);
    }

    @Test
    void isMessageShouldBeProcessedFalseEmpty() throws IOException {
        String message = "{}";

        final JsonNode node = Mapper.instance.readTree(message);

        final boolean messageShouldBeProcessed = uut.isMessageShouldBeProcessed(node, FeedJsonConstants.TRADING_QUOTE);

        assertFalse(messageShouldBeProcessed);
    }
}
