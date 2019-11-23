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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.security.Views;

@Entity
@Table(name = "class_subject", uniqueConstraints=@UniqueConstraint(columnNames= {"class_id", "subject_id", "learning_program"}))
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ClassSubjectEntity {

	private static final Integer STATUS_INACTIVE = 0;
	private static final Integer STATUS_ACTIVE = 1;
	private static final Integer STATUS_ARCHIVED = -1;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.Student.class)
	@Column(name="class_subject_id")
	private Integer id;
	
	//@JsonIgnore
	@JsonView(Views.Student.class)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "class_id", nullable=false)
	@NotNull (message = "Class must be provided.")
	private ClassEntity clas;
	
	@JsonIgnore
	@JsonView(Views.Student.class)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "subject_id", nullable=false)
	@NotNull (message = "Subject must be provided.")
	private SubjectEntity subject;
	
	
	@JsonView(Views.Student.class)
	@Column(name="learning_program")
	@NotNull (message = "Learning program must be provided.")
	private String learningProgram;
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
	
	public ClassSubjectEntity() {
		super();
	}

	public ClassSubjectEntity(@NotNull(message = "Class must be provided.") ClassEntity clas,
			@NotNull(message = "Subject must be provided.") SubjectEntity subject,
			@NotNull(message = "Learning program must be provided.") String learningProgram,
			Integer createdById) {
		super();
		this.clas = clas;
		this.subject = subject;
		this.learningProgram = learningProgram;
		this.status = getStatusActive();
		this.createdById = createdById;
	}

	public ClassEntity getClas() {
		return clas;
	}

	public void setClas(ClassEntity clas) {
		this.clas = clas;
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

	public ClassEntity getClass_() {
		return clas;
	}

	public void setClass_(ClassEntity class_) {
		this.clas = class_;
	}

	public SubjectEntity getSubject() {
		return subject;
	}

	public void setSubject(SubjectEntity subject) {
		this.subject = subject;
	}

	public String getLearningProgram() {
		return learningProgram;
	}

	public void setLearningProgram(String learningProgram) {
		this.learningProgram = learningProgram;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	
	/*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
 
        if (o == null || getClass() != o.getClass())
            return false;
 
        ClassSubjectEntity that = (ClassSubjectEntity) o;
        return Objects.equals(class_, that.class_) &&
               Objects.equals(subject, that.subject);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(class_, subject);
    }*/

	
}
