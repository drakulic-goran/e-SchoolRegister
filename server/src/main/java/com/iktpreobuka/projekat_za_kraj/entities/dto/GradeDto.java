package com.iktpreobuka.projekat_za_kraj.entities.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class GradeDto {

	@JsonView(Views.Student.class)
	@Pattern(regexp = "^[0-9]+$", message="Students Id is not valid, can contain only numbers and must be exactly 13 numbers long.")
	private String student;
	@JsonView(Views.Student.class)
	@Pattern(regexp = "^[0-9]+$", message="Teacher Id is not valid, can contain only numbers and must be exactly 13 numbers long.")
	private String teacher;
	@JsonView(Views.Student.class)
	@Pattern(regexp = "^[0-9]+$", message="Subject Id is not valid, can contain only numbers and must be exactly 13 numbers long.")
	private String subject;
	@JsonView(Views.Student.class)
	@Min(value=1, message = "Grade value must be {value} or higher!")
	@Max(value=5, message = "Grade value must be {value} or lower!")
	private Integer gradeValue;
	@JsonView(Views.Student.class)
	@Pattern(regexp = "^([0][1-9]|[1|2][0-9]|[3][0|1])[./-]([0][1-9]|[1][0-2])[./-]([1-2][0-9]{3})$", message="Grade date is not valid, must be in dd-MM-yyyy format.")
	private String gradeMadeDate;
	@JsonView(Views.Student.class)
	@Pattern(regexp="^(FIRST_MIDTERM|SECOND_MIDTERM)$",message="Semester is not valid, must be FIRST_MIDTERM or SECOND_MIDTERM")
	private String semester;
	
	public GradeDto() {
		super();
	}

	public GradeDto(
			@Pattern(regexp = "^[0-9]+$", message = "Students Id is not valid, can contain only numbers and must be exactly 13 numbers long.") 
			String student,
			@Pattern(regexp = "^[0-9]+$", message = "Teacher Id is not valid, can contain only numbers and must be exactly 13 numbers long.") 
			String teacher,
			@Pattern(regexp = "^[0-9]+$", message = "Subject Id is not valid, can contain only numbers and must be exactly 13 numbers long.") 
			String subject,
			@Min(value = 1, message = "Grade value must be {value} or higher!") @Max(value = 5, message = "Grade value must be {value} or lower!") Integer gradeValue,
			@Pattern(regexp = "^(FIRST_MIDTERM|SECOND_MIDTERM)$", message = "Semester is not valid, must be FIRST_MIDTERM or SECOND_MIDTERM") String semester) {
		super();
		this.student = student;
		this.teacher = teacher;
		this.subject = subject;
		this.gradeValue = gradeValue;
		this.semester = semester;
	}

	public GradeDto(
			@Pattern(regexp = "^[0-9]+$", message = "Students Id is not valid, can contain only numbers and must be exactly 13 numbers long.") 
			String student,
			@Pattern(regexp = "^[0-9]+$", message = "Teacher Id is not valid, can contain only numbers and must be exactly 13 numbers long.") 
			String teacher,
			@Pattern(regexp = "^[0-9]+$", message = "Subject Id is not valid, can contain only numbers and must be exactly 13 numbers long.") 
			String subject,
			@Min(value = 1, message = "Grade value must be {value} or higher!") @Max(value = 5, message = "Grade value must be {value} or lower!") Integer gradeValue,
			@Pattern(regexp = "^([0][1-9]|[1|2][0-9]|[3][0|1])[./-]([0][1-9]|[1][0-2])[./-]([1-2][0-9]{3})$", message = "Employment date is not valid, must be in dd-MM-yyyy format.") String gradeMadeDate,
			@Pattern(regexp = "^(FIRST_MIDTERM|SECOND_MIDTERM)$", message = "Semester is not valid, must be FIRST_MIDTERM or SECOND_MIDTERM") String semester) {
		super();
		this.student = student;
		this.teacher = teacher;
		this.subject = subject;
		this.gradeValue = gradeValue;
		this.gradeMadeDate = gradeMadeDate;
		this.semester = semester;
	}

	public String getStudent() {
		return student;
	}

	public void setStudent(String student) {
		this.student = student;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Integer getGradeValue() {
		return gradeValue;
	}

	public void setGradeValue(Integer gradeValue) {
		this.gradeValue = gradeValue;
	}

	public String getGradeMadeDate() {
		return gradeMadeDate;
	}

	public void setGradeMadeDate(String gradeMadeDate) {
		this.gradeMadeDate = gradeMadeDate;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

}
