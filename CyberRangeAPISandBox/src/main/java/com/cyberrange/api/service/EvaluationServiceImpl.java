package com.cyberrange.api.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cyberrange.api.model.EvaluationAnswers;
import com.cyberrange.api.model.EvaluationQuestions;
import com.cyberrange.api.model.EvaluationUser;
import com.cyberrange.api.model.Modules;
import com.cyberrange.api.model.User;
import com.cyberrange.api.repository.EvaluationAnsRepository;
import com.cyberrange.api.repository.EvaluationQuesRepository;
import com.cyberrange.api.repository.EvaluationUserRepository;
import com.cyberrange.api.repository.ModuleRepository;

@Service
public class EvaluationServiceImpl implements EvaluationService{
	
	@Autowired
	EvaluationQuesRepository evaluationQuesRepository;
	
	@Autowired
	EvaluationAnsRepository evaluationAnsRepository;
	
	@Autowired
	EvaluationUserRepository evaluationUsrRepository;

	@Autowired
	ModuleRepository moduleRepository;
	
	public List<EvaluationQuestions> getQuestionForEvaluation(Long moduleId, String evaluationCd){
		List<EvaluationQuestions> evaluationList=evaluationQuesRepository.find(moduleId, evaluationCd);
		return evaluationList;
	}

	@Override
	@Transactional
	public EvaluationUser submitEvaluation(JSONArray evaluationAnsewers, Long userId, Long moduleId, String evaluationCd) {
		
		Integer sumPoints = 0;
		Integer sumTotalPoints = 0;

		int i=0;
		for(i=0 ;i< evaluationAnsewers.size();i++ ) {
			Integer points = 0;
			JSONObject answerJSON = (JSONObject) evaluationAnsewers.get(i);

			EvaluationAnswers answer = new EvaluationAnswers();
			answer.setEvaluationCd(evaluationCd);
			
			User userAns= new User();
			userAns.setUserId(userId);
			answer.setUser(userAns);
			
			Long evaluationQuesId=Long.parseLong(String.valueOf(answerJSON.get("evaluationQuesId"))) ;
			answer.setEvaluationQuesId(evaluationQuesId);
			String evalAns = String.valueOf(answerJSON.get("evaluationAnswer"));
			answer.setEvaluationAnswer(evalAns);
			System.out.println(evalAns);
			EvaluationQuestions evaluationQuestions=evaluationQuesRepository.findOne(evaluationQuesId);

			if(!"".equals(evalAns)) {	
				if("TECH_ASMT".equalsIgnoreCase(evaluationCd)) {
					points = Integer.parseInt(answer.getEvaluationAnswer()); 
//							this.autoGrad(answer.getEvaluationAnswer(), evaluationQuestions.getEvaluationAnswer(), evaluationQuestions.getPoints());
				}else {
					points = Integer.parseInt(answer.getEvaluationAnswer());
				}
			}else {
				points = 0;
			}
			
			sumTotalPoints =sumTotalPoints + evaluationQuestions.getPoints();
			
			answer.setPoints(points);
			answer = evaluationAnsRepository.save(answer);
			sumPoints = sumPoints + answer.getPoints();
		}
		
		EvaluationUser evaluationUser = new EvaluationUser();
		evaluationUser.setEvaluationCd(evaluationCd);
		evaluationUser.setPoints(sumPoints);
		evaluationUser.setTotalPoints(sumTotalPoints);
		
		Double persentage = ((Double.parseDouble(String.valueOf(sumPoints)))/(Double.parseDouble(String.valueOf(sumTotalPoints)))*100);
		Integer finalpersentage = (int) Math.round(persentage);

		evaluationUser.setPersentage(finalpersentage);
		
		if(persentage >= 90) {
			evaluationUser.setKnowledgeLevel(3);
		}else if(persentage < 90 && persentage >= 70) {
			evaluationUser.setKnowledgeLevel(2);
		}else if(persentage < 70 && persentage >= 50) {
			evaluationUser.setKnowledgeLevel(1);
		}else {
			evaluationUser.setKnowledgeLevel(0);
		}
		
		Modules module = new Modules();
		module.setModuleId(moduleId);
		evaluationUser.setModule(module);

		User user= new User();
		user.setUserId(userId);
		evaluationUser.setUser(user);

		evaluationUser = evaluationUsrRepository.save(evaluationUser);
		
		return evaluationUser;
	}

	@Override
	public EvaluationUser getStatusofEvaluation(Long moduleId, Long userId, String evaluationCd) {
		EvaluationUser evaluationUser=evaluationUsrRepository.find(moduleId, userId, evaluationCd);
		return evaluationUser;
	}
	
	@Override
	public List<EvaluationUser> getStudentProgress(Long userId, String evaluationCd){
		List<EvaluationUser>  evaluationUsers = evaluationUsrRepository.find(userId, evaluationCd);
		return evaluationUsers;
	}
	
	public static int autoGrad(String answer, String expectedKeyWords, int totalMarks) {
		
//		answer = answer.toLowerCase();
//		expectedKeyWords =expectedKeyWords.toLowerCase();
//		String[] keyWords= expectedKeyWords.split(",");
		int finalPoint = 0;
		int finalPoints = 0;
//		Double points = 0.0;
//		for(String keyWord:keyWords) {
//			
//			if(answer.indexOf(keyWord) > -1) {
//				points = points+1.0;
//			}
//			
//		}
//		
//		Double totalNumberOfKeyWords = Double.parseDouble(String.valueOf(keyWords.length));
//		Double totalPoints = (double) ((points/totalNumberOfKeyWords) * 100);
		
		int totalPoints = Integer.parseInt(answer);
		totalPoints = totalPoints*5;
		finalPoint = (int) Math.round(totalPoints);
		
		if(finalPoint >= 100){
			finalPoints = totalMarks;
		}
		else if(finalPoint >= 50 && finalPoint < 70) {
			finalPoints = (int) Math.round(totalMarks * (0.75));
		}
		else if(finalPoint >= 30 && finalPoint < 50) {
			finalPoints = (int) Math.round(totalMarks * (0.50));
		}
		else if(finalPoint >= 10 && finalPoint < 30) {
			finalPoints = (int) Math.round(totalMarks * (0.25));
		}
		else {
			finalPoints = 0;
		}
		return finalPoints;
	}

	@Override
	public List<EvaluationUser> getStudentRank(Long moduleId) {
		
		return evaluationUsrRepository.findRank( moduleId);
	}

	@Override
	public Long addEvaluationQuestions(String request,Long moduleId) {
		// TODO Auto-generated method stub
		try{
			JSONParser parser = new JSONParser();
			JSONObject evaluationData=(JSONObject) parser.parse(request);
			System.out.println("\nEvaluation\n\n"+evaluationData);
			System.out.println(moduleId);
			int preSrvyNo = Integer.parseInt(evaluationData.get("preSrvyNo").toString());
			int techAsmtNo = Integer.parseInt(evaluationData.get("techAsmtNo").toString());

			List<EvaluationQuestions> questions= new ArrayList<EvaluationQuestions>();


			Modules module = moduleRepository.getModuleById(moduleId);

			JSONObject preSrvyQuestions =(JSONObject) evaluationData.get("preSrvyQuestions");
			JSONObject techAsmtQuestions =(JSONObject) evaluationData.get("techAsmtQuestions");
			for (int i = 1; i <= preSrvyNo ; i++) {
				//evaluationQuesRepository.save(entities);
				EvaluationQuestions e = new EvaluationQuestions();
				String evaluationCd = "PRE_SRVY";
				String evaluationQuestions = preSrvyQuestions.get(i+"").toString();
				String evaluationAnswer = "[{\"key\":\"No\",\"Value\":\"0\"},{\"key\":\"Yes\",\"Value\":\"10\"}]";
				String typeAns = "RADIO";
				String questionLabel = i+"";
				int points = 10;
				e.setEvaluationCd(evaluationCd);
				e.setEvaluationQuestions(evaluationQuestions);
				e.setEvaluationAnswer(evaluationAnswer);
				e.setModule(module);
				e.setTypeAns(typeAns);
				e.setQuestionLabel(questionLabel);
				e.setPoints(points);
				System.out.println(e.toString());
				
				//Adding e to questions array;
				questions.add(e);

			}

			for (int i = 1; i <= techAsmtNo ; i++) {
				EvaluationQuestions e = new EvaluationQuestions();
				JSONObject techQandA = (JSONObject) techAsmtQuestions.get(i+"");
				String evaluationCd = "TECH_ASMT";
				String evaluationQuestions = techQandA.get("question").toString();
				String evaluationAnswer = techQandA.get("answer").toString();
				String typeAns = "TEXTAREA";
				String questionLabel = i+"";
				int points = 20;
				e.setEvaluationCd(evaluationCd);
				e.setEvaluationQuestions(evaluationQuestions);
				e.setEvaluationAnswer(evaluationAnswer);
				e.setModule(module);
				e.setTypeAns(typeAns);
				e.setQuestionLabel(questionLabel);
				e.setPoints(points);
				System.out.println(e.toString());
				questions.add(e);
			}
			
			evaluationQuesRepository.save(questions);
			System.out.println("Evaluation questions saved for "+module.getModuleId());
			
		}
		catch(Exception e){
			System.out.print(e.getMessage());
		}
		return null;
	}

	@Override
	public int deleteEvaluations(Long moduleId) {
		// TODO Auto-generated method stub
		System.out.println("Deleting evaluation");
		try{
			List<EvaluationQuestions> evalQues = evaluationQuesRepository.getEvaluationQuesByModuleId(moduleId);

			if(evalQues.size() > 0){
				System.out.println("Deleting Started");

				// delete user evaluations by moduleId
					List<EvaluationUser> evalUserList =  evaluationUsrRepository.findByModuleId(moduleId);
					if(evalUserList.size() >  0){
					//evaluationUsrRepository.deleteEvaluationUserByModuleId(moduleId);
					evaluationUsrRepository.delete(evalUserList);
					System.out.println("USer eval deleted");
					}
				
				
				// get evaluation que Id from evaluationQues and delete evaluation ans
				
				for(EvaluationQuestions e : evalQues){

					EvaluationAnswers evalAns = evaluationAnsRepository.findEvaluationAnsByQueId(e.getId());
					//int r = evaluationAnsRepository.deleteEvaluationAnsByQueId(e.getId());
					if(evalAns != null){
						evaluationAnsRepository.delete(evalAns);
					}
					System.out.println("Ans eval deleted " + e.getId());
				}

				// delete evaluation questions
				
				evaluationQuesRepository.delete(evalQues);
				System.out.println("Ques eval deleted");
		
			}
			
		} catch (Exception e) {
			//TODO: handle exception
			System.out.println(e.getMessage());
		}
		return 0;
	}

	@Override
	public int deleteUserEvaluation(Long userId, Long moduleId) {
		// TODO Auto-generated method stub

		int r = evaluationUsrRepository.deleteUserEvaluationByModuleId(moduleId, userId);
		
		return r;
	}
}
