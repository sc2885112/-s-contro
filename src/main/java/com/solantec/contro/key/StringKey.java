package com.solantec.contro.key;

public class StringKey implements Key<String> {

    private String key;

    public StringKey(String key) {
        this.key = key;
    }

    @Override
    public String get() {
        return key;
    }

    public String getKey() {
        return key;
    }
}
