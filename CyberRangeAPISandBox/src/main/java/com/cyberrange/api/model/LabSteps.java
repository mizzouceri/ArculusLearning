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
@Table(name = "lab_steps")
public class LabSteps {
	
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	
	@Transient
	private Long moduleId;
	
	@JoinColumn(name = "module_id")
	@OneToOne
	private Modules module;
	
	@Column(name = "step_name")
	private String stepName;

	@Column(name = "step_id")
	private String stepId;
	
	@Transient
	private String stepStatus;
	
	@Column(name = "view_name")
	private String viewName;

	public String getStepStatus() {
		return stepStatus;
	}

	public void setStepStatus(String stepStatus) {
		this.stepStatus = stepStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public String getStepId() {
		return stepId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public Modules getModule() {
		
		return module;
	}

	public void setModule(Modules module) {
		if(null != module) {
			moduleId = module.getModuleId();
		}
		this.module = module;
	}
	
	
	

}
