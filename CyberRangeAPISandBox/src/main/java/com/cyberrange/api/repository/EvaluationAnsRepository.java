package com.cyberrange.api.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;

import com.cyberrange.api.model.EvaluationAnswers;

@Repository
public interface EvaluationAnsRepository extends CrudRepository<EvaluationAnswers, Long>{

    @Query("SELECT e FROM EvaluationAnswers e WHERE e.evaluationQuesId = :evaluationQuesId")
    public EvaluationAnswers findEvaluationAnsByQueId(@Param("evaluationQuesId") Long evaluationQuesId);

	@Modifying
    @Query("DELETE FROM EvaluationAnswers e WHERE e.evaluationQuesId = :evaluationQuesId")
    public int deleteEvaluationAnsByQueId(@Param("evaluationQuesId") Long evaluationQuesId);
}
