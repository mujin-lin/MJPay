package com.mujin.enums;

/**
 * Description:
 *
 * @Author: 伍成林
 * @Date： 2021年 03月31日
 */
public enum WxCommonEnums {
    /**
     * 交易的appid
     */
    APPID("appid","交易的appid"),
    /**
     * 商户ID
     */
    MCH_ID("mch_id", "商户ID"),
    /**
     * 随机字符串
     */
    NONCE_STR("nonce_str", "随机字符串"),
    /**
     * 签名
     */
    SIGN("sign", "签名"),
    /**
     * 签名类型
     */
    SIGN_TYPE("sign_type", "签名类型");


    /**
     * 值
     */
    private final String value;
    /**
     * 信息，备注
     */
    private final String message;

    WxCommonEnums(String value, String message) {
        this.value = value;
        this.message = message;
    }

    public String getValue() {
        return value;
    }
}
