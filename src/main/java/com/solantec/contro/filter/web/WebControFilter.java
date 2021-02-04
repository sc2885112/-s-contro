package com.solantec.contro.filter.web;

import com.solantec.contro.Contro;
import com.solantec.contro.ControVariableUtils;
import com.solantec.contro.StringMatchControContext;
import com.solantec.contro.config.ResourceDefindation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.Ordered;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


public class WebControFilter extends ControVariableUtils implements Filter, Ordered {
    private static final Log log = LogFactory.getLog(WebControFilter.class);

    private StringMatchControContext controContext;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

        String requestURI = request.getRequestURI();
        Contro contro = controContext.getContro(requestURI);
        if (contro != null) {
            try {
                ResourceDefindation defindation = contro.getResourceDefindation();
                setCurrentContro(contro);
                if (contro.tryAcquire()) {
                    log.debug("path:" + requestURI + "got resource,key:" + defindation.getKey().get().toString() + ",current resources num:" + contro.getRemainingResource());
                    setFail(false);
                    try {
                        chain.doFilter(req, res);
                    } finally {
                        contro.release();
                        log.debug("path:" + requestURI + "got resource,key:" + defindation.getKey().get().toString() + ",current resources num:" + contro.getRemainingResource());
                    }
                } else {
                    setFail(true);
                    chain.doFilter(req, res);
                }
            } catch (InterruptedException e) {
                log.error("", e);
            }finally {
                contro.release();
            }
        } else {
            chain.doFilter(req, res);
        }
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

    public WebControFilter(StringMatchControContext controContext) {
        this.controContext = controContext;
    }
}
