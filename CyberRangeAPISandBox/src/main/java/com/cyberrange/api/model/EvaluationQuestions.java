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
@Table(name = "evaluation_questions")
public class EvaluationQuestions {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "evaluation_cd")
	private String evaluationCd;

	@Column(name = "evaluation_questions")
	private String evaluationQuestions;

	@Column(name = "evaluation_answer")
	private String evaluationAnswer;

	@OneToOne
	@JoinColumn(name = "module_id")
	private Modules module;

	@Column(name = "type_ans")
	private String typeAns;
	
	@Column(name = "question_label")
	private String questionLabel;
	
	@Column(name = "points")
	private int points ;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEvaluationCd() {
		return evaluationCd;
	}

	public void setEvaluationCd(String evaluationCd) {
		this.evaluationCd = evaluationCd;
	}

	public String getEvaluationQuestions() {
		return evaluationQuestions;
	}

	public void setEvaluationQuestions(String evaluationQuestions) {
		this.evaluationQuestions = evaluationQuestions;
	}

	public String getEvaluationAnswer() {
		return evaluationAnswer;
	}

	public void setEvaluationAnswer(String evaluationAnswer) {
		this.evaluationAnswer = evaluationAnswer;
	}

	public Modules getModule() {
		return module;
	}

	public void setModule(Modules module) {
		this.module = module;
	}

	public String getTypeAns() {
		return typeAns;
	}

	public void setTypeAns(String typeAns) {
		this.typeAns = typeAns;
	}

	public String getQuestionLabel() {
		return questionLabel;
	}

	public void setQuestionLabel(String questionLabel) {
		this.questionLabel = questionLabel;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "EvaluationQuestion class\n" +
				"evaluationCd:" + this.evaluationCd + "\n"+
				"evaluationQuestions:" + this.evaluationQuestions + "\n"+
				"evaluationAnswer:" + this.evaluationAnswer + "\n"+
				"moduleId:" + this.module.getModuleId() + "\n"+
				"typeAns:" + this.typeAns + "\n"+
				"questionLabel:" + this.questionLabel + "\n"+
				"points:" + this.points + "\n";
	}

	
	

}
