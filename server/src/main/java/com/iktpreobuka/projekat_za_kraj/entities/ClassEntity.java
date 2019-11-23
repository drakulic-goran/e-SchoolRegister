package com.iktpreobuka.projekat_za_kraj.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.enumerations.EClass;
import com.iktpreobuka.projekat_za_kraj.security.Views;

@Entity
@Table (name = "class"/*, uniqueConstraints=@UniqueConstraint(columnNames= {"class_label"})*/)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ClassEntity {
	
	private static final Integer STATUS_INACTIVE = 0;
	private static final Integer STATUS_ACTIVE = 1;
	private static final Integer STATUS_ARCHIVED = -1;
	
	@JsonIgnore
	@JsonView(Views.Teacher.class)
	@OneToMany(mappedBy = "clas", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH}, orphanRemoval = true)
	private List<ClassSubjectEntity> subjects = new ArrayList<>();
	
/*	@JsonIgnore
	@JsonView(Views.Teacher.class)
	@OneToMany(mappedBy = "class_department", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH}, orphanRemoval = true)
	private List<DepartmentEntity> departments = new ArrayList<>();
*/
	
	//@JsonIgnore - Skinuto zbog FE
	@JsonView(Views.Teacher.class)
	@OneToMany(mappedBy = "clas", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH}, orphanRemoval = true)
	private List<DepartmentClassEntity> departments = new ArrayList<>();
	
	@JsonView(Views.Teacher.class)
	@JsonIgnore
	@OneToMany(mappedBy = "teachingClass", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private List<TeacherSubjectDepartmentEntity> teachers_subjects_departments = new ArrayList<>();

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.Student.class)
	@Column(name="class_id")
	private Integer id;
	@JsonView(Views.Student.class)
	@Column(name="class_label", length=4)
	@Enumerated(EnumType.STRING)
	//@Pattern(regexp = "^(IV|V?I{1,2})$", message="Student class is not valid, must be I, II, III, IV, V, VI, VII or VIII.")
	@NotNull (message = "Class label must be provided.")
	private EClass classLabel;
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
	
	
	public ClassEntity() {
		super();
	}
	
	public ClassEntity(@NotNull(message = "Class label must be provided.") EClass classLabel, 
			Integer createdById) {
		super();
		this.classLabel = classLabel;
		this.status = getStatusActive();
		this.createdById = createdById;
	}


	public List<ClassSubjectEntity> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<ClassSubjectEntity> subjects) {
		this.subjects = subjects;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	
	public EClass getClassLabel() {
		return classLabel;
	}

	public void setClassLabel(EClass classLabel) {
		this.classLabel = classLabel;
	}

	public List<DepartmentClassEntity> getDepartments() {
		return departments;
	}

	public void setDepartments(List<DepartmentClassEntity> departments) {
		this.departments = departments;
	}

	public List<TeacherSubjectDepartmentEntity> getTeachers_subjects_departments() {
		return teachers_subjects_departments;
	}

	public void setTeachers_subjects_departments(List<TeacherSubjectDepartmentEntity> teachers_subjects_departments) {
		this.teachers_subjects_departments = teachers_subjects_departments;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public void addSubject(SubjectEntity subject, Date assignmentDate, String learningProgram, Integer loggedUserId) {
		ClassSubjectEntity classSubject = new ClassSubjectEntity(this, subject, learningProgram, loggedUserId);
        subjects.add(classSubject);
        subject.getClasses().add(classSubject);
    }
	
    public void removeSubject(SubjectEntity subject) {
        for (Iterator<ClassSubjectEntity> iterator = subjects.iterator();
             iterator.hasNext(); ) {
        	ClassSubjectEntity classSubject = iterator.next();
 
            if (classSubject.getClass_().equals(this) &&
                    classSubject.getSubject().equals(subject)) {
                iterator.remove();
                classSubject.getSubject().getClasses().remove(classSubject);
                classSubject.setClass_(null);
                classSubject.setSubject(null);
            }
        }
    }
 
    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
 
        if (o == null || getClass() != o.getClass())
            return false;
 
        ClassEntity class_ = (ClassEntity) o;
        return Objects.equals(this.getClassLabel(), class_.getClassLabel());
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(this.getClassLabel());
    }*/

}
