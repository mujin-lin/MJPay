package com.mujin.enums;

/**
 * Description:
 *
 * @Author: 伍成林
 * @Date： 2021年 03月31日
 */
public enum OrderEnums {
    /**
     * 微信分配的唯一标志ID
     */
    OPEN_ID("openid", "微信openid"),
    /**
     * 商户订单号
     */
    OUT_TRADE_NO("out_trade_no", "商户订单号"),
    /**
     * 微信交易单号
     */
    TRANSACTION_ID("transaction_id", "微信交易单号"),
    /**
     * 异步回调URL
     */
    NOTIFY_URL("notify_url", "异步回调URL"),
    /**
     * 商品简单描述
     */
    BODY("body", "商品简单描述"),
    /**
     * 订单总金额 单位为分
     */
    TOTAL_FEE("total_fee", "订单总金额,单位为分"),
    /**
     * ip地址
     */
    SPBILL_CREATE_IP("spbill_create_ip", "终端IP地址"),
    /**
     * 交易类型
     */
    TRADE_TYPE("trade_type", "交易类型"),
    /**
     * 货币类型
     */
    FEE_TYPE("fee_type", "货币类型"),
    /**
     * 支付超时时间
     */
    TIME_EXPIRE("time_expire", "支付超时时间");
    /**
     * 值
     */
    private final String value;
    /**
     * 信息，备注
     */
    private final String message;

    OrderEnums(String value, String message) {
        this.value = value;
        this.message = message;
    }

    public String getValue() {
        return value;
    }
}
