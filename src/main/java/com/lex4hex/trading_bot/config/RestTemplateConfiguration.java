package com.lex4hex.trading_bot.config;

import com.lex4hex.trading_bot.model.trading_api.ErrorResponse;
import com.lex4hex.trading_bot.properties.ConnectionProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;

@Configuration
@AllArgsConstructor
@Slf4j
public class RestTemplateConfiguration {

    private ConnectionProperties connectionProperties;

    /**
     * Configures {@link RestTemplate}, sets necessary headers
     */
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList((request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.add("Authorization", connectionProperties.getAuthorization());
            headers.add("Accept-language", connectionProperties.getLanguage());
            headers.add("Content-Type", connectionProperties.getContentType());
            headers.add("Accept", connectionProperties.getContentType());

            return execution.execute(request, body);
        }));

        restTemplate.setErrorHandler(new ApiExceptionHandler());

        return restTemplate;
    }

    /**
     * Basic API error handler
     */
    class ApiExceptionHandler extends DefaultResponseErrorHandler {
        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            final ErrorResponse errorResponse = Mapper.instance.readValue(response.getBody(), ErrorResponse.class);

            log.error("Error occurred during trading API call. Message: {}, developer message: {}, error code: {}",
                    errorResponse.getMessage(), errorResponse.getDeveloperMessage(), errorResponse.getErrorCode());
        }
    }
}
