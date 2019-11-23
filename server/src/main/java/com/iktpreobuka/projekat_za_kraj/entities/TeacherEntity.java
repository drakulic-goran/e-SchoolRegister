package com.iktpreobuka.projekat_za_kraj.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.security.Views;

@Entity
@Table(name = "teacher")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
//@DiscriminatorValue("teacher") 
public class TeacherEntity extends UserEntity {
	
	private static final Integer STATUS_INACTIVE = 0;
	private static final Integer STATUS_ACTIVE = 1;
	private static final Integer STATUS_ARCHIVED = -1;

	@JsonView(Views.Teacher.class)
	//@JsonIgnore - Skinuto zbog FE
	@OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH}, orphanRemoval = true)
	private List<TeacherSubjectEntity> subjects = new ArrayList<>();
	
	@JsonView(Views.Teacher.class)
	//@JsonIgnore - Skinuto zbog FE
	@OneToMany(mappedBy = "primaryTeacher", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private List<PrimaryTeacherDepartmentEntity> departments = new ArrayList<>();
	
	@JsonView(Views.Teacher.class)
	@JsonIgnore
	@OneToMany(mappedBy = "teachingTeacher", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private List<TeacherSubjectDepartmentEntity> subjects_departments = new ArrayList<>();

	
	@JsonView(Views.Teacher.class)
	@Column(name="certificate")
	@NotNull (message = "Certificate must be provided.")
	private String certificate;
	@JsonView(Views.Teacher.class)
	//@Pattern(regexp = "^([1-2][0-9]{3})[./-]([0][1-9]|[1][0-2])[./-]([0][1-9]|[1|2][0-9]|[3][0|1])$", message="Employment date is not valid, must be in yyyy-MM-dd format.")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Column(name="employment_date")
	@NotNull (message = "Employment date must be provided.")
	private Date employmentDate;
	@JsonView(Views.Admin.class)
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


	public TeacherEntity() {
		super();
	}

	public TeacherEntity(List<TeacherSubjectEntity> subjects,
			@NotNull(message = "Certificate must be provided.") String certificate,
			@NotNull(message = "Employment date must be provided.") Date employmentDate, Integer createdById) {
		super();
		this.subjects = subjects;
		this.certificate = certificate;
		this.employmentDate = employmentDate;
		this.status = getStatusActive();
		this.createdById = createdById;
	}

	public TeacherEntity(@NotNull(message = "Certificate must be provided.") String certificate,
			@NotNull(message = "Employment date must be provided.") Date employmentDate, Integer createdById) {
		super();
		this.certificate = certificate;
		this.employmentDate = employmentDate;
		this.status = getStatusActive();
		this.createdById = createdById;
	}

	public TeacherEntity(List<TeacherSubjectEntity> subjects, List<PrimaryTeacherDepartmentEntity> departments,
			@NotNull(message = "Certificate must be provided.") String certificate,
			@NotNull(message = "Employment date must be provided.") Date employmentDate, Integer createdById) {
		super();
		this.subjects = subjects;
		this.departments = departments;
		this.certificate = certificate;
		this.employmentDate = employmentDate;
		this.status = getStatusActive();
		this.createdById = createdById;
	}

	public TeacherEntity(@NotNull(message = "Certificate must be provided.") String certificate,
			@NotNull(message = "Employment date must be provided.") Date employmentDate, 
			List<PrimaryTeacherDepartmentEntity> departments,
			Integer createdById) {
		super();
		this.departments = departments;
		this.certificate = certificate;
		this.employmentDate = employmentDate;
		this.status = getStatusActive();
		this.createdById = createdById;
	}

	public List<TeacherSubjectEntity> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<TeacherSubjectEntity> subjects) {
		this.subjects = subjects;
	}
	
	public List<TeacherSubjectDepartmentEntity> getSubjects_departments() {
		return subjects_departments;
	}

	public void setSubjects_departments(List<TeacherSubjectDepartmentEntity> subjects_departments) {
		this.subjects_departments = subjects_departments;
	}

	public List<PrimaryTeacherDepartmentEntity> getDepartments() {
		return departments;
	}

	public void setDepartments(List<PrimaryTeacherDepartmentEntity> teachers) {
		this.departments = teachers;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public Date getEmploymentDate() {
		return employmentDate;
	}

	public void setEmploymentDate(Date employmentDate) {
		this.employmentDate = employmentDate;
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


	public void addSubject(SubjectEntity subject, Date assignmentDate, Integer loggedUserId) {
        TeacherSubjectEntity teacherSubject = new TeacherSubjectEntity(this, subject, assignmentDate, loggedUserId);
        subjects.add(teacherSubject);
        subject.getTeachers().add(teacherSubject);
    }
	
    public void removeSubject(SubjectEntity subject) {
        for (Iterator<TeacherSubjectEntity> iterator = subjects.iterator();
             iterator.hasNext(); ) {
        	TeacherSubjectEntity teacherSubject = iterator.next();
 
            if (teacherSubject.getTeacher().equals(this) &&
                    teacherSubject.getSubject().equals(subject)) {
                iterator.remove();
                teacherSubject.getSubject().getTeachers().remove(teacherSubject);
                teacherSubject.setTeacher(null);
                teacherSubject.setSubject(null);
            }
        }
    }
 
   /* @Override
    public boolean equals(Object o) {
        if (this == o) return true;
 
        if (o == null || getClass() != o.getClass())
            return false;
 
        UserEntity teacher = (TeacherEntity) o;
        return Objects.equals(this.getjMBG(), teacher.getjMBG());
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(this.getjMBG());
    }*/
	
	 
}
