package com.cyberrange.api.service;

import java.util.List;

import com.cyberrange.api.bean.Mail;
public interface SendMailService {

	public String sendMailTo(String mailAddress);
	public void sendEmail(Mail mail);
	public String sendNotification(String message, String userName, String emailTo);
}
