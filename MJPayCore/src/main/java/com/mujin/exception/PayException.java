/**
 * <li>文件名：BusinessException.java
 * <li>说明：
 * <li>创建人： 曾明辉
 * <li>创建日期：2018年10月29日
 * <li>修改人： 
 * <li>修改日期：
 */
package com.mujin.exception;

/**
 * 
 * @Description: TODO
 * @author: 伍成林
 * @date: 2019年8月14日
 */
public class PayException extends Exception {

	/**
	 * 类型：long
	 */
	private static final long serialVersionUID = -3804995326646218863L;

	/**
	 * 错误代码
	 */
	private int errCode;

	/**
	 * 错误信息
	 */
	private String errMsg;

	/**
	 * @return the errCode
	 */
	public int getErrCode() {
		return errCode;
	}

	/**
	 * @param errCode
	 *            the errCode to set
	 */
	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	/**
	 * @return the errMsg
	 */
	public String getErrMsg() {
		return errMsg;
	}

	/**
	 * @param errMsg
	 *            the errMsg to set
	 */
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	/**
	 * 
	 * @Title:
	 * @Description:
	 * @param errCode
	 *            错误编码
	 * @param errMsg
	 *            错误消息
	 */
	public PayException(int errCode, String errMsg) {
		super(errMsg);
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	/**
	 * 
	 * @Title:
	 * @Description:
	 * @param errMsg
	 *            错误消息
	 */
	public PayException(String errMsg) {
		super(errMsg);
		this.errCode = ErrCodeEnum.BUSINESS.getCode();
		this.errMsg = errMsg;
	}

}
