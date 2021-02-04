package com.solantec.contro.config;

import com.solantec.contro.*;
import com.solantec.contro.key.Key;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties("spring.scontro")
@Validated
@ComponentScan("com.solantec.contro.controller")
public class SControAutoConfiguration {
    private static final Log log = LogFactory.getLog(SControAutoConfiguration.class);

    @Valid
    private List<WebResourceDefindation> reources = new ArrayList<>();

    @Bean
    public DefaultControContext controContext() {
        //为了记录程序启动时间
        long runtime = ControTimeUtil.RUNTIME;
        if (reources.size() > 0) {
            Map<Key, Contro> controMap = new HashMap<>(reources.size());
            Map<Key, ResourceState> states = new HashMap<>(reources.size());
            DefaultControContext defaultControContext = new DefaultControContext(controMap, reources, states);
            for (int i = 0; i < reources.size(); i++) {
                ResourceDefindation defindation = reources.get(i);
                Contro contro = new Contro(defindation, defaultControContext);
                Key key = defindation.getKey();
                controMap.put(key, contro);
                if (defindation.isWatch()) {
                    states.put(key, new ResourceState(key, defindation.getLimit()));
                }
            }
            return defaultControContext;
        } else {
            log.warn("that's 'sContro reources' like null");
            return new DefaultControContext();
        }
    }

    @Bean
    public StringMatchControContext stringMatchControContext(DefaultControContext defaultControContext) {
        return new StringMatchControContext(defaultControContext);
    }

    public List<WebResourceDefindation> getReources() {
        return reources;
    }

    public void setReources(List<WebResourceDefindation> reources) {
        this.reources = reources;
    }

}
