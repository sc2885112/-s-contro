package com.solantec.contro;


import com.solantec.contro.config.ResourceDefindation;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class Contro {

    private DefaultControContext defaultControContext;
    private ResourceDefindation resourceDefindation;
    private final Sync sync;
    private TimeUnit timeUnit = TimeUnit.SECONDS;
    protected ResourceState resourceState;

    abstract static class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 1192457210091910933L;
        private int limit;
        Sync(int limit) {
            setState(limit);
            this.limit = limit;
        }

        final int nonfairTryAcquireShared(int acquires) {
            for (; ; ) {
                int available = getState();
                int remaining = available - acquires;
                if (remaining < 0 ||
                        compareAndSetState(available, remaining))
                    return remaining;
            }
        }

        protected final boolean tryReleaseShared(int releases) {
            for (; ; ) {
                int current = getState();
                int next = current + releases;
                if (next < current) // overflow
                    throw new Error("Maximum permit count exceeded");

                if (compareAndSetState(current, next))
                    return true;
            }
        }

        final int clear() {
            for (; ; ) {
                int current = getState();
                if (current == 0 || compareAndSetState(current, 0))
                    return current;
            }
        }

        final int downgrade(int reductions) {
            for (; ; ) {
                int current = getState();
                int next = current - reductions;
                if (next > current) // underflow
                    throw new Error("Permit count underflow");
                if (compareAndSetState(current, next))
                    this.limit = next;
                    return next;
            }
        }

        public int upgrade(int val) {
            for (; ; ) {
                int current = getState();
                int upgrade = current + val;
                if (compareAndSetState(current, upgrade))
                    this.limit = upgrade;
                    return upgrade;
            }
        }

        final int getRemainingResource() {
            return getState();
        }

        void release(){
            if (getState() < limit) {
                releaseShared(1);
            }
        }
    }

    static final class NonfairSync extends Sync {
        private static final long serialVersionUID = -2694183684443567898L;

        NonfairSync(int limit) {
            super(limit);
        }

        protected int tryAcquireShared(int acquires) {
            return nonfairTryAcquireShared(acquires);
        }
    }

    static final class FairSync extends Sync {
        private static final long serialVersionUID = 2014338818796000944L;

        FairSync(int limit) {
            super(limit);
        }

        protected int tryAcquireShared(int acquires) {
            for (; ; ) {
                if (hasQueuedPredecessors())
                    return -1;
                int available = getState();
                int remaining = available - acquires;
                if (remaining < 0 ||
                        compareAndSetState(available, remaining))
                    return remaining;
            }
        }
    }

    public Contro(int limit) {
        this(limit, false);
    }

    public Contro(int limit, boolean fair) {
        this.sync = fair ? new FairSync(limit) : new NonfairSync(limit);
    }

    public Contro(ResourceDefindation resourceDefindation) {
        this(resourceDefindation.getLimit(), resourceDefindation.isFair());
        this.resourceDefindation = resourceDefindation;
    }

    public Contro(ResourceDefindation resourceDefindation, DefaultControContext defaultControContext) {
        this(resourceDefindation.getLimit(), resourceDefindation.isFair());
        this.defaultControContext = defaultControContext;
        this.resourceDefindation = resourceDefindation;
    }

    public Contro(ResourceDefindation resourceDefindation, DefaultControContext defaultControContext, ResourceState resourceState) {
        this(resourceDefindation.getLimit(), resourceDefindation.isFair());
        this.defaultControContext = defaultControContext;
        this.resourceDefindation = resourceDefindation;
        this.resourceState = resourceState;
    }

    public DefaultControContext getDefaultControContext() {
        return defaultControContext;
    }

    public ResourceDefindation getResourceDefindation() {
        return resourceDefindation;
    }

    public void setDefaultControContext(DefaultControContext defaultControContext) {
        this.defaultControContext = defaultControContext;
    }

    public void setResourceDefindation(ResourceDefindation resourceDefindation) {
        this.resourceDefindation = resourceDefindation;
    }

    /**
     * 升级
     * 增加资源每秒qps的限值
     */
    public int upgrade(int upgrade) {
        int reslut = -1;
        if (upgrade >= 0){
            reslut = sync.upgrade(upgrade);
            if (resourceState != null) resourceState.setLimit(sync.limit);
        }
        return reslut;
    }

    /**
     * 降级
     * 降低每秒qps的限值
     */
    public int downgrade(int downgrade) {
        int reslut = -1;
        if (downgrade >= 0){
            reslut = sync.downgrade(downgrade);
            if (resourceState != null) resourceState.setLimit(sync.limit);
        }
        return reslut;
    }

    public boolean tryAcquire(long timeout) throws InterruptedException {
        return tryAcquire(2, timeout, timeUnit);
    }

    public boolean tryAcquire(int limit, long timeout, TimeUnit unit) throws InterruptedException {
        if (limit < 0) throw new IllegalArgumentException();
        return sync.tryAcquireSharedNanos(limit, unit.toNanos(timeout));
    }

    public boolean tryAcquire() throws InterruptedException {
        long timeOut = resourceDefindation.getTimeOut();
        return tryAcquire(1, timeOut == 0 ? 2 : timeOut, timeUnit);
    }

    public boolean tryAcquire(int limit) throws InterruptedException {
        long timeOut = resourceDefindation.getTimeOut();
        return tryAcquire(limit, timeOut == 0 ? 2 : timeOut, timeUnit);
    }

    public void release() {
        sync.release();
    }

    public boolean release(int limit) {
        return sync.releaseShared(limit);
    }

    public void clear() {
        sync.clear();
    }

    public int getRemainingResource() {
        return sync.getRemainingResource();
    }
}
