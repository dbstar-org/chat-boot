package io.github.dbstarll.chat.boot;

import io.github.dbstarll.utils.http.client.HttpClientFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.Proxy;
import java.net.Proxy.Type;

@Configuration
class HttpClientConfiguration {
    @Bean
    @ConditionalOnMissingBean(HttpClientFactory.class)
    HttpClientFactory httpClientFactory() {
        final HttpClientFactory factory = new HttpClientFactory();

        final String socksProxyHost = System.getProperty("socksProxyHost");
        final String socksProxyPort = System.getProperty("socksProxyPort");
        if (StringUtils.isNoneBlank(socksProxyHost, socksProxyPort)) {
            final Proxy proxy = HttpClientFactory.proxy(Type.SOCKS, socksProxyHost, Integer.parseInt(socksProxyPort));
            factory.setProxy(proxy).setResolveFromProxy(true);
        }

        return factory.setAutomaticRetries(false);
    }

    @Bean
    @ConditionalOnMissingBean(HttpClient.class)
    HttpClient httpClient(final HttpClientFactory httpClientFactory) {
        return httpClientFactory.build();
    }
}
