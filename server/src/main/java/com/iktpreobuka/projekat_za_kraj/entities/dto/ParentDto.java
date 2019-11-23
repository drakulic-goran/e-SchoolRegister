package com.iktpreobuka.projekat_za_kraj.entities.dto;

import java.util.List;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class ParentDto {
	
	@JsonView(Views.Student.class)
	@Pattern(regexp = "^[A-Za-z]{2,}$", message="First name is not valid, can contain only letters and minimum is 2 letter.")
	private String firstName;
	@JsonView(Views.Student.class)
	@Pattern(regexp = "^[A-Za-z]{2,}$", message="Last name is not valid, can contain only letters and minimum is 2 letter.")
	private String lastName;
	@JsonView(Views.Admin.class)
	@Pattern(regexp = "^[0-9]{13,13}$", message="JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.")
	//@Size(min=13, max=13, message = "JMBG must be exactly {min} characters long.")
	private String jMBG;
	@JsonView(Views.Admin.class)
	@Pattern(regexp="^(GENDER_MALE|GENDER_FEMALE)$",message="Gender is not valid, must be GENDER_MALE or GENDER_FEMALE")
	private String gender;
	@JsonView(Views.Teacher.class)
	@Size(max=50, message = "E-mail must be maximum {max} characters long.")
	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message="Email is not valid.")
	private String email;

	@JsonView(Views.Admin.class)
	//@Pattern(regexp = "^[0-9]+$", message="Students Id is not valid, can contain only numbers and minimum 1 number.")
	private List<String> students;
	
	@JsonView(Views.Admin.class)
	@Size(min=5, max=20, message = "Username must be between {min} and {max} characters long.")
	private String username;
	@JsonView(Views.Admin.class)
	@Pattern(regexp="^(ROLE_ADMIN|ROLE_TEACHER|ROLE_PARENT|ROLE_STUDENT)$",message="Role is not valid, must be ROLE_ADMIN, ROLE_TEACHER, ROLE_PARENT or ROLE_STUDENT")
	private String accessRole;
	@Size(min=5, message = "Password must be {min} characters long or higher.")
	@Pattern(regexp = "^[A-Za-z0-9]*$", message="Password is not valid, must contin only letters and numbers.")
	private String password;
	@Size(min=5, message = "Password must be {min} characters long or higher.")
	@Pattern(regexp = "^[A-Za-z0-9]*$", message="Password is not valid, must contin only letters and numbers.")
	private String confirmedPassword;
	
	public ParentDto() {
		super();
	}

	public ParentDto(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid, can contain only letters and minimum is 2 letter.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid, can contain only letters and minimum is 2 letter.") String lastName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") @Size(min = 13, max = 13, message = "JMBG must be exactly {min} characters long.") String jMBG,
			@Pattern(regexp = "^(GENDER_MALE|GENDER_FEMALE)$", message = "Gender is not valid, must be GENDER_MALE or GENDER_FEMALE") String gender,
			@Size(max = 50, message = "E-mail must be maximum {max} characters long.") @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email is not valid.") String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.email = email;
	}

	public ParentDto(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid, can contain only letters and minimum is 2 letter.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid, can contain only letters and minimum is 2 letter.") String lastName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") @Size(min = 13, max = 13, message = "JMBG must be exactly {min} characters long.") String jMBG,
			@Pattern(regexp = "^(GENDER_MALE|GENDER_FEMALE)$", message = "Gender is not valid, must be GENDER_MALE or GENDER_FEMALE") String gender,
			@Size(max = 50, message = "E-mail must be maximum {max} characters long.") @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email is not valid.") String email,
			@Size(min = 5, max = 20, message = "Username must be between {min} and {max} characters long.") String username,
			@Pattern(regexp = "^(ROLE_ADMIN|ROLE_TEACHER|ROLE_PARENT|ROLE_STUDENT)$", message = "Role is not valid, must be ROLE_ADMIN, ROLE_TEACHER, ROLE_PARENT or ROLE_STUDENT") String role,
			@Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String password,
			@Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String confirmedPassword) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.email = email;
		this.username = username;
		this.accessRole = role;
		this.password = password;
		this.confirmedPassword = confirmedPassword;
	}

	public ParentDto(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid, can contain only letters and minimum is 2 letter.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid, can contain only letters and minimum is 2 letter.") String lastName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") @Size(min = 13, max = 13, message = "JMBG must be exactly {min} characters long.") String jMBG,
			@Pattern(regexp = "^(GENDER_MALE|GENDER_FEMALE)$", message = "Gender is not valid, must be GENDER_MALE or GENDER_FEMALE") String gender,
			@Size(max = 50, message = "E-mail must be maximum {max} characters long.") @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email is not valid.") String email,
			//@Pattern(regexp = "^[0-9]{13,13}$", message = "Students JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") 
			List<String> students,
			@Size(min = 5, max = 20, message = "Username must be between {min} and {max} characters long.") String username,
			@Pattern(regexp = "^(ROLE_ADMIN|ROLE_TEACHER|ROLE_PARENT|ROLE_STUDENT)$", message = "Role is not valid, must be ROLE_ADMIN, ROLE_TEACHER, ROLE_PARENT or ROLE_STUDENT") String role,
			@Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String password,
			@Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String confirmedPassword) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.email = email;
		this.students = students;
		this.username = username;
		this.accessRole = role;
		this.password = password;
		this.confirmedPassword = confirmedPassword;
	}

	public ParentDto(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid, can contain only letters and minimum is 2 letter.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid, can contain only letters and minimum is 2 letter.") String lastName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") @Size(min = 13, max = 13, message = "JMBG must be exactly {min} characters long.") String jMBG,
			@Pattern(regexp = "^(GENDER_MALE|GENDER_FEMALE)$", message = "Gender is not valid, must be GENDER_MALE or GENDER_FEMALE") String gender,
			@Size(max = 50, message = "E-mail must be maximum {max} characters long.") @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email is not valid.") String email,
			//@Pattern(regexp = "^[0-9]{13,13}$", message = "Students JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") 
			List<String> students) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.email = email;
		this.students = students;
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

	public String getAccessRole() {
		return accessRole;
	}

	public void setAccessRole(String role) {
		this.accessRole = role;
	}

	public List<String> getStudents() {
		return students;
	}

	public void setStudents(List<String> students) {
		this.students = students;
	}

	public String getjMBG() {
		return jMBG;
	}

	public void setjMBG(String jMBG) {
		this.jMBG = jMBG;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmedPassword() {
		return confirmedPassword;
	}

	public void setConfirmedPassword(String confirmedPassword) {
		this.confirmedPassword = confirmedPassword;
	}

}
