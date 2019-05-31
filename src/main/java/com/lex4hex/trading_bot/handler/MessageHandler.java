package com.lex4hex.trading_bot.handler;

import javax.websocket.Session;
import java.io.IOException;

public interface MessageHandler {
    /**
     * Handles provided message received from websocket product stream.
     *
     * @param message JSON string
     * @param session websocket session
     */
    void handle(String message, Session session) throws IOException;
}
