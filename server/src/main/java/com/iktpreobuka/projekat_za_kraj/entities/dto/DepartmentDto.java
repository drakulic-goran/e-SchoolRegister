package com.iktpreobuka.projekat_za_kraj.entities.dto;

import java.util.List;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.security.Views;
import com.mysql.cj.conf.ConnectionUrlParser.Pair;

public class DepartmentDto {

	@JsonView(Views.Student.class)
	@Pattern(regexp = "^[A-Za-z0-9]{1,2}$", message="Department label is not valid, can contain only one or two letters and/or numbers.")
	private String departmentLabel;
	@JsonView(Views.Student.class)
	@Pattern(regexp = "^(20|[3-9][0-9])\\d{2}$", message="Enrollment year is not valid, must be in format YYYY.")
	private String enrollmentYear;
	@JsonView(Views.Student.class)
	@Pattern(regexp = "^(20|[3-9][0-9])[0-9]{2}\\-(20|[3-9][0-9])[0-9]{2}$", message="School year is not valid, must be in format YYYY-YYYY.")
	private String schoolYear;
	@JsonView(Views.Teacher.class)
	private List<String> students;
	@JsonView(Views.Student.class)
	private String primaryTeacher;
	@JsonView(Views.Student.class)
	private List<Pair<String, String>> teachers_subjects;
	@JsonView(Views.Student.class)
	private String department_class;

	
	public DepartmentDto() {
		super();
	}

	public DepartmentDto(
			@Pattern(regexp = "^[A-Za-z0-9]{1,2}$", message = "Department label is not valid, can contain only one or two letters and/or numbers.") String departmentLabel,
			@Pattern(regexp = "^(20|[3-9][0-9])\\d{2}$", message = "Enrollment year is not valid, must be in format YYYY.") String enrollmentYear,
			@Pattern(regexp = "^(20|[3-9][0-9])[0-9]{2}\\-(20|[3-9][0-9])[0-9]{2}$", message = "School year is not valid, must be in format YYYY-YYYY.") String schoolYear,
			List<String> students, String primaryTeacher, List<Pair<String, String>> teachers_subjects,
			String department_class) {
		super();
		this.departmentLabel = departmentLabel;
		this.enrollmentYear = enrollmentYear;
		this.students = students;
		this.primaryTeacher = primaryTeacher;
		this.teachers_subjects = teachers_subjects;
		this.department_class = department_class;
	}

	public DepartmentDto(
			@Pattern(regexp = "^[A-Za-z0-9]{1,2}$", message = "Department label is not valid, can contain only one or two letters and/or numbers.") String departmentLabel,
			@Pattern(regexp = "^(20|[3-9][0-9])\\d{2}$", message = "Enrollment year is not valid, must be in format YYYY.") String enrollmentYear,
			List<Pair<String, String>> teachers_subjects, String department_class, 
			@Pattern(regexp = "^(20|[3-9][0-9])[0-9]{2}\\-(20|[3-9][0-9])[0-9]{2}$", message = "School year is not valid, must be in format YYYY-YYYY.") String schoolYear) {
		super();
		this.departmentLabel = departmentLabel;
		this.enrollmentYear = enrollmentYear;
		this.teachers_subjects = teachers_subjects;
		this.department_class = department_class;
		this.schoolYear = schoolYear;
	}
	
	public DepartmentDto(
			@Pattern(regexp = "^[A-Za-z0-9]{1,2}$", message = "Department label is not valid, can contain only one or two letters and/or numbers.") String departmentLabel,
			@Pattern(regexp = "^(20|[3-9][0-9])\\d{2}$", message = "Enrollment year is not valid, must be in format YYYY.") String enrollmentYear,
			String primaryTeacher, String department_class, @Pattern(regexp = "^(20|[3-9][0-9])[0-9]{2}\\-(20|[3-9][0-9])[0-9]{2}$", message = "School year is not valid, must be in format YYYY-YYYY.") String schoolYear) {
		super();
		this.departmentLabel = departmentLabel;
		this.enrollmentYear = enrollmentYear;
		this.primaryTeacher = primaryTeacher;
		this.department_class = department_class;
		this.schoolYear = schoolYear;
	}

	
	public DepartmentDto(String department_class,
			@Pattern(regexp = "^(20|[3-9][0-9])[0-9]{2}\\-(20|[3-9][0-9])[0-9]{2}$", message = "School year is not valid, must be in format YYYY-YYYY.") String schoolYear,
			@Pattern(regexp = "^[A-Za-z0-9]{1,2}$", message = "Department label is not valid, can contain only one or two letters and/or numbers.") String departmentLabel,
			@Pattern(regexp = "^(20|[3-9][0-9])\\d{2}$", message = "Enrollment year is not valid, must be in format YYYY.") String enrollmentYear,
			List<String> students) {
		super();
		this.department_class = department_class;
		this.schoolYear = schoolYear;
		this.departmentLabel = departmentLabel;
		this.enrollmentYear = enrollmentYear;
		this.students = students;
	}


	public String getDepartmentLabel() {
		return departmentLabel;
	}

	public void setDepartmentLabel(String departmentLabel) {
		this.departmentLabel = departmentLabel;
	}

	public String getEnrollmentYear() {
		return enrollmentYear;
	}

	public void setEnrollmentYear(String enrollmentYear) {
		this.enrollmentYear = enrollmentYear;
	}

	public List<String> getStudents() {
		return students;
	}

	public void setStudents(List<String> students) {
		this.students = students;
	}

	public String getPrimaryTeacher() {
		return primaryTeacher;
	}

	public void setPrimaryTeacher(String primaryTeacher) {
		this.primaryTeacher = primaryTeacher;
	}

	public List<Pair<String, String>> getTeachers_subjects() {
		return teachers_subjects;
	}

	public void setTeachers_subjects(List<Pair<String, String>> teachers_subjects) {
		this.teachers_subjects = teachers_subjects;
	}

	public String getDepartment_class() {
		return department_class;
	}

	public void setDepartment_class(String department_class) {
		this.department_class = department_class;
	}

	public String getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(String schoolYear) {
		this.schoolYear = schoolYear;
	}
	
}
