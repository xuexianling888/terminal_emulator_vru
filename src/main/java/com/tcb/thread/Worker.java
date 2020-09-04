package com.tcb.thread;

import com.tcb.constant.CNEnum;
import com.tcb.constant.FrameModel;
import com.tcb.frame.PrintOut;
import com.tcb.netty.NettyClient;
import com.tcb.netty.command.Command;
import com.tcb.netty.protocol.Content;
import com.tcb.netty.protocol.Message;
import com.tcb.tools.StringTools;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: terminal_emulator
 * @description: 工作线程
 * @author: laoXue
 * @create: 2020-05-15 15:22
 **/
public class Worker extends Thread {
    private static final int MAX_FAILED = 5;
    private static final String MN_HEAD = "1234567891";
    private static final String DATATIME = "DataTime=";

    volatile boolean running = true;
    private int failNumber;
    private int number;
    private ChannelFuture future;
    private FrameModel frameModel;
    private Message message;
    private Content content;
    private LocalDateTime startTime;
    private String mn;

    private Map<String, Double> thingMap = new HashMap<>(16);
    private List<Command> commands = new ArrayList<>(16);

    public Worker(FrameModel frameModel, int number) {
        this.frameModel = frameModel;
        this.number = number;
        init();
    }

    @Override
    public void run() {
        connect();
        startTime = LocalDateTime.now();
        while (running) {
            try {
                frameModel.getThingMap().forEach((key, value) -> {
                    String[] split = value.split("-");
                    int min = Integer.parseInt(split[0]);
                    int max = Integer.parseInt(split[1]);
                    double thingValue = createThingValue(min, max);
                    thingMap.put(key, thingValue);
                });
                commands.forEach(command -> command.execute(thingMap));
                Thread.sleep(60000);
            } catch (Exception e) {
                System.out.println(e.toString());
                break;
            }
        }
        future.channel().close();
        PrintOut.getInstance().write("终端" + mn + "下线!");
    }

    public void sendMsg(String msg) {
        if (future != null && future.channel().isActive()) {
            Channel channel = future.channel();
            System.out.println("终端" + mn + "发送:" + msg);
            try {
                channel.writeAndFlush(msg).sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
                PrintOut.getInstance().write(e.toString());
                running = false;
            }
        } else {
            PrintOut.getInstance().write("终端" + mn + "未连接成功!");
            if (failNumber >= MAX_FAILED) {
                running = false;
                PrintOut.getInstance().write("终端" + mn + "重连失败!，已踢出该终端");
            } else {
                connect();
                failNumber++;
                try {
                    Thread.sleep(5000);
                    sendMsg(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void init() {
        Command command2011 = new Command2011();
        Command command2031 = new Command2031();
        Command command2051 = new Command2051();
        Command command2061 = new Command2061();

        commands.add(command2011);
        commands.add(command2031);
        commands.add(command2051);
        commands.add(command2061);

        String st = frameModel.getSt();
        String pw = frameModel.getPw();
        mn = MN_HEAD + String.format("%04x", number);
        content = new Content(st, pw, mn, "5");
        message = new Message(content);
    }

    private void connect() {
        Bootstrap bootstrap = NettyClient.getBootstrap();
        try {
            future = bootstrap.connect().sync();
            PrintOut.getInstance().write("终端" + mn + "上线");
        } catch (Exception e) {
            PrintOut.getInstance().write("终端" + mn + "上线失败!\n" + e.getMessage());
            future = null;
        }
    }

    private double createThingValue(int min, int max) {
        double value = Math.random() * (max - min) + min;
        return getDoubleformat(value);
    }

    private void createCp_thing(StringBuilder cp, Map<String, List<Double>> thingMap) {
        final String COU = "-Cou=";
        final String MIN = "-Min=";
        final String AVG = "-Avg=";
        final String MAX = "-Max=";
        final String FLAG = "-Flag=";

        thingMap.forEach((key, value) -> {
            double min = value.get(0);
            double max = value.get(0);
            double cou = 0.0;
            double avg;
            int size = value.size();
            for (int i = 0; i < value.size(); i++) {
                if (value.get(i) < min) {
                    min = value.get(i);
                }
                if (value.get(i) > max) {
                    max = value.get(i);
                }
                cou += value.get(i);
            }
            avg = cou / size;

            cp.append(key).append(COU).append(cou).append(",");
            cp.append(key).append(MIN).append(min).append(",");
            cp.append(key).append(MAX).append(max).append(",");
            cp.append(key).append(AVG).append(getDoubleformat(avg)).append(",");
            cp.append(key).append(FLAG).append("N").append(";");
            value.clear();
        });
    }

    public void shutdown() {
        running = false;
    }

    /**
     * 实时
     */
    private class Command2011 implements Command {
        private final CNEnum CN = CNEnum.CN2011;
        private static final String RTD = "-Rtd=";
        private static final String FLAG = "-Flag=";

        @Override
        public void execute(Map<String, Double> things) {
            LocalDateTime now = LocalDateTime.now();

            String qn = StringTools.getTimeFormatatter_SSS(now);
            message.getContent().setQn(qn);
            message.getContent().setCn(CN.getCode());

            String time = StringTools.getTimeFormatatter(now);
            StringBuilder cp = new StringBuilder();
            cp.append(DATATIME).append(time).append(";");
            things.forEach((key, value) -> {
                cp.append(key).append(RTD).append(value).append(",");
                cp.append(key).append(FLAG).append("N").append(";");
            });
            message.getContent().setCp(cp.toString());
            sendMsg(message.toString());
        }
    }

    /**
     * 日
     */
    private class Command2031 implements Command {
        private final CNEnum CN = CNEnum.CN2031;
        private Map<String, List<Double>> thingMap = new HashMap<>();

        @Override
        public void execute(Map<String, Double> things) {
            LocalDateTime time = LocalDateTime.now();
            int hh = Integer.parseInt(time.format(DateTimeFormatter.ofPattern("HH")));
            int mm = Integer.parseInt(time.format(DateTimeFormatter.ofPattern("mm")));
            if (hh == 0 && mm == 0) {
                String qn = StringTools.getTimeFormatatter_SSS(time);
                message.getContent().setQn(qn);
                message.getContent().setCn(CN.getCode());

                time = time.plusDays(-1);
                long startMilli = startTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
                long currentMilli = time.toInstant(ZoneOffset.of("+8")).toEpochMilli();
                String dateTime;
                if (startMilli >= currentMilli) {
                    dateTime = startTime.format(DateTimeFormatter.ofPattern("yyyyMMdd000000"));
                } else {
                    dateTime = time.format(DateTimeFormatter.ofPattern("yyyyMMdd000000"));
                }

                StringBuilder cp = new StringBuilder();
                cp.append(DATATIME).append(dateTime).append(";");

                createCp_thing(cp, thingMap);

                message.getContent().setCp(cp.toString());
                sendMsg(message.toString());
            }
            things.forEach((key, value) -> getValueList(key).add(value));
        }

        private List<Double> getValueList(String key) {
            List<Double> values;
            if (thingMap.get(key) == null) {
                values = new ArrayList<>(64);
                thingMap.put(key, values);
            } else {
                values = thingMap.get(key);
            }
            return values;
        }
    }

    /**
     * 分钟
     */
    private class Command2051 implements Command {
        private final CNEnum CN = CNEnum.CN2051;
        private Map<String, List<Double>> thingMap = new HashMap<>();

        @Override
        public void execute(Map<String, Double> things) {
            LocalDateTime time = LocalDateTime.now();
            String mm = time.format(DateTimeFormatter.ofPattern("mm"));
            if (Integer.parseInt(mm) % 10 == 0) {
                String qn = StringTools.getTimeFormatatter_SSS(time);
                message.getContent().setQn(qn);
                message.getContent().setCn(CN.getCode());

                time = time.plusMinutes(-10);
                long startMilli = startTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
                long currentMilli = time.toInstant(ZoneOffset.of("+8")).toEpochMilli();
                String dateTime;
                if (startMilli >= currentMilli) {
                    dateTime = startTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm00"));
                } else {
                    dateTime = time.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm00"));
                }
                StringBuilder cp = new StringBuilder();
                cp.append(DATATIME).append(dateTime).append(";");

                createCp_thing(cp, thingMap);

                message.getContent().setCp(cp.toString());
                sendMsg(message.toString());
            }
            things.forEach((key, value) -> getValueList(key).add(value));
        }

        private List<Double> getValueList(String key) {
            List<Double> values;
            if (thingMap.get(key) == null) {
                values = new ArrayList<>(16);
                thingMap.put(key, values);
            } else {
                values = thingMap.get(key);
            }
            return values;
        }
    }

    /**
     * 小时
     */
    private class Command2061 implements Command {
        private final CNEnum CN = CNEnum.CN2061;
        private Map<String, List<Double>> thingMap = new HashMap<>();

        @Override
        public void execute(Map<String, Double> things) {
            LocalDateTime time = LocalDateTime.now();
            String mm = time.format(DateTimeFormatter.ofPattern("mm"));
            if (Integer.parseInt(mm) == 0) {
                String qn = StringTools.getTimeFormatatter_SSS(time);
                message.getContent().setQn(qn);
                message.getContent().setCn(CN.getCode());

                time = time.plusHours(-1);
                long startMilli = startTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
                long currentMilli = time.toInstant(ZoneOffset.of("+8")).toEpochMilli();
                String dateTime;
                if (startMilli >= currentMilli) {
                    dateTime = startTime.format(DateTimeFormatter.ofPattern("yyyyMMddHH0000"));
                } else {
                    dateTime = time.format(DateTimeFormatter.ofPattern("yyyyMMddHH0000"));
                }
                StringBuilder cp = new StringBuilder();
                cp.append(DATATIME).append(dateTime).append(";");

                createCp_thing(cp, thingMap);

                message.getContent().setCp(cp.toString());
                sendMsg(message.toString());
            }
            things.forEach((key, value) -> getValueList(key).add(value));
        }

        private List<Double> getValueList(String key) {
            List<Double> values;
            if (thingMap.get(key) == null) {
                values = new ArrayList<>(64);
                thingMap.put(key, values);
            } else {
                values = thingMap.get(key);
            }
            return values;
        }
    }

    private double getDoubleformat(double var) {
        return (double) Math.round(var * 1000) / 1000;
    }
}
