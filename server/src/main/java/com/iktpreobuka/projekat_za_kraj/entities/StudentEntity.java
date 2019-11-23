package com.iktpreobuka.projekat_za_kraj.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.security.Views;

@Entity
@Table(name = "student"/*, uniqueConstraints=@UniqueConstraint(columnNames= {"school_identification_number"})*/)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
//@DiscriminatorValue("student") 
public class StudentEntity extends UserEntity {
	
	private static final Integer STATUS_INACTIVE = 0;
	private static final Integer STATUS_ACTIVE = 1;
	private static final Integer STATUS_ARCHIVED = -1;

	@JsonView(Views.Student.class)
	@JsonIgnore
	//@ManyToMany(mappedBy = "students", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "parent_student", joinColumns = { @JoinColumn(name = "student_id") }, inverseJoinColumns = { @JoinColumn(name = "parent_id") })
	private Set<ParentEntity> parents = new HashSet<>();
	
	@JsonView(Views.Student.class)
	@JsonIgnore
	@OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private List<GradeEntity> grades = new ArrayList<>();
	
/*	@JsonView(Views.Student.class)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "department")
	private DepartmentEntity student_department;
*/
	@JsonIgnore
	@JsonView(Views.Student.class)
	@OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH}, orphanRemoval = true)
	private List<StudentDepartmentEntity> departments = new ArrayList<>();


	@JsonView(Views.Student.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	//@Pattern(regexp = "^([0][1-9]|[1|2][0-9]|[3][0|1])[./-]([0][1-9]|[1][0-2])[./-]([1-2][0-9]{3})$", message="Enrollment date is not valid, must be in dd-MM-yyyy format.")
	@Column(name="enrollment_date")
	@NotNull (message = "Enrollment date must be provided.")
	private Date enrollmentDate;
	@JsonView(Views.Student.class)
	@Column(name="school_identification_number", unique=true, length=50)
	@Pattern(regexp = "^[0-9]{8,8}$", message="School identification number is not valid, must be 8 only numbers long.")
	//@Size(min=8, message = "School identification number must be {min} characters long.")
	@NotNull (message = "School identification number must be provided.")
	private String schoolIdentificationNumber;
	@JsonView(Views.Student.class)
	@Max(1)
    @Min(-1)
    @Column(name = "status", nullable = false)
	protected Integer status;
	@JsonView(Views.Admin.class)
    @Column(name = "created_by", nullable = false, updatable = false)
	protected Integer createdById;
    @JsonView(Views.Admin.class)
    @Column(name = "updated_by")
    protected Integer updatedById;
	/*@JsonIgnore
	@Version
	protected Integer version;*/

	
	public StudentEntity() {
		super();
	}

	public StudentEntity(@NotNull(message = "Enrollment date must be provided.") Date enrollmentDate,
			@Pattern(regexp = "^[0-9]{8,8}$", message = "School identification number is not valid, must be 8 only numbers long.") @NotNull(message = "School identification number must be provided.") String schoolIdentificationNumber,
			Integer createdById) {
		super();
		this.enrollmentDate = enrollmentDate;
		this.schoolIdentificationNumber = schoolIdentificationNumber;
		this.status = getStatusActive();
		this.createdById = createdById;
	}

	public StudentEntity(StudentDepartmentEntity student_department,
			@NotNull(message = "Enrollment date must be provided.") Date enrollmentDate,
			@Pattern(regexp = "^[0-9]{8,8}$", message = "School identification number is not valid, must be 8 only numbers long.") @NotNull(message = "School identification number must be provided.") String schoolIdentificationNumber,
			Integer createdById) {
		super();
		this.departments.add(student_department);
		this.enrollmentDate = enrollmentDate;
		this.schoolIdentificationNumber = schoolIdentificationNumber;
		this.status = getStatusActive();
		this.createdById = createdById;
	}

	public StudentEntity(Set<ParentEntity> parents,
			@NotNull(message = "Enrollment date must be provided.") Date enrollmentDate,
			@Pattern(regexp = "^[0-9]{8,8}$", message = "School identification number is not valid, must be 8 only numbers long.") @NotNull(message = "School identification number must be provided.") String schoolIdentificationNumber,
			Integer createdById) {
		super();
		this.parents = parents;
		this.enrollmentDate = enrollmentDate;
		this.schoolIdentificationNumber = schoolIdentificationNumber;
		this.status = getStatusActive();
		this.createdById = createdById;
	}

	public StudentEntity(Set<ParentEntity> parents, StudentDepartmentEntity student_department,
			@NotNull(message = "Enrollment date must be provided.") Date enrollmentDate,
			@Pattern(regexp = "^[0-9]{8,8}$", message = "School identification number is not valid, must be 8 only numbers long.") @NotNull(message = "School identification number must be provided.") String schoolIdentificationNumber,
			Integer createdById) {
		super();
		this.parents = parents;
		this.departments.add(student_department);
		this.enrollmentDate = enrollmentDate;
		this.schoolIdentificationNumber = schoolIdentificationNumber;
		this.status = getStatusActive();
		this.createdById = createdById;
	}

	public Set<ParentEntity> getParents() {
		return parents;
	}

	public void setParents(Set<ParentEntity> parents) {
		this.parents = parents;
	}

	public List<StudentDepartmentEntity> getDepartments() {
		return departments;
	}

	public void setDepartments(List<StudentDepartmentEntity> departments) {
		this.departments = departments;
	}

	public List<GradeEntity> getGrades() {
		return grades;
	}

	public void setGrades(List<GradeEntity> grades) {
		this.grades = grades;
	}

	public Date getEnrollmentDate() {
		return enrollmentDate;
	}

	public void setEnrollmentDate(Date enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}

	public String getSchoolIdentificationNumber() {
		return schoolIdentificationNumber;
	}

	public void setSchoolIdentificationNumber(String schoolIdentificationNumber) {
		this.schoolIdentificationNumber = schoolIdentificationNumber;
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

/*	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}*/

	public Integer getStatus() {
		return status;
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
