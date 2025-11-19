package com.cyberrange.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;


import com.cyberrange.api.bean.PeerRanks;
import com.cyberrange.api.model.EvaluationQuestions;
import com.cyberrange.api.model.EvaluationUser;
import com.cyberrange.api.repository.EvaluationUserRepository;
import com.cyberrange.api.service.EvaluationService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/evaluation")
public class EvaluationAPI {
	
	@Autowired
	EvaluationService evaluationService;

	@Autowired
	EvaluationUserRepository evaluationUserRepository;
	
	@PostMapping("/getStatusofEvaluation")
	public ResponseEntity<EvaluationUser> getStatusofEvaluation(@RequestBody String request){
		EvaluationUser evaluationUser =  null;
		
		try {
			JSONParser parser = new JSONParser();
			JSONObject json=(JSONObject) parser.parse(request);
			Long moduleId =  Long.parseLong(String.valueOf(json.get("moduleId")));
			Long userId =  Long.parseLong(String.valueOf(json.get("userId")));
			String evaluationCd = String.valueOf(json.get("evaluationCd"));
			evaluationUser = evaluationService.getStatusofEvaluation(moduleId, userId, evaluationCd);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<EvaluationUser>(evaluationUser, HttpStatus.OK);
	}
	
	@PostMapping("/getQuestionForEvaluation")
	public ResponseEntity<List<EvaluationQuestions>> getQuestionForEvaluation(@RequestBody String request){
		List<EvaluationQuestions> evaluations =  null;
		
		try {

			JSONParser parser = new JSONParser();
			JSONObject json=(JSONObject) parser.parse(request);

			Long moduleId =  Long.parseLong(String.valueOf(json.get("moduleCd")));
			String evaluationCd =  String.valueOf(json.get("evaluationCd"));

			evaluations = evaluationService.getQuestionForEvaluation(moduleId, evaluationCd);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		

		return new ResponseEntity<List<EvaluationQuestions>>(evaluations, HttpStatus.OK);
	}
	
	@PostMapping("/submitEvaluation")
	public ResponseEntity<EvaluationUser> submitEvaluation(@RequestBody String request){
		
		EvaluationUser evaluationUser =  null;
		
		try {

			JSONParser parser = new JSONParser();
			JSONObject json=(JSONObject) parser.parse(request);

			Long moduleId =  Long.parseLong(String.valueOf(json.get("moduleCd")));
			Long userId =  Long.parseLong(String.valueOf(json.get("userId")));
			String evaluationCd =  String.valueOf(json.get("evaluationCd"));
			
			JSONArray evaluationAnsewer =  (JSONArray) (json.get("answers"));
			
			evaluationUser = evaluationService.submitEvaluation(evaluationAnsewer, userId, moduleId, evaluationCd);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		

		return new ResponseEntity<EvaluationUser>(evaluationUser, HttpStatus.OK);
	}
	
	@PostMapping("/getStudentProgress")
	public ResponseEntity<List<EvaluationUser>> getStudentProgress(@RequestBody String request){
		List<EvaluationUser> evaluationUsers =  null;
		
		try {

			JSONParser parser = new JSONParser();
			JSONObject json=(JSONObject) parser.parse(request);

			Long userId =  Long.parseLong(String.valueOf(json.get("userId")));
			String evaluationCd = String.valueOf(json.get("evaluationCd"));
			evaluationUsers = evaluationService.getStudentProgress( userId, evaluationCd);
			
			for(EvaluationUser evaluationUser:evaluationUsers) {
				
				List<EvaluationUser> evaluationUsersRank = evaluationService.getStudentRank( evaluationUser.getModule().getModuleId());

				int rank=0;
				int persentage = 0;
				
				for(EvaluationUser evaluationUserRank:evaluationUsersRank) {
					
					
					if(persentage == 0) {
						rank = rank + 1;
						persentage = evaluationUserRank.getPersentage();
					}
					
					if(persentage != evaluationUserRank.getPersentage()) {
						rank = rank + 1;
					}
					
					if(evaluationUserRank.getUser().getUserId() == userId) {
						evaluationUser.setRank(String.valueOf(rank));
						
						break;
					}
				}
				
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<List<EvaluationUser>>(evaluationUsers, HttpStatus.OK);
	}
	
	@PostMapping("/getStudentRank")
	public ResponseEntity<List<EvaluationUser>> getStudentRank(@RequestBody String request){
		List<EvaluationUser> evaluationUsers =  null;
		
		try {

			JSONParser parser = new JSONParser();
			JSONObject json=(JSONObject) parser.parse(request);

			Long moduleId =  Long.parseLong(String.valueOf(json.get("moduleId")));

			evaluationUsers = evaluationService.getStudentRank(moduleId);
			
			int rank=0;
			int persentage = 0;
			for(EvaluationUser evaluationUser:evaluationUsers) {

				if(persentage == 0) {
					rank = rank + 1;
					persentage = evaluationUser.getPersentage();
				}
				
				if(persentage != evaluationUser.getPersentage()) {
					rank = rank + 1;
				}
				
				evaluationUser.setRank(String.valueOf(rank));
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<List<EvaluationUser>>(evaluationUsers, HttpStatus.OK);
	}

	@PostMapping(value="/getPeerRanks", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<List<JSONObject>> getPeerRanks(@RequestBody String request){
		List<PeerRanks> peerRanks =  null;
		List<JSONObject> response = new ArrayList<JSONObject>();
		try {

			JSONParser parser = new JSONParser();
			JSONObject json=(JSONObject) parser.parse(request);

			Long moduleId =  Long.parseLong(String.valueOf(json.get("moduleId")));

			//evaluationUsers = evaluationService.getStudentRank(moduleId);
			peerRanks = evaluationUserRepository.getPeerRanking(moduleId,"TECH_ASMT");
			for (PeerRanks ranks : peerRanks) {
				json = new JSONObject();
				json.put("moduleId", ranks.getModuleId());
				json.put("userName", ranks.getUserName());
				json.put("points", ranks.getPoints());
				json.put("totalPoints", ranks.getTotalPoints());
				response.add(json);
			}
			System.out.println("\n\nCOunt: "+peerRanks.size()+"\n\n");
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<List<JSONObject>>(response, HttpStatus.OK);
	}
	
}
