package com.cyberrange.api.service;

import com.cyberrange.api.model.User;



public interface LoginService {

	public User getUserDetails(String userName, String githubId);
	public User insertUserDetails(User user);
	public Long getNumberodStudents();
	public int toggleRole(String userName, String githubId);
	public User updateEmail(String userName, String githubId, String customEmail);
}
