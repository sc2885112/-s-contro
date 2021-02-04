package com.solantec.contro.config;


import com.solantec.contro.key.Key;
import org.springframework.validation.annotation.Validated;

@Validated
public class ResourceDefindation {
    protected Key key;
    private int limit = 5;
    private long timeOut = 0;

    /*是否公平获取资源*/
    private boolean isFair = false;

    private boolean watch = false;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public long getTimeOut() {
        return timeOut;
    }

    public boolean isFair() {
        return isFair;
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }

    public void setFair(boolean fair) {
        isFair = fair;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public boolean isWatch() {
        return watch;
    }

    public void setWatch(boolean watch) {
        this.watch = watch;
    }
}
