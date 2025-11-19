package com.cyberrange.api.service;

import java.util.List;

import com.cyberrange.api.model.LabSteps;
import com.cyberrange.api.model.Modules;
import com.cyberrange.api.model.UserModule;

public interface LabService {
	
	public UserModule getStatusofLab(Long moduleId, Long userId);

	public Modules getLab(Long moduleId);
	
	public UserModule startLab(Long moduleId, Long userId);

    public UserModule endLab(Long moduleId, Long userId);

	public int updateLabSliceName(UserModule userModule) ;

	public int updateStatusofLab(Long moduleId, Long userId, String step);

	public int updateLabResoucesStatus(UserModule userModule);

	public List<LabSteps> getLabStep(Long moduleId);

	public int updateStatusAndCompleteLab(Long moduleId, Long userId, String step);
	
	public Long getCountOfEntrollement(Long moduleId);

	public Long getTotalCountOfEntrollement(long l);

	public int updateGoogleVMInfo(UserModule userModule) ;

	public int createLabsForModule(String request);

	public int deleteLabSteps(Long moduleId);
	
}
