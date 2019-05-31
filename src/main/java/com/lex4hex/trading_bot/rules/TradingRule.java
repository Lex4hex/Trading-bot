package com.lex4hex.trading_bot.rules;

import com.lex4hex.trading_bot.model.Product;
import com.lex4hex.trading_bot.model.feed.Quote;

public interface TradingRule {

    /**
     * Process this rule for acquired quote
     *
     * @param quote New {@link Quote} of subscribed {@link Product}
     */
    void apply(Quote quote);

    /**
     * Return whether this rule should be applied for current {@link Quote} and {@link Product}
     *
     * @param quote New {@link Quote} of subscribed {@link Product}
     * @return true if rule should be applied, false otherwise
     */
    boolean shouldBeApplied(Quote quote);
}
