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
@Table(name = "modules")
public class Modules {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "module_id")
	private Long moduleId;
	
	@Transient
	private String labStarted;
	
	@Column(name = "module_title")
	private String moduleTitle;
	
	@Column(name = "module_description")
	private String moduleDescription;
	
	@Column(name = "difficulty_level")
	private String difficultyLevel;
	
	@Column(name = "created_by")
	private Long createdBy;
	
	@Column(name = "created_dt")
	private Date createdDt;

	@Column(name = "updated_by")
	private Long updatedBy;
	
	@Column(name = "updated_dt")
	private Date updatedDt;
	
	
	@Column(name = "module_tags")
	private String moduleTag;
	
	@Column(name = "module_time")
	private String moduleTime;
	
	@Transient
	private String[] moduleTags;
	
	@Column(name = "module_image")
	private String moduleImage;
	
	
	@Column(name = "module_bg")
	private String moduleBg;
	
	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}


	public String getModuleDescription() {
		return moduleDescription;
	}

	public void setModuleDescription(String moduleDescription) {
		this.moduleDescription = moduleDescription;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDt() {
		return updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	public String getDifficultyLevel() {
		return difficultyLevel;
	}

	public void setDifficultyLevel(String difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}

	public String getModuleTitle() {
		return moduleTitle;
	}

	public void setModuleTitle(String moduleTitle) {
		this.moduleTitle = moduleTitle;
	}

	public String getModuleTag() {
		if(null != moduleTag && !"".equals(moduleTag)) {
			String[] tags = moduleTag.split(",");
			this.setModuleTags(tags);
		}
		return moduleTag;
	}

	public void setModuleTag(String moduleTag) {
		this.moduleTag = moduleTag;
	}

	public String[] getModuleTags() {
		return moduleTags;
	}

	public void setModuleTags(String[] moduleTags) {
		this.moduleTags = moduleTags;
	}

	public String getModuleTime() {
		return moduleTime;
	}

	public void setModuleTime(String moduleTime) {
		this.moduleTime = moduleTime;
	}

	public String getModuleImage() {
		return moduleImage;
	}

	public void setModuleImage(String moduleImage) {
		this.moduleImage = moduleImage;
	}

	public String getLabStarted() {
		return labStarted;
	}

	public void setLabStarted(String labStarted) {
		this.labStarted = labStarted;
	}

	public String getModuleBg() {
		return moduleBg;
	}

	public void setModuleBg(String moduleBg) {
		this.moduleBg = moduleBg;
	}

	
	
}
