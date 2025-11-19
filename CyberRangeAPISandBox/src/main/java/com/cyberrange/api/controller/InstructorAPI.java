package com.cyberrange.api.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyberrange.api.service.LabService;
import com.cyberrange.api.service.LoginService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/instructor")
public class InstructorAPI {
	
	@Autowired 
	LabService labService;
	

	@Autowired 
	LoginService loginService;
	
	@PostMapping("/getEnrollMentData")
	public ResponseEntity<JSONObject> createSlice(){
		 
		JSONObject object =new JSONObject();
		try {

			Long noStudents = loginService.getNumberodStudents();	
			object.put("noStudents", noStudents);
			
			Long noStudentsLab2 = labService.getCountOfEntrollement(2L);	
			object.put("noStudentsLab2", noStudentsLab2);
			
			Long noStudentsLab3 = labService.getCountOfEntrollement(3L);	
			object.put("noStudentsLab3", noStudentsLab3);
			
			Long noStudentsLab4 = labService.getCountOfEntrollement(4L);	
			object.put("noStudentsLab4", noStudentsLab4);
			
			Long noEnrollment = labService.getTotalCountOfEntrollement(4L);	
			object.put("noEnrollment", noEnrollment);
			
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return new ResponseEntity<JSONObject>(object, HttpStatus.OK);


	}
	
	@PostMapping("/getBeforeAfterLevelInfo")
	public ResponseEntity<JSONObject> getBeforeAfterLevelInfo(){
		 
		JSONObject object =new JSONObject();
		try {

			
			
			Long noStudentsLab2 = labService.getCountOfEntrollement(2L);	
			object.put("noStudentsLab2", noStudentsLab2);
			
			
			
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return new ResponseEntity<JSONObject>(object, HttpStatus.OK);


	}
	
}
