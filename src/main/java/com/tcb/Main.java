package com.tcb;

import com.tcb.constant.ThingEnum;
import com.tcb.frame.TerminalFrame;

import javax.swing.*;

/**
 * @program: terminal_emulator
 * @description: 入口类
 * @author: laoXue
 * @create: 2020-05-19 13:05
 **/
public class Main {
    public static void main(String[] args) {
        DefaultListModel<String> defaultListModel = new DefaultListModel<>();
        for (ThingEnum thingEnum : ThingEnum.values()) {
            defaultListModel.addElement(thingEnum.getThingName());
        }
        TerminalFrame terminalFrame = new TerminalFrame(defaultListModel);
        terminalFrame.setVisible(true);
    }
}
