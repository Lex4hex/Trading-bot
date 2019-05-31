package com.lex4hex.trading_bot.repository;

import com.lex4hex.trading_bot.exception.TradingBotException;
import com.lex4hex.trading_bot.model.Product;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple in-memory repository for {@link Product} persistence
 */
@Repository
public class ProductRepository {

    /**
     * In memory storage of products, where key is {@link Product}'s id.
     */
    private final Map<String, Product> products = new ConcurrentHashMap<>(10);

    /**
     * Saves provided {@link Product} in repository
     *
     * @param product {@link Product} to save
     */
    public void save(Product product) {
        products.put(product.getProductId(), product);
    }

    /**
     * Finds {@link Product} by id
     */
    public Product findById(String id) {
        if (!products.containsKey(id)) {
            throw new TradingBotException("No product with id = " + id);
        }

        return products.get(id);
    }
}
