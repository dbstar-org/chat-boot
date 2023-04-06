package io.github.dbstarll.chat.boot.config;

import io.github.dbstarll.utils.http.client.HttpClientFactory;
import org.apache.hc.client5.http.async.HttpAsyncClient;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class HttpClientConfiguration {
    private static final int TIMEOUT = 5000;

    @Bean
    @ConditionalOnMissingBean(HttpClientFactory.class)
    HttpClientFactory httpClientFactory() {
        return new HttpClientFactory().setAutomaticRetries(false).setConnectTimeout(TIMEOUT).setSocketTimeout(TIMEOUT);
    }

    @Bean
    @ConditionalOnMissingBean(HttpClient.class)
    CloseableHttpClient httpClient(final HttpClientFactory httpClientFactory) {
        return httpClientFactory.build();
    }

    @Bean(initMethod = "start")
    @ConditionalOnMissingBean(HttpAsyncClient.class)
    CloseableHttpAsyncClient httpAsyncClient(final HttpClientFactory httpClientFactory) {
        return httpClientFactory.buildAsync();
    }
}
