package com.mujin.enums;

/**
 * Description:
 *
 * @Author: 伍成林
 * @Date： 2021年 03月15日
 */
public enum PayTypeEnums {
    /**
     * 扫一扫当面支付
     */
    NATIVE("NATIVE", "扫一扫当面支付"),
    /**
     * 小程序公众号的支付方式
     */
    JSAPI("JSAPI", "引入微信方jsapi的公众号小程序支付"),
    /**
     * html页面的支付方式
     */
    MWEB("MWEB", "H5页面支付");


    /**
     * 值
     */
    private final String value;
    /**
     * 信息
     */
    private final String message;

    /**
     * 枚举构造方法
     *
     * @param value   值
     * @param message 信息
     */
    PayTypeEnums(String value, String message) {
        this.value = value;
        this.message = message;
    }

    /**
     * @return the key
     */
    public String getMessage() {
        return message;
    }


    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
}
