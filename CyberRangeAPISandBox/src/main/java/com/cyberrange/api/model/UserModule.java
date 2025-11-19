package com.cyberrange.api.model;

import java.util.Date;

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
@Table(name = "user_module")
public class UserModule {
	
    private static final long serialVersionUID = 2219737L;
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "module_id")
	private Long moduleId;
	 
	@Transient
	private Modules module;
	
	
	@Column(name = "steps_completed")
	private String stepsCompleted;

	@Column(name = "slice_created")
	private String sliceCreated;
	
	@Column(name = "slice_created_dt")
	private Date sliceCreateddt;
	
	@Column(name = "resources_reserved")
	private String resourcesReserved;
	
	@Column(name = "resources_reserved_dt")
	private Date resourcesReservedDt;
	
	@Column(name = "enrollment_dt")
	private Date enrollmentDate;
	
	@Column(name = "geni_slice_status")
	private String geniSliceStatus;

	@Column(name = "geni_username")
	private String geniUsername;
	
	@Column(name = "total_steps")
	private String totalSteps;
	
	
	
	@Column(name = "lab_completed")
	private String labCompleted;
	
	
	@Column(name = "lab_completed_dt")
	private Date labCompletedDt;
	
	@Transient
	private String labCompletedPersentage;
	
	@Transient
	private String resourceStatus;
	
	@Transient
	private String deleteStatus;
	
	
	public String getDeleteStatus() {
		return deleteStatus;
	}
	
	public Date getEnrollmentDate() {
		return enrollmentDate;
	}

	public void setEnrollmentDate(Date enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}

	public void setDeleteStatus(String deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public String getResourceStatus() {
		return resourceStatus;
	}

	public void setResourceStatus(String resourceStatus) {
		this.resourceStatus = resourceStatus;
	}

	public String getLabCompletedPersentage() {
		return labCompletedPersentage;
	}

	public void setLabCompletedPersentage(String labCompletedPersentage) {
		this.labCompletedPersentage = labCompletedPersentage;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getStepsCompleted() {
		return stepsCompleted;
	}

	public void setStepsCompleted(String stepsCompleted) {
		this.stepsCompleted = stepsCompleted;
	}

	public String getSliceCreated() {
		return sliceCreated;
	}

	public void setSliceCreated(String sliceCreated) {
		this.sliceCreated = sliceCreated;
	}

	public Date getSliceCreateddt() {
		return sliceCreateddt;
	}

	public void setSliceCreateddt(Date sliceCreateddt) {
		this.sliceCreateddt = sliceCreateddt;
	}

	public String getResourcesReserved() {
		return resourcesReserved;
	}

	public void setResourcesReserved(String resourcesReserved) {
		this.resourcesReserved = resourcesReserved;
	}

	public Date getResourcesReservedDt() {
		return resourcesReservedDt;
	}

	public void setResourcesReservedDt(Date resourcesReservedDt) {
		this.resourcesReservedDt = resourcesReservedDt;
	}

	public String getGeniUsername() {
		return geniUsername;
	}

	public void setGeniUsername(String geniUsername) {
		this.geniUsername = geniUsername;
	}

	public String getTotalSteps() {
		return totalSteps;
	}

	public void setTotalSteps(String totalSteps) {
		this.totalSteps = totalSteps;
	}

	public String getLabCompleted() {
		return labCompleted;
	}

	public void setLabCompleted(String labCompleted) {
		this.labCompleted = labCompleted;
	}

	public Date getLabCompletedDt() {
		return labCompletedDt;
	}

	public void setLabCompletedDt(Date labCompletedDt) {
		this.labCompletedDt = labCompletedDt;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public Modules getModule() {
		return module;
	}

	public void setModule(Modules module) {
		this.module = module;
	}

	public String getGeniSliceStatus() {
		return geniSliceStatus;
	}

	public void setGeniSliceStatus(String geniSliceStatus) {
		this.geniSliceStatus = geniSliceStatus;
	}

	
	


}
