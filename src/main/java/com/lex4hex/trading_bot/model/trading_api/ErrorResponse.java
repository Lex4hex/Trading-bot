package com.lex4hex.trading_bot.model.trading_api;

import com.lex4hex.trading_bot.config.RestTemplateConfiguration;
import lombok.Data;

/**
 * Represents error response of trading api.
 * Used for API exception handling {@link RestTemplateConfiguration}
 */
@Data
public class ErrorResponse {
    /**
     * User readable message
     */
    private String message;

    /**
     * Developer additional info
     */
    private String developerMessage;

    private String errorCode;
}
