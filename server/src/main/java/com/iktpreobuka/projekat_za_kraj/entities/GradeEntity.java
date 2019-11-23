package com.iktpreobuka.projekat_za_kraj.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.enumerations.ESemester;
import com.iktpreobuka.projekat_za_kraj.security.Views;

@Entity
@Table(name = "grade")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class GradeEntity {
	
	private static final Integer STATUS_INACTIVE = 0;
	private static final Integer STATUS_ACTIVE = 1;
	private static final Integer STATUS_ARCHIVED = -1;

	//@JsonIgnore - Skinuto zbog FE
	@JsonView(Views.Parent.class)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "student")
	@NotNull (message = "Student must be provided.")
	private StudentEntity student;
	
	@JsonView(Views.Student.class)
	//@JsonIgnore - Skinuto zbog FE
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "teacher_subject_department")
	@NotNull (message = "Teacher-subject-department conection must be provided.")
	private TeacherSubjectDepartmentEntity teacher_subject_department;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.Student.class)
	@Column(name="grade_id")
	private Integer id;
	/*@JsonView(Views.Student.class)
	@Column(name="subject_name")
	@NotNull (message = "Subject name must be provided.")
	private String subjectName;*/
	@JsonView(Views.Student.class)
	@Column(name="grade_value")
	@NotNull (message = "Grade value must be provided.")
	@Min(value=1, message = "Grade value must be {value} or higher!")
	@Max(value=5, message = "Grade value must be {value} or lower!")
	private Integer gradeValue;
	@JsonView(Views.Student.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@NotNull (message = "Grade made date must be provided.")
	@Column(name="made_on_date")
	private Date gradeMadeDate;
	/*@JsonView(Views.Student.class)
	@Column(name="department")
	@NotNull (message = "Department label must be provided.")
	private String departmentLabel;
	@JsonView(Views.Student.class)
	@Column(name="class")
	@Enumerated(EnumType.STRING)
	@NotNull (message = "Class label must be provided.")
	private EClass classLabel;*/
	@JsonView(Views.Student.class)
	@Column(name="semester")
	@Enumerated(EnumType.STRING)
	@NotNull (message = "Semester must be provided.")
	private ESemester semester;
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
	
	public GradeEntity() {
		super();
	}
	
	public GradeEntity(@NotNull(message = "Student must be provided.") StudentEntity student,
			@NotNull(message = "Teacher-subject-department conection must be provided.") TeacherSubjectDepartmentEntity teacher_subject_department,
			@NotNull(message = "Grade value must be provided.") @Min(value = 1, message = "Grade value must be {value} or higher!") @Max(value = 5, message = "Grade value must be {value} or lower!") Integer gradeValue,
			@NotNull(message = "Grade made date must be provided.") Date gradeMadeDate,
			@NotNull(message = "Semester must be provided.") ESemester semester, 
			Integer createdById) {
		super();
		this.student = student;
		this.teacher_subject_department = teacher_subject_department;
		this.gradeValue = gradeValue;
		this.gradeMadeDate = gradeMadeDate;
		this.semester = semester;
		this.status = getStatusActive();
		this.createdById = createdById;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TeacherSubjectDepartmentEntity getTeacher_subject_department() {
		return teacher_subject_department;
	}

	public void setTeacher_subject_department(TeacherSubjectDepartmentEntity teacher_subject_department) {
		this.teacher_subject_department = teacher_subject_department;
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

	public Integer getGradeValue() {
		return gradeValue;
	}

	public void setGradeValue(Integer gradeValue) {
		this.gradeValue = gradeValue;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public StudentEntity getStudent() {
		return student;
	}

	public void setStudent(StudentEntity student) {
		this.student = student;
	}

	public Date getGradeMadeDate() {
		return gradeMadeDate;
	}

	public void setGradeMadeDate(Date gradeMadeDate) {
		this.gradeMadeDate = gradeMadeDate;
	}

	public ESemester getSemester() {
		return semester;
	}

	public void setSemester(ESemester semester) {
		this.semester = semester;
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
