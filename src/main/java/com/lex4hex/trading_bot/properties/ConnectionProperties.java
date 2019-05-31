package com.lex4hex.trading_bot.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * Trading API and websocket feed connection properties
 */
@ConfigurationProperties("trading.bot.connection")
@Data
@Component
@Validated
public class ConnectionProperties {
    /**
     * Authorization header used in both websocket and rest api connections
     */
    @NotBlank
    private String authorization;

    /**
     * Accepted language header used in both websocket and rest api connections
     */
    @NotBlank
    private String language;

    /**
     * Content type header used in both websocket and rest api connections
     */
    @NotBlank
    private String contentType;

    /**
     * Websocket product feed connection url
     */
    @NotBlank
    private String productFeedUrl;

    /**
     * API url for buy trading requests
     */
    @NotBlank
    private String buyApiUrl;

    /**
     * API url for close trading requests
     */
    @NotBlank
    private String closeApiUrl;
}
