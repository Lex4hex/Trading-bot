package com.lex4hex.trading_bot.config;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Static {@link ObjectMapper} wrapper.
 * Spring bean is not used for simplicity purpose because we need to call it from static configuration context
 * in {@link ProductFeedEndpoint}
 */
public class Mapper {
    public static final ObjectMapper instance = new ObjectMapper();
}
