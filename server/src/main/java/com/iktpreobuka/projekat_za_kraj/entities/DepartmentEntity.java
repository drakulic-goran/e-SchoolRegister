package com.iktpreobuka.projekat_za_kraj.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@Table(name = "department", uniqueConstraints=@UniqueConstraint(name = "UniqueDepartments", columnNames= {"department_label", "enrollment_year"}))
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class DepartmentEntity {
	
	private static final Integer STATUS_INACTIVE = 0;
	private static final Integer STATUS_ACTIVE = 1;
	private static final Integer STATUS_ARCHIVED = -1;
	
/*	@JsonView(Views.Teacher.class)
	@JsonIgnore
	@OneToMany(mappedBy = "student_department", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private List<StudentEntity> students = new ArrayList<>();
*/
	
	@JsonIgnore
	@JsonView(Views.Teacher.class)
	@OneToMany(mappedBy = "department", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH}, orphanRemoval = true)
	private List<StudentDepartmentEntity> students = new ArrayList<>();

	
	@JsonView(Views.Teacher.class)
	@JsonIgnore
	@OneToMany(mappedBy = "primaryDepartment", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private List<PrimaryTeacherDepartmentEntity> teachers = new ArrayList<>();
	
/*	@JsonIgnore
	@JsonView(Views.Student.class)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "class_id")
	@NotNull (message = "Class must be provided.")
	private ClassEntity class_department;
*/
	
	@JsonIgnore
	@JsonView(Views.Teacher.class)
	@OneToMany(mappedBy = "department", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH}, orphanRemoval = true)
	private List<DepartmentClassEntity> classes = new ArrayList<>();


	@JsonView(Views.Student.class)
	@JsonIgnore
	@OneToMany(mappedBy = "teachingDepartment", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private List<TeacherSubjectDepartmentEntity> teachers_subjects = new ArrayList<>();


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.Student.class)
	@Column(name="department_id")
	private Integer id;
	@Column(name="department_label", nullable=false)
	@JsonView(Views.Student.class)
	//@Min(value=1, message = "Department label must be {value} or higher!")
	@Pattern(regexp = "^[A-Za-z0-9]{1,2}$", message="Department label is not valid, can contain only one or two letters and/or numbers.")
	@NotNull (message = "Department label must be provided.")
	//@NaturalId
	private String departmentLabel;
	@JsonView(Views.Student.class)
	@Column(name="enrollment_year", nullable=false)
	@NotNull (message = "Enrollment year must be provided.")
	@Pattern(regexp = "^(20|[3-9][0-9])\\d{2}$", message="Enrollment year is not valid, must be in format YYYY.")
	private String enrollmentYear;
	/*@JsonView(Views.Student.class)
	@Column(name="semester")
	@Enumerated(EnumType.STRING)
	@NotNull (message = "Semester must be provided.")
	private ESemester semester;*/
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

	public DepartmentEntity() {
		super();
	}

	public DepartmentEntity(
			@Pattern(regexp = "^[A-Za-z0-9]{1,2}$", message = "Department label is not valid, can contain only one or two letters and/or numbers.") @NotNull(message = "Department label must be provided.") String departmentLabel,
			@NotNull(message = "Enrollment year must be provided.") @Pattern(regexp = "^(20|[3-9][0-9])\\d{2}$", message = "Enrollment year is not valid, must be in format YYYY.") String enrollmentYear,
			Integer createdById) {
		super();
		this.departmentLabel = departmentLabel;
		this.enrollmentYear = enrollmentYear;
		this.status = getStatusActive();
		this.createdById = createdById;
	}

/*	public DepartmentEntity(ClassEntity class_department,
			@Pattern(regexp = "^[a-zA-Z]$", message = "Department label is not valid, can contain only one letter.") @NotNull(message = "Department label must be provided.") String departmentLabel,
			Integer createdById) {
		super();
		this.class_department = class_department;
		this.departmentLabel = departmentLabel;
		this.status = getStatusActive();
		this.createdById = createdById;
	}
*/


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

	public List<StudentDepartmentEntity> getStudents() {
		return students;
	}

	public void setStudents(List<StudentDepartmentEntity> students) {
		this.students = students;
	}

	public List<PrimaryTeacherDepartmentEntity> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<PrimaryTeacherDepartmentEntity> teachers) {
		this.teachers = teachers;
	}

	public List<DepartmentClassEntity> getClasses() {
		return classes;
	}

	public void setClasses(List<DepartmentClassEntity> classes) {
		this.classes = classes;
	}

	public List<TeacherSubjectDepartmentEntity> getTeachers_subjects() {
		return teachers_subjects;
	}

	public void setTeachers_subjects(List<TeacherSubjectDepartmentEntity> teachers_subjects) {
		this.teachers_subjects = teachers_subjects;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDepartmentLabel() {
		return departmentLabel;
	}

	public void setDepartmentLabel(String departmentLabel) {
		this.departmentLabel = departmentLabel;
	}

	public String getEnrollmentYear() {
		return enrollmentYear;
	}

	public void setEnrollmentYear(String enrollmentYear) {
		this.enrollmentYear = enrollmentYear;
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


/*	public ClassEntity getClass_department() {
		return class_department;
	}

	public void setClass_department(ClassEntity class_department) {
		this.class_department = class_department;
	}
*/
	
	

	/*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
 
        if (o == null || getClass() != o.getClass())
            return false;
 
        DepartmentEntity department = (DepartmentEntity) o;
        return Objects.equals(this.getDepartmentLabel(), department.getDepartmentLabel());
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(this.getDepartmentLabel());
    }*/

}
