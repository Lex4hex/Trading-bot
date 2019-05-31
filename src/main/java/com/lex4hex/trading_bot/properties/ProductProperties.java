package com.lex4hex.trading_bot.properties;

import com.lex4hex.trading_bot.exception.TradingBotException;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Product trading properties
 */
@ConfigurationProperties("trading.bot.product")
@Data
@Component
@Validated
public class ProductProperties {
    /**
     * Product id to trade
     */
    @NotBlank
    private String productId;

    /**
     * Maximum buying price. Bot will buy provided product when current quote is less or equals to this price
     */
    @NotNull
    private BigDecimal buyingPrice;

    /**
     * The price when bot will close a position with profit.
     */
    @NotNull
    private BigDecimal upperLimitPrice;

    /**
     * The price when bot will close a position with loss.
     */
    @NotNull
    private BigDecimal lowerLimitPrice;

    /**
     * Amount to invest in position
     */
    @NotNull
    private BigDecimal investingAmount;

    /**
     * Leverage to use
     */
    @NotNull
    private Integer leverage;

    /**
     * Validates relation between prices.
     */
    @PostConstruct
    public void init() {
        if (lowerLimitPrice.compareTo(buyingPrice) > 0) {
            throw new TradingBotException("Lower limit should be smaller than buying price");
        }

        if (buyingPrice.compareTo(upperLimitPrice) > 0) {
            throw new TradingBotException("Buying price should be smaller than upper limit");
        }
    }

}
