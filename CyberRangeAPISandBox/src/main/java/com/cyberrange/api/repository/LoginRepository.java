package com.cyberrange.api.repository;

import org.springframework.data.jpa.repository.Query;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cyberrange.api.model.User;

@Repository
public interface LoginRepository extends CrudRepository<User, Long>{
	
	@Query("SELECT u FROM User u WHERE LOWER(u.userName) = :userName and u.githubId = :githubId ")
	public User find(@Param("userName") String userName, @Param("githubId") String githubId);
	
	@Query("SELECT u FROM User u WHERE u.githubId = :githubId")
	public User findByGithubId(@Param("githubId") String githubId);

	@Transactional
	@Modifying
	@Query("UPDATE User u SET u.approval_status = :approval_status WHERE u.githubId = :githubId")
	public int approve(@Param("githubId") String githubId,@Param("approval_status") String approval_status);
	
	@Transactional
	@Modifying
	@Query("UPDATE User u SET u.approval_status = :approval_status")
	public int setAccessToAll(@Param("approval_status") String approval_status);
	
	@Transactional
	@Modifying
	@Query("UPDATE User u SET u.customEmail = :customEmail WHERE LOWER(u.userName) = :userName and u.githubId = :githubId")
	public User updateEmail(@Param("userName") String userName, @Param("githubId") String githubId,@Param("customEmail") String customEmail);
	
	@Query("SELECT u FROM User u WHERE LOWER(u.role) = :role")
	public List<User> findAllStudents(@Param("role") String role);
	
	@Query("SELECT COUNT(*) FROM User")
	public int findStudentsCount();
}
