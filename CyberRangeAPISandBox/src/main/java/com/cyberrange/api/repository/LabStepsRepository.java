package com.cyberrange.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.cyberrange.api.model.LabSteps;

@Repository
public interface LabStepsRepository extends CrudRepository<LabSteps, Long>{
	
	@Query("SELECT u FROM LabSteps u WHERE  u.module.moduleId = :moduleId ")
	public List<LabSteps> find(@Param("moduleId") Long moduleId);

	@Transactional
	@Modifying
	@Query("UPDATE LabSteps r SET r.viewName = :viewName WHERE r.module.moduleId = :moduleId and r.stepId = :stepId")
	public int setViewName(@Param("viewName") String viewName, @Param("moduleId") Long moduleId, @Param("stepId") String stepId);

	@Modifying
	@Query("DELETE FROM LabSteps u WHERE u.module.moduleId = :moduleId")
	public int deleteLabStepsByModuleId(@Param("moduleId") Long moduleId);

	
}
