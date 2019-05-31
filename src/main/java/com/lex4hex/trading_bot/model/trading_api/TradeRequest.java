package com.lex4hex.trading_bot.model.trading_api;

import lombok.Builder;
import lombok.Data;

/**
 * Request model to make make a trade
 */
@Builder
@Data
public class TradeRequest {
    /**
     * Id of the product to open a position on
     */
    private String productId;

    /**
     * Currency of the investing amount
     */
    private Amount investingAmount;

    /**
     * Leverage to use
     */
    private Integer leverage;

    /**
     * Trade direction
     */
    private TradeDirection direction;

    /**
     * Type of the trade source
     */
    private SourceType sourceType;
}
