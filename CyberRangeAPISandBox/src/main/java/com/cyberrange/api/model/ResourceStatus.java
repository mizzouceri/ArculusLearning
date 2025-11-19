package com.cyberrange.api.model;

public class ResourceStatus {
	
	private String name;
	private String agg;
	private String geniOpId;
	private boolean status;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAgg() {
		return agg;
	}
	public void setAgg(String agg) {
		this.agg = agg;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getGeniOpId() {
		return geniOpId;
	}
	public void setGeniOpId(String geniOpId) {
		this.geniOpId = geniOpId;
	}
	
	

}
