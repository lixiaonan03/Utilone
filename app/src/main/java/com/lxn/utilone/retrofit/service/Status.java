package com.lxn.utilone.retrofit.service;

import com.google.gson.annotations.SerializedName;
import com.lxn.utilone.util.StringUtils;

import java.io.Serializable;

public class Status implements Serializable{

	@SerializedName("error_code")
	public String error_code;
	
	@SerializedName("error_message")
	public String error_message;

	public String getError_code() {
		if(StringUtils.isNotBlank(error_code)){
			return error_code;
		}else{
			return "";
		}
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	public String getError_message() {
		if(StringUtils.isNotBlank(error_message)){
			return error_message;
		}else{
			return "";
		}
	}

	public void setError_message(String error_message) {
		this.error_message = error_message;
	}
	
}
