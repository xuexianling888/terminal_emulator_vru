package com.tcb.constant;

/**
 * 系统编码
 * @author Laoxue
 */
public enum STEnum {
    ST_21(21), ST_22(22), ST_23(23), ST_24(24), ST_25(25), ST_26(26), ST_27(27), ST_31(31),
    ST_32(32), ST_33(33), ST_34(34), ST_35(35), ST_36(36), ST_37(37), ST_38(38), ST_39(39),
    SN_41(41), ST_51(51), ST_52(52), ST_91(91);

    private int code;

    STEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
