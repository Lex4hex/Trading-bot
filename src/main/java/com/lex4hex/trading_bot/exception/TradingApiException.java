package com.lex4hex.trading_bot.exception;

/**
 * Exception for errors related to trading api requests and processing
 */
public class TradingApiException extends TradingBotException {

    public TradingApiException(String message) {
        super(message);
    }
}
