package com.mujin.serverice;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.mujin.config.OrderCreate;
import com.mujin.config.WxConfigs;
import com.mujin.dto.PaymentWx;
import com.mujin.dto.wx.UnifiedOrderDto;
import com.mujin.enums.OrderEnums;
import com.mujin.enums.SuccessEnums;
import com.mujin.enums.WxCommonEnums;
import com.mujin.enums.WxPayEnum;
import com.mujin.exception.ErrCodeEnum;
import com.mujin.exception.PayException;
import com.mujin.utils.PayUtils;
import com.mujin.utils.WxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Description:
 *
 * @Author: 伍成林
 * @Date： 2021年 03月15日
 */
@Slf4j
@Service
public class WxpayService {
    @Autowired
    private OrderCreate orderCreate;
    @Autowired
    private WXPay wxPay;
    @Autowired
    private WxConfigs wxConfigs;



    /**
     * 生成微信支付二维码供给用户扫码
     *
     * @Title createQrCode
     * @Description
     * @param paymentWx 微信支付的原始参数的接受
     * @throws Exception
     * @return ResponseResult
     * @author 伍成林
     * @date: 2020年3月13日
     */
    public ResponseResult<String> createUnifiedOrder(UnifiedOrderDto unifiedOrder, HttpServletRequest request) throws Exception {

        Map<String, String> resultMap = this.getUnifiedOrderResult(unifiedOrder);
        // 请求微信失败返回异常
        if (WXPayConstants.FAIL.equals(resultMap.get(WxPayEnum.RETURN_CODE.getValue()))){
            throw new PayException(ErrCodeEnum.SUBMIT_ORDER.getCode(), resultMap.get(WxPayEnum.RETURN_MSG.getValue())))
        }

        if (WXPayConstants.SUCCESS.equals(resultMap.get(WxPayEnum.RESULT_CODE.getValue()))) {

            // 预付款成功后需要完成的业务逻辑

            return ResponseUtils.success(resultMap.get(WxPayEnum.CODE_URL.getValue()));
        }
        paymentWx.setErrCode(resultMap.get(WxPayEnum.ERR_CODE.getValue()));
        paymentWx.setErrCodeDes(resultMap.get(WxPayEnum.ERR_CODE_DES.getValue()));


        // 微信预生成订单之后的业务逻辑

        log.info("微信返回的错误码信息为，{}", resultMap.get(WxPayEnum.ERR_CODE_DES.getValue()));

        return ResponseUtils.fail(ErrCodeEnum.BUSINESS.getCode(), "下单失败！");

    }

    /**
     * 生成微信支付二维码供给用户扫码
     *
     * @Title createQrCode
     * @Description
     * @param paymentWx 微信支付的原始参数的接受
     * @throws Exception
     * @return ResponseResult
     * @author 伍成林
     * @date: 2020年3月13日
     */
//    public ResponseResult<String> createH5Pay(PaymentWx paymentWx) throws Exception {
//        // 调用统一下单接口
//        Map<String, String> resultMap = getUnifiedOrderResult(paymentWx, PayTypeEnums.MWEB, wxConfigs.getRechargeNotifyUrl());
//
//        if ("SUCCESS".equals(resultMap.get(WxPayEnum.RETURN_CODE.getValue()))
//                && "SUCCESS".equals(resultMap.get(WxPayEnum.RESULT_CODE.getValue()))) {
//
//            // 预付款成功后需要完成的业务逻辑
//
//            return ResponseUtils.success(resultMap.get("h5_url"));
//        }
//        paymentWx.setErrCode(resultMap.get(WxPayEnum.ERR_CODE.getValue()));
//        paymentWx.setErrCodeDes(resultMap.get(WxPayEnum.ERR_CODE_DES.getValue()));
//
//        // 微信预生成订单之后的业务逻辑
//
//        log.info("微信返回的错误码信息为，{}", resultMap.get(WxPayEnum.ERR_CODE_DES.getValue()));
//
//        return ResponseUtils.fail(ErrCodeEnum.BUSINESS.getCode(), resultMap.get(WxPayEnum.RETURN_MSG.getValue()));
//
//    }

    /**
     * 统一下单的接口调用
     *
     * @param unifiedOrder 统一下单的dto
     * @return Map<String, String>
     * @throws Exception exception
     */
    private Map<String, String> getUnifiedOrderResult(UnifiedOrderDto unifiedOrder) throws Exception {
        Map<String, String> data = WxUtils.createRequestMap(unifiedOrder);
        // 通知地址
        data.put(OrderEnums.NOTIFY_URL.getValue(), unifiedOrder.getNotifyUrl());
        wxPay.fillRequestData(data);

        log.info("生成订单时的sign_type是：{}", wxConfigs.getSignType());
        String responseString = wxPay.requestWithoutCert(WXPayConstants.UNIFIEDORDER_URL, data,
                wxConfigs.getHttpConnectTimeoutMs(), wxConfigs.getHttpReadTimeoutMs());

        log.info("调用统一下单API返回的结果XML为：{}", responseString);
        // 解析返回的xml
        Map<String, String> resultMap = wxPay.processResponseXml(responseString);
        data.putAll(resultMap);
        return resultMap;
    }

    /**
     * 处理数据
     *
     * @Title dealWithData
     * @Description
     * @param reqMap 支付回调的map
     * @throws Exception
     * @return boolean
     * @author 伍成林
     * @date: 2020年3月18日
     */
    private boolean dealWithData(Map<String, String> reqMap) throws Exception {
        String resultCode = reqMap.get(WxPayEnum.RESULT_CODE.getValue());
        // 打印出所有的key 和 value 查看出哪些是需要的属性
        // 具体返回的值有哪些建议参照官方文档： https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=9_7&index=8
        reqMap.forEach((key, value) -> {
            log.info("key: {} value : {} ",key,value);
        });

        log.info("异步回调时订单时的sign_type：{}", reqMap.get(WxPayEnum.SIGN_TYPE.getValue()));

        // 签名认证：微信方异步回调的数据需要进行签名认证,判断签名是否为这次支付的签名
        Boolean flagBoolean = this.signVerification(reqMap);

        return WXPayConstants.SUCCESS.equals(resultCode) && flagBoolean;

    }

    /**
     * 签名验证
     *
     * @Title signVerification
     * @Description
     * @param xmlMap
     * @return
     * @throws Exception
     * @return Boolean
     * @author 木槿
     * @date: 2020年3月19日
     */
    public Boolean signVerification(Map<String, String> xmlMap) throws Exception {

        String oldSign = xmlMap.get(WxCommonEnums.SIGN.getValue());
        log.info("回调的原始签名为：{}", oldSign);
        String newSign = WXPayUtil.generateSignature(xmlMap, wxConfigs.getKey(),
                PayUtils.getSignType(xmlMap.get(WxPayEnum.SIGN_TYPE.getValue())));
        log.info("重新签名时的签名为：{}", newSign);
        return oldSign.equals(newSign);
    }





}
