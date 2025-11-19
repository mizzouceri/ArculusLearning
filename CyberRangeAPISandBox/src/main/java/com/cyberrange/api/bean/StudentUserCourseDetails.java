package com.cyberrange.api.bean;
import java.util.Date;
import java.util.List;

public class StudentUserCourseDetails {

	private Long userId;
	
	private Long moduleId;
	
	private String labCompleted;
	
	private String stepsCompleted;
	
	private String totalSteps;
	
	private int points;
	
	private int totalPoints;
	
	private int knowledgeLevel;
	
	

	public StudentUserCourseDetails(Long userId, Long moduleId, String labCompleted, String stepsCompleted,
			String totalSteps, int points, int totalPoints, int knowledgeLevel) {
		super();
		this.userId = userId;
		this.moduleId = moduleId;
		this.labCompleted = labCompleted;
		this.stepsCompleted = stepsCompleted;
		this.totalSteps = totalSteps;
		this.points = points;
		this.totalPoints = totalPoints;
		this.knowledgeLevel = knowledgeLevel;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public String getLabCompleted() {
		return labCompleted;
	}

	public void setLabCompleted(String labCompleted) {
		this.labCompleted = labCompleted;
	}

	public String getStepsCompleted() {
		return stepsCompleted;
	}

	public void setStepsCompleted(String stepsCompleted) {
		this.stepsCompleted = stepsCompleted;
	}

	public String getTotalSteps() {
		return totalSteps;
	}

	public void setTotalSteps(String totalSteps) {
		this.totalSteps = totalSteps;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}

	public int getKnowledgeLevel() {
		return knowledgeLevel;
	}

	public void setKnowledgeLevel(int knowledgeLevel) {
		this.knowledgeLevel = knowledgeLevel;
	}
	
	
	
}
