package com.iktpreobuka.projekat_za_kraj.entities;

import java.util.Date;

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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.security.Views;

@Entity
@Table(name = "primary_teacher_department", uniqueConstraints=@UniqueConstraint(columnNames= {"teacher_id", "department_id", "assignment_date"}))
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class PrimaryTeacherDepartmentEntity {
	
	private static final Integer STATUS_INACTIVE = 0;
	private static final Integer STATUS_ACTIVE = 1;
	private static final Integer STATUS_ARCHIVED = -1;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.Teacher.class)
	@Column(name="primary_teacher_id")
	private Integer id;

	@JsonIgnore
	@JsonView(Views.Admin.class)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "teacher_id", nullable=false)
	@NotNull (message = "Primary teacher must be provided.")
	private TeacherEntity primaryTeacher;
	
	//@JsonIgnore - Skinuto zbog FE
	@JsonView(Views.Teacher.class)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id", nullable=false)
	//@Pattern(regexp = "^([0][1-9]|[1|2][0-9]|[3][0|1])[./-]([0][1-9]|[1][0-2])[./-]([1-2][0-9]{3})$", message="Enrollment date is not valid, must be in dd-MM-yyyy format.")
	@NotNull (message = "Department must be provided.")
	private DepartmentEntity primaryDepartment;

	
	@JsonView(Views.Teacher.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@NotNull (message = "Assignment date must be provided.")
	@Column(name="assignment_date")
	private Date assignmentDate;
	@JsonView(Views.Admin.class)
	@Max(1)
    @Min(-1)
    @Column(name = "status", nullable = false )
	private Integer status;
	@JsonView(Views.Admin.class)
    @Column(name = "created_by", nullable=false, updatable = false)
	private Integer createdById;
    @JsonView(Views.Admin.class)
    @Column(name = "updated_by")
    private Integer updatedById;
    
	public PrimaryTeacherDepartmentEntity() {
		super();
	}
	
	public PrimaryTeacherDepartmentEntity(
			@NotNull(message = "Primary teacher must be provided.") TeacherEntity primary_teacher,
			@NotNull(message = "Department must be provided.") DepartmentEntity primary_department,
			@NotNull(message = "Assignment date must be provided.") Date assignmentDate,
			Integer createdById) {
		super();
		this.primaryTeacher = primary_teacher;
		this.primaryDepartment = primary_department;
		this.assignmentDate = assignmentDate;
		this.status = getStatusActive();
		this.createdById = createdById;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TeacherEntity getTeacher() {
		return primaryTeacher;
	}

	public void setTeacher(TeacherEntity teacher) {
		this.primaryTeacher = teacher;
	}

	public DepartmentEntity getDepartment() {
		return primaryDepartment;
	}

	public void setDepartment(DepartmentEntity department) {
		this.primaryDepartment = department;
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

	private static Integer getStatusInactive() {
		return STATUS_INACTIVE;
	}

	private static Integer getStatusActive() {
		return STATUS_ACTIVE;
	}

	private static Integer getStatusArchived() {
		return STATUS_ARCHIVED;
	}
    
}
