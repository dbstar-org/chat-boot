package io.github.dbstarll.chat.boot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dbstarll.utils.openai.OpenAiAsyncClient;
import io.github.dbstarll.utils.openai.OpenAiClient;
import org.apache.hc.client5.http.async.HttpAsyncClient;
import org.apache.hc.client5.http.classic.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OpenAiConfiguration {
    @Bean
    @ConditionalOnMissingBean(OpenAiClient.class)
    OpenAiClient openAiClient(final HttpClient httpClient, final ObjectMapper objectMapper,
                              @Value("${char.open-ai.api-key}") final String apiKey) {
        return new OpenAiClient(httpClient, objectMapper, apiKey);
    }

    @Bean
    @ConditionalOnMissingBean(OpenAiAsyncClient.class)
    OpenAiAsyncClient openAiAsyncClient(final HttpAsyncClient httpClient, final ObjectMapper objectMapper,
                                        @Value("${char.open-ai.api-key}") final String apiKey) {
        return new OpenAiAsyncClient(httpClient, objectMapper, apiKey);
    }
}
