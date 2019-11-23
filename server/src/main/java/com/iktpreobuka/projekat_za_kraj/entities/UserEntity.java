package com.iktpreobuka.projekat_za_kraj.entities;

import java.util.ArrayList;
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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.DiscriminatorOptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.enumerations.EGender;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;
import com.iktpreobuka.projekat_za_kraj.security.Views;

/*@Entity
@Table (name = "user", uniqueConstraints=@UniqueConstraint(columnNames= {"jmbg"}))
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)  
@DiscriminatorColumn(name="type",discriminatorType=DiscriminatorType.STRING)  
@DiscriminatorValue(value="user")  */
/*@MappedSuperclass
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })*/
@Entity
@Table (name = "user"/*, uniqueConstraints=@UniqueConstraint(columnNames= {"jmbg", "role"})*/)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorOptions(force = true)
public class UserEntity {
	
	/*private static final Integer STATUS_INACTIVE = 0;
	private static final Integer STATUS_ACTIVE = 1;
	private static final Integer STATUS_ARCHIVED = -1;*/
    
	@JsonView(Views.Admin.class)
	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	//@JsonManagedReference
	//@JsonBackReference
	private List<UserAccountEntity> accounts = new ArrayList<>();
	/*@JsonView(Views.Admin.class)
	@OneToOne
	@JoinColumn(name="user_account")
    protected UserAccountEntity userAccount;*/
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.Student.class)
	@Column(name="user_id")
	protected Integer id;
	@JsonView(Views.Student.class)
	@Column(name="first_name")
	@Pattern(regexp = "^[A-Za-z]{2,}$", message="First name is not valid.")
	@NotNull (message = "First name must be provided.")
	protected String firstName;
	@JsonView(Views.Student.class)
	@Column(name="last_name")
	@Pattern(regexp = "^[A-Za-z]{2,}$", message="Last name is not valid.")
	@NotNull (message = "Last name must be provided.")
	protected String lastName;
	@JsonView(Views.Student.class)
	@Column(name="jmbg", unique=true, length=13, nullable=false)
	@Pattern(regexp = "^[0-9]{13,13}$", message="JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.")
	//@Size(min=13, max=13, message = "JMBG must be {min} characters long.")
	@NotNull (message = "JMBG must be provided.")
	protected String jMBG;
	@JsonView(Views.Student.class)
	@Column(name="gender")
	@Enumerated(EnumType.STRING)
	@NotNull (message = "Gender must be provided.")
	protected EGender gender;
	@JsonView(Views.Admin.class)
	@Column(name="role", nullable=false)
	@Enumerated(EnumType.STRING)
	@NotNull (message = "User role must be provided.")
	protected EUserRole role;
	@JsonIgnore
	@Version
	protected Integer version;
	
	
	public UserEntity() { 
		super();
	}

	public UserEntity(Integer id,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid.") @NotNull(message = "First name must be provided.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid.") @NotNull(message = "Last name must be provided.") String lastName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") @NotNull(message = "JMBG must be provided.") String jMBG,
			@NotNull(message = "Gender must be provided.") EGender gender,
			@NotNull(message = "User role must be provided.") EUserRole role) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.role = role;
	}

	public UserEntity(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid.") @NotNull(message = "First name must be provided.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid.") @NotNull(message = "Last name must be provided.") String lastName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") @NotNull(message = "JMBG must be provided.") String jMBG,
			@NotNull(message = "Gender must be provided.") EGender gender,
			@NotNull(message = "User role must be provided.") EUserRole role) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.role = role;
	}

	/*public UserEntity(@NotNull(message = "First name must be provided.") String firstName,
			@NotNull(message = "Last name must be provided.") String lastName,
			@Pattern(regexp = "^[0-9]*$", message = "JMBG is not valid.") @Size(min = 13, message = "JMBG must be {min} characters long.") @NotNull(message = "JMBG must be provided.") String jMBG,
			@NotNull(message = "Gender must be provided.") EGender gender,
			@NotNull(message = "User role must be provided.") EUserRole role,
			Integer createdById) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.role = role;
		this.status = getStatusActive();
		this.createdById = createdById;
	}

	public UserEntity(Integer id,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid.") @NotNull(message = "First name must be provided.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid.") @NotNull(message = "Last name must be provided.") String lastName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") @NotNull(message = "JMBG must be provided.") String jMBG,
			@NotNull(message = "Gender must be provided.") EGender gender,
			@NotNull(message = "User role must be provided.") EUserRole role, @Max(1) @Min(-1) Integer status,
			Integer createdById, Integer updatedById, Integer version) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.role = role;
		this.status = status;
		this.createdById = createdById;
		this.updatedById = updatedById;
		this.version = version;
	}
	
	public UserEntity(Integer id,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid.") @NotNull(message = "First name must be provided.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid.") @NotNull(message = "Last name must be provided.") String lastName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") @NotNull(message = "JMBG must be provided.") String jMBG,
			@NotNull(message = "Gender must be provided.") String gender,
			@NotNull(message = "User role must be provided.") String role, @Max(1) @Min(-1) Integer status,
			Integer createdById, Integer updatedById, Integer version) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = EGender.valueOf(gender);
		this.role = EUserRole.valueOf(role);
		this.status = status;
		this.createdById = createdById;
		this.updatedById = updatedById;
		this.version = version;
	}*/

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public EGender getGender() {
		return gender;
	}

	public void setGender(EGender gender) {
		this.gender = gender;
	}

	public String getjMBG() {
		return jMBG;
	}

	public void setjMBG(String jMBG) {
		this.jMBG = jMBG;
	}

	public EUserRole getRole() {
		return role;
	}

	public List<UserAccountEntity> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<UserAccountEntity> accounts) {
		this.accounts = accounts;
	}

	public void setRole(EUserRole role) {
		this.role = role;
	}

	/*public Integer getStatus() {
		return status;
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
	
	public void setStatusInactive() {
		this.status = getStatusInactive();
	}

	public void setStatusActive() {
		this.status = getStatusActive();
	}

	public void setStatusArchived() {
		this.status = getStatusArchived();
	}*/

}
