package com.cyberrange.api.controller;

import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyberrange.api.service.RequestService;
import com.cyberrange.api.service.OverviewService;
//import com.cyberrange.api.utility.JSONtoObject;

import com.cyberrange.api.model.Requests;
import com.cyberrange.api.model.User;
import com.cyberrange.api.model.UserModule;
import com.cyberrange.api.model.EvaluationUser;
import com.cyberrange.api.bean.StudentUserCourseDetails;
import com.cyberrange.api.repository.LoginRepository;
import com.cyberrange.api.repository.RequestRepository;
import com.cyberrange.api.repository.UserModuleRepository;
import com.cyberrange.api.repository.EvaluationUserRepository;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/overview")
public class OverviewAPI {

	@Autowired
	OverviewService overviewService;
	
	@Autowired
	UserModuleRepository userModuleRepository;
	
	@Autowired
	EvaluationUserRepository evaluationUserRepository;
	
	@PostMapping("/getAllStudentUsers")
	public List<User> getAllStudentUsers(){
		//System.out.println("Overview API : in getallstudentusers function");
		List<User> studentUsers =  overviewService.getAllStudentUsers();
		return studentUsers;
	}
	
	@PostMapping("/getStudentsCount")
	public int getStudentsCount(){
		return overviewService.getStudentsCount();
	}
	
	/*@PostMapping("/getStudeUserCourseDetails")
	public List<StudentUserCourseDetails> getStudeUserCourseDetails(@RequestBody String request){
		List<StudentUserCourseDetails> result = overviewService.getStudeUserCourseDetails(request);
		return result;
	}*/
	
	@PostMapping("/getUserModuleDetails")
	public List<UserModule> getUserModuleDetails(@RequestBody String request){
		Long userId = Long.parseLong(request);
		List<UserModule> result = userModuleRepository.getModulesForUserInOrder(userId);
		return result;
	}
	@PostMapping("/getEvaluatioinUserDetails")
	public List<EvaluationUser> getEvaluatioinUserDetails(@RequestBody String request){
		Long userId = Long.parseLong(request);
		List<EvaluationUser> result = evaluationUserRepository.find(userId, "TECH_ASMT");
		return result;
	}

}
