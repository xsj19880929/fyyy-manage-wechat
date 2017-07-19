package com.suryani.manage.common;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author soldier
 */
public class IPHost {
    private final String ip;
    private final int port;
    private final AtomicInteger failedCount = new AtomicInteger(0);

    public IPHost(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }


    public int getFailedCount() {
        return failedCount.get();
    }

    public void clearFailedCount() {
        failedCount.set(0);
    }

    public void incrementAndGet() {
        failedCount.incrementAndGet();
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
