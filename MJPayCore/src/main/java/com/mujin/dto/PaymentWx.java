package com.mujin.dto;

import com.mujin.exception.PayException;
import com.mujin.exception.ErrCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * The persistent class for the payment_wx database table.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentWx implements Serializable {
    private static final long serialVersionUID = 1L;
    // -----------------------------------------------------------------------------
    //           用于接收支付信息其中属性名参照微信支付官方文档
    //           https://pay.weixin.qq.com/wiki/doc/api/index.html
    // -----------------------------------------------------------------------------
    private String id;
    /**
     * appid
     */
    private String appId;

    private String attach;
    /**
     * bank_type
     */
    private String bankType;

    private String body;
    /**
     * cash_fee
     */
    private Integer cashFee;
    /**
     * cash_fee_type
     */
    private String cashFeeType;
    /**
     * qr_code
     */
    private String codeUrl;
    /**
     * coupon_count
     */
    private Integer couponCount;
    /**
     * coupon_fee
     */
    private Integer couponFee;

    /**
     * err_code
     */
    private String errCode;
    /**
     * err_code_msg
     */
    private String errCodeDes;
    /**
     * fee_type
     */
    private String feeType;
    /**
     * goods_tag
     */
    private String goodsTag;
    /**
     * is_subscribe
     */
    private String isSubscribe;
    /**
     * limit_pay
     */
    private String limitPay;

    private String mchId;
    /**
     * nonce_str
     */
    private String nonceStr;

    private String openid;

    private String outTradeNo;
    /**
     * prepay_id
     */
    private String prepayId;
    /**
     * product_id
     */
    private String productId;
    /**
     * product_sort
     */
    private String productSort;
    /**
     * settlement_total_fee
     */
    private Integer settlementTotalFee;

    private String sign;
    /**
     * sign_type
     */
    private String signType;
    /**
     * spbill_create_ip
     */
    private String spbillCreateIp;

    private String status;

    private Date timeEnd;
    /**
     * time_expire
     */
    private Date timeExpire;
    /**
     * time_start
     */
    private Date timeStart;
    /**
     * total_fee
     */
    private Integer totalFee;
    /**
     * trade_type
     */
    private String tradeType;
    /**
     * transaction_id
     */
    private String transactionId;
    /**
     * returnCode
     */
    private String returnCode;
    /**
     * returnMessage
     */
    private String returnMsg;
    /**
     * resultCode:
     */
    private String resultCode;

    public void setTimeEnd(Object endTime) throws Exception {
        this.timeEnd = this.objectToDate(endTime);
    }

    public void setTimeStart(Object timeStart) throws Exception {
        this.timeStart = this.objectToDate(timeStart);
    }

    /**
     * @param time 时间的处理
     * @return Date
     * @throws Exception
     * @Title objectToDate
     * @Description: 将object符合时间格式的字符串转换为date类型
     * @author 伍成林
     * @date 2021年03月15日
     */
    private Date objectToDate(Object time) throws Exception {
        Date date = null;
        if (time == null) {
            return null;
        } else if (time instanceof Long) {
            long l = (long) time;
            if (l <= 0) {
                return null;
            }
            date = new Date((Long) time);
        } else if (time instanceof Date) {
            date = (Date) time;
        } else if (time instanceof String) {
            try {
                Long t = Long.parseLong((String) time);
                date = new Date(t);
            } catch (Exception e) {
                if (((String) time).length() > 10) {
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    date = sdf1.parse((String) time);
                } else {
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                    date = sdf2.parse((String) time);
                }
            }
        } else if (time instanceof Integer) {
            if ((Integer) time > 0) {
                date = new Date((Integer) time);
            }
        } else {
            throw new PayException(ErrCodeEnum.DATACHECK.getCode(), "时间类型数据错误");
        }
        return date;
    }
}