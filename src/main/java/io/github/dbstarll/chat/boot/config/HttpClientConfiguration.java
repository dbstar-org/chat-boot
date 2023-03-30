package io.github.dbstarll.chat.boot.config;

import io.github.dbstarll.utils.http.client.HttpClientFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.async.HttpAsyncClient;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClientBuilder;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.URIScheme;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.Proxy;
import java.net.Proxy.Type;

@Configuration
class HttpClientConfiguration {
    private static final int TIMEOUT = 5000;

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

        return factory.setAutomaticRetries(false).setConnectTimeout(TIMEOUT).setSocketTimeout(TIMEOUT);
    }

    @Bean
    @ConditionalOnMissingBean(HttpClient.class)
    HttpClient httpClient(final HttpClientFactory httpClientFactory) {
        return httpClientFactory.build();
    }

    @Bean
    @ConditionalOnMissingBean(HttpAsyncClient.class)
    HttpAsyncClient httpAsyncClient(final HttpClientFactory httpClientFactory) {
        return httpClientFactory.buildAsync(this::proxy);
    }

    private void proxy(final HttpAsyncClientBuilder builder) {
        final String httpProxyHost = System.getProperty("http.proxyHost");
        final String httpProxyPort = System.getProperty("http.proxyPort");
        if (StringUtils.isNoneBlank(httpProxyHost, httpProxyPort)) {
            builder.setProxy(new HttpHost(URIScheme.HTTP.id, httpProxyHost, Integer.parseInt(httpProxyPort)));
        }
    }
}
