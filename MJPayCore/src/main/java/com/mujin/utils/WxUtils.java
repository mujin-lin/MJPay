package com.mujin.utils;

import com.mujin.dto.PaymentWx;
import com.mujin.dto.wx.UnifiedOrderDto;
import com.mujin.enums.OrderEnums;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class WxUtils {
	/**
	 * 默认字符编码
	 */
	private static final String DEFAULT_CHARSET = "UTF-8";
	/**
	 * 向微信返回正确的数据
	 */
	public static final String SUCCESS_REPONSE="<xml>" + 
			"<return_code><![CDATA[SUCCESS]]></return_code>" + 
			"<return_msg><![CDATA[OK]]></return_msg>" + 
			"</xml>";
    /**
     * 向微信返回失败的数据方便微信重新推送
     */
	public static final String FAUILT_REPONSE="<xml>" + 
			"<return_code><![CDATA[FAIL]]></return_code>" + 
			"<return_msg><![CDATA[签名失败]]></return_msg>" + 
			"</xml>";
	 /**
	    * 输入流转化为字符串
     *
     * @param inputStream 流
     * @return String 字符串
     * @throws Exception
     */
    public static String getStreamString(InputStream inputStream,String charSet) throws Exception {
        StringBuffer buffer = new StringBuffer();
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
        	if (charSet == null) {
        		inputStreamReader = new InputStreamReader(inputStream,DEFAULT_CHARSET);
        	}else {
        		inputStreamReader = new InputStreamReader(inputStream,charSet);
        	}
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            throw new Exception();
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return buffer.toString();
    }

    /**
     * 统一下单API的基础map封装
     *
     * @param unifiedOrder 统一下单的dto
     * @return Map<String, String> 封装完毕的map
     */
    public static Map<String, String> createRequestMap(UnifiedOrderDto unifiedOrder) {

        // 微信支付请求的封装
        Map<String, String> data = new HashMap<String, String>();
        // 商户订单号
        data.put(OrderEnums.OUT_TRADE_NO.getValue(), unifiedOrder.getOutTradeNo());
        // wxPay.fillRequestData(data);
        // 商品描述
        data.put(OrderEnums.BODY.getValue(), unifiedOrder.getBody());
        // 标价币种
        data.put(OrderEnums.FEE_TYPE.getValue(), "CNY");
        // 标价金额
        data.put(OrderEnums.TOTAL_FEE.getValue(), String.valueOf(unifiedOrder.getTotalFee()));
        // 用户的IP
        data.put(OrderEnums.SPBILL_CREATE_IP.getValue(), unifiedOrder.getSpbillCreateIp());

        // 交易类型
        data.put(OrderEnums.TRADE_TYPE.getValue(), unifiedOrder.getTradeType());
        // 判断传递进来的超时时间的毫秒数
        if (StrUtils.isNotBlank(unifiedOrder.getTimeExpire())){
            // 过期时间
            data.put(OrderEnums.TIME_EXPIRE.getValue(), unifiedOrder.getTimeExpire());
        }
        return data;
    }

    /**
     * 主要用于将微信返回值的数据处理完毕后将逗号替换成""
     * @Title 
     * @Description
     * @param str
     * @return
     * @return String
     * @author 伍成林
     * @date: 2020年3月20日
     */
    private static  String replaceData(String str) {
    	return str.replaceAll(",", "").trim();
    }

}
