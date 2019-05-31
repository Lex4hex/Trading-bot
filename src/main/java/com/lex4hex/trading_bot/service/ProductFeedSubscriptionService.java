package com.lex4hex.trading_bot.service;

import com.lex4hex.trading_bot.model.Product;
import com.lex4hex.trading_bot.model.feed.ProductSubscriptionRequest;
import com.lex4hex.trading_bot.properties.ProductProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;

@Service
@AllArgsConstructor
public class ProductFeedSubscriptionService {

    private ProductProperties productProperties;

    /**
     * Subscribes for websocket quote feed for provided {@link Product}
     *
     * @param session Websocket feed session
     * @throws IOException     In case of websocket IO errors
     * @throws EncodeException In case of outgoing message encoding errors
     */
    public void subscribe(Session session) throws IOException, EncodeException {
        ProductSubscriptionRequest subscribeRequestDto = new ProductSubscriptionRequest();
        subscribeRequestDto.addSubscribeTo(productProperties.getProductId());

        session.getBasicRemote().sendObject(subscribeRequestDto);
    }
}
