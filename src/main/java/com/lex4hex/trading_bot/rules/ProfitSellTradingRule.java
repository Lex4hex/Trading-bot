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
 * Rule for processing a sell with a profit
 */
@Slf4j
@Component
@AllArgsConstructor
public class ProfitSellTradingRule implements TradingRule {

    private ProductRepository productRepository;

    private PositionAction positionAction;

    private ProductProperties productProperties;

    /**
     * {@inheritDoc}
     * <br>
     * Rule is applied if current {@link Product} is bought and current quote price is greater or equal to upper limit
     * price and bigger than purchase price
     */
    @Override
    public boolean shouldBeApplied(Quote quote) {
        Product product = productRepository.findById(quote.getProductId());

        return product.isBought()
                && quote.getCurrentPrice().compareTo(productProperties.getUpperLimitPrice()) >= 0
                && quote.getCurrentPrice().compareTo(product.getPurchasePrice()) > 0;
    }

    /**
     * {@inheritDoc}
     * <br>
     * Closes position with profit
     */
    @Override
    public void apply(Quote quote) {
        TradeResponse tradeResponse = positionAction.closePosition(quote);

        log.info("Closing position for product {} at the price {} with profit = {} ",
                tradeResponse.getProduct().getProductId(), quote.getCurrentPrice(),
                tradeResponse.getProfitAndLoss().getAmount());
    }
}
