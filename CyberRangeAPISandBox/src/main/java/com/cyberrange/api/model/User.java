package com.cyberrange.api.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "user_name")
	private String userName;
	
	@Column(name = "github_id")
	private String githubId;
	
	@Column(name = "avatar_url")
	private String avatarUrl;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "email")
	private String email;

	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "institution")
	private String institution;
	
	@Column(name = "custom_email")
	private String customEmail;
	
	@Column(name = "reason_to_attend")
	private String reasonToAttend;
	
	@Column(name = "role")
	private String role;
	
	@Column(name = "created_time")
	private Date createdTime;
	
	@Column(name = "approval_status")
	private String approval_status;
	
	public String getApprovalStatus() {
		return approval_status;
	}
	public void setApprovalStatus(String approval_status) {
		this.approval_status = approval_status;
	}
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
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
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCustomEmail() {
		return customEmail;
	}
	public void setCustomEmail(String customEmail) {
		this.customEmail = customEmail;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public String getGithubId() {
		return githubId;
	}
	public void setGithubId(String githubId) {
		this.githubId = githubId;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	
	
}
