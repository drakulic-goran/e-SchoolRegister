package com.iktpreobuka.projekat_za_kraj.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.security.Views;

@Entity
@Table(name = "department_class", uniqueConstraints=@UniqueConstraint(columnNames= {"class_id", "department_id", "school_year"}))
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class DepartmentClassEntity {

	private static final Integer STATUS_INACTIVE = 0;
	private static final Integer STATUS_ACTIVE = 1;
	private static final Integer STATUS_ARCHIVED = -1;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.Student.class)
	@Column(name="department_class_id")
	private Integer id;
		
	@JsonIgnore
	@JsonView(Views.Student.class)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "class_id")
	@NotNull (message = "Class must be provided.")
	private ClassEntity clas;

	
	//@JsonIgnore
	@JsonView(Views.Student.class)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id", nullable=false)
	@NotNull (message = "Department must be provided.")
	private DepartmentEntity department;
	
	
	@JsonView(Views.Student.class)
	@Column(name="school_year", nullable=false)
	@NotNull (message = "School year must be provided.")
	@Pattern(regexp = "^(20|[3-9][0-9])[0-9]{2}\\-(20|[3-9][0-9])[0-9]{2}$", message="School year is not valid, must be in format YYYY-YYYY.")
	private String schoolYear;
	@JsonView(Views.Admin.class)
	@Max(1)
    @Min(-1)
    @Column(name = "status", nullable = false)
	private Integer status;
	@JsonView(Views.Admin.class)
    @Column(name = "created_by", nullable = false, updatable = false)
	private Integer createdById;
    @JsonView(Views.Admin.class)
    @Column(name = "updated_by")
    private Integer updatedById;
	@JsonIgnore
	@Version
	private Integer version;

	
	public DepartmentClassEntity() {
		super();
	}

	public DepartmentClassEntity(@NotNull(message = "Class must be provided.") ClassEntity class_,
			@NotNull(message = "Subject must be provided.") DepartmentEntity department,
			@NotNull(message = "School year must be provided.") @Pattern(regexp = "^(20|[3-9][0-9])\\d{2}\\-(20|[3-9][0-9])\\\\d{2}$", message = "School year is not valid, must be in format YYYY-YYYY.") String schoolYear,
			Integer createdById) {
		super();
		this.clas = class_;
		this.department = department;
		this.schoolYear = schoolYear;
		this.status = getStatusActive();
		this.createdById = createdById;
	}

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public DepartmentEntity getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentEntity department) {
		this.department = department;
	}

	public String getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(String schoolYear) {
		this.schoolYear = schoolYear;
	}

	public Integer getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Integer createdById) {
		this.createdById = createdById;
	}

	public Integer getUpdatedById() {
		return updatedById;
	}

	public void setUpdatedById(Integer updatedById) {
		this.updatedById = updatedById;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public ClassEntity getClas() {
		return clas;
	}

	public void setClas(ClassEntity clas) {
		this.clas = clas;
	}

	public static Integer getStatusInactive() {
		return STATUS_INACTIVE;
	}

	public static Integer getStatusActive() {
		return STATUS_ACTIVE;
	}

	public static Integer getStatusArchived() {
		return STATUS_ARCHIVED;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatusInactive() {
		this.status = getStatusInactive();
	}

	public void setStatusActive() {
		this.status = getStatusActive();
	}

	public void setStatusArchived() {
		this.status = getStatusArchived();
	}

}
