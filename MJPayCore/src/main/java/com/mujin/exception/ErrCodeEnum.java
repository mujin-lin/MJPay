/**   
* @Title: ErrCodeEnum.java 
* @Package com.fa.core.exception
* @Description: TODO 
* @author: 曾明辉  
* @date 2019年8月15日   
*/
package com.mujin.exception;

/**
 * @Description: TODO
 * @author: 曾明辉
 * @date: 2019年8月15日
 */
public enum ErrCodeEnum {

	/** 
	* SUBMIT_ORDER: 下单异常
	*/
	SUBMIT_ORDER(101);


	/**
	 * 错误代码
	 */
	private int code;

	/**
	 * 
	 * @Title:
	 * @Description:
	 * @param code 错误码
	 */
	ErrCodeEnum(int code) {
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

}
