package com.lex4hex.trading_bot.model.trading_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lex4hex.trading_bot.model.feed.FeedJsonConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Product representation in trading API
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Product {
    /**
     * Id of the product
     */
    @JsonProperty(FeedJsonConstants.PRODUCT_ID)
    private String productId;

    /**
     * Symbol of the product
     */
    private String symbol;

    /**
     * Display name of the product
     */
    private String displayName;
}



