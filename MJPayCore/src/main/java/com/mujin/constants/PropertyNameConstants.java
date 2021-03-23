package com.mujin.constants;

/**
 * Description:
 *
 * @Author: 伍成林
 * @Date： 2021年 03月18日
 */
public final class PropertyNameConstants {
    /**
     * 支付宝配置文件名开头
     */
    public static final String ALI_PAY_PREFIX = "alipay";

    /**
     * 微信支付
     */
    public static final String WX_PAY_PREFIX = "wxpay";
    /**
     * Spring系统的配置文件
     */
    public static final String APPLICATION_PREFIX = "application";
    /**
     * 以.properties结尾的
     */
    public static final String PROPERTIES_SUFFIX = ".properties";
    /**
     * 以.yml结尾的配置文件 只读取application的.yml文件
     */
    public static final String YML_SUFFIX = ".yml";

    /**
     * 私有化构造方法
     */
    private PropertyNameConstants() {

    }
}
