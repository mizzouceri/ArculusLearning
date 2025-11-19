package com.cyberrange.api.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.cyberrange.api.service.LoginService;
import com.cyberrange.api.service.RequestService;
import com.cyberrange.api.repository.SendEmailRepository;
import com.cyberrange.api.model.User;
import com.cyberrange.api.model.Requests;
import com.cyberrange.api.model.SendEmail;
import com.cyberrange.api.google.SendMailLib;

import com.cyberrange.api.bean.Mail;
import com.google.gson.Gson;
@Service
public class SendMailServiceImpl implements SendMailService{

	 
    @Autowired
    JavaMailSender mailSender;
    
	@Autowired
	LoginService loginService;
	
	@Autowired
	RequestService requestService;

	@Autowired
	SendEmailRepository sendEmailRepository;

	@Autowired
	SendMailLib sendMailLib;
    
	public String sendMailTo( String input){
		
		System.out.println(input);
		String userName = "";
		String gitId = "";
		
		String[] arr = input.split(",",2);
		String input1 = arr[0];
		String input2 = arr[1];
		
		String[] line = input2.split(":",2);
		Pattern p = Pattern.compile("\"([^\"]*)\"");
		Matcher m = p.matcher(line[1]);
		while (m.find()) {
			gitId = m.group(1);
		}
		
		line = input1.split(":",2);
		m = p.matcher(line[1]);
		while (m.find()) {
			userName = m.group(1);
		}
		System.out.println("Got : "+ gitId +" "+ userName);
		
		User user_form = loginService.getUserDetails(userName.toLowerCase(), gitId);
		
		System.out.println("User : "+ user_form.getEmail());
		
		Requests student_request = new Requests();
		student_request.setStudentName(userName);
		student_request.setAccessStatus("N");
		student_request.setStudentGithub(gitId);
		student_request.setStudentRole(user_form.getRole());
		
		Requests r = requestService.findByGit(gitId);
		if(r == null) {
			requestService.insertRequest(student_request);
		}
		
		
		System.out.println(gitId);

		SendEmail sendEmailTo = sendEmailRepository.findEmailAddressByType("TO");
		String sendEmailFrom = "";
		try {
			 sendEmailFrom = sendMailLib.getFromMailAddressFromPropertiesFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		Mail mail = new Mail();
        mail.setMailFrom(sendEmailFrom);
        mail.setMailTo(sendEmailTo.getEmailAddress());
        mail.setMailSubject("New Student Approval");
        mail.setMailContent(userName.toUpperCase()+", with Github Id "+gitId +" is requesting approval for student to access the courses\n\nThank you\n\n");
        sendEmail(mail);
		
		return "Database sent mail";
	}
	
	public String sendNotification(String message, String userName, String emailTo){
		
		String sendEmailFrom = "";
		try {
			 sendEmailFrom = sendMailLib.getFromMailAddressFromPropertiesFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		Mail mail = new Mail();
        mail.setMailFrom(sendEmailFrom);
        mail.setMailTo(emailTo);
        if (message == "Approved") {
        	mail.setMailSubject("Request Approved");
            mail.setMailContent("Dear " + userName.toLowerCase()+", your request to access Mizzou Cloud DevOps portal has been approved.\n\nYou may now log in to your student dashboard from here: https://mizzouclouddevops.net \n\nThank you,\nMizzou Cloud and DevOps Team\n");
        }
        else {
        	mail.setMailSubject("Request Denied");
            mail.setMailContent("Dear " + userName.toLowerCase()+", your request to access Mizzou Cloud DevOps portal has been denied.\n\nMizzou Cloud and DevOps Team");
        }
        sendEmail(mail);
		
		return "Database sent mail";
	}
	
	
    public void sendEmail(Mail mail) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
 
        try {
 
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
 
            mimeMessageHelper.setSubject(mail.getMailSubject());
            mimeMessageHelper.setFrom(new InternetAddress(mail.getMailFrom(), "mizzouclouddevops.net"));
            mimeMessageHelper.setTo(mail.getMailTo());
            mimeMessageHelper.setText(mail.getMailContent());
 
            mailSender.send(mimeMessageHelper.getMimeMessage());
 
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
	

}
