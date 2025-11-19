package com.cyberrange.web.bean;

 
public class RequestBean<T> {
	
	private String request ;

	private T  object ;

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}
	
	

	

	
}
