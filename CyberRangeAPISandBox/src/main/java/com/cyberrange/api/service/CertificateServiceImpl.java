package com.cyberrange.api.service;

import java.io.File;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cyberrange.api.model.Modules;
import com.cyberrange.api.repository.ModuleRepository;
import com.cyberrange.api.utility.CertificateGenerator;

@Service
public class CertificateServiceImpl implements CertificateService 
{	
	@Autowired
	ModuleRepository moduleRepository;
	
	@Autowired
	CertificateGenerator certificateGenerator;
	
	
	
	@Override
	public void generateCertificate(Long moduleId, Long userId, String studentName)
	{
		// TODO Auto-generated method stub
		Modules module = moduleRepository.getModuleById(moduleId);
		certificateGenerator.manipulatePDF(moduleId, userId, studentName);
	}



	@Override
	public InputStream downloadCertificate(Long moduleId, Long userId) {
		// TODO Auto-generated method stub
		
		return certificateGenerator.downloadPDF(moduleId, userId);
		
	}

}
