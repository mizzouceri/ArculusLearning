package com.cyberrange.api.service;


import java.util.List;


import com.cyberrange.api.model.Requests;
import com.cyberrange.api.model.User;
import com.cyberrange.api.bean.StudentUserCourseDetails;
public interface OverviewService {

	public List<User> getAllStudentUsers();
	public List<StudentUserCourseDetails> getStudeUserCourseDetails(String request);
	public int getStudentsCount();
}
