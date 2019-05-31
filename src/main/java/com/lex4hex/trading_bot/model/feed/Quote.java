package com.lex4hex.trading_bot.model.feed;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lex4hex.trading_bot.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Represents {@link Product} quote from websocket feed
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Quote {
    /**
     * Id of the {@link Product}
     */
    @JsonProperty(FeedJsonConstants.PRODUCT_ID)
    private String productId;

    /**
     * Actual real time price of the {@link Product}
     */
    private BigDecimal currentPrice;

    /**
     * Event timestamp
     */
    private Timestamp timeStamp;

    /**
     * In case of error contains description of the error
     */
    private String developerMessage;

    /**
     * In case of error contains code of the error
     */
    private String errorCode;
}
