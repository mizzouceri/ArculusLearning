package com.cyberrange.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cyberrange.api.model.GeniOutput;

@Repository
public interface GENIOutputRepository extends CrudRepository<GeniOutput, Long>{
	

}
