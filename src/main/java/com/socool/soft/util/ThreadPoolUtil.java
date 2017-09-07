package com.socool.soft.util;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 多线程工具类
 */
public class ThreadPoolUtil {
    private static ThreadPoolTaskExecutor poolTaskExecutor;
    /**
     * 单例，不允许实例化本类
     */
    private ThreadPoolUtil(){}

    static{
        init();
    }
    private static void init() {
        poolTaskExecutor = new ThreadPoolTaskExecutor();
        // 线程池所使用的缓冲队列
        poolTaskExecutor.setQueueCapacity(100);
        // 线程池维护线程的最少数量(核心线程数)
        poolTaskExecutor.setCorePoolSize(1);
        // 线程池维护线程的最大数量
        poolTaskExecutor.setMaxPoolSize(20);
        // 线程池维护线程所允许的空闲时间
        poolTaskExecutor.setKeepAliveSeconds(300);
        poolTaskExecutor.initialize();
    }

    /**
     * 执行线程
     * @param runnable
     */
    public static void execute(Runnable runnable) {
        poolTaskExecutor.execute(runnable);
    }

}
