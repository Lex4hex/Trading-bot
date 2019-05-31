package com.lex4hex.trading_bot.exception;

/**
 * Root business trading bot exception
 */
public class TradingBotException extends RuntimeException {

    public TradingBotException(String message) {
        super(message);
    }

    public TradingBotException(String message, Throwable cause) {
        super(message, cause);
    }
}
