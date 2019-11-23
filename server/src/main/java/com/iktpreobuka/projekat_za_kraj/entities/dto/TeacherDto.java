package com.iktpreobuka.projekat_za_kraj.entities.dto;

import java.util.List;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class TeacherDto {
	
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
	private String certificate;
	@JsonView(Views.Teacher.class)
	@Pattern(regexp = "^([1-2][0-9]{3})[./-]([0][1-9]|[1][0-2])[./-]([0][1-9]|[1|2][0-9]|[3][0|1])$", message="Employment date is not valid, must be in yyyy-MM-dd format.")
	private String employmentDate;
	
	@JsonView(Views.Teacher.class)
	//@Pattern(regexp = "^[0-9]+$", message="Subjects Id is not valid, can contain only numbers and minimum 1 number.")
	private List<String> subjects;
	@JsonView(Views.Student.class)
	@Pattern(regexp = "^[0-9]+$", message="Department Id is not valid, can contain only numbers and minimum 1 number.")
	private String primaryDepartment;
	/*@JsonView(Views.Student.class)
	@Pattern(regexp = "^(IV|V?I{1,2})$", message="Student class is not valid, must be I, II, III, IV, V, VI, VII or VIII.")
	private String primaryClass;
	@JsonView(Views.Teacher.class)
	//@Pattern(regexp = "^[0-9]+$", message="Subjects in departments Id is not valid, can contain only numbers and minimum 1 number.")
	private List<Pair<String, String>> subject_at_department;*/
	@JsonView(Views.Teacher.class)
	private String teachingDepartment;
	@JsonView(Views.Teacher.class)
	private List<String> teachingDepartments;
	@JsonView(Views.Teacher.class)
	private String teachingSubject;
	@JsonView(Views.Teacher.class)
	@Pattern(regexp = "^([2-9][0-9])[0-9]{2}\\-([2-9][0-9])[0-9]{2}$", message="School year is not valid, must be in format YYYY-YYYY.")
	private String schoolYear;

	
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

	public TeacherDto() {
		super();
	}

	public TeacherDto(List<String> teachingDepartments, String teachingSubject,
			@Pattern(regexp = "^([2-9][0-9])[0-9]{2}\\-([2-9][0-9])[0-9]{2}$", message = "School year is not valid, must be in format YYYY-YYYY.") String schoolYear) {
		super();
		this.teachingDepartments = teachingDepartments;
		this.teachingSubject = teachingSubject;
		this.schoolYear = schoolYear;
	}

	public TeacherDto(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid, can contain only letters and minimum is 2 letter.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid, can contain only letters and minimum is 2 letter.") String lastName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") @Size(min = 13, max = 13, message = "JMBG must be exactly {min} characters long.") String jMBG,
			@Pattern(regexp="^(GENDER_MALE|GENDER_FEMALE)$",message="Gender is not valid, must be GENDER_MALE or GENDER_FEMALE") String gender, 
			String certificate, 
			@Pattern(regexp = "^([0][1-9]|[1|2][0-9]|[3][0|1])[./-]([0][1-9]|[1][0-2])[./-]([1-2][0-9]{3})$", message="Employment date is not valid, must be in dd-MM-yyyy format.") String employmentDate) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.certificate = certificate;
		this.employmentDate = employmentDate;
	}

	public TeacherDto(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid, can contain only letters and minimum is 2 letter.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid, can contain only letters and minimum is 2 letter.") String lastName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") @Size(min = 13, max = 13, message = "JMBG must be exactly {min} characters long.") String jMBG,
			@Pattern(regexp = "^(GENDER_MALE|GENDER_FEMALE)$", message = "Gender is not valid, must be GENDER_MALE or GENDER_FEMALE") String gender,
			String certificate,
			@Pattern(regexp = "^([0][1-9]|[1|2][0-9]|[3][0|1])[./-]([0][1-9]|[1][0-2])[./-]([1-2][0-9]{3})$", message = "Employment date is not valid, must be in dd-MM-yyyy format.") String employmentDate,
			@Size(min = 5, max = 20, message = "Username must be between {min} and {max} characters long.") String username,
			@Pattern(regexp = "^(ROLE_ADMIN|ROLE_TEACHER|ROLE_PARENT|ROLE_STUDENT)$", message = "Role is not valid, must be ROLE_ADMIN, ROLE_TEACHER, ROLE_PARENT or ROLE_STUDENT") String role,
			@Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String password,
			@Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String confirmedPassword) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.certificate = certificate;
		this.employmentDate = employmentDate;
		this.username = username;
		this.accessRole = role;
		this.password = password;
		this.confirmedPassword = confirmedPassword;
	}

	public TeacherDto(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid, can contain only letters and minimum is 2 letter.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid, can contain only letters and minimum is 2 letter.") String lastName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") String jMBG,
			@Pattern(regexp = "^(GENDER_MALE|GENDER_FEMALE)$", message = "Gender is not valid, must be GENDER_MALE or GENDER_FEMALE") String gender,
			String certificate,
			@Pattern(regexp = "^([0][1-9]|[1|2][0-9]|[3][0|1])[./-]([0][1-9]|[1][0-2])[./-]([1-2][0-9]{3})$", message = "Employment date is not valid, must be in dd-MM-yyyy format.") String employmentDate,
			List<String> subjects) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.certificate = certificate;
		this.employmentDate = employmentDate;
		this.subjects = subjects;
	}

	public TeacherDto(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid, can contain only letters and minimum is 2 letter.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid, can contain only letters and minimum is 2 letter.") String lastName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") String jMBG,
			@Pattern(regexp = "^(GENDER_MALE|GENDER_FEMALE)$", message = "Gender is not valid, must be GENDER_MALE or GENDER_FEMALE") String gender,
			String certificate,
			@Pattern(regexp = "^([0][1-9]|[1|2][0-9]|[3][0|1])[./-]([0][1-9]|[1][0-2])[./-]([1-2][0-9]{3})$", message = "Employment date is not valid, must be in dd-MM-yyyy format.") String employmentDate,
			List<String> subjects,
			@Size(min = 5, max = 20, message = "Username must be between {min} and {max} characters long.") String username,
			@Pattern(regexp = "^(ROLE_ADMIN|ROLE_TEACHER|ROLE_PARENT|ROLE_STUDENT)$", message = "Role is not valid, must be ROLE_ADMIN, ROLE_TEACHER, ROLE_PARENT or ROLE_STUDENT") String accessRole,
			@Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String password,
			@Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String confirmedPassword) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.certificate = certificate;
		this.employmentDate = employmentDate;
		this.subjects = subjects;
		this.username = username;
		this.accessRole = accessRole;
		this.password = password;
		this.confirmedPassword = confirmedPassword;
	}

	public TeacherDto(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid, can contain only letters and minimum is 2 letter.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid, can contain only letters and minimum is 2 letter.") String lastName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") String jMBG,
			@Pattern(regexp = "^(GENDER_MALE|GENDER_FEMALE)$", message = "Gender is not valid, must be GENDER_MALE or GENDER_FEMALE") String gender,
			String certificate,
			@Pattern(regexp = "^([0][1-9]|[1|2][0-9]|[3][0|1])[./-]([0][1-9]|[1][0-2])[./-]([1-2][0-9]{3})$", message = "Employment date is not valid, must be in dd-MM-yyyy format.") String employmentDate,
			@Pattern(regexp = "^[0-9]+$", message = "Department Id is not valid, can contain only numbers and minimum 1 number.") String primaryDepartment) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.certificate = certificate;
		this.employmentDate = employmentDate;
		this.primaryDepartment = primaryDepartment;
	}

	public TeacherDto(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid, can contain only letters and minimum is 2 letter.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid, can contain only letters and minimum is 2 letter.") String lastName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") String jMBG,
			@Pattern(regexp = "^(GENDER_MALE|GENDER_FEMALE)$", message = "Gender is not valid, must be GENDER_MALE or GENDER_FEMALE") String gender,
			String certificate,
			@Pattern(regexp = "^([0][1-9]|[1|2][0-9]|[3][0|1])[./-]([0][1-9]|[1][0-2])[./-]([1-2][0-9]{3})$", message = "Employment date is not valid, must be in dd-MM-yyyy format.") String employmentDate,
			@Pattern(regexp = "^[0-9]+$", message = "Department Id is not valid, can contain only numbers and minimum 1 number.") String primaryDepartment,
			@Size(min = 5, max = 20, message = "Username must be between {min} and {max} characters long.") String username,
			@Pattern(regexp = "^(ROLE_ADMIN|ROLE_TEACHER|ROLE_PARENT|ROLE_STUDENT)$", message = "Role is not valid, must be ROLE_ADMIN, ROLE_TEACHER, ROLE_PARENT or ROLE_STUDENT") String accessRole,
			@Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String password,
			@Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String confirmedPassword) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.certificate = certificate;
		this.employmentDate = employmentDate;
		this.primaryDepartment = primaryDepartment;
		this.username = username;
		this.accessRole = accessRole;
		this.password = password;
		this.confirmedPassword = confirmedPassword;
	}

	public TeacherDto(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid, can contain only letters and minimum is 2 letter.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid, can contain only letters and minimum is 2 letter.") String lastName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") String jMBG,
			@Pattern(regexp = "^(GENDER_MALE|GENDER_FEMALE)$", message = "Gender is not valid, must be GENDER_MALE or GENDER_FEMALE") String gender,
			String certificate,
			@Pattern(regexp = "^([0][1-9]|[1|2][0-9]|[3][0|1])[./-]([0][1-9]|[1][0-2])[./-]([1-2][0-9]{3})$", message = "Employment date is not valid, must be in dd-MM-yyyy format.") String employmentDate,
			String teachingDepartment, String teachingSubject,
			@Pattern(regexp = "^(20|[3-9][0-9])\\d{2}\\-(20|[3-9][0-9])\\\\d{2}$", message = "School year is not valid, must be in format YYYY-YYYY.") String schoolYear) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.certificate = certificate;
		this.employmentDate = employmentDate;
		this.teachingDepartment = teachingDepartment;
		this.teachingSubject = teachingSubject;
		this.schoolYear = schoolYear;
	}

	public TeacherDto(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid, can contain only letters and minimum is 2 letter.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid, can contain only letters and minimum is 2 letter.") String lastName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") String jMBG,
			@Pattern(regexp = "^(GENDER_MALE|GENDER_FEMALE)$", message = "Gender is not valid, must be GENDER_MALE or GENDER_FEMALE") String gender,
			String certificate,
			@Pattern(regexp = "^([0][1-9]|[1|2][0-9]|[3][0|1])[./-]([0][1-9]|[1][0-2])[./-]([1-2][0-9]{3})$", message = "Employment date is not valid, must be in dd-MM-yyyy format.") String employmentDate,
			String teachingDepartment, String teachingSubject,
			@Pattern(regexp = "^(20|[3-9][0-9])\\d{2}\\-(20|[3-9][0-9])\\\\d{2}$", message = "School year is not valid, must be in format YYYY-YYYY.") String schoolYear,
			@Size(min = 5, max = 20, message = "Username must be between {min} and {max} characters long.") String username,
			@Pattern(regexp = "^(ROLE_ADMIN|ROLE_TEACHER|ROLE_PARENT|ROLE_STUDENT)$", message = "Role is not valid, must be ROLE_ADMIN, ROLE_TEACHER, ROLE_PARENT or ROLE_STUDENT") String accessRole,
			@Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String password,
			@Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String confirmedPassword) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.certificate = certificate;
		this.employmentDate = employmentDate;
		this.teachingDepartment = teachingDepartment;
		this.teachingSubject = teachingSubject;
		this.schoolYear = schoolYear;
		this.username = username;
		this.accessRole = accessRole;
		this.password = password;
		this.confirmedPassword = confirmedPassword;
	}

	public TeacherDto(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid, can contain only letters and minimum is 2 letter.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid, can contain only letters and minimum is 2 letter.") String lastName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") String jMBG,
			@Pattern(regexp = "^(GENDER_MALE|GENDER_FEMALE)$", message = "Gender is not valid, must be GENDER_MALE or GENDER_FEMALE") String gender,
			String certificate,
			@Pattern(regexp = "^([0][1-9]|[1|2][0-9]|[3][0|1])[./-]([0][1-9]|[1][0-2])[./-]([1-2][0-9]{3})$", message = "Employment date is not valid, must be in dd-MM-yyyy format.") String employmentDate,
			List<String> subjects,
			@Pattern(regexp = "^[0-9]+$", message = "Department Id is not valid, can contain only numbers and minimum 1 number.") String primaryDepartment) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.certificate = certificate;
		this.employmentDate = employmentDate;
		this.subjects = subjects;
	}

	public TeacherDto(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid, can contain only letters and minimum is 2 letter.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid, can contain only letters and minimum is 2 letter.") String lastName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") String jMBG,
			@Pattern(regexp = "^(GENDER_MALE|GENDER_FEMALE)$", message = "Gender is not valid, must be GENDER_MALE or GENDER_FEMALE") String gender,
			String certificate,
			@Pattern(regexp = "^([1-2][0-9]{3})[./-]([0][1-9]|[1][0-2])[./-]([0][1-9]|[1|2][0-9]|[3][0|1])$", message = "Employment date is not valid, must be in yyyy-MM-dd format.") String employmentDate,
			List<String> subjects,
			@Pattern(regexp = "^[0-9]+$", message = "Department Id is not valid, can contain only numbers and minimum 1 number.") String primaryDepartment,
			String teachingDepartment, String teachingSubject,
			@Pattern(regexp = "^(20|[3-9][0-9])\\d{2}\\-(20|[3-9][0-9])\\\\d{2}$", message = "School year is not valid, must be in format YYYY-YYYY.") String schoolYear,
			@Size(min = 5, max = 20, message = "Username must be between {min} and {max} characters long.") String username,
			@Pattern(regexp = "^(ROLE_ADMIN|ROLE_TEACHER|ROLE_PARENT|ROLE_STUDENT)$", message = "Role is not valid, must be ROLE_ADMIN, ROLE_TEACHER, ROLE_PARENT or ROLE_STUDENT") String accessRole,
			@Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String password,
			@Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String confirmedPassword) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.certificate = certificate;
		this.employmentDate = employmentDate;
		this.subjects = subjects;
		this.primaryDepartment = primaryDepartment;
		this.teachingDepartment = teachingDepartment;
		this.teachingSubject = teachingSubject;
		this.schoolYear = schoolYear;
		this.username = username;
		this.accessRole = accessRole;
		this.password = password;
		this.confirmedPassword = confirmedPassword;
	}

	public List<String> getSubjects() {
		return subjects;
	}

	public List<String> getTeachingDepartments() {
		return teachingDepartments;
	}

	public void setTeachingDepartments(List<String> teachingDepartments) {
		this.teachingDepartments = teachingDepartments;
	}

	public void setSubjects(List<String> subjects) {
		this.subjects = subjects;
	}

	public String getPrimaryDepartment() {
		return primaryDepartment;
	}

	public void setPrimaryDepartment(String primaryDepartment) {
		this.primaryDepartment = primaryDepartment;
	}

/*	public String getPrimaryClass() {
		return primaryClass;
	}

	public void setPrimaryClass(String primaryClass) {
		this.primaryClass = primaryClass;
	}
*/
	public String getAccessRole() {
		return accessRole;
	}

	public void setAccessRole(String role) {
		this.accessRole = role;
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

	public String getjMBG() {
		return jMBG;
	}

	public void setjMBG(String jMBG) {
		this.jMBG = jMBG;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String geneder) {
		this.gender = geneder;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public String getEmploymentDate() {
		return employmentDate;
	}

	public void setEmploymentDate(String employmentDate) {
		this.employmentDate = employmentDate;
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

	public String getTeachingDepartment() {
		return teachingDepartment;
	}

	public void setTeachingDepartment(String teachingDepartment) {
		this.teachingDepartment = teachingDepartment;
	}

	public String getTeachingSubject() {
		return teachingSubject;
	}

	public void setTeachingSubject(String teachingSubject) {
		this.teachingSubject = teachingSubject;
	}

	public String getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(String schoolYear) {
		this.schoolYear = schoolYear;
	}

}
