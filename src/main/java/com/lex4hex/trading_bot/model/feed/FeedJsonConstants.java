package com.lex4hex.trading_bot.model.feed;

import lombok.AllArgsConstructor;

/**
 * Field names for processing json from product feed websocket
 */
@AllArgsConstructor
public class FeedJsonConstants {

    /**
     * New quote message type
     */
    public static final String TRADING_QUOTE = "trading.quote";

    /**
     * Message type key
     */
    public static final String MESSAGE_TYPE = "t";

    /**
     * Body of the message
     */
    public static final String BODY = "body";

    /**
     * Connected message type
     */
    public static final String CONNECTED = "connect.connected";

    /**
     * Product id key
     */
    public static final String PRODUCT_ID = "securityId";

    /**
     * Prefix for product subscription request
     */
    static final String TRADING_REQUEST_PREFIX = "trading.product.";

}
