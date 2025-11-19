package com.cyberrange.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.cyberrange.api.model.Requests;
import com.cyberrange.api.model.User;
import com.cyberrange.api.repository.RequestRepository;
import com.cyberrange.api.repository.LoginRepository;


@Service
public class RequestServiceImpl implements RequestService{
	
	@Autowired
	RequestRepository requestRepository;
	
	@Autowired
	LoginRepository loginRepository;
	
	public List<Requests> getAllRequests(){
		List<Requests> requests =  requestRepository.findAll();
		return requests;
	}

	
	public void insertRequest(Requests req) {
		// TODO Auto-generated method stub
		Requests r_ = requestRepository.save( req);
		//return r_;
		
	}
	
	public Requests findByGit(String githubId) {
		return requestRepository.findByGit(githubId);
	}


	
	public Requests grantAccessTo(String request) {
		// TODO Auto-generated method stub
		Long req_id = Long.parseLong(request);
		int  r_ = requestRepository.setAccess(req_id,"Y");
		Requests req = requestRepository.findOne(req_id);
		loginRepository.approve(req.getStudentGithub(),"Y");
		return null;
	}


	
	public Requests denyAccessTo(String request) {
		// TODO Auto-generated method stub
		Long req_id = Long.parseLong(request);
		int  r_ = requestRepository.setAccess(req_id,"N");
		Requests req = requestRepository.findOne(req_id);
		loginRepository.approve(req.getStudentGithub(),"N");
		return null;
	}


	
	public void deleteRequest(String request) {
		// TODO Auto-generated method stub
		Long req_id = Long.parseLong(request);
		int  r_ = requestRepository.setAccess(req_id,"N");
		Requests req = requestRepository.findOne(req_id);
		loginRepository.approve(req.getStudentGithub(),"N");
		requestRepository.delete(req);
		
	}

	@Override
	public int toggleRole(String requestId) {
		// TODO Auto-generated method stub
		Long id = Long.parseLong(requestId);
		Requests re = requestRepository.findOne(id);
		if("STUDENT".equalsIgnoreCase(re.getStudentRole())){
			//change to INSTRUCTOR
			System.out.println(re.getStudentRole()+", changing to INSTRUCTOR");
			re.setStudentRole("INSTRUCTOR");
			requestRepository.save(re);
		}
		else{
			//change to STUDENT
			System.out.println(re.getStudentRole()+", changing to STUDENT");
			re.setStudentRole("STUDENT");
			requestRepository.save(re);
		}
		return 0;
	}

}
