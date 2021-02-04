package com.solantec.contro.config;

import com.solantec.contro.DefaultControContext;
import com.solantec.contro.StringMatchControContext;
import com.solantec.contro.filter.gateway.GateWayControFilter;
import com.solantec.contro.filter.gateway.GatewayComputFilter;
import com.solantec.contro.filter.gateway.GatewayFailureFilter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.DispatcherHandler;

@ConditionalOnClass(GatewayAutoConfiguration.class)
@AutoConfigureBefore(GatewayAutoConfiguration.class)
@AutoConfigureAfter(SControAutoConfiguration.class)
public class GateWayFilterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(type = "com.solantec.contro.filter.web.WebControFilter")
    public GateWayControFilter controFilter(StringMatchControContext stringMatchControContext) {
        GateWayControFilter webFilter = new GateWayControFilter();
        webFilter.setControContext(stringMatchControContext);
        return webFilter;
    }

    @Bean
    @ConditionalOnMissingBean(type = "com.solantec.contro.filter.web.WebFailureFilter")
    public GatewayFailureFilter failureFilter(ObjectProvider<DispatcherHandler> dispatcherHandler) {
        GatewayFailureFilter gatewayFailureFilter = new GatewayFailureFilter();
        gatewayFailureFilter.setDispatcherHandlerProvider(dispatcherHandler);
        return gatewayFailureFilter;
    }

    @Bean
    @ConditionalOnMissingBean(type = "com.solantec.contro.filter.web.WebFailureFilter")
    public GatewayComputFilter computFilter(DefaultControContext defaultControContext) {
        return new GatewayComputFilter(defaultControContext);
    }
}
