package com.cyberrange.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.cyberrange.api.model.SendEmail;

@Repository
public interface SendEmailRepository extends CrudRepository<SendEmail, Long>{
	
    @Query("SELECT u FROM SendEmail u WHERE u.type = :type")
	public SendEmail findEmailAddressByType(@Param("type") String type);

    @Transactional
	@Modifying
	@Query("UPDATE SendEmail r SET r.emailAddress = :emailAddress WHERE r.id = :id")
	public int setAccess(@Param("id") Long id,@Param("emailAddress") String emailAddress);
	

}