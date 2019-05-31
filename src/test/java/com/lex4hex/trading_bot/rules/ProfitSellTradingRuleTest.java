package com.lex4hex.trading_bot.rules;

import com.lex4hex.trading_bot.model.Product;
import com.lex4hex.trading_bot.model.feed.Quote;
import com.lex4hex.trading_bot.model.trading_api.Amount;
import com.lex4hex.trading_bot.model.trading_api.TradeResponse;
import com.lex4hex.trading_bot.properties.ProductProperties;
import com.lex4hex.trading_bot.repository.ProductRepository;
import com.lex4hex.trading_bot.service.PositionAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProfitSellTradingRuleTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PositionAction positionAction;

    @Mock
    private ProductProperties productProperties;

    @InjectMocks
    private ProfitSellTradingRule uut;
    private String productId = "123";
    private Quote quote = new Quote(productId, BigDecimal.TEN, null, null, null);

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldBeAppliedTrue() {
        when(productProperties.getUpperLimitPrice()).thenReturn(BigDecimal.ONE);
        when(productRepository.findById(productId)).thenReturn(new Product(productId, "position",
                BigDecimal.valueOf(5)));

        final boolean shouldBeApplied = uut.shouldBeApplied(quote);

        verify(productRepository).findById(productId);
        verify(productProperties).getUpperLimitPrice();

        assertTrue(shouldBeApplied);
    }

    @Test
    void shouldBeAppliedFalseNotBought() {
        when(productRepository.findById(productId)).thenReturn(new Product(productId, null, null));

        final boolean shouldBeApplied = uut.shouldBeApplied(quote);

        verify(productRepository).findById(productId);

        assertFalse(shouldBeApplied);
    }

    @Test
    void shouldBeAppliedFalseQuoteLessThanUpperLimit() {
        when(productProperties.getUpperLimitPrice()).thenReturn(BigDecimal.valueOf(100));
        when(productRepository.findById(productId)).thenReturn(new Product(productId, "position", null));

        final boolean shouldBeApplied = uut.shouldBeApplied(quote);

        verify(productRepository).findById(productId);
        verify(productProperties).getUpperLimitPrice();

        assertFalse(shouldBeApplied);
    }

    @Test
    void shouldBeAppliedFalseQuoteLessThanPurchasePrice() {
        when(productProperties.getUpperLimitPrice()).thenReturn(BigDecimal.valueOf(100));
        when(productRepository.findById(productId)).thenReturn(new Product(productId, "position",
                BigDecimal.valueOf(20)));

        final boolean shouldBeApplied = uut.shouldBeApplied(quote);

        verify(productRepository).findById(productId);
        verify(productProperties).getUpperLimitPrice();

        assertFalse(shouldBeApplied);
    }


    @Test
    void apply() {
        final TradeResponse tradeResponse = new TradeResponse();
        tradeResponse.setProduct(new com.lex4hex.trading_bot.model.trading_api.Product());
        tradeResponse.setProfitAndLoss(new Amount());

        when(positionAction.closePosition(quote)).thenReturn(tradeResponse);

        uut.apply(quote);

        verify(positionAction).closePosition(quote);
    }
}
