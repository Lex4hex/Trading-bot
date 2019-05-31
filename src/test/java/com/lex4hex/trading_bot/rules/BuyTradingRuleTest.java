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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BuyTradingRuleTest {

    @Mock
    private PositionAction positionAction;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductProperties productProperties;

    @InjectMocks
    private BuyTradingRule uut;

    private String productId = "123";
    private Quote quote = new Quote(productId, BigDecimal.TEN, null, null, null);

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldBeAppliedTrue() {
        when(productProperties.getBuyingPrice()).thenReturn(BigDecimal.TEN);
        when(productProperties.getLowerLimitPrice()).thenReturn(BigDecimal.ZERO);
        when(productRepository.findById(productId)).thenReturn(new Product(productId, null, null));

        final boolean shouldBeApplied = uut.shouldBeApplied(quote);

        verify(productRepository).findById(productId);
        verify(productProperties).getBuyingPrice();

        assertTrue(shouldBeApplied);
    }

    @Test
    void shouldBeAppliedFalseAlreadyBought() {
        when(productRepository.findById(productId)).thenReturn(new Product(productId, "position", null));

        final boolean shouldBeApplied = uut.shouldBeApplied(quote);

        verify(productRepository).findById(productId);

        assertFalse(shouldBeApplied);
    }

    @Test
    void shouldBeAppliedFalseQuoteGreaterThanBuying() {
        Quote quote = new Quote(productId, BigDecimal.ONE, null, null, null);
        when(productProperties.getBuyingPrice()).thenReturn(BigDecimal.ZERO);
        when(productRepository.findById(productId)).thenReturn(new Product(productId, null, null));

        final boolean shouldBeApplied = uut.shouldBeApplied(quote);

        verify(productRepository).findById(productId);

        assertFalse(shouldBeApplied);
    }

    @Test
    void apply() {
        when(productRepository.findById(productId)).thenReturn(new Product(productId, null, null));
        Quote quote = new Quote(productId, BigDecimal.ZERO, null, null, null);
        final TradeResponse tradeResponse = new TradeResponse();
        tradeResponse.setProduct(new com.lex4hex.trading_bot.model.trading_api.Product());
        tradeResponse.setPrice(new Amount());
        when(positionAction.openPosition(quote)).thenReturn(tradeResponse);

        uut.apply(quote);

        verify(positionAction).openPosition(quote);
    }

    @Test
    void applySkipsBoughtProduct() {
        when(productRepository.findById(productId)).thenReturn(new Product(productId, "position", null));

        uut.apply(quote);

        verify(positionAction, times(0)).openPosition(quote);
    }
}
