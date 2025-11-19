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
//import com.cyberrange.api.utility.JSONtoObject;

import com.cyberrange.api.model.Requests;
import com.cyberrange.api.model.SendEmail;
import com.cyberrange.api.repository.LoginRepository;
import com.cyberrange.api.repository.RequestRepository;
import com.cyberrange.api.repository.SendEmailRepository;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/requestapproval")
public class RequestAPI {

	@Autowired
	RequestService requestService;
	
	@Autowired
	LoginRepository loginRepository;
	
	@Autowired
	RequestRepository requestRepository;

	@Autowired
	SendEmailRepository sendEmailRepository;
	
	@PostMapping("/getAllRequests")
	public List<Requests> getAllRequests(){
		 
		System.out.println("Now??");
		
		List<Requests> response = requestService.getAllRequests();
		
		System.out.println("Done API");
		//return new ResponseEntity<String>("Nothing", HttpStatus.OK);
		return response;


	}
	
	@PostMapping("/grantAccess")
	public void grantAccess(@RequestBody String request) {
		System.out.println("Grant "+request);
		
		Requests r_ = requestService.grantAccessTo(request); 
		
	}
	
	@PostMapping("/denyAccess")
	public void denyAccess(@RequestBody String request) {
		System.out.println("Deny "+request);
		
		Requests r_ = requestService.denyAccessTo(request); 
	}
	
	@PostMapping("/grantAll")
	public void grantAll() {
		
		requestRepository.setAccessToAll("Y");
		loginRepository.setAccessToAll("Y");
	
	}
	
	@PostMapping("/denyAll")
	public void denyAll() {
		
		requestRepository.setAccessToAll("N");
		loginRepository.setAccessToAll("N");
	
	}
	@PostMapping("/deleteRequest")
	public void deleteRequest(@RequestBody String request) {
		requestService.deleteRequest(request); 
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
	
	
	
}
