package com.cyberrange.api.bean;

import com.cyberrange.api.utility.JSONtoObject;

public class RequestBean {
	
	private String request ;



	public <T> T getObject(Class<T> classType) {
		Object object = JSONtoObject.jsonToObject(request, classType);
        return classType.cast(object);
    }
	

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}


	
}
