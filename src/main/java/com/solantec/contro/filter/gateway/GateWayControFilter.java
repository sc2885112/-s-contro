package com.solantec.contro.filter.gateway;

import com.solantec.contro.Contro;
import com.solantec.contro.ControVariableUtils;
import com.solantec.contro.StringMatchControContext;
import com.solantec.contro.config.ResourceDefindation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


public class GateWayControFilter extends ControVariableUtils implements GlobalFilter, Ordered {

    private static final Log log = LogFactory.getLog(GateWayControFilter.class);

    private StringMatchControContext smContext;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();
        Contro contro = smContext.getContro(path);
        if (contro != null) {
            try {
                ResourceDefindation defindation = contro.getResourceDefindation();
                setCurrentContro(contro);
                if (contro.tryAcquire()) {
                    setFail(false);
                    log.debug("path:" + path + "got resource,key:" + defindation.getKey().get().toString() + ",current resources num:" + contro.getRemainingResource());
                    return chain.filter(exchange)
                            .doOnError(throwable -> contro.release())
                            .doOnCancel(() -> contro.release())
                            .thenEmpty(s -> {
                                contro.release();
                                log.debug("path:" + path + "release resource ,key:" + defindation.getKey().get().toString() + ",current resources num:" + contro.getRemainingResource());
                            });
                } else {
                    setFail(true);
                    return chain.filter(exchange);
                }
            } catch (InterruptedException e) {
                log.info("", e);
            }finally {
                contro.release();
            }
        }
        return chain.filter(exchange);
    }


    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

    public void setControContext(StringMatchControContext controContext) {
        this.smContext = controContext;
    }
}
