package com.cyberrange.api.service;

import java.io.InputStream;

public interface CertificateService 
{
	public void generateCertificate(Long moduleId, Long userId, String studentName);
	
	public InputStream downloadCertificate(Long moduleId, Long userId);
	
}
