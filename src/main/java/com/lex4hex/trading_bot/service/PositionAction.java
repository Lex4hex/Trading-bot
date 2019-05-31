package com.lex4hex.trading_bot.service;

import com.lex4hex.trading_bot.model.Product;
import com.lex4hex.trading_bot.model.feed.Quote;
import com.lex4hex.trading_bot.model.trading_api.TradeResponse;

/**
 * Position trading actions
 */
public interface PositionAction {

    /**
     * Opens trading position, e.g. places a buy order for current quote
     *
     * @param quote New {@link Quote} of current {@link Product}
     * @return Trading API response
     */
    TradeResponse openPosition(Quote quote);

    /**
     * Closes trading position, e.g. places a sell order for current quote
     *
     * @param quote New {@link Quote} of current {@link Product}
     * @return Trading API response
     */
    TradeResponse closePosition(Quote quote);
}
