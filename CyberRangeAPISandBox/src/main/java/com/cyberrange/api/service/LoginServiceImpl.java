package com.cyberrange.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cyberrange.api.model.Requests;
import com.cyberrange.api.model.User;
import com.cyberrange.api.repository.LoginRepository;

@Service
public class LoginServiceImpl implements LoginService {

	
	@Autowired
	LoginRepository loginRepository;
	
	@Override
	public User getUserDetails(String userName, String githubId) {
		// System.out.println("getting : " + userName + " "+ githubId);
		User user = loginRepository.find( userName,  githubId);
		System.out.println("Here is the user" + user);
		return user;
	}

	@Override
	public User insertUserDetails(User user) {
		User user_ = loginRepository.save( user);
		return user_;
		
	}

	@Override
	public User updateEmail(String userName, String githubId, String customEmail) {
		User user = loginRepository.find(userName, githubId);
		user.setCustomEmail(customEmail);
		loginRepository.save(user);
		return user;
	}
	
	
	@Override
	public Long getNumberodStudents() {

		Long userCount = loginRepository.count();
		return userCount;

	}

	@Override
	public int toggleRole(String userName,String githubId) {
		// TODO Auto-generated method stub
		User user = loginRepository.find(userName, githubId);
		if("STUDENT".equalsIgnoreCase(user.getRole())){
			//change to INSTRUCTOR
			System.out.println(user.getUserId()+"--"+user.getRole()+", change to INSTRUCTOR");
			user.setRole("Instructor");
			loginRepository.save(user);
		}
		else{
			//change to STUDENT
			System.out.println(user.getUserId()+"--"+user.getRole()+", change to STUDENT");
			user.setRole("STUDENT");
			loginRepository.save(user);
		}
		return 0;
	}

}
