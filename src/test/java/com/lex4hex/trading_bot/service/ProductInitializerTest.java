package com.lex4hex.trading_bot.service;

import com.lex4hex.trading_bot.model.Product;
import com.lex4hex.trading_bot.properties.ProductProperties;
import com.lex4hex.trading_bot.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductInitializerTest {

    @InjectMocks
    private ProductInitializer uut;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductProperties productProperties;

    @Captor
    private ArgumentCaptor<Product> productArgumentCaptor;

    @BeforeEach
    private void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void initializeProduct() {
        final String productId = "product-id";
        when(productProperties.getProductId()).thenReturn(productId);
        doNothing().when(productRepository).save(productArgumentCaptor.capture());

        uut.initializeProduct();

        verify(productRepository).save(any());
        assertEquals(productId, productArgumentCaptor.getValue().getProductId());
    }
}
