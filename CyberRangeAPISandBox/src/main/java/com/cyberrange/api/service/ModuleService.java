package com.cyberrange.api.service;

import java.util.List;

import com.cyberrange.api.bean.ModuleStats;
import com.cyberrange.api.bean.ModuleGraphStats;
import com.cyberrange.api.model.Modules;
import com.cyberrange.api.model.UserModule;

public interface ModuleService {

	public List<Modules> getModulesFromDifficultyLevel( String difficultyLevel);

	public List<UserModule> getModulesForUser(Long userId);

	public Modules getModuleDetailsFromId(Long moduleId);
	
	public  List<Modules> getAllModules();

	public Long createNewModule(String request);

	public int deleteModule(Long moduleId);

	public List<ModuleStats> getUserModuleCount();

	public List<UserModule> getUserModulesFromId(Long moduleId);
	
	public List<ModuleGraphStats> getUserModuleDataMonthWise();

}
