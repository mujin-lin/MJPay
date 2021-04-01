package com.mujin.enums;

/**
 * Description:
 *
 * @Author: 伍成林
 * @Date： 2021年 03月31日
 */
public enum SuccessEnums {
    /**
     * 大写的success
     */
    UPPER_SUCCESS("SUCCESS"),
    /**
     * 小写的success
     */
    LOWER_SUCCESS("success"),
    /**
     * 大写的fail
     */
    UPPER_FAIL("FAIL"),
    /**
     * 小写的fail
     */
    LOWER_FAIL("fail");
    /**
     * 值
     */
    private final String value;


    SuccessEnums(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
