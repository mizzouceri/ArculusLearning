package com.cyberrange.api.service;

import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cyberrange.api.bean.ModuleStats;
import com.cyberrange.api.bean.ModuleGraphStats;
import com.cyberrange.api.model.Modules;
import com.cyberrange.api.model.UserModule;
import com.cyberrange.api.repository.ModuleRepository;
import com.cyberrange.api.repository.UserModuleRepository;

@Service
public class ModuleServiceImpl implements ModuleService{
	
	@Autowired
	ModuleRepository moduleRepository;
	
	@Autowired
	UserModuleRepository userModuleRepository;
	
	public List<Modules> getModulesFromDifficultyLevel( String difficultyLevel){
		List<Modules> evaluationList=moduleRepository.find( difficultyLevel);
		return evaluationList;
	}

	@Override
	public List<UserModule> getModulesForUser(Long userId) {
		List<UserModule> moUserModules=userModuleRepository.getModulesForUser( userId);
		for(UserModule userModule:moUserModules) {
			Double persentage =  ((Double.parseDouble(userModule.getStepsCompleted())/Double.parseDouble(userModule.getTotalSteps()))*100);
			Long roundpersentage = Math.round(persentage);
				
			userModule.setLabCompletedPersentage(roundpersentage.toString());
		}
		return moUserModules;
	}
	
	public Modules getModuleDetailsFromId(Long moduleId) {
		Modules module = moduleRepository.findOne(moduleId);
		return module;
	}

	@Override
	public List<Modules> getAllModules() {
		// TODO Auto-generated method stub
		List<Modules> allModules = moduleRepository.findAllModules();

		return  allModules;
	}

	@Override
	public Long createNewModule(String request) {

		try{
			
			JSONParser parser = new JSONParser();
			JSONObject module_json=(JSONObject) parser.parse(request);
			String createdBy = null;
			String createdDt = null;
			String difficultyLevel = module_json.get("difficultyLevel").toString();
			String labStarted = null;
			String moduleBg = module_json.get("moduleBg").toString();
			String moduleDescription = module_json.get("moduleDescription").toString();
			String moduleImage = "#";
			String moduleTag = module_json.get("moduleTag").toString();
			String[] moduleTags = module_json.get("moduleTag").toString().split(",");
			String moduleTime = module_json.get("moduleTime").toString() + " hours";
			String moduleTitle = module_json.get("moduleTitle").toString();
			String updatedBy = null;
			String updatedDt = null;
			
			Modules module = new Modules();
			module.setDifficultyLevel(difficultyLevel);
			module.setModuleBg(moduleBg);
			module.setModuleDescription(moduleDescription);
			module.setModuleImage(moduleImage);
			module.setModuleTag(moduleTag);
			module.setModuleTags(moduleTags);
			module.setModuleTime(moduleTime);
			module.setModuleTitle(moduleTitle);
			
			Modules saved_module = moduleRepository.save(module);
			System.out.println("\nIn Module Service\n\n"+module_json);
			System.out.println("Module Saved");
			return saved_module.getModuleId() ;
			
			//return Long.parseLong("");
		}
		catch(Exception e){
			System.out.print(e.getMessage());
			return Long.parseLong("-1");
		}
	}

	@Override
	public int deleteModule(Long moduleId) {
		// TODO Auto-generated method stub
		System.out.println("Deleting module");
		//Modules module = moduleRepository.findOne(moduleId);
		try{
			List<UserModule> userModules =  userModuleRepository.getUserModulesByModuleId(moduleId);
			if(userModules.size() > 0){
				userModuleRepository.delete(userModules);
			}
			moduleRepository.delete(moduleId);
			return 1;
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		return 0;
	}

	@Override
	public List<ModuleStats> getUserModuleCount() {
		// TODO Auto-generated method stub
		
		List<ModuleStats> moduleStats = moduleRepository.getUserModuleCount();
		System.out.println(moduleStats.size());
		return moduleStats;
	}

	@Override
	public List<UserModule> getUserModulesFromId(Long moduleId) {
		// TODO Auto-generated method stub

		List<UserModule> userModules = userModuleRepository.getUserModulesByModuleId(moduleId);
		return userModules;
	}

	@Override
	public List<ModuleGraphStats> getUserModuleDataMonthWise() {
		// TODO Auto-generated method stub
		List<ModuleGraphStats> moduleGraphStats = userModuleRepository.getGraphStats();
		System.out.println(moduleGraphStats.size());
		return moduleGraphStats;
	}


}

