package com.lex4hex.trading_bot.service;

import com.lex4hex.trading_bot.exception.TradingApiException;
import com.lex4hex.trading_bot.exception.TradingBotException;
import com.lex4hex.trading_bot.model.Product;
import com.lex4hex.trading_bot.model.feed.Quote;
import com.lex4hex.trading_bot.model.trading_api.Amount;
import com.lex4hex.trading_bot.model.trading_api.SourceType;
import com.lex4hex.trading_bot.model.trading_api.TradeDirection;
import com.lex4hex.trading_bot.model.trading_api.TradeRequest;
import com.lex4hex.trading_bot.model.trading_api.TradeResponse;
import com.lex4hex.trading_bot.properties.ConnectionProperties;
import com.lex4hex.trading_bot.properties.ProductProperties;
import com.lex4hex.trading_bot.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * {@inheritDoc}
 */
@Service
@AllArgsConstructor
@Slf4j
public class PositionService implements PositionAction {

    private RestTemplate restTemplate;

    private ProductRepository productRepository;

    private ConnectionProperties connectionProperties;

    private ProductProperties productProperties;

    /**
     * {@inheritDoc}
     */
    @Override
    public TradeResponse openPosition(Quote quote) {
        String productId = quote.getProductId();

        TradeRequest tradeRequest = TradeRequest.builder()
                .productId(productId)
                .investingAmount(new Amount("BUX", 2, productProperties.getInvestingAmount()))
                .leverage(productProperties.getLeverage())
                .direction(TradeDirection.BUY)
                .sourceType(SourceType.OTHER)
                .build();

        ResponseEntity<TradeResponse>
                orderResponseEntity = restTemplate.postForEntity(connectionProperties.getBuyApiUrl(), tradeRequest,
                TradeResponse.class);

        final TradeResponse tradeResponse = orderResponseEntity.getBody();

        if (tradeResponse == null) {
            throw new TradingApiException("Empty trading API response received on position opening");
        }

        productRepository.save(new Product(tradeResponse.getProduct().getProductId(), tradeResponse.getPositionId(),
                tradeResponse.getPrice().getAmount()));

        return tradeResponse;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public TradeResponse closePosition(Quote quote) {
        Product product = productRepository.findById(quote.getProductId());

        if (!product.isBought()) {
            throw new TradingBotException("Product was not bought, can't close the position");
        }

        ResponseEntity<TradeResponse> closePositionResponse =
                restTemplate.exchange(buildCloseRequestUri(product),
                        HttpMethod.DELETE, null,
                        TradeResponse.class);

        if (closePositionResponse.getBody() == null) {
            throw new TradingApiException("Empty trading API response received on position closing");
        }

        // Indicates that product is not bought anymore
        product.setPositionId(null);

        return closePositionResponse.getBody();
    }

    /**
     * Builds an {@link URI} for closing position trading API request.
     * Uses a template in url to set a position id path parameter
     *
     * @param product Product with positionId
     * @return URI for close position request
     */
    private URI buildCloseRequestUri(Product product) {
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("position", product.getPositionId());

        return UriComponentsBuilder.fromHttpUrl(connectionProperties.getCloseApiUrl())
                .buildAndExpand(uriParams).toUri();
    }
}
