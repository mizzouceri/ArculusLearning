package com.cyberrange.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.cyberrange.api.model.Modules;
import com.cyberrange.api.model.UserModule;
import com.cyberrange.api.bean.ModuleStats;

@Repository
public interface ModuleRepository extends CrudRepository<Modules, Long>{
	
	@Query("SELECT e FROM Modules e WHERE  UPPER(e.difficultyLevel) = :difficultyLevel ")
	public List<Modules> find(@Param("difficultyLevel") String difficultyLevel);

	@Query("SELECT e FROM Modules e")
	public List<Modules> findAllModules();

	@Query("SELECT e from Modules e WHERE e.moduleId = :moduleId ")
	public Modules getModuleById(@Param("moduleId") Long moduleId);

	@Transactional
	@Modifying
	@Query("UPDATE Modules r SET r.moduleImage = :moduleImage WHERE r.moduleId = :moduleId")
	public int setModuleImage(@Param("moduleImage") String moduleImage, @Param("moduleId") Long moduleId);

	@Query("SELECT " +
	           "    new com.cyberrange.api.bean.ModuleStats(COUNT(e.moduleId),e.moduleId,m.moduleTitle) " +
	           "FROM " +
	           "    Modules m, UserModule e " +
			   "WHERE "+
			   "	m.moduleId = e.moduleId " +
	           "GROUP BY " +
	           "    e.moduleId")
	    List<ModuleStats> getUserModuleCount();	
}
