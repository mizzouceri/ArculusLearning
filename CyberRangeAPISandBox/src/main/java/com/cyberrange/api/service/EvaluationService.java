package com.cyberrange.api.service;

import java.util.List;

import org.json.simple.JSONArray;

import com.cyberrange.api.model.EvaluationQuestions;
import com.cyberrange.api.model.EvaluationAnswers;
import com.cyberrange.api.model.EvaluationUser;

public interface EvaluationService {
	
	public List<EvaluationQuestions> getQuestionForEvaluation(Long moduleId, String evaluationCd);

	public EvaluationUser submitEvaluation(JSONArray evaluationAnsewers, Long userId, Long moduleId, String evaluationCd);

	public EvaluationUser getStatusofEvaluation(Long moduleId, Long userId, String evaluationCd);

	public List<EvaluationUser> getStudentProgress(Long userId, String evaluationCd);

	public List<EvaluationUser> getStudentRank(Long moduleId);

	public Long addEvaluationQuestions(String evaluationData, Long moduleId);

	public int deleteEvaluations(Long moduleId);

	public int deleteUserEvaluation(Long userId, Long moduleId);

}
