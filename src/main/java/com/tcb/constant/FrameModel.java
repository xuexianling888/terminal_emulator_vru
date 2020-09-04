package com.tcb.constant;

import java.util.Map;

/**
 * @program: terminal_emulator
 * @description: 窗口参数
 * @author: laoXue
 * @create: 2020-05-15 13:13
 **/
public class FrameModel {
    private String host;
    private int port;
    private String pw;
    private String st;
    private int number;
    private Map<String, String> thingMap;

    public FrameModel(String host, int port, String pw, String st, int number, Map<String, String> thingMap) {
        this.host = host;
        this.port = port;
        this.pw = pw;
        this.st = st;
        this.number = number;
        this.thingMap = thingMap;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Map<String, String> getThingMap() {
        return thingMap;
    }

    public void setThingMap(Map<String, String> thingMap) {
        this.thingMap = thingMap;
    }
}
