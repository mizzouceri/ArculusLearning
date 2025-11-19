package com.cyberrange.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "evaluation_answers")
public class EvaluationAnswers {
	
    private static final long serialVersionUID = 2219737L;

    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "points")
	private Integer points;
	
	@Column(name = "evaluation_cd")
	private String evaluationCd;

	@Column(name = "evaluation_answer")
	private String evaluationAnswer;

	@Column(name = "evaluation_ques_id")
	private Long evaluationQuesId;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

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

	
	
	public String getEvaluationAnswer() {
		return evaluationAnswer;
	}

	public void setEvaluationAnswer(String evaluationAnswer) {
		this.evaluationAnswer = evaluationAnswer;
	}

	public Long getEvaluationQuesId() {
		return evaluationQuesId;
	}

	public void setEvaluationQuesId(Long evaluationQuesId) {
		this.evaluationQuesId = evaluationQuesId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	


}
