package com.cyberrange.api.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cyberrange.api.model.Modules;
import com.cyberrange.api.model.UserModule;
import com.cyberrange.api.service.EvaluationService;
import com.cyberrange.api.service.LabService;
import com.cyberrange.api.service.ModuleService;
import org.springframework.http.MediaType;
import com.cyberrange.api.bean.ModuleStats;
import com.cyberrange.api.bean.ModuleGraphStats;
import com.cyberrange.api.google.UploadLib;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/modules")
public class ModulesAPI {

	@Autowired
	ModuleService moduleService;
	
	@Autowired
	LabService labService;

	@Autowired
	EvaluationService evaluationService;

	@Autowired
    UploadLib  uploadLib;

	@PostMapping("/getAllModules")
	public ResponseEntity<List<Modules>> getAllModules(){
		List<Modules> modules =  null;
		try{
			modules = moduleService.getAllModules();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<Modules>>(modules, HttpStatus.OK);
	}

	@PostMapping("/getModulesFromDifficultyLevel")
	public ResponseEntity<List<Modules>> getModulesFromDifficultyLevel(@RequestBody String request){
		List<Modules> modules =  null;
		
		try {

			JSONParser parser = new JSONParser();
			JSONObject json=(JSONObject) parser.parse(request);

			String difficultyLevel =  String.valueOf(json.get("difficultyLevel"));
			Long userId =  Long.parseLong(String.valueOf(json.get("userId")));

			modules = moduleService.getModulesFromDifficultyLevel( difficultyLevel.toUpperCase());
			
			if(null != modules) {
				
				for(Modules module:modules) {
					UserModule userModule= labService.getStatusofLab(module.getModuleId() , userId);
					if(null != userModule) {
						module.setLabStarted("true");
					}else {
						module.setLabStarted("false");
					}
				}
				
				
				
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		

		return new ResponseEntity<List<Modules>>(modules, HttpStatus.OK);
	}
	
	@PostMapping("/getModulesForUser")
	public ResponseEntity<List<UserModule>> getModulesForUser(@RequestBody String request){
		List<UserModule> userModules =  null;
		
		try {

			JSONParser parser = new JSONParser();
			JSONObject json=(JSONObject) parser.parse(request);

			Long userId =  Long.parseLong(String.valueOf(json.get("userId")));

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

	@PostMapping("/createModule")
	@ResponseBody
	public ResponseEntity<?> createModule(@RequestBody String request){

		int result = 0;
		Long mod_id = Long.parseLong("-1");
		try{
			
			JSONParser parser = new JSONParser();
			JSONObject module_json=(JSONObject) parser.parse(request);
			
			
			JSONObject lab_json = new JSONObject();
			lab_json.put("labNames", module_json.get("labNames"));
			lab_json.put("noOfLabs",Integer.parseInt( module_json.get("noOfLabs").toString()) );
			
			
			String evaluationData = module_json.get("evaluation").toString();

			module_json.remove("labNames");
			module_json.remove("noOfLabs");
			module_json.remove("evaluation");

			

			Long module_id = moduleService.createNewModule(module_json.toJSONString());
			if(module_id >0){
				lab_json.put("module_id", module_id);
				result = labService.createLabsForModule(lab_json.toJSONString());
				Long res = evaluationService.addEvaluationQuestions(evaluationData, module_id);
				mod_id = module_id;
			}else{
				result = -1;
			}

			
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		return new ResponseEntity<Long>(mod_id, HttpStatus.OK);
	}

	@PostMapping("/deleteModule")
	@ResponseBody
	public ResponseEntity<?> deleteModule(@RequestBody String request){
		String response = "null";
		
		JSONParser parser = new JSONParser();
		try {
			JSONObject module_json=(JSONObject) parser.parse(request);
			String moduleIdString = module_json.get("moduleId").toString();
			Long moduleId = Long.parseLong(moduleIdString);
			System.out.println("Module Id = "+moduleId);

			// evaluation service
			evaluationService.deleteEvaluations(moduleId);
			// lab service
			labService.deleteLabSteps(moduleId);
			// module service
			moduleService.deleteModule(moduleId);

			// File delete
			String dirName = module_json.get("moduleTitle").toString().replaceAll("\\s+", "_").toLowerCase();
			uploadLib.deleteUploadedFiles(dirName);

			
			

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

	@PostMapping(value = "/getUserModuleCount", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<List<JSONObject>> getUserModuleCount(){
		List<ModuleStats> moduleStats = null;
		List<JSONObject> response = new ArrayList<JSONObject>();
		JSONObject json ;
		//JSONParser parser = new JSONParser();
		
		try {
			//JSONObject _json=(JSONObject) parser.parse(request);
			//String moduleId = _json.get("moduleId").toString();
			moduleStats =  moduleService.getUserModuleCount();
			for(ModuleStats m : moduleStats){
				json = new JSONObject();
				json.put("count", m.getCount());
				json.put("moduleId", m.getModuleId());
				json.put("moduleTitle", m.getModuleTitle());
				response.add(json);
			}
			
			//System.out.println("\n\n\nhey\n\n\n");
			//response = Long.parseLong(count+"");
		} catch (Exception e) {
			//TODO: handle exception
			System.out.println(e.getMessage());
		}
		return new ResponseEntity<List<JSONObject>>(response, HttpStatus.OK);
	}

	@PostMapping(value = "/getUserModuleDataMonthWise", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<JSONObject> getUserModuleDataMonthWise(){
		List<ModuleGraphStats> moduleGraphStats = null;
		List<JSONObject> response = new ArrayList<JSONObject>();
		JSONObject json = new JSONObject();;
		//JSONParser parser = new JSONParser();
		
		try {
			//JSONObject _json=(JSONObject) parser.parse(request);
			//String moduleId = _json.get("moduleId").toString();
			moduleGraphStats =  moduleService.getUserModuleDataMonthWise();
			for(ModuleGraphStats m : moduleGraphStats){
				
				// json.put("count", m.getCount());
				// json.put("moduleId", m.getModuleId());
				// json.put("moduleTitle", m.getModuleTitle());
				//System.out.println(m.toString());
				int month = m.getMonth();
				int year = m.getYear();
				Long moduleId = m.getModuleId();
				Long count = m.getCount();
				if(json.get(moduleId) !=null){
					JSONArray jsonArray = (JSONArray) json.get(moduleId);
					JSONObject item = new JSONObject();
					item.put("Month", month);
					item.put("Year", year);
					item.put("Count", count);
					jsonArray.add(item);
					
				}
				else{
					JSONArray jsonArray = new JSONArray();
					JSONObject item = new JSONObject();
					item.put("Month", month);
					item.put("Year", year);
					item.put("Count", count);
					jsonArray.add(item);
					json.put(moduleId, jsonArray);

				}
				//System.out.println(json.toJSONString());
				//response.add(json);
			}
			
			//response.add(json);
			//System.out.println(response.toString());
			//System.out.println("\n\n\nhey\n\n\n");
			//response = Long.parseLong(count+"");
		} catch (Exception e) {
			//TODO: handle exception
			System.out.println(e.getMessage());
		}
		return new ResponseEntity<JSONObject>(json, HttpStatus.OK);
	}
	
	
	@PostMapping(value = "/getUserModuleStepData", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<List<JSONObject>> getUserModuleCount(@RequestBody String request){

		List<JSONObject> response = new ArrayList<JSONObject>();
		JSONObject json = new JSONObject();

		//System.out.println("\n\n\nhey\n\n\n");
		JSONParser parser = new JSONParser();
		try {
			JSONObject module_json=(JSONObject) parser.parse(request);
			String moduleIdString = module_json.get("moduleId").toString();
			Long moduleId = Long.parseLong(moduleIdString);
			List<UserModule> userModules = moduleService.getUserModulesFromId(moduleId);
			//System.out.println(userModules.size());
			List<JSONObject> stepCompleted = new ArrayList<JSONObject>();
			for (UserModule userModule : userModules) {
				//json = new JSONObject();
				String stepNo = userModule.getStepsCompleted();
				String completed = userModule.getLabCompleted();
				if(completed == null){
					//System.out.print(stepNo+"\t");
					if(json.get("Step-"+stepNo) != null){
						String value = json.get("Step-"+stepNo).toString();
						int val = Integer.parseInt(value) + 1;
						json.put("Step-"+stepNo, val);
					}else{
						json.put("Step-"+stepNo, 1);
					}
					json.put("totalSteps",Integer.parseInt(userModule.getTotalSteps()));
					//stepCompleted.add(json);
				}
				else{
					if(json.get("Completed") != null){
						String value = json.get("Completed").toString();
						int val = Integer.parseInt(value) + 1;
						json.put("Completed", val );
					}else{
						json.put("Completed", 1);
					}
				}
				
			}
			//System.out.println(stepCompleted.toString());
			System.out.println(json.toJSONString());
			response.add(json);
		} catch (Exception e) {
			//TODO: handle exception
		}


		return new ResponseEntity<List<JSONObject>>(response, HttpStatus.OK);
	}

}