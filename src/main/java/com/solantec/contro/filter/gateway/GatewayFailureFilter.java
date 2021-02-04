package com.solantec.contro.filter.gateway;

import com.solantec.contro.Contro;
import com.solantec.contro.ControVariableUtils;
import com.solantec.contro.config.WebResourceDefindation;
import com.solantec.contro.config.ResourceDefindation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;

public class GatewayFailureFilter extends ControVariableUtils implements GlobalFilter, Ordered {

    private ObjectProvider<DispatcherHandler> dispatcherHandlerProvider;

    private volatile DispatcherHandler dispatcherHandler;

    private static final Log log = LogFactory.getLog(GatewayFailureFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Contro contro = getCurrentContro();
        if (contro != null) {
            if (isFail()) {
                URI uri = null;
                try {
                    ResourceDefindation defindation = contro.getResourceDefindation();
                    String fallbackUri = null;
                    if (defindation instanceof WebResourceDefindation) {
                        fallbackUri = ((WebResourceDefindation) defindation).getFallbackUri();
                    }
                    uri = (fallbackUri != null && !StringUtils.isEmpty(fallbackUri))
                            ? new URI(fallbackUri) :
                            new URI(DEFAULT_FALLBACK_PATH);
                } catch (URISyntaxException e) {
                    log.info("", e);
                }
                ServerHttpRequest request = exchange.getRequest()
                        .mutate()
                        .uri(uri)
                        .build();
                return getDispatcherHandler()
                        .handle(exchange.mutate().request(request).build())
                        .doOnError(throwable -> contro.release());
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 1 + 1;
    }

    private DispatcherHandler getDispatcherHandler() {
        if (dispatcherHandler == null) {
            dispatcherHandler = dispatcherHandlerProvider.getIfAvailable();
        }

        return dispatcherHandler;
    }

    public void setDispatcherHandlerProvider(ObjectProvider<DispatcherHandler> dispatcherHandlerProvider) {
        this.dispatcherHandlerProvider = dispatcherHandlerProvider;
    }
}
