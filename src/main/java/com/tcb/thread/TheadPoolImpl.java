package com.tcb.thread;

import com.tcb.constant.FrameModel;
import com.tcb.netty.NettyClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @program: terminal_emulator
 * @description:
 * @author: laoXue
 * @create: 2020-05-15 14:57
 **/
public class TheadPoolImpl implements ThreadPool {
    private final List<Worker> workers = Collections.synchronizedList(new ArrayList<>());
    private FrameModel frameModel;

    public TheadPoolImpl(FrameModel frameModel) {
        this.frameModel = frameModel;
        init();
    }

    private void init() {
        NettyClient.getBootstrap().remoteAddress(this.frameModel.getHost(), this.frameModel.getPort());
    }

    @Override
    public void execute() {
        int number = frameModel.getNumber();
        for (int i = 1; i <= number; i++) {
            Worker worker = new Worker(frameModel, i);
            workers.add(worker);
            worker.start();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void shutdown() {
        for (Worker w : workers) {
            w.running = false;
            w.interrupt();
        }
        try {
            Thread.sleep(3000);
            workers.clear();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
