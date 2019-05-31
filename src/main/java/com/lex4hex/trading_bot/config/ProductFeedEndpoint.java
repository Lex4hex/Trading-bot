package com.lex4hex.trading_bot.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lex4hex.trading_bot.exception.TradingBotException;
import com.lex4hex.trading_bot.handler.MessageHandler;
import com.lex4hex.trading_bot.model.feed.ProductSubscriptionRequest;
import com.lex4hex.trading_bot.properties.ConnectionProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.websocket.ClientEndpoint;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.ContainerProvider;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * Websocket client endpoint component.
 */
@Component
@Slf4j
@ClientEndpoint(configurator = ProductFeedEndpoint.WebSocketConfigurator.class,
        encoders = ProductFeedEndpoint.SubscribeEncoder.class)
public class ProductFeedEndpoint {
    /**
     * Static authorization field for passing property from {@link ConnectionProperties} to static
     * {@link WebSocketConfigurator}
     */
    private static String authorization;

    /**
     * Static language field for passing property from {@link ConnectionProperties} to static
     * {@link WebSocketConfigurator}
     */
    private static String language;

    private final ConnectionProperties connectionProperties;

    private final List<MessageHandler> messageHandlers;

    public ProductFeedEndpoint(ConnectionProperties connectionProperties,
            List<MessageHandler> messageHandlers) {
        this.connectionProperties = connectionProperties;
        this.messageHandlers = messageHandlers;
        authorization = connectionProperties.getAuthorization();
        language = connectionProperties.getLanguage();
    }

    /**
     * Creates a websocket session
     */
    @Bean
    private Session getSession() throws Exception {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        return container.connectToServer(this, new URI(connectionProperties.getProductFeedUrl()));
    }

    /**
     * Receives message and forwards it to handlers for processing
     */
    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        for (MessageHandler messageHandler : messageHandlers) {
            messageHandler.handle(message, session);
        }
    }

    /**
     * Closes session when error occurred during message handling
     */
    @OnError
    public void onError(Session session, Throwable throwable) throws IOException {
        log.error("Error occurred during websocket connection. Socket is closing", throwable);
        session.close();
    }

    /**
     * Websocket configuration, adds headers to websocket requests
     */
    public static class WebSocketConfigurator extends ClientEndpointConfig.Configurator {

        @Override
        public void beforeRequest(Map<String, List<String>> headers) {
            headers.put("Authorization", Collections.singletonList(authorization));
            headers.put("Accept-Language", Collections.singletonList(language));
        }
    }

    /**
     * Encoder of {@link ProductSubscriptionRequest} for sending subscription message to websocket
     */
    public static class SubscribeEncoder implements Encoder.Text<ProductSubscriptionRequest> {

        @Override
        public String encode(ProductSubscriptionRequest productSubscriptionRequest) {
            try {
                return Mapper.instance.writeValueAsString(productSubscriptionRequest);
            } catch (JsonProcessingException e) {
                throw new TradingBotException("Couldn't serialize request for product subscription", e);
            }
        }

        @Override
        public void init(EndpointConfig endpointConfig) {
            // not required
        }

        @Override
        public void destroy() {
            // not required
        }
    }
}





