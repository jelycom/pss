/*
 * 捷利商业进销存管理系统
 * @(#)ActionJsonResult.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-2-21
 */
package cn.jely.cd.util;

/**封装Action返回JSON格式的操作结果
 * 
 * @ClassName:ActionJsonResult
 * @Description:一般情况下:成功不用返回message,而不成功则需要返回message用于提示用户
 * @author 周义礼
 * @version 2013-2-21 下午9:12:10
 */
public class ActionJsonResult {
//	public static final String SUCCESS="success";
//	public static final String FAIL="fail";
//	public static final String INFO="info";
//	public static final String WARN="warn";
//	public static final String ERROR="error";
	
	/**@Fields operate:操作是否成功*/
	private boolean operate;
	/**@Fields messageType:返回的信息类型:信息,警告,错误*/
	private MessageType messageType;
	/**@Fields message:返回的信息*/
	private String message;
	/**@Fields resultObj:返回的结果实体*/
	private Object resultObj;
	
	
	public ActionJsonResult() {
		this.operate=true;
		this.message="";
	}
	
	
	public ActionJsonResult(Object resultObj) {
		this.resultObj = resultObj;
		this.messageType=MessageType.INFO;
		this.operate=resultObj==null?false:true;
		this.message="";
	}


	public ActionJsonResult(String erroMessage) {
		this.messageType=MessageType.ERROR;
		this.message = erroMessage;
	}

	public ActionJsonResult(MessageType messageType, String message) {
		this.messageType = messageType;
		this.message = message;
	}


	public ActionJsonResult(boolean operate, String message) {
		if(operate){
			this.messageType=MessageType.INFO;
		}else{
			this.messageType=MessageType.ERROR;
		}
		this.operate = operate;
		this.message = message;
	}
	
	public ActionJsonResult(boolean operate, MessageType messageType, String message) {
		this.operate = operate;
		this.messageType = messageType;
		this.message = message;
	}


	public ActionJsonResult(boolean operate, String message, Object resultObj) {
		if(operate){
			this.messageType=MessageType.INFO;
		}else{
			this.messageType=MessageType.ERROR;
		}
		this.operate = operate;
		this.message = message;
		this.resultObj = resultObj;
	}
	
	public ActionJsonResult(boolean operate, MessageType messageType, String message, Object resultObj) {
		this.operate = operate;
		this.messageType = messageType;
		this.message = message;
		this.resultObj = resultObj;
	}


	public boolean getOperate() {
		return operate;
	}
	public void setOperate(boolean operate) {
		this.operate = operate;
	}
	
	public MessageType getMessageType() {
		return messageType;
	}


	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}


	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getResultObj() {
		return resultObj;
	}
	public void setResultObj(Object resultObj) {
		this.resultObj = resultObj;
	}
	
	public enum MessageType{
		INFO(),WARN(),ERROR(),INTERRUPT();
	}
}

