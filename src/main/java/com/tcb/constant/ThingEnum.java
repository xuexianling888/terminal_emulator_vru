package com.tcb.constant;

/**
 * 监测物枚举类
 *
 * @author laoXue
 */
public enum ThingEnum {
    /**
     * 监测物类型
     */
    CKND("CKND", "出口NMHC浓度", "g/m3"),
    CKWD("CKWD", "出口温度", "℃"),
    CKYL("CKYL", "出口压力", "kPa"),
    CKLL("CKLL ", "出口流量", "m3/h"),
    JKWD("JKWD", "进口温度", "℃"),
    JKYL("JKYL", "进口压力", "kPa"),
    JKLL("JKLL", "进口流量", "m3/h");

    /**
     * 编码
     */
    private String thingCode;
    /**
     * 名称
     */
    private String thingName;
    /**
     * 计量单位
     */
    private String unit;

    ThingEnum(String thingCode, String thingName, String unit) {
        this.thingCode = thingCode;
        this.thingName = thingName;
        this.unit = unit;
    }

    public String getThingCode() {
        return thingCode;
    }

    public String getThingName() {
        return thingName;
    }

    public String getUnit() {
        return unit;
    }
}
