package com.solantec.contro;

import com.solantec.contro.config.WebResourceDefindation;
import com.solantec.contro.config.ResourceDefindation;
import com.solantec.contro.key.Key;
import java.util.*;

public class DefaultControContext implements ControContext {

    private Map<Key, Contro> contros;
    private List<WebResourceDefindation> resources;
    private List<Key> keys = new ArrayList<>();
    private Map<Key, ResourceState> resourceStates;

    public Contro getContro(Key key) {
        return this.contros.get(key);
    }

    public DefaultControContext(Map<Key, Contro> contros) {
        this.contros = contros;
    }

    public DefaultControContext(Map<Key, Contro> contros, List<WebResourceDefindation> resources) {
        this.contros = contros;
        this.resources = resources;
    }

    public DefaultControContext(Map<Key, Contro> contros, List<WebResourceDefindation> resources, Map<Key, ResourceState> resourceStates) {
        this.contros = contros;
        this.resources = resources;
        this.resourceStates = resourceStates;
    }

    public DefaultControContext(Map<Key, Contro> contros, List<WebResourceDefindation> resources, List<Key> keys, Map<Key, ResourceState> resourceStates) {
        this.contros = contros;
        this.resources = resources;
        this.keys = keys;
        this.resourceStates = resourceStates;
    }

    @Override
    public Map<Key, Contro> getContros() {
        return contros;
    }

    @Override
    public List<WebResourceDefindation> getResources() {
        return resources;
    }

    public void addContro(Key key, Contro contro) {
        this.contros.put(key, contro);
    }

    public void addContro(Key key, int limit) {
        this.contros.put(key, new Contro(limit));
    }

    public void addContro(Key key, ResourceDefindation defindation) {
        this.contros.put(key, new Contro(defindation));
    }

    public void addContro(Key key, int climit, boolean isFair) {
        this.contros.put(key, new Contro(climit, isFair));
    }

    public DefaultControContext() {
        this.contros = new HashMap<>();
    }

    @Override
    public List<Key> getKeys() {
        if (keys.size() == 0) {
            initKeys();
        }
        return keys;
    }

    private synchronized void initKeys() {
        contros.keySet().stream().forEach(item -> keys.add(item));
    }

    @Override
    public Map<Key, ResourceState> getResourceStates() {
        return resourceStates;
    }

    @Override
    public ResourceState getResourceState(Key key) {
        return resourceStates.get(key);
    }
}

