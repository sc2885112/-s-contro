package com.solantec.contro.filter.gateway;

import com.solantec.contro.*;
import com.solantec.contro.config.ResourceDefindation;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class GatewayComputFilter extends ControVariableUtils implements GlobalFilter, Ordered {

    ControContext controContext;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Contro contro = getCurrentContro();
        if (contro != null) {
            ResourceDefindation defindation = contro.getResourceDefindation();
            ResourceState resourceState = controContext.getResourceStates().get(defindation.getKey());
            if (resourceState != null) {
                return chain.filter(exchange)
                        .doOnError(throwable -> contro.release())
                        .then(Mono.create(monoSink -> resourceState.update(isFail())));
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 1;
    }

    public GatewayComputFilter(DefaultControContext defaultControContext) {
        this.controContext = defaultControContext;
    }
}
