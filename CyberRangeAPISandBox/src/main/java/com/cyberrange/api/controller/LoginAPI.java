package com.cyberrange.api.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.cyberrange.api.model.Requests;
import com.cyberrange.api.model.SendEmail;
import com.cyberrange.api.model.User;
import com.cyberrange.api.service.LoginService;
import com.cyberrange.api.service.RequestService;
import com.cyberrange.api.utility.JSONtoObject;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/login")
public class LoginAPI {

	@Autowired
	LoginService loginService;

	@Autowired
	RequestService requestService;
	
	 
	@PostMapping("/getUserDetails")
	public ResponseEntity<User> getUserDetails(@RequestBody String request){
		 
		User user = JSONtoObject.jsonToObject(request, User.class);

		user.setRole("STUDENT");

		user.setApprovalStatus("N");

		User user_from = loginService.getUserDetails(user.getUserName().toLowerCase(), user.getGithubId());
		
		if(null == user_from) {
			user_from = loginService.insertUserDetails(user);
		}
		
		return new ResponseEntity<User>(user_from, HttpStatus.OK);
	}
	
	@PostMapping("/updateEmail")
	@ResponseBody
	public ResponseEntity<User> updateEmail(@RequestBody String request){
		String emailAddress = "";
		JSONParser parser = new JSONParser();
		try {
			JSONObject json=(JSONObject) parser.parse(request);
			emailAddress = json.get("customEmail").toString();
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		User user = JSONtoObject.jsonToObject(request, User.class);

		User user_from = loginService.getUserDetails(user.getUserName().toLowerCase(), user.getGithubId());
		
		user_from.setCustomEmail(emailAddress);
		
		loginService.updateEmail(user_from.getUserName(), user_from.getGithubId(), user_from.getCustomEmail());
		
		return new ResponseEntity<User>(user_from, HttpStatus.OK);
	}
	
	@PostMapping("/toggleRole")
	@ResponseBody
	public ResponseEntity<?> toggleRole(@RequestBody String request){
		String response = null;
		JSONParser parser = new JSONParser();
		try {
			JSONObject json = (JSONObject) parser.parse(request);
			System.out.println(json);
			String userName = json.get("studentName").toString();
			String githubId = json.get("githubId").toString();
			String requestId = json.get("requestId").toString();
			int i = loginService.toggleRole(userName,githubId);
			int j = requestService.toggleRole(requestId);
			//requestRepository.findOne(id);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}
}
