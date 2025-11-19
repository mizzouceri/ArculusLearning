package com.cyberrange.api.controller;

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

import com.cyberrange.api.model.User;
import com.cyberrange.api.service.LoginService;
import com.cyberrange.api.service.SendMailService;
import com.cyberrange.api.utility.JSONtoObject;

import com.cyberrange.api.model.User;
import com.cyberrange.api.model.SendEmail;
import com.cyberrange.api.repository.SendEmailRepository;
import com.cyberrange.api.repository.LoginRepository;
import com.cyberrange.api.google.SendMailLib;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/mailer")
public class SendMailAPI {

	@Autowired
	SendMailService mailservice;
	
	@Autowired
	LoginRepository loginRepository;

	@Autowired
	SendEmailRepository sendEmailRepository;

	@Autowired
	SendMailLib sendMailLib;
	
	
	@PostMapping("/sendMailTo")
	public void sendMailTo(@RequestBody String request){
		 
		System.out.println("Now??");
		
		String response = mailservice.sendMailTo(request);
		
		System.out.println("Done API");
		//return new ResponseEntity<String>("Nothing", HttpStatus.OK);


	}
	
	@PostMapping("/grantAccessNotification")
	public void grantAccessNotification(@RequestBody String request) {
		String githubId = "";
		JSONParser parser = new JSONParser();
		try {
			JSONObject json=(JSONObject) parser.parse(request);
			githubId = json.get("githubId").toString();
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		System.out.println("Github Id "+ githubId);
		User user = loginRepository.findByGithubId(githubId);
		mailservice.sendNotification("Approved", user.getUserName(), user.getCustomEmail());
	}
	
	@PostMapping("/denyAccessNotification")
	public void denyAccessNotification(@RequestBody String request) {
		String githubId = "";
		JSONParser parser = new JSONParser();
		try {
			JSONObject json=(JSONObject) parser.parse(request);
			githubId = json.get("githubId").toString();
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		System.out.println("Github Id "+ githubId);
		User user = loginRepository.findByGithubId(githubId);
		mailservice.sendNotification("Denied", user.getUserName(), user.getCustomEmail());
	}
	
	@GetMapping("/approveRequest/{id}")
	@ResponseBody
	public String approveRequest(@PathVariable(value="id") String githubId) {
		System.out.println("Github Id "+ githubId);
		String approval_status = "Y";
		int i = loginRepository.approve(githubId,approval_status);
		if(i != 0) {
			return "<center><h3>Student Approved</h3><br><br> <button onclick=\"window.close()\">Close</button> </center>";
		}
		return "Error Updating the status of approval.";
	}

	@PostMapping("/getToEmailAddress")
	@ResponseBody
	public ResponseEntity<SendEmail> getToEmailAddress(){

		String response = null;
		SendEmail sendEmailTo = sendEmailRepository.findEmailAddressByType("TO");
		response = sendEmailTo.getEmailAddress();
		return new ResponseEntity<SendEmail>(sendEmailTo, HttpStatus.OK);
	}

	@PostMapping("/setToEmailAddress")
	@ResponseBody
	public ResponseEntity<SendEmail> setToEmailAddress(@RequestBody String request){

		String response = null;
		String emailAddress = "";
		JSONParser parser = new JSONParser();
		try {
			JSONObject json=(JSONObject) parser.parse(request);
			emailAddress = json.get("emailAddress").toString();
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}

		SendEmail sendEmailTo = sendEmailRepository.findEmailAddressByType("TO");
		sendEmailTo.setEmailAddress(emailAddress);
		sendEmailRepository.save(sendEmailTo);
		return new ResponseEntity<SendEmail>(sendEmailTo, HttpStatus.OK);
	}

	@PostMapping("/getAdminMailAddress")
	@ResponseBody
	public ResponseEntity<SendEmail> getAdminMailAddress(){
		String adminMailAddress = "";
		try {
			 adminMailAddress = sendMailLib.getFromMailAddressFromPropertiesFile();
		} catch (Exception e) {
			//TODO: handle exception
			System.out.println(e.getMessage());
		}
		
		SendEmail sendEmailFrom = new SendEmail();
		sendEmailFrom.setId(Long.parseLong("999"));
		sendEmailFrom.setEmailAddress(adminMailAddress);
		sendEmailFrom.setVerificationCode("");
		sendEmailFrom.setVerified(1);
		
		return new ResponseEntity<SendEmail>(sendEmailFrom, HttpStatus.OK);
	}

}
