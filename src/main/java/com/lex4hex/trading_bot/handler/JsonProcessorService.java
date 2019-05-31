package com.lex4hex.trading_bot.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.lex4hex.trading_bot.model.feed.FeedJsonConstants;
import org.springframework.stereotype.Service;

@Service
public class JsonProcessorService {

    /**
     * Checks if provided message should be processed for provided message type
     *
     * @param node        Json node of read message
     * @param messageType type of message to check {@link FeedJsonConstants}
     * @return true if message should by process false otherwise
     */
    boolean isMessageShouldBeProcessed(JsonNode node, String messageType) {
        JsonNode jsonElement = node.get(FeedJsonConstants.MESSAGE_TYPE);

        // Skip message if it doesn't contain message type
        if (jsonElement == null) {
            return false;
        }

        // Skip other message types
        return messageType.equals(jsonElement.asText());
    }
}
