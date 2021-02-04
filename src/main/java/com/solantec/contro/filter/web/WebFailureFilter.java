package com.solantec.contro.filter.web;

import com.solantec.contro.Contro;
import com.solantec.contro.ControVariableUtils;
import com.solantec.contro.config.WebResourceDefindation;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class WebFailureFilter extends ControVariableUtils implements Filter, Ordered {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        Contro contro = getCurrentContro();
        if (contro != null) {
            if (isFail()) {
                HttpServletRequest request = (HttpServletRequest) req;
                WebResourceDefindation defindation = (WebResourceDefindation) contro.getResourceDefindation();
                String fallbackUri = defindation.getFallbackUri();
                if (fallbackUri != null) {
                    fallbackUri = fallbackUri.trim();
                    if (!StringUtils.isEmpty(fallbackUri)) {
                        request.getRequestDispatcher(fallbackUri).forward(req, res);
                    } else {
                        request.getRequestDispatcher(DEFAULT_FALLBACK_PATH).forward(req, res);
                    }
                } else {
                    request.getRequestDispatcher(DEFAULT_FALLBACK_PATH).forward(req, res);
                }
            } else {
                chain.doFilter(req, res);
            }
        } else {
            chain.doFilter(req, res);
        }
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 1 + 1;
    }
}
