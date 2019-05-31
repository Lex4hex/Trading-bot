package com.lex4hex.trading_bot.model.feed;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lex4hex.trading_bot.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Response message of product websocket feed
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    /**
     * Message type.
     * Differs in each type of feed response
     */
    @JsonProperty(FeedJsonConstants.MESSAGE_TYPE)
    private String messageType;

    /**
     * Current quote of the {@link Product}
     */
    @JsonProperty(FeedJsonConstants.BODY)
    private Quote quote;
}
