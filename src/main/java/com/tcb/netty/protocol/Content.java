package com.tcb.netty.protocol;

/**
 * @program: terminal_emulator
 * @description: 数据段结构
 * @author: laoXue
 * @create: 2020-05-15 10:09
 **/
public class Content {
    private static final String QN = "QN=";
    private static final String ST = ";ST=";
    private static final String CN = ";CN=";
    private static final String PW = ";PW=";
    private static final String MN = ";MN=";
    private static final String Flag = ";Flag=";
    private static final String CP = ";CP=&&";

    private String qn;
    private String st;
    private String cn;
    private String pw;
    private String mn;
    private String flag;
    private String cp;

    public Content(String st, String pw, String mn, String flag) {
        this.st = st;
        this.pw = pw;
        this.mn = mn;
        this.flag = flag;
    }

    public String getQn() {
        return qn;
    }

    public void setQn(String qn) {
        this.qn = qn;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getMn() {
        return mn;
    }

    public void setMn(String mn) {
        this.mn = mn;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(QN).append(qn).append(ST).append(st).append(CN).append(cn).append(PW).append(pw).append(MN).append(mn).append(Flag).append(flag).append(CP).append(cp).append("&&");
        return stringBuilder.toString();
    }
}
