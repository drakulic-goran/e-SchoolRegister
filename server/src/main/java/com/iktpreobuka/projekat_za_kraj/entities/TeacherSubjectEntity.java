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
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.security.Views;

@Entity
@Table(name = "teacher_subject", uniqueConstraints=@UniqueConstraint(columnNames= {"teacher_id", "subject_id", "assignment_date"}))
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class TeacherSubjectEntity {
	
	private static final Integer STATUS_INACTIVE = 0;
	private static final Integer STATUS_ACTIVE = 1;
	private static final Integer STATUS_ARCHIVED = -1;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.Admin.class)
	@Column(name="teacher_subject_id")
	private Integer id;

	@JsonIgnore
	@JsonView(Views.Admin.class)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "teacher_id", nullable=false)
	@NotNull (message = "Teacher must be provided.")
	private TeacherEntity teacher;
	
	//@JsonIgnore - Skinuto zbog FE
	@JsonView(Views.Teacher.class)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "subject_id", nullable=false)
	@NotNull (message = "Subject must be provided.")
	private SubjectEntity subject;

	
	@JsonView(Views.Teacher.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@NotNull (message = "Assignment date must be provided.")
	@Column(name="assignment_date")
	private Date assignmentDate;
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
	
	public TeacherSubjectEntity() {
		super();
	}

	public TeacherSubjectEntity(@NotNull(message = "Teacher must be provided.") TeacherEntity teacher,
			@NotNull(message = "Subject must be provided.") SubjectEntity subject,
			@NotNull(message = "Assignment date must be provided.") Date assignmentDate,
			Integer createdById) {
		super();
		this.teacher = teacher;
		this.subject = subject;
		this.assignmentDate = assignmentDate;
		this.status = getStatusActive();
		this.createdById = createdById;
	}

	public TeacherEntity getTeacher() {
		return teacher;
	}

	public void setTeacher(TeacherEntity teacher) {
		this.teacher = teacher;
	}

	public SubjectEntity getSubject() {
		return subject;
	}

	public void setSubject(SubjectEntity subject) {
		this.subject = subject;
	}

	public Date getAssignmentDate() {
		return assignmentDate;
	}

	public void setAssignmentDate(Date assignmentDate) {
		this.assignmentDate = assignmentDate;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public static Integer getStatusInactive() {
		return STATUS_INACTIVE;
	}

	public static Integer getStatusActive() {
		return STATUS_ACTIVE;
	}

	public static Integer getStatusArchived() {
		return STATUS_ARCHIVED;
	}
	

	/*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
 
        if (o == null || getClass() != o.getClass())
            return false;
 
        TeacherSubjectEntity that = (TeacherSubjectEntity) o;
        return Objects.equals(teacher, that.teacher) &&
               Objects.equals(subject, that.subject);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(teacher, subject);
    }*/

}
