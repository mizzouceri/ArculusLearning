package com.cyberrange.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cyberrange.api.model.EvaluationQuestions;

@Repository
public interface EvaluationQuesRepository extends CrudRepository<EvaluationQuestions, Long>{

	@Query("SELECT e FROM EvaluationQuestions e WHERE e.module.moduleId = :moduleId and UPPER(e.evaluationCd) = :evaluationCd ")
	public List<EvaluationQuestions> find(@Param("moduleId") Long moduleId, @Param("evaluationCd") String evaluationCd);

	@Query("SELECT e FROM EvaluationQuestions e WHERE e.module.moduleId = :moduleId ")
	public List<EvaluationQuestions> getEvaluationQuesByModuleId(@Param("moduleId") Long moduleId);

	

}
