package com.tcb.netty.protocol;

/**
 * @program: terminal_emulator
 * @description: 协议数据格式类
 * @author: laoXue
 * @create: 2020-05-15 09:51
 **/
public class Message {
    private static final String HEAD = "##";
    private static final String END = "\r\n";
    private String length;
    private String crc;
    private Content content;

    public Message(Content content) {
        this.content = content;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getCrc() {
        return crc;
    }

    public void setCrc(String crc) {
        this.crc = crc;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    @Override
    public String toString() {
        String contentStr = content.toString();
        length = String.format("%04d", contentStr.length());
        //TODO 未执行CRC校验
        crc = "0000";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(HEAD).append(length).append(content.toString()).append(crc).append(END);
        return stringBuilder.toString();
    }
}
