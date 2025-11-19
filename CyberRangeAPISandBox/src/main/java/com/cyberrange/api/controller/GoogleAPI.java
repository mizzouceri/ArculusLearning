package com.cyberrange.api.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyberrange.api.google.GoogleLib;
import com.cyberrange.api.model.GoogleVMDetails;
import com.cyberrange.api.model.UserModule;
import com.cyberrange.api.service.LabService;
import com.cyberrange.api.utility.JSONtoObject;
import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.cyberrange.api.repository.UserModuleRepository;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/goolgeApi")
@PropertySource("classpath:googleCloudProp.properties")
public class GoogleAPI {
	
	@Value("${projectId}")
    private String projectId;
	
	@Autowired 
	LabService labService;
	
	
	@Autowired
	GoogleLib googleLib;
	
	@Autowired
	UserModuleRepository userModuleRepository;

	@PostMapping("/getStorageDetails")
	public ResponseEntity<List<String>> getStorageDetails(@RequestBody String request) throws IOException{
		
		GoogleCredentials credentials = googleLib.authExplicit();
		List<String> fileNames = new ArrayList<String>();
		Storage storage = StorageOptions.newBuilder().setCredentials(credentials)
			  	.setProjectId("mizzoucyberrange").build().getService();
	    Bucket bucket = storage.get("mizzou-cyber-range-script");
	    Page<Blob> blobs = bucket.list();

	    for (Blob blob : blobs.iterateAll()) {
	    	fileNames.add(blob.getName());
	    }
		 

		return new ResponseEntity<List<String>>(fileNames, HttpStatus.OK);


	}
	
	
	
@PostMapping("/createInstanceForLab1")
	public ResponseEntity<UserModule> createInstanceForLab1(@RequestBody UserModule userModule) throws Exception{
		//System.out.println("Google API : Inside Create Instance Func");
		
		String githubId =  userModule.getGeniUsername();
		String zoneName = "us-central1-c";
		String responseInstance = "";
		String responseDetails = "";
		ArrayList<GoogleVMDetails> response = new ArrayList<GoogleVMDetails>();
		String instance = "dolus-lab1-mtd";
		String timestamp = System.currentTimeMillis()+"";
		String instanceName =  githubId+"-"+instance+"-"+timestamp;
		System.out.println("Google API : Create Instance Func : Inside Loop : "+ instanceName);
		//System.out.println("Creating instance with name : "+ instanceName);
		responseInstance = googleLib.createInstance(zoneName, instanceName, instance);
		Thread.sleep(5000);
		responseDetails = googleLib.getInstanceDetails(zoneName, instanceName);
		GoogleVMDetails details =JSONtoObject.jsonToGoogleVMDetails(responseInstance,responseDetails);
		response.add(details);
		
		userModule.setSliceCreated("GCP");
		userModule.setSliceCreateddt(new Date());
		userModule.setResourcesReservedDt(new Date());
		String resourcesReserved = JSONtoObject.ObjecttoJson(response);
		userModule.setResourcesReserved(resourcesReserved);
		
		labService.updateGoogleVMInfo(userModule);
		
		return new ResponseEntity<UserModule>(userModule, HttpStatus.OK);
	
	}


	@PostMapping("/deleteCompletedResourcesForLab")
	public int deleteCompletedResourcesForLab(@RequestBody String request) {
		String zoneName = "us-central1-c";
		String instanceName = "";
		try {
		
			JSONParser parser = new JSONParser();
			JSONObject json=(JSONObject) parser.parse(request);
			System.out.println(json.get("moduleId") + " "+ json.get("userId"));
			Long moduleId = Long.parseLong(json.get("moduleId")+"");
			Long userId = Long.parseLong(json.get("userId")+"");
			
			UserModule userMod = userModuleRepository.find(moduleId, userId);
			String githubId =  userMod.getGeniUsername();
			System.out.println(userMod.getResourcesReserved());
			
			//Delete VM instances in GCP
			JSONArray resource_json=(JSONArray) parser.parse(userMod.getResourcesReserved());
			for(int i = 0 ; i < resource_json.size(); i++) {
				JSONObject myjson=(JSONObject) parser.parse(resource_json.get(i)+"");
				
				instanceName = myjson.get("name")+"";
				
				String response = googleLib.deleteInstance(zoneName, instanceName, "machineImage");
				//System.out.println(response);
				System.out.println("Instance : "+myjson.get("name") + " Deleted.");
			}
			
			//Deleting Slices from Database
			userModuleRepository.updateLabResoucesStatus(moduleId, userId, null, null);
			userModuleRepository.updateSlice(null, moduleId, userId, null, null, null);
			//userModuleRepository.updateStatusofLab(moduleId, userId, "0");
			//int i = userModuleRepository.updateLabResoucesStatus(moduleId, userId, null, null);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return 0;
	}
	
}
