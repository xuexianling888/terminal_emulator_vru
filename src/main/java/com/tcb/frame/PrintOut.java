package com.tcb.frame;

import javax.swing.*;

/**
 * @program: terminal_emulator
 * @description: 打印
 * @author: laoXue
 * @create: 2020-05-19 09:21
 **/
public class PrintOut {
    private static final Integer MAX = 50;
    private JTextArea jTextArea;

    private PrintOut() {
    }

    private static class SingleInstance {
        private static final PrintOut printOut = new PrintOut();
    }

    public static PrintOut getInstance() {
        return SingleInstance.printOut;
    }

    public void setjTextArea(JTextArea jTextArea) {
        this.jTextArea = jTextArea;
    }

    public void write(String msg) {
        if (jTextArea.getColumns() >= MAX) {
            jTextArea.setText("");
        }
        jTextArea.append(msg+"\n");
    }
}
