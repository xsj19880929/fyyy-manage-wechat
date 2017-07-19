package com.suryani.manage.common;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author soldier
 */
public class IPProxyPool {
    public static final int CAPACITY = 50;
    private static final BlockingQueue<IPHost> QUEUE = new LinkedBlockingQueue<>(CAPACITY);

    /**
     * 获取资源,如果当前无可用资源会阻塞等待.
     *
     * @return
     * @throws InterruptedException
     */
    public static IPHost get() throws InterruptedException {
        return QUEUE.take();
    }

    /**
     * 将资源返回,如果已经满了,则丢弃
     *
     * @param ipHost
     * @return true, 表示成功返回, false表示丢弃
     */
    public static Boolean release(IPHost ipHost) {
        return QUEUE.offer(ipHost);
    }

    /**
     * 将资源放入池子,如果已经满了则阻塞,如果是释放资源请使用release方法
     *
     * @param ipHost
     * @throws InterruptedException
     */
    public static void put(IPHost ipHost) throws InterruptedException {
        QUEUE.put(ipHost);
    }

    public static int size() {
        return QUEUE.size();
    }

    public static Boolean isEmpty() {
        return QUEUE.isEmpty();
    }
}
