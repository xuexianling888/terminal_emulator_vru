package com.tcb.tools;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: terminal_emulator
 * @description:
 * @author: laoXue
 * @create: 2020-05-14 13:01
 **/
public final class StringTools {
    private static final Pattern numberPattern = Pattern.compile("[0-9]*");
    private static final Pattern ipPattern = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}");
    private static final Pattern pattern1 = Pattern.compile("(.*)\\(([0-9]*)-([0-9]*)\\)");
    private static final DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    /**
     * 是否是数字字符串
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        Matcher matcher = numberPattern.matcher(str);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 是否是IP地址
     *
     * @param str
     * @return
     */
    public static boolean isIp(String str) {
        Matcher matcher = ipPattern.matcher(str);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 已选监测物格式
     *
     * @param str
     * @return
     */
    public static String getString(String str, int index) {
        Matcher matcher = pattern1.matcher(str);
        if (matcher.find()) {
            return matcher.group(index);
        }
        return null;
    }

    /**
     * 时间转字符串（yyyyMMddHHmmss）
     *
     * @param dateTime
     * @return
     */
    public static String getTimeFormatatter(LocalDateTime dateTime) {
        return dateTimeFormatter1.format(dateTime);
    }

    /**
     * 时间转字符串（yyyyMMddHHmmssSSS）
     *
     * @param dateTime
     * @return
     */
    public static String getTimeFormatatter_SSS(LocalDateTime dateTime) {
        return dateTimeFormatter2.format(dateTime);
    }
}
