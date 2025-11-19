package com.cyberrange.api.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.cyberrange.api.model.LabSteps;
import com.cyberrange.api.model.Modules;
import com.cyberrange.api.model.UserModule;
import com.cyberrange.api.repository.LabStepsRepository;
import com.cyberrange.api.repository.ModuleRepository;
import com.cyberrange.api.repository.UserModuleRepository;
import com.cyberrange.api.controller.GoogleAPI;

@Service
@Transactional
public class LabServiceImpl implements LabService {

	
	@Autowired
	UserModuleRepository repository ;
	
	@Autowired
	LabStepsRepository labStepsRepository  ;
	
	@Autowired
	ModuleRepository moduleRepository;
	
	@Autowired
	UserModuleRepository userModuleRespository;

	@Autowired
	EvaluationService evaluationService;

	@Autowired
	GoogleAPI googleAPI;
	
	@Override
	public UserModule getStatusofLab(Long moduleId, Long userId) {
		
		UserModule userModule = repository.find( moduleId,  userId);
		if(null != userModule) {
			userModule.setModule(moduleRepository.findOne(moduleId));
		}
		return userModule;
	}

	@Override
	public Modules getLab(Long moduleId) {
		// TODO Auto-generated method stub
		Modules module = moduleRepository.findOne(moduleId);
		return module;
	}

	@Override
	public UserModule startLab(Long moduleId, Long userId) {

		UserModule userModule = new UserModule();
		userModule.setSliceCreated(null);
		userModule.setStepsCompleted("0");
		
		userModule.setModuleId(moduleId);
		userModule.setUserId(userId);
		
		Date today = new Date();
		SimpleDateFormat sf =  new SimpleDateFormat("yyyy-MM-dd");
		Date simpleDate;
		try {
			simpleDate = sf.parse(sf.format(today));
			System.out.println(today+"\t"+simpleDate.toString()+"\t"+sf.format(today));
			userModule.setEnrollmentDate(simpleDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		List<LabSteps> labSteps= labStepsRepository.find(moduleId);
		userModule.setTotalSteps(String.valueOf(labSteps.size()));
		userModule = repository.save( userModule);
		
		return userModule;
	
	}
	
	@Override
	public int updateLabSliceName(UserModule userModule) {
		int status =0;
	
		status = repository.updateSlice( userModule.getSliceCreated(), userModule.getModuleId(), userModule.getUserId(), userModule.getGeniUsername(), new Date(), userModule.getGeniSliceStatus());

		return status;
	}
	
	@Override
	public int updateGoogleVMInfo(UserModule userModule) {
		return repository.updateGoogleVMInfo(userModule.getSliceCreated(), userModule.getModuleId(), userModule.getUserId(), userModule.getResourcesReserved(),
				userModule.getSliceCreateddt(), userModule.getResourcesReservedDt());
	}
	
	@Override
	public int updateLabResoucesStatus(UserModule userModule) {
		int status = repository.updateLabResoucesStatus(  userModule.getModuleId(), userModule.getUserId(), userModule.getResourcesReservedDt() , userModule.getResourcesReserved());
		return status;
	}

	
	public int updateStatusofLab(Long moduleId, Long userId, String step) {
		int userModule = repository.updateStatusofLab( moduleId, userId, step);
		return userModule;
		
	}

	@Override
	public List<LabSteps> getLabStep(Long moduleId) {

		List<LabSteps>  labSteps=labStepsRepository.find(moduleId);
		return labSteps;
	}

	@Override
	public int updateStatusAndCompleteLab(Long moduleId, Long userId, String step) {
		int userModule = repository.updateStatusAndCompleteLab( moduleId, userId, step, new Date());
		return userModule;
		
	}

	@Override
	public Long getCountOfEntrollement(Long moduleId) {
		Long enrollNo = repository.getCountOfEntrollement(moduleId);
		return enrollNo;
	}

	@Override
	public Long getTotalCountOfEntrollement(long l) {
		return repository.count();
	}

	@Override
	public UserModule endLab(Long moduleId, Long userId) {
		// TODO Auto-generated method stub
		System.out.println("Deleting Module");
		try{
			String request = "{moduleId:"+moduleId+", userId:"+userId+"}";
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("moduleId", moduleId);
			jsonObject.put("userId", userId);
			googleAPI.deleteCompletedResourcesForLab(jsonObject.toJSONString());
			evaluationService.deleteUserEvaluation(userId, moduleId);
			userModuleRespository.deleteCourse(moduleId, userId);
		}
		catch (Exception e) {
			System.out.println("Error Occured : " + e);
		}
		return null;
	}

	@Override
	public int createLabsForModule(String request) {
		// TODO Auto-generated method stub
		int result = 1;
		try{
			JSONParser parser = new JSONParser();
			JSONObject lab_json = (JSONObject) parser.parse(request);
			System.out.println("\nIn Lab Service\n\n"+lab_json);

			Long moduleId = Long.parseLong(lab_json.get("module_id").toString());
			Modules module = moduleRepository.getModuleById(moduleId);
			
			
			JSONObject labNames = (JSONObject) parser.parse(lab_json.get("labNames").toString()) ;
			int noOfLabs = Integer.parseInt(lab_json.get("noOfLabs").toString());
			for (int i = 1; i <= noOfLabs; i++) {

				String stepName = labNames.get(i+"").toString();
				String stepId = i+"";
				String stepStatus = "";
				String viewName = "";
				
				LabSteps steps = new LabSteps();
				steps.setModule(module);
				steps.setModuleId(moduleId);
				steps.setStepId(stepId);
				steps.setStepName(stepName);
				steps.setStepStatus(stepStatus);
				steps.setViewName(viewName);
				
				LabSteps added_steps =  labStepsRepository.save(steps);
				System.out.println("Lab steps succesfully saved for "+moduleId);
				
			}


		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		return result;
	}

	@Override
	public int deleteLabSteps(Long moduleId) {
		// TODO Auto-generated method stub
		System.out.println("Deleting lab steps");
		int r = 0;
		try {
			r = labStepsRepository.deleteLabStepsByModuleId(moduleId);
		} catch (Exception e) {
			//TODO: handle exception
			System.out.println(e.getMessage());
		}
		
		return r;
	}

	

	

	

}
