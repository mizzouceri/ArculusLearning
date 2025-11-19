package com.cyberrange.api.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cyberrange.api.bean.StudentUserCourseDetails;
import com.cyberrange.api.model.Requests;
import com.cyberrange.api.model.User;
import com.cyberrange.api.repository.RequestRepository;
import com.cyberrange.api.repository.LoginRepository;
import com.cyberrange.api.bean.StudentUserCourseDetails;

@Service
public class OverviewServiceImpl implements OverviewService {

	@Autowired
	LoginRepository loginRepository;
	
	@Autowired
	RequestRepository requestRepository;
	
	@Override
	public List<User> getAllStudentUsers() {
		// TODO Auto-generated method stub
		//System.out.println("Ovrview serv Impl : getAllStudentUsers");
		List<User> studentUsers = loginRepository.findAllStudents("student");
		//System.out.println(studentUsers.size());
		return studentUsers;
	}
	
	@Override
	public int getStudentsCount() {
		return loginRepository.findStudentsCount();
	}

	
	public List<StudentUserCourseDetails> getStudeUserCourseDetails(String request) {
		// TODO Auto-generated method stub
		System.out.println("Inside OverviewService Imp getStudentUserCourseDetails");
		Long userId = Long.parseLong(request);
		//List<StudentUserCourseDetails> result = requestRepository.getUserCourseDetails(userId,"TECH_ASMT");
		//return result;
		return null;
	}

}
