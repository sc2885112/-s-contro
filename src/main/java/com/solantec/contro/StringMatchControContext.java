package com.solantec.contro;

import com.solantec.contro.config.WebResourceDefindation;
import com.solantec.contro.key.Key;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.Map;


/**
 * 使用字符串的方式匹配 {@link Contro}
 */
public class StringMatchControContext implements ControContext {

    private ControContext controContext;
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    public StringMatchControContext(ControContext controContext) {
        this.controContext = controContext;
    }

    public Contro getContro(String key) {
        Key key1 = serachKeys(key);
        return key1 == null ? null : controContext.getContro(key1);
    }

    public Key serachKeys(String match) {
        List<Key> keys = controContext.getKeys();
        for (Key k : keys) {
            if (k.get() instanceof String) {
                if (antPathMatcher.match((String) k.get(), match)) {
                    return k;
                }
            }
        }
        return null;
    }

    @Override
    public Map<Key, Contro> getContros() {
        return controContext.getContros();
    }

    @Override
    public List<WebResourceDefindation> getResources() {
        return controContext.getResources();
    }

    @Override
    public List<Key> getKeys() {
        return controContext.getKeys();
    }

    @Override
    public Map<Key, ResourceState> getResourceStates() {
        return controContext.getResourceStates();
    }

    @Override
    public Contro getContro(Key key) {
        return controContext.getContro(key);
    }

    @Override
    public ResourceState getResourceState(Key key) {
        return controContext.getResourceState(key);
    }
}
