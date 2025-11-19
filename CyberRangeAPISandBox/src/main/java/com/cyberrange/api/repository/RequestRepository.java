package com.cyberrange.api.repository;

//import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cyberrange.api.model.Requests;
//import com.cyberrange.api.model.User;
//import com.cyberrange.api.model.UserModule;
//import com.cyberrange.api.model.EvaluationUser;
//import com.cyberrange.api.bean.StudentUserCourseDetails;

@Repository
public interface RequestRepository extends JpaRepository<Requests, Long>{

	@Query("SELECT r FROM Requests r WHERE  r.studentGithub = :githubId ")
	public Requests findByGit(@Param("githubId") String githubId);
	
	@Transactional
	@Modifying
	@Query("UPDATE Requests r SET r.accessStatus = :accessStatus WHERE r.requestId = :requestId")
	public int setAccess(@Param("requestId") Long requestId,@Param("accessStatus") String accessStatus);
	
	@Transactional
	@Modifying
	@Query("UPDATE Requests r SET r.accessStatus = :accessStatus")
	public int setAccessToAll(@Param("accessStatus") String accessStatus);
	
	//@Query("SELECT DISTINCT new com.cyberrange.api.bean.StudentUserCourseDetails(u.userId, u.moduleId , u.labCompleted , u.stepsCompleted , u.totalSteps , e.points , e.totalPoints , e.knowledgeLevel) from UserModule u , EvaluationUser e  WHERE u.userId =:userId and e.evaluationCd =:evaluationCd")
	//public List<StudentUserCourseDetails> getUserCourseDetails(@Param("userId") Long userId, @Param("evaluationCd") String evaluationCd );
	
	//@Query("SELECT u.userId,u.moduleId,u.labCompleted,u.stepsCompleted,u.totalSteps,e.points,e.totalPoints,e.knowledgeLevel from UserModule u LEFT JOIN EvaluationUser e ON u.userId = e.userId WHERE u.userId =:userId")
	//public List<StudentUserCourseDetails> getUserCourseDetails(@Param("userId") Long userId);
	// SELECT u.user_id, u.module_id , u.lab_completed , u.steps_completed , u.total_steps , e.points , e.total_points , e.knowledge_level from user_module u join evaluation_user e on e.user_id = u.user_id WHERE u.user_id = 25 and e.evaluation_cd = "TECH_ASMT";
}
