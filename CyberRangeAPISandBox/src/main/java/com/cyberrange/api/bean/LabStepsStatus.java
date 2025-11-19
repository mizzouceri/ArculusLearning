package com.cyberrange.api.bean;

import java.util.List;

import com.cyberrange.api.model.LabSteps;
import com.cyberrange.api.model.UserModule;

public class LabStepsStatus {
	private List<LabSteps> labSteps= null;
	private UserModule userModule =  null;
	public List<LabSteps> getLabSteps() {
		return labSteps;
	}
	public void setLabSteps(List<LabSteps> labSteps) {
		this.labSteps = labSteps;
	}
	public UserModule getUserModule() {
		return userModule;
	}
	public void setUserModule(UserModule userModule) {
		this.userModule = userModule;
	}
	
	
	
}
