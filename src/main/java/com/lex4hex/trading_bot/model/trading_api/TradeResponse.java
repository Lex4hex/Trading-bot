package com.lex4hex.trading_bot.model.trading_api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * Response model of trading request
 */
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class TradeResponse {

    /**
     * Unique identifier of the trade (every close position request is also a trade)
     */
    private String id;

    /**
     * Id of the position
     */
    private String positionId;

    /**
     * Profit/loss recorded with the close trade (gross, does not take into account fees)
     */
    private Amount profitAndLoss;

    /**
     * Product for the trade
     */
    private Product product;

    /**
     * The amount invested when opening the position
     */
    private Amount investingAmount;

    /**
     * The product price at the moment of the current trade
     */
    private Amount price;

    /**
     * The multiplier used when opening the trade
     */
    private Integer leverage;

    /**
     * The direction of the trade
     */
    private TradeDirection direction;

    /**
     * The type of trade
     */
    private String type;

    /**
     * Date at which the trade was executed
     */
    private Timestamp dateCreated;
}
