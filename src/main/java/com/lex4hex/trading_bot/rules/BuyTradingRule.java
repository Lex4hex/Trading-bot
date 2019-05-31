package com.lex4hex.trading_bot.rules;

import com.lex4hex.trading_bot.model.Product;
import com.lex4hex.trading_bot.model.feed.Quote;
import com.lex4hex.trading_bot.model.trading_api.TradeResponse;
import com.lex4hex.trading_bot.properties.ProductProperties;
import com.lex4hex.trading_bot.repository.ProductRepository;
import com.lex4hex.trading_bot.service.PositionAction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Rule for processing buying of product
 */
@Slf4j
@Component
@AllArgsConstructor
public class BuyTradingRule implements TradingRule {
    private PositionAction positionAction;

    private ProductRepository productRepository;

    private ProductProperties productProperties;

    /**
     * {@inheritDoc}
     * <p>
     * Rule is applied if current {@link Product} isn't already bought and current quote price is equal to buying price
     */
    @Override
    public boolean shouldBeApplied(Quote quote) {
        final Product product = productRepository.findById(quote.getProductId());

        return !product.isBought() &&
                productProperties.getBuyingPrice().compareTo(quote.getCurrentPrice()) == 0;
    }

    /**
     * {@inheritDoc}
     * <br>
     * Opens a position for {@link Product} at current {@link Quote} price
     */
    @Override
    public void apply(Quote quote) {
        Product product = productRepository.findById(quote.getProductId());

        if (product.isBought()) {
            return;
        }

        TradeResponse tradeResponse = positionAction.openPosition(quote);

        log.info("Bought product {} for {}", tradeResponse.getProduct().getProductId(),
                tradeResponse.getPrice().getAmount());
    }
}
