package com.solantec.contro;

public class ControTimeUtil {
    public static final long RUNTIME;

    static {
        RUNTIME = System.currentTimeMillis();
    }

    public static long getRunTimeS() {
        return (System.currentTimeMillis() - RUNTIME) / 1000;
    }

    public static long getRunTimeS(long startTime) {
        return (System.currentTimeMillis() - startTime) / 1000;
    }

    public static int getRunTimeMin() {
        return (int) getRunTimeS() / 60;
    }

    public static int getRunTimeMin(long startTime) {
        return (int) getRunTimeS(startTime) / 60;
    }

    public static int getRunTimeHours() {
        return getRunTimeMin() / 60;
    }

    public static int getRunTimeHours(long startTime) {
        return getRunTimeMin(startTime) / 60;
    }

    public static int getRunTimeDays() {
        return getRunTimeHours() / 24;
    }

    public static int getRunTimeDays(long startTime) {
        return getRunTimeHours(startTime) / 24;
    }

    public static int getRunTimeMonth() {
        return getRunTimeDays() / 30;
    }

    public static int getRunTimeMonth(long startTime) {
        return getRunTimeDays(startTime) / 30;
    }

    /**
     * @see #RUN 程序启动时间
     * @see #START 资源监控开始时间
     */
    class TimeScope {
        public static final String RUN = "1";
        public static final String START = "2";
    }
}
