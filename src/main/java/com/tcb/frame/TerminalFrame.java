package com.tcb.frame;

import com.tcb.constant.FrameModel;
import com.tcb.constant.ThingEnum;
import com.tcb.netty.NettyClient;
import com.tcb.thread.TheadPoolImpl;
import com.tcb.thread.ThreadPool;
import com.tcb.tools.StringTools;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: terminal_emulator
 * @description: 显示
 * @author: laoXue
 * @create: 2020-05-12 14:21
 **/
public class TerminalFrame extends JFrame {
    private JPanel contentPane;
    private DefaultListModel<String> listModel1;
    private DefaultListModel<String> listModel2 = new DefaultListModel();
    /**
     * 上位机IP
     */
    private JTextField hostField;
    /**
     * 端口
     */
    private JTextField portField;
    /**
     * TCP连接测试按钮
     */
    private JButton testButton;
    /**
     * 连接状态
     */
    private JLabel lblState;
    /**
     * 启动按钮
     */
    private JButton startButton;
    /**
     * 关闭按钮
     */
    private JButton closeButton;

    /**
     * 系统编码
     */
    private JTextField systemField;
    /**
     * 模拟设备个数
     */
    private JTextField countField;
    /**
     * 访问密码
     */
    private JTextField pwField;
    /**
     * 监测物列表
     */
    private JList listNo1;
    /**
     * 已选监测物列表
     */
    private JList listNo2;
    /**
     * 监测物最小值
     */
    private JTextField minField;
    /**
     * 监测物最小值
     */
    private JTextField maxField;
    /**
     * 增加按钮
     */
    private JButton btnInsert;
    /**
     * 删除按钮
     */
    private JButton btnDelete;
    /**
     * 删除所有按钮
     */
    private JButton btnDeleteAll;
    /**
     * 输出日志
     */
    private JTextArea textArea;
    /**
     * 线程池
     *
     * @param thingListModel
     */
    private ThreadPool threadPool;

    public TerminalFrame(DefaultListModel<String> thingListModel) {
        this.listModel1 = thingListModel;
        init();
    }

    private void init() {
        setTitle("HJ212终端模拟器");
        setPreferredSize(new Dimension(800, 800));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);

        contentPane = (JPanel) getContentPane();
        contentPane.setLayout(null);
        contentPane.add(creatConnectionPanel());
        contentPane.add(creatOptionPanel());
        contentPane.add(createThingsPanel());
        contentPane.add(creatPrintPanel());
        contentPane.setBorder(BorderFactory.createEmptyBorder());
        listNo1.setSelectedIndex(0);

        PrintOut.getInstance().setjTextArea(textArea);

        addActionListener();
    }

    private JPanel creatConnectionPanel() {
        JPanel panel = new JPanel(null);
        panel.setLocation(0, 0);
        panel.setSize(new Dimension(800, 30));
        panel.setBorder(BorderFactory.createLineBorder(Color.lightGray));

        //IP
        JLabel lblIp = new JLabel("上位机IP:");
        lblIp.setHorizontalAlignment(SwingConstants.RIGHT);
        lblIp.setFont(new Font("宋体", Font.PLAIN, 12));
        lblIp.setBounds(new Rectangle(0, 2, 60, 26));

        hostField = new JTextField();
        hostField.setBounds(new Rectangle(65, 6, 100, 20));
        hostField.setHorizontalAlignment(SwingConstants.CENTER);

        //port
        JLabel lblPort = new JLabel("端口:");
        lblPort.setHorizontalAlignment(SwingConstants.RIGHT);
        lblPort.setFont(new Font("宋体", Font.PLAIN, 12));
        lblPort.setBounds(new Rectangle(175, 2, 60, 26));

        portField = new JTextField();
        portField.setBounds(new Rectangle(240, 6, 50, 20));
        portField.setHorizontalAlignment(SwingConstants.CENTER);

        //test
        testButton = new JButton("Test Connection");
        testButton.setFont(new Font("Default", Font.PLAIN, 12));
        testButton.setActionCommand("TEST");
        testButton.setBounds(new Rectangle(300, 4, 125, 24));

        //state
        lblState = new JLabel();
        lblState.setFont(new Font("Default", Font.PLAIN, 12));
        lblState.setHorizontalAlignment(SwingConstants.CENTER);
        lblState.setBounds(new Rectangle(450, 6, 60, 20));
        lblState.setOpaque(true);

        panel.add(lblIp);
        panel.add(hostField);
        panel.add(lblPort);
        panel.add(portField);
        panel.add(testButton);
        panel.add(lblState);

        return panel;
    }

    private JPanel creatOptionPanel() {
        JPanel panel = new JPanel(null);
        panel.setLocation(0, 30);
        panel.setSize(new Dimension(800, 30));

        //访问密码
        JLabel lblPassword = new JLabel("访问密码:");
        lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
        lblPassword.setFont(new Font("宋体", Font.PLAIN, 12));
        lblPassword.setBounds(new Rectangle(0, 2, 60, 26));

        pwField = new JTextField();
        pwField.setBounds(65, 6, 100, 20);
        pwField.setColumns(10);
        pwField.setHorizontalAlignment(SwingConstants.CENTER);

        //系统编码
        JLabel lblSystem = new JLabel("系统编码:");
        lblSystem.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSystem.setFont(new Font("宋体", Font.PLAIN, 12));
        lblSystem.setBounds(new Rectangle(175, 2, 60, 26));

        systemField = new JTextField();
        systemField.setBounds(new Rectangle(240, 6, 50, 20));
        systemField.setHorizontalAlignment(SwingConstants.CENTER);

        //站点数
        JLabel lblCout = new JLabel("站点数:");
        lblCout.setHorizontalAlignment(SwingConstants.RIGHT);
        lblCout.setFont(new Font("宋体", Font.PLAIN, 12));
        lblCout.setBounds(new Rectangle(300, 2, 60, 26));

        countField = new JTextField();
        countField.setBounds(new Rectangle(365, 6, 60, 20));
        countField.setHorizontalAlignment(SwingConstants.CENTER);

        startButton = new JButton("Start");
        startButton.setFont(new Font("Default", Font.PLAIN, 12));
        startButton.setActionCommand("START");
        startButton.setBounds(new Rectangle(435, 2, 60, 28));

        closeButton = new JButton("取消");
        closeButton.setFont(new Font("Default", Font.PLAIN, 12));
        closeButton.setActionCommand("CLOSE");
        closeButton.setBounds(new Rectangle(500, 2, 60, 28));

        panel.add(lblPassword);
        panel.add(pwField);
        panel.add(lblSystem);
        panel.add(systemField);
        panel.add(lblCout);
        panel.add(countField);
        panel.add(startButton);
        panel.add(closeButton);

        return panel;
    }

    private JPanel createThingsPanel() {
        JPanel panel = new JPanel(null);
        panel.setBounds(2, 65, 796, 400);

        minField = new JTextField();
        maxField = new JTextField();
        btnInsert = new JButton("增加>");
        btnDelete = new JButton("<删除");
        btnDeleteAll = new JButton("<<删除");

        listNo1 = new JList(listModel1);
        listNo2 = new JList(listModel2);

        JPanel panel2 = new JPanel(null);
        JScrollPane scrollPane1 = new JScrollPane();
        JScrollPane scrollPane3 = new JScrollPane();

        scrollPane1.setBounds(0, 0, 300, 400);
        scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane1.setBorder(BorderFactory.createTitledBorder(null, "可监测物列表", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, new Font("宋体", Font.PLAIN, 12), null));
        scrollPane1.setViewportView(listNo1);

        panel2.setBounds(300, 0, 96, 400);
        //监测值范围
        JLabel lblScope = new JLabel("监测值范围");
        lblScope.setHorizontalAlignment(SwingConstants.CENTER);
        lblScope.setFont(new Font("宋体", Font.PLAIN, 12));
        lblScope.setBounds(new Rectangle(0, 25, 96, 30));

        minField.setBounds(0, 50, 40, 20);
        minField.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lbllabel = new JLabel("-");
        lbllabel.setHorizontalAlignment(SwingConstants.CENTER);
        lbllabel.setFont(new Font("宋体", Font.PLAIN, 12));
        lbllabel.setBounds(new Rectangle(40, 50, 16, 20));

        maxField.setBounds(56, 50, 40, 20);
        maxField.setHorizontalAlignment(SwingConstants.CENTER);

        btnInsert.setBounds(0, 145, 96, 30);
        btnInsert.setFont(new Font("宋体", Font.PLAIN, 12));
        btnDelete.setBounds(0, 185, 96, 30);
        btnDelete.setFont(new Font("宋体", Font.PLAIN, 12));
        btnDeleteAll.setBounds(0, 225, 96, 30);
        btnDeleteAll.setFont(new Font("宋体", Font.PLAIN, 12));

        panel2.add(lblScope);
        panel2.add(minField);
        panel2.add(lbllabel);
        panel2.add(maxField);
        panel2.add(btnInsert);
        panel2.add(btnDelete);
        panel2.add(btnDeleteAll);

        scrollPane3.setBounds(396, 0, 392, 400);
        scrollPane3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane3.setBorder(BorderFactory.createTitledBorder(null, "可监测物列表", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, new Font("宋体", Font.PLAIN, 12), null));
        scrollPane3.setViewportView(listNo2);

        panel.add(scrollPane1);
        panel.add(panel2);
        panel.add(scrollPane3);

        return panel;
    }

    private JScrollPane creatPrintPanel() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(2, 470, 787, 295);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.lightGray), "输出日志", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, new Font("宋体", Font.PLAIN, 12)));
        textArea = new JTextArea(1, 1);
        textArea.setBounds(10, 15, 767, 275);
        textArea.setLineWrap(true);
        textArea.setFont(new Font("宋体", Font.PLAIN, 10));
        scrollPane.setViewportView(textArea);
        return scrollPane;
    }

    private void addActionListener() {
        testButton.addActionListener(new ButtonClickListener());
        startButton.addActionListener(new ButtonClickListener());
        closeButton.addActionListener(new ButtonClickListener());

        btnInsert.addActionListener(e -> {
            String minString = minField.getText();
            String maxString = maxField.getText();

            if ("".equals(minString) || !StringTools.isNumber(minString)) {
                JOptionPane.showMessageDialog(contentPane, "必须是整数", "最小监测值", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if ("".equals(maxString) || !StringTools.isNumber(maxString)) {
                JOptionPane.showMessageDialog(contentPane, "必须是整数", "最大监测值", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (Integer.parseInt(minString) >= Integer.parseInt(maxString)) {
                JOptionPane.showMessageDialog(contentPane, "最大值必须大于最小值", "监测值范围", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int size = listModel1.getSize();
            if (size <= 0) {
                return;
            } else {
                int index = listNo1.getSelectedIndex();
                String thingName = (String) listNo1.getSelectedValue();
                listModel2.addElement(thingName + "(" + minString + "-" + maxString + ")");
                listModel1.remove(index);
                listNo1.setSelectedIndex(0);
                listNo2.setSelectedIndex(0);
            }
        });

        btnDelete.addActionListener(e -> {
            int size = listModel2.getSize();
            if (size <= 0) {
                return;
            } else {
                int index = listNo2.getSelectedIndex();
                String selectedValue = (String) listNo2.getSelectedValue();
                String thingName = StringTools.getString(selectedValue, 1);
                listModel1.addElement(thingName);
                listModel2.remove(index);
                listNo1.setSelectedIndex(0);
                listNo2.setSelectedIndex(0);
            }
        });

        btnDeleteAll.addActionListener(e -> {
            int size = listModel2.getSize();
            if (size <= 0) {
                return;
            } else {
                while (!listModel2.isEmpty()) {
                    String selectedValue = listModel2.firstElement();
                    String thingName = StringTools.getString(selectedValue, 1);
                    listModel1.addElement(thingName);
                    listModel2.remove(0);
                }
            }
            listNo1.setSelectedIndex(0);
        });
    }


    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            switch (command) {
                case "TEST":
                    connecting();
                    break;
                case "START":
                    start();
                    break;
                case "CLOSE":
                    close();
                    break;
                default:
                    break;
            }
        }

        private void connecting() {
            if (checkAdress()) {
                try {
                    Bootstrap bootstrap = NettyClient.getBootstrap();
                    bootstrap.remoteAddress(hostField.getText(), Integer.parseInt(portField.getText()));
                    ChannelFuture future = bootstrap.connect().sync();
                    if (future != null && future.channel().isActive()) {
                        lblState.setText("success");
                        lblState.setBackground(Color.GREEN);
                    } else {
                        lblState.setText("fail");
                        lblState.setBackground(Color.RED);
                    }
                    future.channel().close();
                } catch (Exception exception) {
                    lblState.setText("except");
                    lblState.setBackground(Color.RED);
                }
            }
        }

        private void start() {
            boolean isOk = checkAdress();
            if (!isOk) {
                return;
            }
            isOk = checkOption();
            if (!isOk) {
                return;
            }
            isOk = checkThingList();
            if (!isOk) {
                return;
            }
            FrameModel frameModel = build();
            threadPool = new TheadPoolImpl(frameModel);
            startButton.setEnabled(false);
            try {
                threadPool.execute();
            } catch (Exception e) {
                startButton.setEnabled(true);
                return;
            }

            startButton.setEnabled(false);
            closeButton.setEnabled(true);
        }

        private void close() {
            closeButton.setEnabled(false);
            try {
                threadPool.shutdown();
            } catch (Exception e) {
                return;
            }
            startButton.setEnabled(true);
        }

        private boolean checkAdress() {
            boolean isOk = true;
            if ("".equals(hostField.getText())) {
                JOptionPane.showMessageDialog(contentPane, "IP不能为空", "服务器IP地址", JOptionPane.ERROR_MESSAGE);
                isOk = false;
                return isOk;
            }
            if (!StringTools.isIp(hostField.getText())) {
                JOptionPane.showMessageDialog(contentPane, "IP地址格式不对", "服务器IP地址", JOptionPane.ERROR_MESSAGE);
                isOk = false;
                return isOk;
            }
            if ("".equals(portField.getText())) {
                JOptionPane.showMessageDialog(contentPane, "端口号不能为空", "服务器IP端口", JOptionPane.ERROR_MESSAGE);
                isOk = false;
                return isOk;
            }
            if (!StringTools.isNumber(portField.getText())) {
                JOptionPane.showMessageDialog(contentPane, "端口号必须为数字", "服务器IP端口", JOptionPane.ERROR_MESSAGE);
                isOk = false;
                return isOk;
            }
            return isOk;
        }

        private boolean checkOption() {
            boolean isOk = true;
            if ("".equals(pwField.getText()) || pwField.getText().length() > 9) {
                JOptionPane.showMessageDialog(contentPane, "密码不能为空且长度小于9", "访问密码", JOptionPane.ERROR_MESSAGE);
                isOk = false;
                return isOk;
            }
            if ("".equals(systemField.getText()) || systemField.getText().length() > 5) {
                JOptionPane.showMessageDialog(contentPane, "系统编码不能为空且长度小于5", "系统编码", JOptionPane.ERROR_MESSAGE);
                isOk = false;
                return isOk;
            }
            if ("".equals(countField.getText())) {
                JOptionPane.showMessageDialog(contentPane, "站点数不能为空", "站点数", JOptionPane.ERROR_MESSAGE);
                isOk = false;
                return isOk;
            }
            if (!StringTools.isNumber(countField.getText())) {
                JOptionPane.showMessageDialog(contentPane, "站点数必须为数字", "站点数", JOptionPane.ERROR_MESSAGE);
                isOk = false;
                return isOk;
            }
            if (Integer.parseInt(countField.getText()) <= 0) {
                JOptionPane.showMessageDialog(contentPane, "站点数必须大于0", "站点数", JOptionPane.ERROR_MESSAGE);
                isOk = false;
                return isOk;
            }
            return isOk;
        }

        private boolean checkThingList() {
            boolean isOk = true;
            if (listModel2.size() <= 0) {
                JOptionPane.showMessageDialog(contentPane, "监测物不能为空", "监测物列表", JOptionPane.ERROR_MESSAGE);
                isOk = false;
            }
            return isOk;
        }

        private FrameModel build() {
            String host = hostField.getText();
            int port = Integer.parseInt(portField.getText());
            String pw = pwField.getText();
            String st = systemField.getText();
            int number = Integer.parseInt(countField.getText());
            Map<String, String> thingMap = new HashMap<>(16);

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < listModel2.size(); i++) {
                String thingName = StringTools.getString(listModel2.get(i), 1);
                String min = StringTools.getString(listModel2.get(i), 2);
                String max = StringTools.getString(listModel2.get(i), 3);
                for (ThingEnum thingEnum : ThingEnum.values()) {
                    if (thingName.equals(thingEnum.getThingName())) {
                        stringBuilder.append(min).append("-").append(max);
                        thingMap.put(thingEnum.getThingCode(), stringBuilder.toString());
                        stringBuilder.delete(0, stringBuilder.length());
                    }
                }
            }
            FrameModel frameModel = new FrameModel(host, port, pw, st, number, thingMap);

            return frameModel;
        }
    }
}
