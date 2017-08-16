package com.example.hhb.by12306.model;


import java.io.Serializable;

public class ResponseObject implements Serializable {
	private String obj;
	private boolean isSuccess = true;
	private String message;

	public ResponseObject() {
		// TODO Auto-generated constructor stub
	}

	public ResponseObject(Exception e) {
		this.isSuccess = false;
		this.message = e.getMessage();
	}

	public void setParameter(boolean isSuccess, String message) {
		this.isSuccess = isSuccess;
		this.message = message;
	}

	public void setParameter(Object obj, String message) {
//		this.obj = BaseUtil.getJson(obj);//obj to json
//		this.obj = JSON.toJSONStringWithDateFormat(obj,"yyyy-MM-dd HH:mm:ss");
		this.setObj(obj);
		this.message = message;
		this.isSuccess = true;
	}

	public String getObj() {
		return obj;
	}

	public void setObj(Object obj) {
//		this.obj = BaseUtil.getJson(obj);
//		this.obj = JSON.toJSONStringWithDateFormat(obj,"yyyy-MM-dd HH:mm:ss");
		if(obj instanceof String){
			String str = (String)obj;
			this.obj = str;
		}else{
			this.obj = obj.toString();
		}

	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setError(Exception e) {
		this.message = e.getMessage();
		this.isSuccess = false;
	}
}
