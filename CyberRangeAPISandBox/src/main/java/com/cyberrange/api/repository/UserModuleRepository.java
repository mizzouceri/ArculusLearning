package com.cyberrange.api.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cyberrange.api.bean.ModuleGraphStats;
import com.cyberrange.api.model.UserModule;


@Repository
public interface UserModuleRepository extends CrudRepository< UserModule, Long>{
	
	@Query("SELECT u FROM UserModule u WHERE u.userId = :userId and u.moduleId = :moduleId ")
	public UserModule find(@Param("moduleId") Long moduleId, @Param("userId") Long userId);

	@Modifying
	@Query("DELETE FROM UserModule u WHERE  u.userId = :userId and u.moduleId = :moduleId ")
	public void deleteCourse(@Param("moduleId") Long moduleId, @Param("userId") Long userId);

	@Transactional
	@Modifying
	@Query("UPDATE UserModule u set u.sliceCreated=:sliceName , u.sliceCreateddt=:sliceCreatedDt, u.geniUsername=:geniUserName , u.geniSliceStatus = :geniSliceStatus WHERE u.userId = :userId and u.moduleId = :moduleId ")
	public int updateSlice(@Param("sliceName") String sliceName,@Param("moduleId") Long moduleId,@Param("userId") Long userId,@Param("geniUserName") String geniUserName, @Param("sliceCreatedDt") Date sliceCreatedDt, @Param("geniSliceStatus") String geniSliceStatus );

	@Modifying
	@Query("UPDATE UserModule u set u.stepsCompleted=:step WHERE u.userId = :userId and u.moduleId = :moduleId ")
	public int updateStatusofLab(@Param("moduleId") Long moduleId,@Param("userId") Long userId,@Param("step") String step);

	@Transactional
	@Modifying
	@Query("UPDATE UserModule u set u.resourcesReserved=:resourcesReserved , u.resourcesReservedDt=:resourcesReservedDt WHERE u.userId = :userId and u.moduleId = :moduleId ")
	public int updateLabResoucesStatus( @Param("moduleId") Long moduleId, @Param("userId") Long userId, @Param("resourcesReservedDt") Date resourcesReservedDt, @Param("resourcesReserved") String resourcesReserved);

	@Modifying
	@Query("UPDATE UserModule u set u.stepsCompleted=:step, u.labCompleted='Y', u.labCompletedDt = :labCompletedDt  WHERE u.userId = :userId and u.moduleId = :moduleId ")
	public int updateStatusAndCompleteLab(@Param("moduleId") Long moduleId, @Param("userId") Long userId, @Param("step") String step, @Param("labCompletedDt") Date labCompletedDt);

	@Query("SELECT u FROM UserModule u WHERE u.userId = :userId  ")
	public List<UserModule> getModulesForUser(@Param("userId") Long userId);
	
	@Query("SELECT count(u.id) FROM UserModule u WHERE u.moduleId = :moduleId  ")
	public Long getCountOfEntrollement(@Param("moduleId")Long moduleId);
	
	@Modifying
	@Query("UPDATE UserModule u set u.sliceCreated=:sliceName , u.sliceCreateddt=:sliceCreatedDt,  u.resourcesReserved=:resourcesReserved , u.resourcesReservedDt=:resourcesReservedDt WHERE u.userId = :userId and u.moduleId = :moduleId ")
	public int updateGoogleVMInfo(@Param("sliceName") String sliceName,@Param("moduleId") Long moduleId,@Param("userId") Long userId,@Param("resourcesReserved") String resourcesReserved, @Param("sliceCreatedDt") Date sliceCreatedDt, @Param("resourcesReservedDt") Date resourcesReservedDt );

    @Query("SELECT u FROM UserModule u WHERE u.userId = :userId ORDER BY u.moduleId ASC ")
	public List<UserModule> getModulesForUserInOrder(@Param("userId") Long userId);

	@Query("SELECT u from UserModule u WHERE u.moduleId = :moduleId")
	public List<UserModule> getUserModulesByModuleId(@Param("moduleId") Long moduleId);
	
	@Query("SELECT " +
	           "    new com.cyberrange.api.bean.ModuleGraphStats(COUNT(u.moduleId), u.moduleId, MONTH(u.enrollmentDate), YEAR(u.enrollmentDate)) " +
	           "FROM " +
	           "    Modules m, UserModule u " +
			   "WHERE "+
			   "	m.moduleId = u.moduleId AND YEAR(u.enrollmentDate) = YEAR(CURDATE()) " +
	           "GROUP BY " +
	           "    MONTH(u.enrollmentDate), YEAR(u.enrollmentDate), u.moduleId, m.moduleTitle ")
		List<ModuleGraphStats> getGraphStats();
}

