package com.mujin.config;

import com.mujin.annotation.PropertyKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 支付宝支付的配置类
 *
 * @Description: TODO
 * @author: 伍成林
 * @date: 2020年3月10日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AliPayConfig {
    /**
     * 是否使用证书
     */
    @PropertyKey("alipay.use.cert")
    private boolean useCert;

    /**
     * 支付宝网关名、partnerId和appId
     */
    @PropertyKey("alipay.open.api.domain")
    private String openApiDomain;

    @PropertyKey("alipay.mcloud.api.domain")
    private String mcloudApiDomain;

    @PropertyKey("alipay.pid")
    private String pid;

    @PropertyKey("alipay.appid")
    private String appid;

    // 签名类型: RSA->SHA1withRsa,RSA2->SHA256withRsa
    @PropertyKey("alipay.sign.type")
    private String signType;

    //私钥
    @PropertyKey("alipay.private.key")
    private String privateKey;

    // 字符编码格式
    @PropertyKey("alipay.charset")
    private String charset;

    // 请求格式
    @PropertyKey("alipay.request.format")
    private String requestFormat;

    // 当面付最大查询次数和查询间隔（毫秒）
    @PropertyKey("alipay.max.query.retry")
    private String maxQueryRetry;

    @PropertyKey("alipay.query.duration")
    private String queryDuration;

    // 当面付最大撤销次数和撤销间隔（毫秒）
    @PropertyKey("alipay.max.cancel.retry")
    private String maxCancelRetry;

    @PropertyKey("alipay.cancel.duration")
    private String cancelDuration;

    // 交易保障线程第一次调度延迟和调度间隔（秒）
    @PropertyKey("alipay.heartbeat.delay")
    private String heartbeatDelay;

    @PropertyKey("alipay.heartbeat.duration")
    private String heartbeatDuration;

    @PropertyKey("alipay.app.cert.path")
    private String appCertPath;

    @PropertyKey("alipay.alipay.cert.path")
    private String alipayCertPath;

    @PropertyKey("alipay.alipay.root.cert.path")
    private String alipayRootCertPath;

    @PropertyKey("alipay.public.key")
    private String alipayPublicKey;

}
