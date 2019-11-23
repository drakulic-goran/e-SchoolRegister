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
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.security.Views;

@Entity
@Table (name = "subject"/*, uniqueConstraints=@UniqueConstraint(columnNames= {"subject_name"})*/)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SubjectEntity {
	
	private static final Integer STATUS_INACTIVE = 0;
	private static final Integer STATUS_ACTIVE = 1;
	private static final Integer STATUS_ARCHIVED = -1;
	
	//@JsonIgnore - Skinuto zbog FE
	@JsonView(Views.Teacher.class)
	@OneToMany(mappedBy = "subject", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH}, orphanRemoval = true)
	private List<ClassSubjectEntity> classes = new ArrayList<>();

	@JsonIgnore
	@JsonView(Views.Teacher.class)
	@OneToMany(mappedBy = "subject", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH}, orphanRemoval = true)
	private List<TeacherSubjectEntity> teachers = new ArrayList<>();

	@JsonView(Views.Teacher.class)
	@JsonIgnore
	@OneToMany(mappedBy = "teachingSubject", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private List<TeacherSubjectDepartmentEntity> teachers_departments = new ArrayList<>();
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.Teacher.class)
	@Column(name="subject_id")
	private Integer id;
	@JsonView(Views.Student.class)
	@Column(name="subject_name", unique=true, length=50)
	@Pattern(regexp = "^([_A-Za-z0-9- _])+$", message="Subject is not valid.")
	@NotNull (message = "Subject name must be provided.")
	private String subjectName;
	@JsonView(Views.Student.class)
	@Column(name="week_classes_number")
	@NotNull (message = "Number of classes in a week must be provided.")
	@Min(value=0, message = "Number of classes in a week must be {value} or higher!")
	private Integer weekClassesNumber;
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

	public SubjectEntity() {
		super();
	}

	public SubjectEntity(
			@Pattern(regexp = "^([_A-Za-z0-9- _])+$", message = "Subject is not valid.") @NotNull(message = "Subject name must be provided.") String subjectName,
			@NotNull(message = "Number of classes in a week must be provided.") @Min(value = 0, message = "Number of classes in a week must be {value} or higher!") Integer weekClassesNumber,
			Integer createdById) {
		super();
		this.subjectName = subjectName;
		this.weekClassesNumber = weekClassesNumber;
		this.status = getStatusActive();
		this.createdById = createdById;
	}

	public SubjectEntity(List<TeacherSubjectEntity> teachers,
			@Pattern(regexp = "^([_A-Za-z0-9- _])+$", message = "Subject is not valid.") @NotNull(message = "Subject name must be provided.") String subjectName,
			@NotNull(message = "Number of classes in a week must be provided.") @Min(value = 0, message = "Number of classes in a week must be {value} or higher!") Integer weekClassesNumber,
			Integer createdById) {
		super();
		this.teachers = teachers;
		this.subjectName = subjectName;
		this.weekClassesNumber = weekClassesNumber;
		this.status = getStatusActive();
		this.createdById = createdById;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<TeacherSubjectDepartmentEntity> getTeachers_departments() {
		return teachers_departments;
	}

	public void setTeachers_departments(List<TeacherSubjectDepartmentEntity> teachers_departments) {
		this.teachers_departments = teachers_departments;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Integer getWeekClassesNumber() {
		return weekClassesNumber;
	}

	public void setWeekClassesNumber(Integer weekClassesNumber) {
		this.weekClassesNumber = weekClassesNumber;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public List<ClassSubjectEntity> getClasses() {
		return classes;
	}

	public void setClasses(List<ClassSubjectEntity> classes) {
		this.classes = classes;
	}

	public List<TeacherSubjectEntity> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<TeacherSubjectEntity> teachers) {
		this.teachers = teachers;
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

	public void setStatusInactive() {
		this.status = getStatusInactive();
	}

	public void setStatusActive() {
		this.status = getStatusActive();
	}

	public void setStatusArchived() {
		this.status = getStatusArchived();
	}

	/*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubjectEntity subject = (SubjectEntity) o;
        return Objects.equals(subjectName, subject.subjectName);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(subjectName);
    }*/
	
}
