
package com.mujin.dto.wx;

import lombok.AllArgsConstructor;
import lombok.Data;



@AllArgsConstructor
@Data
public class UnifiedOrderDto extends BaseWxPayDto{
    /**
     * 商品简单描述
     */
    private String body;
    /**
     * 商品详情
     */
    private String detail;
    /**
     * 附加数据
     */
    private String attach;
    /**
     * 商户订单号
     */
    private String outTradeNo;
    /**
     * 标价币种
     */
    private String feeType;
    /**
     * 标价金额 单位：分
     */
    private String totalFee;
    /**
     * 终端IP
     */
    private String spbillCreateIp;
    /**
     * 交易起始时间
     */
    private String timeStart;
    /**
     * 交易结束时间: 格式为yyyyMMddHHmmss
     */
    private String timeExpire;
    /**
     * 订单优惠标记
     */
    private String goodsTag;
    /**
     * 异步通知地址
     */
    private String notifyUrl;
    /**
     * 交易类型
     */
    private String tradeType;
    /**
     * 商品ID
     */
    private String productId;
    /**
     * 指定支付方式
     */
    private String limitPay;
    /**
     * 用户标识
     */
    private String openid;
    /**
     * 电子发票入口开放标识
     */
    private String receipt;
    /**
     * 场景信息
     */
    private String sceneInfo;
    /**
     * 是否需要分账: Y-是，需要分账 N-否，不分账 默认不分
     */
    private String profitSharing;
}
