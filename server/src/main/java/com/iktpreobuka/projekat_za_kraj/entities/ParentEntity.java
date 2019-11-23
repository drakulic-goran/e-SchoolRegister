package com.iktpreobuka.projekat_za_kraj.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.security.Views;

@Entity
@Table(name = "parent")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
//@DiscriminatorValue("parent") 
public class ParentEntity extends UserEntity {
	
	private static final Integer STATUS_INACTIVE = 0;
	private static final Integer STATUS_ACTIVE = 1;
	private static final Integer STATUS_ARCHIVED = -1;

	@JsonView(Views.Parent.class)
	//@JsonIgnore - Skinuto zbog FE
	//@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    //@JoinTable(name = "parent_student", joinColumns = { @JoinColumn(name = "parent_id") }, inverseJoinColumns = { @JoinColumn(name = "student_id") })
	@ManyToMany(mappedBy = "parents", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private Set<StudentEntity> students = new HashSet<>();

	@JsonView(Views.Parent.class)
	@Column(name="e_mail", unique=true, length=50)
	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message="Email is not valid.")
	@NotNull (message = "E-mail must be provided.")
	private String email;
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
	
	public ParentEntity() {
		super();
	}


	public ParentEntity(
			@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email is not valid.") @NotNull(message = "E-mail must be provided.") String email,
			Integer createdById) {
		super();
		this.email = email;
		this.status = getStatusActive();
		this.createdById = createdById;
	}

	public ParentEntity(Set<StudentEntity> students,
			@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email is not valid.") @NotNull(message = "E-mail must be provided.") String email,
			Integer createdById) {
		super();
		this.students = students;
		this.email = email;
		this.status = getStatusActive();
		this.createdById = createdById;
	}

	
	public Set<StudentEntity> getStudents() {
		return students;
	}

	public void setStudents(Set<StudentEntity> students) {
		this.students = students;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
