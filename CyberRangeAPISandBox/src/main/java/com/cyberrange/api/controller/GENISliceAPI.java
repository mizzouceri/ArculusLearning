package com.cyberrange.api.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyberrange.api.model.GeniOutput;
import com.cyberrange.api.model.ResourceStatus;
import com.cyberrange.api.model.UserModule;
import com.cyberrange.api.remote.RemoteConnection;
import com.cyberrange.api.service.GENISliceService;
import com.cyberrange.api.service.LabService;
import com.cyberrange.api.utility.JSONtoObject;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/slice")
public class GENISliceAPI {
	
	@Autowired
	RemoteConnection remoteConnection;
		
	@Autowired 
	LabService labService;
	
	@Autowired 
	GENISliceService geniSliceService;
	
	@PostMapping("/createSlice")
	public ResponseEntity<UserModule> createSlice(@RequestBody UserModule userModule) throws Exception{
		 
		try {
			
			String geniSliceStatus = "SUCCESS";
		
			String sliceName  = "";
			String output = "";
			
			if(null == userModule.getGeniSliceStatus()){
				sliceName = userModule.getGeniUsername().toUpperCase() + "-MCR-LAB-" + userModule.getModuleId();
				String command = "sudo /data/scripts/omni_create_slice.sh " + userModule.getGeniUsername()+ " " + sliceName ;
				output = remoteConnection.remoteConnection(command);
			}else {
				sliceName = userModule.getSliceCreated();
				String command = "sudo /data/scripts/omni_add_slice_member.sh " + userModule.getGeniUsername()+ " " + sliceName ;
				output = remoteConnection.remoteConnection(command);
			}
			
			if(output.indexOf("No such member") > -1 || output.indexOf("Invalid user name") > -1) {
				GeniOutput geniOutput = new GeniOutput();
				geniOutput.setOutput(output);
				geniOutput.setUserId(userModule.getUserId());
				geniOutput.setModuleId(userModule.getModuleId());
				geniOutput.setSliceName(sliceName);
				geniOutput.setShName("omni_create_slice");
				geniOutput.setStatus("FAIL");
				geniOutput =geniSliceService.saveOutputData(geniOutput);
				geniSliceStatus = "NO_MEMBER";
			}else if(output.indexOf("not in project") > -1) {
				GeniOutput geniOutput = new GeniOutput();
				geniOutput.setOutput(output);
				geniOutput.setUserId(userModule.getUserId());
				geniOutput.setModuleId(userModule.getModuleId());
				geniOutput.setSliceName(sliceName);
				geniOutput.setShName("omni_create_slice");
				geniOutput.setStatus("FAIL");
				geniOutput =geniSliceService.saveOutputData(geniOutput);
				geniSliceStatus = "NO_IN_PROJECT";

			}
			
			userModule.setSliceCreated(sliceName);
			userModule.setGeniSliceStatus(geniSliceStatus);

			labService.updateLabSliceName(userModule);

		} catch (IOException e) {

			e.printStackTrace();
		}
		
		return new ResponseEntity<UserModule>(userModule, HttpStatus.OK);


	}
	
	@PostMapping("/reserveResources")
	public ResponseEntity<List<ResourceStatus>> reserveResources(@RequestBody UserModule userModule){
		List<ResourceStatus> resourceStatuss = null;
		try {
			
			
	        JSONArray reserveNodes = null;
	        HashMap<String, ResourceStatus> status = new HashMap<String, ResourceStatus>();
	        
	        boolean isController = false;
	        boolean isOther = false;
	        
	        if(!"".equals(userModule.getResourcesReserved()) && null != userModule.getResourcesReserved()) {
	        	
	        	JSONParser parser = new JSONParser();
	        	reserveNodes = (JSONArray) parser.parse(userModule.getResourcesReserved());
	        	for(int i=0 ; i < reserveNodes.size() ; i++) {
	        		JSONObject reserveNode = (JSONObject) reserveNodes.get(i);
	        		ResourceStatus resourceStatus = JSONtoObject.jsonToObject(reserveNode.toString(), ResourceStatus.class);
	        		status.put(resourceStatus.getName(), resourceStatus);
	        	}
	        	
	        	isController=status.get("controller").isStatus();
	        	isOther = status.get("other").isStatus();
	        }
	        
	        resourceStatuss= new ArrayList<ResourceStatus>();
	        
	        String[] aggregateArray = {"nyu-ig","wisconsin-ig","umkc-ig","missouri-ig","kansas-ig","illinois-ig","cornell-ig","colorado-ig","clemson-ig","uchicago-ig"};

	        int lenght = aggregateArray.length;
	        Random rand = new Random(); 

	        String aggregateController = "";
	        String aggregateNode  = "";
	        
	        if(isController) {
	        	aggregateController  = status.get("controller").getAgg();
	        }else {
	        	int randNumber = rand.nextInt(lenght); 
	        	aggregateController  = aggregateArray[randNumber];
	        }
	        
	        if(isOther) {
	        	aggregateNode  = status.get("other").getAgg();
	        }else {
	        	int randNumber = rand.nextInt(lenght); 
	        	aggregateNode = aggregateArray[randNumber];
	        }
	        
	        while(aggregateController.equalsIgnoreCase(aggregateNode)) {
	        	 if(!isController) { 
	             	int randNumber = rand.nextInt(lenght); 
	 	        	aggregateController  = aggregateArray[randNumber];
	        	 }
	        	 if(!isOther) {
	             	int randNumber = rand.nextInt(lenght); 
	        		 aggregateNode = aggregateArray[randNumber];
	        	 }
	        	 
	        }
	        
	        if(!isController) {
	        
		        String command = "sudo /data/scripts/omni_create_sliver_controller.sh " +userModule.getSliceCreated() + "  " + userModule.getModuleId()
				+ " " + aggregateController;
		
				String output = remoteConnection.remoteConnection(command);
				GeniOutput geniOutput = new GeniOutput();
				geniOutput.setOutput(output);
				geniOutput.setUserId(userModule.getUserId());
				geniOutput.setModuleId(userModule.getModuleId());
				geniOutput.setSliceName(userModule.getSliceCreated());
				geniOutput.setShName("omni_create_sliver_controller");
				
				ResourceStatus resourceStatus = new ResourceStatus();
				resourceStatus.setName("controller");
				if(output.indexOf("ERROR") > -1 || output.indexOf("Result Summary: Failed") > -1) {
					geniOutput.setStatus("FAIL");
					resourceStatus.setStatus( false);
				}else {
					geniOutput.setStatus("SUCCESS");
					resourceStatus.setStatus( true);
					resourceStatus.setAgg(aggregateController);
				}
				geniOutput =geniSliceService.saveOutputData(geniOutput);
				
				resourceStatus.setGeniOpId(String.valueOf(geniOutput.getId()));
				resourceStatuss.add(resourceStatus);

				
				
	        }else {
				ResourceStatus resourceStatus = status.get("controller");
				resourceStatuss.add(resourceStatus);

	        }
	        
	        if(!isOther) {
	        	  String command = "sudo /data/scripts/omni_create_sliver.sh " +userModule.getSliceCreated() + "  " + userModule.getModuleId()
	  			+ " " + aggregateNode;
	  			
	  				String output = remoteConnection.remoteConnection(command);
	  				GeniOutput geniOutput = new GeniOutput();
	  				geniOutput.setOutput(output);
	  				geniOutput.setUserId(userModule.getUserId());
	  				geniOutput.setModuleId(userModule.getModuleId());
	  				geniOutput.setSliceName(userModule.getSliceCreated());
	  				geniOutput.setShName("omni_create_sliver");
	  				
	  				ResourceStatus resourceStatus = new ResourceStatus();
					resourceStatus.setName("other");
					if(output.indexOf("ERROR") > -1 || output.indexOf("Result Summary: Failed") > -1) {
						geniOutput.setStatus("FAIL");
						resourceStatus.setStatus( false);
					}else {
						geniOutput.setStatus("SUCCESS");
						resourceStatus.setStatus( true);
						resourceStatus.setAgg(aggregateNode);
					}
					
	  				geniOutput =geniSliceService.saveOutputData(geniOutput);
					resourceStatus.setGeniOpId(String.valueOf(geniOutput.getId()));
					resourceStatuss.add(resourceStatus);
	  				
	        	
	        }else {
				ResourceStatus resourceStatus = status.get("other");
			
				resourceStatuss.add(resourceStatus);
	        }
	        
	        
	        String resourceFinalStatus = JSONtoObject.ObjecttoJson(resourceStatuss);
	        userModule.setResourcesReserved(resourceFinalStatus);
	        userModule.setResourcesReservedDt(new Date());
	        
			labService.updateLabResoucesStatus(userModule);

			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return new ResponseEntity<List<ResourceStatus>>(resourceStatuss, HttpStatus.OK);


	}
	
	@PostMapping("/reserveResourcesLab1")
	public ResponseEntity<List<ResourceStatus>> reserveResourcesLab1(@RequestBody UserModule userModule){
		List<ResourceStatus> resourceStatuss = null;
		try {
			
	        JSONArray reserveNodes = null;
	        HashMap<String, ResourceStatus> status = new HashMap<String, ResourceStatus>();
	        
	        if(!"".equals(userModule.getResourcesReserved()) && null != userModule.getResourcesReserved()) {
	        	
	        	JSONParser parser = new JSONParser();
	        	reserveNodes = (JSONArray) parser.parse(userModule.getResourcesReserved());
	        	for(int i=0 ; i < reserveNodes.size() ; i++) {
	        		JSONObject reserveNode = (JSONObject) reserveNodes.get(i);
	        		ResourceStatus resourceStatus = JSONtoObject.jsonToObject(reserveNode.toString(), ResourceStatus.class);
	        		status.put(resourceStatus.getName(), resourceStatus);
	        	}
	        }
	        
	        resourceStatuss= new ArrayList<ResourceStatus>();
	        
	        String[] aggregateArray = {"illinois-ig","colorado-ig","nyu-ig","wisconsin-ig","umkc-ig","missouri-ig","kansas-ig","cornell-ig","clemson-ig","uchicago-ig"};

	        int lenght = aggregateArray.length;
	        Random rand = new Random(); 

	        String aggregateNodes = "";
	        int randNumber = rand.nextInt(lenght); 
	        aggregateNodes  = aggregateArray[randNumber];

	        
		        String command = "sudo /data/scripts/omni_create_sliver.sh " +userModule.getSliceCreated() + "  " + userModule.getModuleId() + " " + aggregateNodes;
		
				String output = remoteConnection.remoteConnection(command);
				GeniOutput geniOutput = new GeniOutput();
				geniOutput.setOutput(output);
				geniOutput.setUserId(userModule.getUserId());
				geniOutput.setModuleId(userModule.getModuleId());
				geniOutput.setSliceName(userModule.getSliceCreated());
				geniOutput.setShName("omni_create_sliver");
				
				ResourceStatus resourceStatus = new ResourceStatus();
				resourceStatus.setName("nodes");
				if(output.indexOf("ERROR") > -1 || output.indexOf("Result Summary: Failed") > -1) {
					geniOutput.setStatus("FAIL");
					resourceStatus.setStatus( false);
				}else {
					geniOutput.setStatus("SUCCESS");
					resourceStatus.setStatus( true);
					resourceStatus.setAgg(aggregateNodes);
				}
				geniOutput =geniSliceService.saveOutputData(geniOutput);
				
				resourceStatus.setGeniOpId(String.valueOf(geniOutput.getId()));
				resourceStatuss.add(resourceStatus);

	        
	        String resourceFinalStatus = JSONtoObject.ObjecttoJson(resourceStatuss);
	        userModule.setResourcesReserved(resourceFinalStatus);
	        userModule.setResourcesReservedDt(new Date());
	        
			labService.updateLabResoucesStatus(userModule);

			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return new ResponseEntity<List<ResourceStatus>>(resourceStatuss, HttpStatus.OK);


	}
	
	@PostMapping("/checkStatusSlice")
	public ResponseEntity<UserModule> checkStatusSlice(@RequestBody UserModule userModule){
		
		
		try {
			
	        JSONArray reserveNodes = null;
	        HashMap<String, ResourceStatus> status = new HashMap<String, ResourceStatus>();
	        
	    	JSONParser parser = new JSONParser();
        	reserveNodes = (JSONArray) parser.parse(userModule.getResourcesReserved());
        	for(int i=0 ; i < reserveNodes.size() ; i++) {
        		JSONObject reserveNode = (JSONObject) reserveNodes.get(i);
        		ResourceStatus resourceStatus = JSONtoObject.jsonToObject(reserveNode.toString(), ResourceStatus.class);
        		status.put(resourceStatus.getName(), resourceStatus);
        	}
        	ResourceStatus resourceStatusCont = status.get("controller");
        	String controllerAggName = resourceStatusCont.getAgg();
        	
        	ResourceStatus resourceStatusOthr = status.get("other");
        	String otherAggName = resourceStatusOthr.getAgg();
        	
	      
	        
	        String command = "sudo /data/scripts/omni_check_status.sh  " +userModule.getSliceCreated() + "  " + controllerAggName
			+ " " + otherAggName;
		
			String output = remoteConnection.remoteConnection(command);
			if(output.indexOf("overall SliverStatus: failed") > -1) {
				
				GeniOutput geniOutput = new GeniOutput();
				geniOutput.setOutput(output);
				geniOutput.setUserId(userModule.getUserId());
				geniOutput.setModuleId(userModule.getModuleId());
				geniOutput.setSliceName(userModule.getSliceCreated());
				geniOutput.setShName("omni_check_status");
				
				geniOutput.setStatus("FAIL");
				
				geniOutput =geniSliceService.saveOutputData(geniOutput);
				userModule.setResourceStatus("FAIL");
			}
			else if(output.indexOf("overall SliverStatus: unknown") > -1 || output.indexOf("No slice or aggregate here") > -1) {
				GeniOutput geniOutput = new GeniOutput();
				geniOutput.setOutput(output);
				geniOutput.setUserId(userModule.getUserId());
				geniOutput.setModuleId(userModule.getModuleId());
				geniOutput.setSliceName(userModule.getSliceCreated());
				geniOutput.setShName("omni_check_status");
				
				geniOutput.setStatus("UNKNOWN");
				
				geniOutput =geniSliceService.saveOutputData(geniOutput);
				userModule.setResourceStatus("UNKNOWN");
			}else {
	        	userModule.setResourceStatus("SUCCESS");	

			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return new ResponseEntity<UserModule>(userModule, HttpStatus.OK);


	}
	
	
	@PostMapping("/checkStatusSliceForLab1")
	public ResponseEntity<UserModule> checkStatusSliceForLab1(@RequestBody UserModule userModule){
		
		
		try {
			
	        JSONArray reserveNodes = null;
	        HashMap<String, ResourceStatus> status = new HashMap<String, ResourceStatus>();
	        
	    	JSONParser parser = new JSONParser();
        	reserveNodes = (JSONArray) parser.parse(userModule.getResourcesReserved());
        	for(int i=0 ; i < reserveNodes.size() ; i++) {
        		JSONObject reserveNode = (JSONObject) reserveNodes.get(i);
        		ResourceStatus resourceStatus = JSONtoObject.jsonToObject(reserveNode.toString(), ResourceStatus.class);
        		status.put(resourceStatus.getName(), resourceStatus);
        	}
        	ResourceStatus resourceStatusCont = status.get("nodes");
        	String nodesAggName = resourceStatusCont.getAgg();
        	
	        String command = "sudo /data/scripts/omni_check_status_Lab1.sh  " +userModule.getSliceCreated() + "  " + nodesAggName;
		
			String output = remoteConnection.remoteConnection(command);
			if(output.indexOf("overall SliverStatus: failed") > -1) {
				
				GeniOutput geniOutput = new GeniOutput();
				geniOutput.setOutput(output);
				geniOutput.setUserId(userModule.getUserId());
				geniOutput.setModuleId(userModule.getModuleId());
				geniOutput.setSliceName(userModule.getSliceCreated());
				geniOutput.setShName("omni_check_status");
				
				geniOutput.setStatus("FAIL");
				
				geniOutput =geniSliceService.saveOutputData(geniOutput);
				userModule.setResourceStatus("FAIL");
			}
			else if(output.indexOf("overall SliverStatus: unknown") > -1 || output.indexOf("No slice or aggregate here") > -1) {
				GeniOutput geniOutput = new GeniOutput();
				geniOutput.setOutput(output);
				geniOutput.setUserId(userModule.getUserId());
				geniOutput.setModuleId(userModule.getModuleId());
				geniOutput.setSliceName(userModule.getSliceCreated());
				geniOutput.setShName("omni_check_status");
				
				geniOutput.setStatus("UNKNOWN");
				
				geniOutput =geniSliceService.saveOutputData(geniOutput);
				userModule.setResourceStatus("UNKNOWN");
			}else {
	        	userModule.setResourceStatus("SUCCESS");	

			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return new ResponseEntity<UserModule>(userModule, HttpStatus.OK);


	}
	
	
	
	@PostMapping("/deleteResoucesFromSlice")
	public ResponseEntity<UserModule> deleteResoucesFromSlice(@RequestBody UserModule userModule){
		
		
		try {
			
	        JSONArray reserveNodes = null;
	        HashMap<String, ResourceStatus> status = new HashMap<String, ResourceStatus>();
	        
	    	JSONParser parser = new JSONParser();
        	reserveNodes = (JSONArray) parser.parse(userModule.getResourcesReserved());
        	for(int i=0 ; i < reserveNodes.size() ; i++) {
        		JSONObject reserveNode = (JSONObject) reserveNodes.get(i);
        		ResourceStatus resourceStatus = JSONtoObject.jsonToObject(reserveNode.toString(), ResourceStatus.class);
        		status.put(resourceStatus.getName(), resourceStatus);
        	}
        	
        	ResourceStatus resourceStatusCont = status.get("controller");
        	String controllerAggName = resourceStatusCont.getAgg();
        	
        	ResourceStatus resourceStatusOthr = status.get("other");
        	String otherAggName = resourceStatusOthr.getAgg();
        	
        	userModule.setResourceStatus("SUCCESS");	
	      
	        
	        String command = "sudo /data/scripts/omni_delete_resources_slice.sh  " +userModule.getSliceCreated() + "  " + controllerAggName
			+ " " + otherAggName;
		
			String output = remoteConnection.remoteConnection(command);
			if(output.indexOf(" Result Summary: Failed") > -1) {
				
				GeniOutput geniOutput = new GeniOutput();
				geniOutput.setOutput(output);
				geniOutput.setUserId(userModule.getUserId());
				geniOutput.setModuleId(userModule.getModuleId());
				geniOutput.setSliceName(userModule.getSliceCreated());
				geniOutput.setShName("omni_delete_resources_slice");
				
				geniOutput.setStatus("FAIL");
				
				geniOutput =geniSliceService.saveOutputData(geniOutput);
				userModule.setDeleteStatus("FAIL");
			}else {
			
				GeniOutput geniOutput = new GeniOutput();
				geniOutput.setOutput(output);
				geniOutput.setUserId(userModule.getUserId());
				geniOutput.setModuleId(userModule.getModuleId());
				geniOutput.setSliceName(userModule.getSliceCreated());
				geniOutput.setShName("omni_delete_resources_slice");
				
				geniOutput.setStatus("SUCCESS");
				
				geniOutput =geniSliceService.saveOutputData(geniOutput);
				
				userModule.setResourcesReserved(null);
				userModule.setResourcesReservedDt(null);
				labService.updateLabResoucesStatus(userModule);
				userModule.setDeleteStatus("SUCCESS");
			}

			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return new ResponseEntity<UserModule>(userModule, HttpStatus.OK);


	}
	

	
	@PostMapping("/deleteResoucesFromSliceForLab1")
	public ResponseEntity<UserModule> deleteResoucesFromSliceForLab1(@RequestBody UserModule userModule){
		
		try {
	        JSONArray reserveNodes = null;
	        HashMap<String, ResourceStatus> status = new HashMap<String, ResourceStatus>();
	        
	    	JSONParser parser = new JSONParser();
        	reserveNodes = (JSONArray) parser.parse(userModule.getResourcesReserved());
        	for(int i=0 ; i < reserveNodes.size() ; i++) {
        		JSONObject reserveNode = (JSONObject) reserveNodes.get(i);
        		ResourceStatus resourceStatus = JSONtoObject.jsonToObject(reserveNode.toString(), ResourceStatus.class);
        		status.put(resourceStatus.getName(), resourceStatus);
        	}
        	
        	ResourceStatus resourceStatusCont = status.get("nodes");
        	String nodeAggName = resourceStatusCont.getAgg();
        	userModule.setResourceStatus("SUCCESS");	
	        
	        String command = "sudo /data/scripts/omni_delete_resources_slice_Lab1.sh  " +userModule.getSliceCreated() + "  " + nodeAggName;
		
			String output = remoteConnection.remoteConnection(command);
			if(output.indexOf(" Result Summary: Failed") > -1) {
				
				GeniOutput geniOutput = new GeniOutput();
				geniOutput.setOutput(output);
				geniOutput.setUserId(userModule.getUserId());
				geniOutput.setModuleId(userModule.getModuleId());
				geniOutput.setSliceName(userModule.getSliceCreated());
				geniOutput.setShName("omni_delete_resources_slice");
				
				geniOutput.setStatus("FAIL");
				
				geniOutput =geniSliceService.saveOutputData(geniOutput);
				userModule.setDeleteStatus("FAIL");
			}else {
			
				GeniOutput geniOutput = new GeniOutput();
				geniOutput.setOutput(output);
				geniOutput.setUserId(userModule.getUserId());
				geniOutput.setModuleId(userModule.getModuleId());
				geniOutput.setSliceName(userModule.getSliceCreated());
				geniOutput.setShName("omni_delete_resources_slice");
				
				geniOutput.setStatus("SUCCESS");
				
				geniOutput =geniSliceService.saveOutputData(geniOutput);
				
				userModule.setResourcesReserved(null);
				userModule.setResourcesReservedDt(null);
				labService.updateLabResoucesStatus(userModule);
				userModule.setDeleteStatus("SUCCESS");
			}

			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return new ResponseEntity<UserModule>(userModule, HttpStatus.OK);


	}

}
