package com.cyberrange.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "evaluation_user")
public  class EvaluationUser {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "points")
	private Integer  points;
	
	@Column(name = "total_points")
	private Integer  totalPoints;

	@Column(name = "evaluation_cd")
	private String evaluationCd;
	

	@OneToOne
	@JoinColumn(name = "module_id")
	private Modules module;


	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "persentage")
	private Integer persentage;
	
	@Column(name = "knowledge_level")
	private Integer knowledgeLevel;
	

	@Transient
	private String rank;
	
	
	
	public String getRank() {
		return rank;
	}


	public void setRank(String rank) {
		this.rank = rank;
	}


	public Integer getKnowledgeLevel() {
		return knowledgeLevel;
	}


	public void setKnowledgeLevel(Integer knowledgeLevel) {
		this.knowledgeLevel = knowledgeLevel;
	}


	public Integer getPersentage() {
		return persentage;
	}


	public void setPersentage(Integer persentage) {
		this.persentage = persentage;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Integer getPoints() {
		return points;
	}


	public void setPoints(Integer points) {
		this.points = points;
	}


	public String getEvaluationCd() {
		return evaluationCd;
	}


	public void setEvaluationCd(String evaluationCd) {
		this.evaluationCd = evaluationCd;
	}


	public Modules getModule() {
		return module;
	}


	public void setModule(Modules module) {
		this.module = module;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public Integer getTotalPoints() {
		return totalPoints;
	}


	public void setTotalPoints(Integer totalPoints) {
		this.totalPoints = totalPoints;
	}
	
	
}
