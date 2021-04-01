package com.mujin.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 *
 * @Author: 伍成林
 * @Date： 2021年 03月18日
 */
@Configuration
@Data
public class PayPropertiesPath {
//    @Value("${alipay.property.path}")
    private String aliPayProperty;

//    @Value("${wxpay.property.path}")
    private String wxPayProperty;
}
