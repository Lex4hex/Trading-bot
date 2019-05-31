package com.lex4hex.trading_bot.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Internal product state representation model
 */
@Data
@AllArgsConstructor
public class Product {

    /**
     * Id of the product
     */
    private String productId;

    /**
     * Position id used for closing the position
     */
    private String positionId;

    /**
     * Purchase price of the product
     */
    private BigDecimal purchasePrice;

    public boolean isBought() {
        return positionId != null;
    }
}
