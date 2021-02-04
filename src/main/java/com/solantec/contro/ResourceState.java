package com.solantec.contro;

import com.solantec.contro.key.Key;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ResourceState {

    /**
     * 资源监控开始时间
     */
    private long startTime = 0;

    private Key key;

    private AtomicInteger qps = new AtomicInteger(0);

    private AtomicInteger successCount = new AtomicInteger(0);

    private AtomicInteger failCount = new AtomicInteger(0);

    private AtomicInteger totalCount = new AtomicInteger(0);

    private AtomicInteger limit = new AtomicInteger(0);

    /**
     * 平均每秒成功的请求数
     */
    private AtomicLong avgSuccessS = new AtomicLong(0);

    /**
     * 平均每秒失败的请求数
     */
    private AtomicLong avgFailS = new AtomicLong(0);

    /**
     * 平均每分钟成功的请求数
     */
    private AtomicInteger avgSuccessMin = new AtomicInteger(0);

    /**
     * 平均每分钟失败的请求数
     */
    private AtomicInteger avgFailMin = new AtomicInteger(0);

    private AtomicLong updateTime = new AtomicLong(0);

    /**
     * 时间区域
     * {@link ControTimeUtil.TimeScope}
     */
    private String timeScope = ControTimeUtil.TimeScope.START;

    public ResourceState(Key key) {
        this.key = key;
    }

    public ResourceState(Key key,int limit) {
        this.key = key;
        this.limit = new AtomicInteger(limit);
    }

    public void update(int size, boolean isFail) {
        if (startTime == 0) startTime = System.currentTimeMillis();

        computQPS(size);
        computSuccessCount(size, isFail);
        computFailCount(size, isFail);
        computTotalCount();

        long computTime = startTime;
        if(timeScope.equals(ControTimeUtil.TimeScope.RUN))computTime = ControTimeUtil.RUNTIME;

        computAvgSuccessS(isFail,computTime);
        computAvgFailS(isFail,computTime);
        computAvgSuccessMin(isFail,computTime);
        computAvgFailMin(isFail,computTime);
    }

    public void update(boolean isFail) {
        update(1, isFail);
    }

    private void computQPS(int size) {
        long currentTime = System.currentTimeMillis();
        if (updateTime.get() == 0) updateTime.compareAndSet(updateTime.get(), currentTime);
        if ((currentTime - updateTime.get()) / 1000 >= 1) {
            qps.compareAndSet(qps.get(), size);
            updateTime.compareAndSet(updateTime.get(), currentTime);
        } else {
            qps.compareAndSet(qps.get(), qps.get() + size);
        }
    }

    private void computSuccessCount(int size, boolean isFail) {
        if (!isFail) successCount.compareAndSet(successCount.get(), successCount.get() + size);
    }

    private void computFailCount(int size, boolean isFail) {
        if (isFail) failCount.compareAndSet(failCount.get(), failCount.get() + size);
    }

    private void computTotalCount() {
        int total = totalCount.get();
        int suc = successCount.get();
        int fai = failCount.get();
        totalCount.compareAndSet(total, (suc + fai));
    }

    private void computAvgSuccessS(boolean isFail,long computTime) {
        if (!isFail) {
            long runTimeMillis = ControTimeUtil.getRunTimeS(computTime);
            if (runTimeMillis > 0){
                long avgSuc = successCount.get() / runTimeMillis;
                avgSuccessS.compareAndSet(avgSuccessS.get(), avgSuc);
            }
        }
    }

    private void computAvgFailS(boolean isFail,long computTime) {
        if (isFail) {
            long runTimeMillis = ControTimeUtil.getRunTimeS(computTime);
            if (runTimeMillis > 0){
                long avgFai = successCount.get() / runTimeMillis;
                avgFailS.compareAndSet(avgFailS.get(), avgFai);
            }
        }
    }

    private void computAvgSuccessMin(boolean isFail,long computTime) {
        if (!isFail) {
            int runTimeMin = ControTimeUtil.getRunTimeMin(computTime);
            if (runTimeMin > 0){
                int avgSuc = successCount.get() / runTimeMin;
                avgSuccessMin.compareAndSet(avgSuccessMin.get(), avgSuc);
            }
        }
    }

    private void computAvgFailMin(boolean isFail,long computTime) {
        if (isFail) {
            int runTimeMin = ControTimeUtil.getRunTimeMin(computTime);
            if (runTimeMin > 0){
                int avgFail = failCount.get() / runTimeMin;
                avgFailMin.compareAndSet(avgFailMin.get(), avgFail);
            }
        }
    }

    public AtomicInteger getQps() {
        return qps;
    }

    public AtomicInteger getSuccessCount() {
        return successCount;
    }

    public AtomicInteger getFailCount() {
        return failCount;
    }

    public AtomicInteger getTotalCount() {
        return totalCount;
    }

    public AtomicLong getAvgSuccessS() {
        return avgSuccessS;
    }

    public AtomicLong getAvgFailS() {
        return avgFailS;
    }

    public AtomicInteger getAvgSuccessMin() {
        return avgSuccessMin;
    }

    public AtomicInteger getAvgFailMin() {
        return avgFailMin;
    }

    public long getStartTime() {
        return startTime;
    }

    public AtomicLong getUpdateTime() {
        return updateTime;
    }

    public Key getKey() {
        return key;
    }

    public void setTimeScope(String timeScope) {
        this.timeScope = timeScope;
    }

    public AtomicInteger getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit.compareAndSet(this.limit.get(),limit);
    }
}
