package com.solantec.contro.config;


import com.solantec.contro.DefaultControContext;
import com.solantec.contro.StringMatchControContext;
import com.solantec.contro.filter.web.WebComputFilter;
import com.solantec.contro.filter.web.WebControFilter;
import com.solantec.contro.filter.web.WebFailureFilter;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.GenericFilterBean;

@ConditionalOnClass(GenericFilterBean.class)
@AutoConfigureAfter(SControAutoConfiguration.class)
public class WebFilterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(type = "com.solantec.contro.filter.gateway.GateWayControFilter")
    public WebControFilter webControFilter(StringMatchControContext controContext) {
        return new WebControFilter(controContext);
    }

    @Bean
    @ConditionalOnMissingBean(type = "com.solantec.contro.filter.gateway.GatewayComputFilter")
    public WebComputFilter webComputFilter(DefaultControContext defaultControContext) {
        return new WebComputFilter(defaultControContext);
    }

    @Bean
    @ConditionalOnMissingBean(type = "com.solantec.contro.filter.gateway.GatewayFailureFilter")
    public WebFailureFilter webFailureFilter() {
        return new WebFailureFilter();
    }
}
