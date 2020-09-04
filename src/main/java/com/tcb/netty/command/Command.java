package com.tcb.netty.command;

import java.util.Map;

/**
 * @program: terminal_emulator
 * @description:
 * @author: laoXue
 * @create: 2020-05-15 10:24
 **/
public interface Command {

    /**
     * 执行命令
     * @param things
     */
    void execute(Map<String, Double> things);

}
