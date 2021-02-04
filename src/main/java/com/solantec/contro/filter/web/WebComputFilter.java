package com.solantec.contro.filter.web;

import com.solantec.contro.*;
import com.solantec.contro.config.ResourceDefindation;
import org.springframework.core.Ordered;
import javax.servlet.*;
import java.io.IOException;

public class WebComputFilter extends ControVariableUtils implements Filter, Ordered {

    ControContext controContext;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
        Contro contro = getCurrentContro();
        if (contro != null) {
            ResourceDefindation defindation = contro.getResourceDefindation();
            ResourceState resourceState = controContext.getResourceStates().get(defindation.getKey());
            resourceState.update(isFail());
        }

    }

    public WebComputFilter(DefaultControContext defaultControContext) {
        this.controContext = defaultControContext;
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 1;
    }
}
