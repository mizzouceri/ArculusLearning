package com.cyberrange.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cyberrange.api.model.GeniOutput;
import com.cyberrange.api.repository.GENIOutputRepository;

@Service
public class GENISliceServiceImpl implements GENISliceService{
	
	@Autowired
	GENIOutputRepository geniOutputRepo;

	@Override
	public GeniOutput saveOutputData(GeniOutput geniOutput) {
		GeniOutput output = geniOutputRepo.save(geniOutput);
		return output;
	}
	
}
