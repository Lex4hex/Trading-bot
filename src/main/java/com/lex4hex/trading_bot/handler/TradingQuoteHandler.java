package com.lex4hex.trading_bot.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.lex4hex.trading_bot.config.Mapper;
import com.lex4hex.trading_bot.exception.TradingBotException;
import com.lex4hex.trading_bot.model.feed.FeedJsonConstants;
import com.lex4hex.trading_bot.model.feed.Quote;
import com.lex4hex.trading_bot.properties.ProductProperties;
import com.lex4hex.trading_bot.rules.TradingRule;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.IOException;
import java.util.List;

/**
 * Handles quote update messages
 */
@Slf4j
@Service
@Order(2)
@AllArgsConstructor
public class TradingQuoteHandler implements MessageHandler {
    private ProductProperties productProperties;

    private List<TradingRule> tradingRules;

    private JsonProcessorService jsonProcessorService;

    /**
     * {@inheritDoc}
     * <br>
     * <p>
     * After successful subscription receives trading {@link Quote} messages.
     * Handles trading quote messages with message type "trading.quote".
     * Runs a list of {@link TradingRule}s which process trading logic.
     */
    @Override
    public void handle(String message, Session session) throws IOException {
        final JsonNode node = Mapper.instance.readTree(message);

        final boolean messageShouldBeProcessed =
                jsonProcessorService.isMessageShouldBeProcessed(node, FeedJsonConstants.TRADING_QUOTE);

        if (!messageShouldBeProcessed) {
            return;
        }

        final Quote tradingQuote =
                Mapper.instance.treeToValue(node.get(FeedJsonConstants.BODY), Quote.class);

        if (!tradingQuote.getProductId().equals(productProperties.getProductId())) {
            log.warn("Received quote for other product with id = {}, Skipping quote update.",
                    tradingQuote.getProductId());

            return;
        }

        log.info("Received quote update for product with id '{}', new quote value =  {}", tradingQuote.getProductId(),
                tradingQuote.getCurrentPrice());

        try {
            // Apply all trading rules
            tradingRules.stream()
                    .filter(tradingRule -> tradingRule.shouldBeApplied(tradingQuote))
                    .forEach(tradingRule -> tradingRule.apply(tradingQuote));
        } catch (Exception e) {
            throw new TradingBotException("Error occurred during trading rules processing", e);
        }
    }
}
