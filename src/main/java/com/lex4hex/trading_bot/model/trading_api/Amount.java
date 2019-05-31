package com.lex4hex.trading_bot.model.trading_api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Amount DTO, used for representing money
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Amount {
    /**
     * Currency of the investing amount. Should be the same currency as the home currency (the currency in which the
     * cash Yes balance is shown)
     */
    private String currency;

    /**
     * Number of decimals for the amount
     */
    private Integer decimals;

    /**
     * Amount of the order
     */
    private BigDecimal amount;
}
