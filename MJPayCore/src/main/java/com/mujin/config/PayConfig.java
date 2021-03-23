package com.mujin.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.mujin.constants.PropertyNameConstants;
import com.mujin.utils.PropertyLoadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Description:
 *
 * @Author: 伍成林
 * @Date： 2021年 03月15日
 */
@Slf4j
@Configuration
public class PayConfig {
    /**
     * @return OrderCreate
     * @Title snowflakes
     * @Description: 创建订单生成的算法，真实环境中可以利用redis作为发好器，保证订单号的唯一性
     * @author 伍成林
     * @date 2021年03月15日
     */
    @Bean
    public OrderCreate snowflakes() {
        return new OrderCreate();
    }

    /**
     * @return AliPayConfig 配置类的对象
     * @throws IOException               IO异常
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @Bean
    public AliPayConfig createAliPayConfig() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return PropertyLoadUtils.loadProperties(AliPayConfig.class, PropertyNameConstants.ALI_PAY_PREFIX);
    }

    /**
     * @return WxConfigs 配置类的对象
     * @throws IOException               IO异常
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @Bean
    public WxConfigs createWxPayConfig() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return PropertyLoadUtils.loadProperties(WxConfigs.class, PropertyNameConstants.WX_PAY_PREFIX);
    }


    /**
     *
     * @Title createWxpay
     * @Description: 构造微信支付的client
     * @param wxConfigs resource
     * @return WXPay
     * @author 伍成林
     * @date 2021年03月17日
     */
//    @Bean
//    public WXPay createWxpay(WxConfigs wxConfigs) {
//        log.info("我的classPath:{}", System.getProperty("java.class.path"));
//        WXPayConstants.SignType signType = PayUtils.getSignType(wxConfigs.getSignType());
//        return new WXPay(wxConfigs, signType);
//    }
    /**
     *
     * @Title createAlipayClient
     * @Description: 构造支付宝的请求client
     * @param aliPayConfig 支付宝配置类
     * @return AlipayClient
     * @author 伍成林
     * @date 2021年03月17日
     */
//    @Bean
//    public AlipayClient createAlipayClient(AliPayConfig aliPayConfig) throws AlipayApiException {
//        if (aliPayConfig.isUseCert()) {
//            return createCertAlipayClient(aliPayConfig);
//        } else {
//            return createNoCertAliPayClient(aliPayConfig);
//        }
//    }

    /**
     * @param aliPayConfig 支付宝支付的配置类
     * @return DefaultAlipayClient
     * @Title createNoCertAliPayClient
     * @Description: 构造无证书的支付宝支付的client
     * @author 伍成林
     * @date 2021年03月17日
     */
    private DefaultAlipayClient createNoCertAliPayClient(AliPayConfig aliPayConfig) {
        return new DefaultAlipayClient(aliPayConfig.getOpenApiDomain(), aliPayConfig.getAppid(), aliPayConfig.getPrivateKey(),
                aliPayConfig.getRequestFormat(), aliPayConfig.getCharset(), aliPayConfig.getAlipayPublicKey(), aliPayConfig.getSignType());
    }

    /**
     * @param aliPayConfig 支付宝支付的配置文件
     * @return DefaultAlipayClient
     * @Title createCertAlipayClient
     * @Description: 构造有证书的支付宝支付的client
     * @author 伍成林
     * @date 2021年03月17日
     */
    private DefaultAlipayClient createCertAlipayClient(AliPayConfig aliPayConfig) throws AlipayApiException {
        //构造client
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        //设置网关地址
        certAlipayRequest.setServerUrl(aliPayConfig.getOpenApiDomain());
        //设置应用Id
        certAlipayRequest.setAppId(aliPayConfig.getAppid());
        //设置应用私钥
        certAlipayRequest.setPrivateKey(aliPayConfig.getPrivateKey());
        //设置请求格式，固定值json
        certAlipayRequest.setFormat(aliPayConfig.getRequestFormat());
        //设置字符集
        certAlipayRequest.setCharset(aliPayConfig.getCharset());
        //设置签名类型
        certAlipayRequest.setSignType(aliPayConfig.getSignType());
        //设置应用公钥证书路径
        certAlipayRequest.setCertPath(aliPayConfig.getAppCertPath());
        //设置支付宝公钥证书路径
        certAlipayRequest.setAlipayPublicCertPath(aliPayConfig.getAlipayCertPath());
        //设置支付宝根证书路径
        certAlipayRequest.setRootCertPath(aliPayConfig.getAlipayRootCertPath());
        return new DefaultAlipayClient(certAlipayRequest);
    }
}
