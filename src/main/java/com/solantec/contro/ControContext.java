package com.solantec.contro;

import com.solantec.contro.config.WebResourceDefindation;
import com.solantec.contro.key.Key;

import java.util.List;
import java.util.Map;

public interface ControContext {

    Map<Key, Contro> getContros();

    List<WebResourceDefindation> getResources();

    List<Key> getKeys();

    Map<Key, ResourceState> getResourceStates();

    Contro getContro(Key key);

    ResourceState getResourceState(Key key);
}
