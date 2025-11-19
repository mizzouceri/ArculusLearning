package com.cyberrange.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyberrange.api.bean.LabDetails;
import com.cyberrange.api.bean.LabStepsStatus;
import com.cyberrange.api.model.EvaluationAnswers;
import com.cyberrange.api.model.EvaluationQuestions;
import com.cyberrange.api.model.LabSteps;
import com.cyberrange.api.model.Modules;
import com.cyberrange.api.model.UserModule;
import com.cyberrange.api.service.EvaluationService;
import com.cyberrange.api.service.LabService;
import com.cyberrange.api.service.ModuleService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/lab")
public class LabsAPI {
	
	@Autowired
	ModuleService moduleService;
	@Autowired
	LabService labService;

	@Autowired
	EvaluationService evaluationService;
	
	@PostMapping("/getLabStepWithStatus")
	public ResponseEntity<LabStepsStatus> getLabStepWithStatus(@RequestBody String request){
		LabStepsStatus labStepsStatus = new LabStepsStatus();
	
		try {
			List<LabSteps> labSteps= null;
			UserModule userModule =  null;
			
			JSONParser parser = new JSONParser();

			JSONObject json=(JSONObject) parser.parse(request);
			Long moduleId =  Long.parseLong(String.valueOf(json.get("moduleId")));
			Long userId =  Long.parseLong(String.valueOf(json.get("userId")));

			labSteps = labService.getLabStep(moduleId);
			
			userModule =	labService.getStatusofLab(moduleId, userId);

			labStepsStatus.setLabSteps(labSteps);
			labStepsStatus.setUserModule(userModule);
		
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		return new ResponseEntity<LabStepsStatus>(labStepsStatus, HttpStatus.OK);
	
	}

	@PostMapping("/getLabDetails")
	public ResponseEntity<LabDetails> getLabDetails(@RequestBody String request){

		LabDetails labDetails = new LabDetails();
		try {
			List<LabSteps> labSteps= null;
			Modules module =  null;
			List<EvaluationQuestions> evaluationTechQuestions= null;
			List<EvaluationQuestions> evaluationSrvyQuestions= null;
			List<EvaluationAnswers> evaluationTechAnswers = null;
			
			JSONParser parser = new JSONParser();

			JSONObject json=(JSONObject) parser.parse(request);
			Long moduleId =  Long.parseLong(String.valueOf(json.get("moduleId")));

			labSteps = labService.getLabStep(moduleId);
			
			module =	labService.getLab(moduleId);

			evaluationTechQuestions = evaluationService.getQuestionForEvaluation(moduleId, "TECH_ASMT");
			evaluationSrvyQuestions = evaluationService.getQuestionForEvaluation(moduleId, "PRE_SRVY");
			
			List<EvaluationQuestions> newList = new ArrayList<EvaluationQuestions>(evaluationSrvyQuestions);
			newList.addAll(evaluationTechQuestions);

			

			labDetails.setLabSteps(labSteps);
			labDetails.setModule(module);
			labDetails.setEvaluationQuestions(newList);
			
		
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		return new ResponseEntity<LabDetails>(labDetails, HttpStatus.OK);
	}
	
	
	@PostMapping("/getLabStep")
	public ResponseEntity<List<LabSteps>> getLabStep(@RequestBody String request){
		
		List<LabSteps> labSteps= null;
		try {
			
			JSONParser parser = new JSONParser();

			JSONObject json=(JSONObject) parser.parse(request);
			Long moduleId =  Long.parseLong(String.valueOf(json.get("moduleId")));

			labSteps = labService.getLabStep(moduleId);
		
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		return new ResponseEntity<List<LabSteps>>(labSteps, HttpStatus.OK);
	
	}
	
	@PostMapping("/getStatusofLab")
	public ResponseEntity<UserModule> getStatusofLab(@RequestBody String request){
		UserModule userModule =  null;
		
		try {

			JSONParser parser = new JSONParser();
			JSONObject json=(JSONObject) parser.parse(request);

			Long moduleId =  Long.parseLong(String.valueOf(json.get("moduleId")));
			Long userId =  Long.parseLong(String.valueOf(json.get("userId")));

			userModule = labService.getStatusofLab(moduleId, userId);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		

		return new ResponseEntity<UserModule>(userModule, HttpStatus.OK);
	}
	
	
	
	@PostMapping("/startLab")
	public ResponseEntity<UserModule> startLab(@RequestBody String request){
		UserModule userModule =  null;
		
		try {

			JSONParser parser = new JSONParser();
			JSONObject json=(JSONObject) parser.parse(request);

			Long moduleId =  Long.parseLong(String.valueOf(json.get("moduleId")));
			Long userId =  Long.parseLong(String.valueOf(json.get("userId")));

			userModule = labService.startLab(moduleId, userId);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		

		return new ResponseEntity<UserModule>(userModule, HttpStatus.OK);
	}


	@PostMapping("/endLab")
	public ResponseEntity<List<UserModule>> endLab(@RequestBody String request){
		List<UserModule> userModules =  null;
		
		try {

			JSONParser parser = new JSONParser();
			JSONObject json=(JSONObject) parser.parse(request);
			Long userId = Long.parseLong(String.valueOf(json.get("userId")));

			Long moduleId =  Long.parseLong(String.valueOf(json.get("moduleId")));
		
			 labService.endLab(moduleId, userId);
			 userModules = moduleService.getModulesForUser( userId);
			
			 if(null != userModules  && !userModules.isEmpty()) {
				 //List<Long> moduleIds = userModules.stream().map(UserModule::getModuleId).collect(Collectors.toList());
				 
				 userModules.stream().forEach(x -> x.setModule(moduleService.getModuleDetailsFromId(x.getModuleId())));
			 } 	
		} catch (ParseException e) {
			e.printStackTrace();
		}

	return new ResponseEntity<List<UserModule>>(userModules, HttpStatus.OK);

	
	}


	@PostMapping("/updateStatusofLab")
	public ResponseEntity<String> updateStatusofLab(@RequestBody String request){
		String userModule ="FAIL";
		
		try {

			JSONParser parser = new JSONParser();
			JSONObject json=(JSONObject) parser.parse(request);

			Long moduleId =  Long.parseLong(String.valueOf(json.get("moduleId")));
			Long userId =  Long.parseLong(String.valueOf(json.get("userId")));
			String step =  String.valueOf(json.get("stepsCompleted"));


			int status = labService.updateStatusofLab(moduleId, userId,step);
			userModule = "SUCCESS";
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", userModule);

		return new ResponseEntity<String>(jsonObject.toString(), HttpStatus.OK);
	}
	
	@PostMapping("/updateStatusAndCompleteLab")
	public ResponseEntity<String> updateStatusAndCompleteLab(@RequestBody String request){
		String userModule ="FAIL";
		
		try {

			JSONParser parser = new JSONParser();
			JSONObject json=(JSONObject) parser.parse(request);

			Long moduleId =  Long.parseLong(String.valueOf(json.get("moduleId")));
			Long userId =  Long.parseLong(String.valueOf(json.get("userId")));
			String step =  String.valueOf(json.get("stepsCompleted"));


			int status = labService.updateStatusAndCompleteLab(moduleId, userId,step);
			userModule = "SUCCESS";
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", userModule);

		return new ResponseEntity<String>(jsonObject.toString(), HttpStatus.OK);
	}
}
