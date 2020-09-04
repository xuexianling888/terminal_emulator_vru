package com.tcb.constant;

/**
 * 命令编码
 *
 * @author laoXue
 */
public enum CNEnum {
    /**
     * 实时数据
     */
    CN2011("2011"),
    /**
     * 日数据
     */
    CN2031("2031"),
    /**
     * 分钟数据
     */
    CN2051("2051"),
    /**
     * 小时数据
     */
    CN2061("2061");

    private String code;

    CNEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
