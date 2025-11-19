package com.cyberrange.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;

import com.cyberrange.api.bean.PeerRanks;
import com.cyberrange.api.model.EvaluationUser;

@Repository
public interface EvaluationUserRepository extends CrudRepository<EvaluationUser, Long>{

	@Query("SELECT u FROM EvaluationUser u WHERE u.module.moduleId = :moduleId and u.user.userId = :userId and u.evaluationCd = :evaluationCd ")
	public EvaluationUser find(@Param("moduleId") Long moduleId, @Param("userId") Long userId, @Param("evaluationCd") String evaluationCd);
	

	@Query("SELECT u FROM EvaluationUser u WHERE u.user.userId = :userId and u.evaluationCd = :evaluationCd ")
	public List<EvaluationUser> find( @Param("userId") Long userId, @Param("evaluationCd") String evaluationCd);

	@Query("SELECT u FROM EvaluationUser u WHERE u.module.moduleId = :moduleId and u.evaluationCd='TECH_ASMT' order by persentage desc ")
	public List<EvaluationUser> findRank(@Param("moduleId") Long moduleId);

	
	@Modifying
	@Query("DELETE FROM EvaluationUser u WHERE u.module.moduleId = :moduleId")
	public int deleteEvaluationUserByModuleId(@Param("moduleId") Long moduleId);

	@Query("SELECT u FROM EvaluationUser u WHERE u.module.moduleId = :moduleId ")
	public List<EvaluationUser> findByModuleId(@Param("moduleId") Long moduleId);

	@Modifying
	@Query("DELETE FROM EvaluationUser u WHERE u.module.moduleId = :moduleId AND u.user.userId = :userId")
	public int deleteUserEvaluationByModuleId(@Param("moduleId") Long moduleId, @Param("userId") Long userId);
	
	@Query("SELECT " +
	           "    new com.cyberrange.api.bean.PeerRanks(e.module.moduleId,u.userName,e.points, e.totalPoints) " +
	           "FROM " +
	           "    User u, EvaluationUser e " +
			   "WHERE "+
			   "	u.userId = e.user.userId AND e.module.moduleId = :moduleId AND e.evaluationCd = :evaluationCd " +
	           "ORDER BY " +
	           "    e.points/e.totalPoints DESC ")
	    List<PeerRanks> getPeerRanking(@Param("moduleId") Long moduleId,@Param("evaluationCd") String evaluationCd);

}
