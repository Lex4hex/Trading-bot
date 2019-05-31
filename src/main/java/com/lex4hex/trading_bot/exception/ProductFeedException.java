package com.lex4hex.trading_bot.exception;

/**
 * Exception for errors related to websocket product stream
 */
public class ProductFeedException extends TradingBotException {

    public ProductFeedException(String message, Throwable cause) {
        super(message, cause);
    }
}
