package com.lex4hex.trading_bot.service;

import com.lex4hex.trading_bot.model.Product;
import com.lex4hex.trading_bot.properties.ProductProperties;
import com.lex4hex.trading_bot.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@AllArgsConstructor
public class ProductInitializer {

    private ProductRepository productRepository;

    private ProductProperties productProperties;

    /**
     * Initializes provided product from properties
     */
    @PostConstruct
    public void initializeProduct() {
        productRepository.save(new Product(productProperties.getProductId(), null,
                null));
    }
}
