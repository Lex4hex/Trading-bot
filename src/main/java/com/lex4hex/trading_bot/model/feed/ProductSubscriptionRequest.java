package com.lex4hex.trading_bot.model.feed;

import com.lex4hex.trading_bot.config.ProductFeedEndpoint;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Product subscription feed request. Used to send subscription messages to {@link ProductFeedEndpoint}
 */
public class ProductSubscriptionRequest {

    /**
     * Product ids to subscribe for
     */
    @Getter
    private List<String> subscribeTo = new ArrayList<>();

    public void addSubscribeTo(String subscribeTo) {
        this.subscribeTo.add(FeedJsonConstants.TRADING_REQUEST_PREFIX + subscribeTo);
    }
}
