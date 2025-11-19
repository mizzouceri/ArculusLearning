package com.cyberrange.web.bean;

public class UserSessionVO {

	private String userId;
	private String userName;
	private String githubId;
	private String avatarUrl;

	private String password;

	private String email;
	
	private String customEmail;

	private String role;	

	private String approvalStatus;
	public String getApprovalStatus() {
		return approvalStatus;
	}
	
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public String getGithubId() {
		return githubId;
	}

	public void setGithubId(String githubId) {
		this.githubId = githubId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}
	
	
	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getCustomEmail() {
		return customEmail;
	}
	
	public void setCustomEmail(String customEmail) {
		this.customEmail = customEmail;
	}
	
}
