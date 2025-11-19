package com.cyberrange.api.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "requests")
public class Requests {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "request_id")
	private Long requestId;
	
	
	@Column(name = "student_name")
	private String studentName;
	
	@Column(name = "student_role")
	private String studentRole;
	
	@Column(name = "student_github")
	private String studentGithub;
	
	@Column(name = "access_status")
	private String accessStatus;

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentRole() {
		return studentRole;
	}

	public void setStudentRole(String studentRole) {
		this.studentRole = studentRole;
	}

	public String getStudentGithub() {
		return studentGithub;
	}

	public void setStudentGithub(String studentGithub) {
		this.studentGithub = studentGithub;
	}

	public String getAccessStatus() {
		return accessStatus;
	}

	public void setAccessStatus(String accessStatus) {
		this.accessStatus = accessStatus;
	}
	
	
	
	

	
	
}
