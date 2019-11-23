package com.iktpreobuka.projekat_za_kraj.entities.dto;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.enumerations.EClass;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class StudentDto {
	
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
	@JsonView(Views.Parent.class)
	@Pattern(regexp = "^[0-9]{8,8}$", message="School identification number is not valid, must be 8 only numbers long.")
	//@Size(min=8, message = "School identification number must be {min} characters long.")
	private String schoolIdentificationNumber;
	@JsonView(Views.Parent.class)
	//@Pattern(regexp = "^([0][1-9]|[1|2][0-9]|[3][0|1])[./-]([0][1-9]|[1][0-2])[./-]([1-2][0-9]{3})$", message="Enrollment date is not valid, must be in dd-MM-yyyy format.")
	private String enrollmentDate;
	
	@JsonView(Views.Student.class)
	//@Pattern(regexp = "^(IV|V?I{1,4})$", message="Student class is not valid, must be I, II, III, IV, V, VI, VII or VIII.")
	private EClass student_class;
	@JsonView(Views.Student.class)
	@Pattern(regexp = "^([A-Za-z]{1,1})$", message="Student department is not valid, must be only one letter.")
	private String student_department;

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
	
	public StudentDto() {
		super();
	}

	public StudentDto(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid, can contain only letters and minimum is 2 letter.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid, can contain only letters and minimum is 2 letter.") String lastName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") @Size(min = 13, max = 13, message = "JMBG must be exactly {min} characters long.") String jMBG,
			@Pattern(regexp = "^(GENDER_MALE|GENDER_FEMALE)$", message = "Gender is not valid, must be GENDER_MALE or GENDER_FEMALE") String gender,
			@Pattern(regexp = "^[0-9]*$", message = "School identification number is not valid.") @Size(min = 8, message = "School identification number must be {min} characters long.") String schoolIdentificationNumber,
			@Pattern(regexp = "^([0][1-9]|[1|2][0-9]|[3][0|1])[./-]([0][1-9]|[1][0-2])[./-]([1-2][0-9]{3})$", message = "Enrollment date is not valid, must be in dd-MM-yyyy format.") String enrollmentDate,
			@Pattern(regexp = "^([A-Za-z]{1,1})$", message = "Student department is not valid, must be only one letter.") String student_department,
			//@Pattern(regexp = "^(IV|V?I{1,4})$", message = "Student class is not valid, must be I, II, III, IV, V, VI, VII or VIII.") 
			EClass student_class,
			@Size(min = 5, max = 20, message = "Username must be between {min} and {max} characters long.") String username,
			@Pattern(regexp = "^(ROLE_ADMIN|ROLE_TEACHER|ROLE_PARENT|ROLE_STUDENT)$", message = "Role is not valid, must be ROLE_ADMIN, ROLE_TEACHER, ROLE_PARENT or ROLE_STUDENT") String role,
			@Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String password,
			@Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String confirmedPassword) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.schoolIdentificationNumber = schoolIdentificationNumber;
		this.enrollmentDate = enrollmentDate;
		this.student_department = student_department;
		this.student_class = student_class;
		this.username = username;
		this.accessRole = role;
		this.password = password;
		this.confirmedPassword = confirmedPassword;
	}

	public StudentDto(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid, can contain only letters and minimum is 2 letter.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid, can contain only letters and minimum is 2 letter.") String lastName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") @Size(min = 13, max = 13, message = "JMBG must be exactly {min} characters long.") String jMBG,
			@Pattern(regexp = "^(GENDER_MALE|GENDER_FEMALE)$", message = "Gender is not valid, must be GENDER_MALE or GENDER_FEMALE") String gender,
			@Pattern(regexp = "^[0-9]*$", message = "School identification number is not valid.") @Size(min = 8, message = "School identification number must be {min} characters long.") String schoolIdentificationNumber,
			@Pattern(regexp = "^([0][1-9]|[1|2][0-9]|[3][0|1])[./-]([0][1-9]|[1][0-2])[./-]([1-2][0-9]{3})$", message = "Enrollment date is not valid, must be in dd-MM-yyyy format.") String enrollmentDate,
			@Size(min = 5, max = 20, message = "Username must be between {min} and {max} characters long.") String username,
			@Pattern(regexp = "^(ROLE_ADMIN|ROLE_TEACHER|ROLE_PARENT|ROLE_STUDENT)$", message = "Role is not valid, must be ROLE_ADMIN, ROLE_TEACHER, ROLE_PARENT or ROLE_STUDENT") String role,
			@Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String password,
			@Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String confirmedPassword) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.schoolIdentificationNumber = schoolIdentificationNumber;
		this.enrollmentDate = enrollmentDate;
		this.username = username;
		this.accessRole = role;
		this.password = password;
		this.confirmedPassword = confirmedPassword;
	}

	public StudentDto(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid, can contain only letters and minimum is 2 letter.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid, can contain only letters and minimum is 2 letter.") String lastName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") @Size(min = 13, max = 13, message = "JMBG must be exactly {min} characters long.") String jMBG,
			@Pattern(regexp = "^(GENDER_MALE|GENDER_FEMALE)$", message = "Gender is not valid, must be GENDER_MALE or GENDER_FEMALE") String gender,
			@Pattern(regexp = "^[0-9]*$", message = "School identification number is not valid.") @Size(min = 8, message = "School identification number must be {min} characters long.") String schoolIdentificationNumber,
			@Pattern(regexp = "^([0][1-9]|[1|2][0-9]|[3][0|1])[./-]([0][1-9]|[1][0-2])[./-]([1-2][0-9]{3})$", message = "Enrollment date is not valid, must be in dd-MM-yyyy format.") String enrollmentDate) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.schoolIdentificationNumber = schoolIdentificationNumber;
		this.enrollmentDate = enrollmentDate;
	}

	public StudentDto(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid, can contain only letters and minimum is 2 letter.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid, can contain only letters and minimum is 2 letter.") String lastName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") @Size(min = 13, max = 13, message = "JMBG must be exactly {min} characters long.") String jMBG,
			@Pattern(regexp = "^(GENDER_MALE|GENDER_FEMALE)$", message = "Gender is not valid, must be GENDER_MALE or GENDER_FEMALE") String gender,
			@Pattern(regexp = "^[0-9]*$", message = "School identification number is not valid.") @Size(min = 8, message = "School identification number must be {min} characters long.") String schoolIdentificationNumber,
			@Pattern(regexp = "^([0][1-9]|[1|2][0-9]|[3][0|1])[./-]([0][1-9]|[1][0-2])[./-]([1-2][0-9]{3})$", message = "Enrollment date is not valid, must be in dd-MM-yyyy format.") String enrollmentDate,
			@Pattern(regexp = "^([A-Za-z]{1,1})$", message = "Student department is not valid, must be only one letter.") String student_department,
			//@Pattern(regexp = "^(IV|V?I{1,4})$", message = "Student class is not valid, must be I, II, III, IV, V, VI, VII or VIII.") 
			EClass student_class) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.schoolIdentificationNumber = schoolIdentificationNumber;
		this.enrollmentDate = enrollmentDate;
		this.student_department = student_department;
		this.student_class = student_class;
	}

	public StudentDto(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid, can contain only letters and minimum is 2 letter.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid, can contain only letters and minimum is 2 letter.") String lastName,
			@Pattern(regexp = "^[0-9]*$", message = "School identification number is not valid.") @Size(min = 8, message = "School identification number must be {min} characters long.") String schoolIdentificationNumber,
			//@Pattern(regexp = "^(IV|V?I{1,4})$", message = "Student class is not valid, must be I, II, III, IV, V, VI, VII or VIII.") 
			EClass student_class,
			@Pattern(regexp = "^([A-Za-z]{1,1})$", message = "Student department is not valid, must be only one letter.") String student_department) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.schoolIdentificationNumber = schoolIdentificationNumber;
		this.student_class = student_class;
		this.student_department = student_department;
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

	public EClass getStudent_class() {
		return student_class;
	}

	public void setStudent_class(EClass student_class) {
		this.student_class = student_class;
	}

	public String getjMBG() {
		return jMBG;
	}

	public void setjMBG(String jMBG) {
		this.jMBG = jMBG;
	}

	public String getAccessRole() {
		return accessRole;
	}

	public void setAccessRole(String role) {
		this.accessRole = role;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSchoolIdentificationNumber() {
		return schoolIdentificationNumber;
	}

	public void setSchoolIdentificationNumber(String schoolIdentificationNumber) {
		this.schoolIdentificationNumber = schoolIdentificationNumber;
	}

	public String getEnrollmentDate() {
		return enrollmentDate;
	}

	public void setEnrollmentDate(String enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}

	public String getStudent_department() {
		return student_department;
	}

	public void setStudent_department(String student_department) {
		this.student_department = student_department;
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
