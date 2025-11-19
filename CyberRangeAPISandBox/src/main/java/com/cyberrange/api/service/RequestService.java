package com.cyberrange.api.service;

import java.util.List;


import com.cyberrange.api.model.Requests;
import com.cyberrange.api.model.User;


public interface RequestService {

	public List<Requests> getAllRequests();
	public void insertRequest(Requests req);
	public Requests findByGit(String githubId);
	public Requests grantAccessTo(String request);
	public Requests denyAccessTo(String request);
	public void deleteRequest(String request);
	public int toggleRole(String requestId);
	

}
