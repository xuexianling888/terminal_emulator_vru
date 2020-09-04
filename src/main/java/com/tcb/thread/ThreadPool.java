package com.tcb.thread;

/**
 * @author laoXue
 */
public interface ThreadPool{
    /**
     * 开启模拟站点
     */
    void execute();

    /**
     * 关闭所有站点
     */
    void shutdown();
}
