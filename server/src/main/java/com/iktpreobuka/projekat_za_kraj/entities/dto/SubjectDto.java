package com.iktpreobuka.projekat_za_kraj.entities.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class SubjectDto {
	
	@JsonView(Views.Student.class)
	@Pattern(regexp = "^([_A-Za-z0-9- _])+$", message="Subject is not valid.")
	private String subjectName;
	@JsonView(Views.Student.class)
	@Min(value=0, message = "Number of classes in a week must be {value} or higher!")
	private Integer weekClassesNumber;
	@JsonView(Views.Teacher.class)
	private List<String> classes;
	@JsonView(Views.Student.class)
	private String learningProgram;
	@JsonView(Views.Teacher.class)
	private List<String> teachers;
	@JsonView(Views.Admin.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date assignmentDate;
	@JsonView(Views.Teacher.class)
	private String teachingDepartment;
	@JsonView(Views.Teacher.class)
	private String teachingTeacher;
	@JsonView(Views.Student.class)
	//@Pattern(regexp = "^(20|[3-9][0-9])[0-9]{2}[- \\.\\/]{1}(20|[3-9][0-9])[0-9]{2}$", message="School year is not valid, must be in format YYYY-YYYY.")
	private String schoolYear;



	
	public SubjectDto() {
		super();
	}

	public SubjectDto(@Pattern(regexp = "^([_A-Za-z0-9- _])+$", message = "Subject is not valid.") String subjectName,
			@Min(value = 0, message = "Number of classes in a week must be {value} or higher!") Integer weekClassesNumber) {
		super();
		this.subjectName = subjectName;
		this.weekClassesNumber = weekClassesNumber;
	}

	public SubjectDto(List<String> classes, String learningProgram, List<String> teachers, Date assignmentDate,
			String teachingDepartment, String teachingTeacher,
			@Pattern(regexp = "^(20|[3-9][0-9])[0-9]{2}[- \\\\.\\\\/]{1}(20|[3-9][0-9])[0-9]{2}$", message = "School year is not valid, must be in format YYYY-YYYY.") String schoolYear) {
		super();
		this.classes = classes;
		this.learningProgram = learningProgram;
		this.teachers = teachers;
		this.assignmentDate = assignmentDate;
		this.teachingDepartment = teachingDepartment;
		this.teachingTeacher = teachingTeacher;
		this.schoolYear = schoolYear;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Integer getWeekClassesNumber() {
		return weekClassesNumber;
	}

	public void setWeekClassesNumber(Integer weekClassesNumber) {
		this.weekClassesNumber = weekClassesNumber;
	}

	public List<String> getClasses() {
		return classes;
	}

	public void setClasses(List<String> classes) {
		this.classes = classes;
	}

	public List<String> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<String> teachers) {
		this.teachers = teachers;
	}

	public Date getAssignmentDate() {
		return assignmentDate;
	}

	public void setAssignmentDate(Date assignmentDate) {
		this.assignmentDate = assignmentDate;
	}

	public String getTeachingDepartment() {
		return teachingDepartment;
	}

	public void setTeachingDepartment(String teachingSubject) {
		this.teachingDepartment = teachingSubject;
	}

	public String getTeachingTeacher() {
		return teachingTeacher;
	}

	public void setTeachingTeacher(String teachingTeacher) {
		this.teachingTeacher = teachingTeacher;
	}

	public String getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(String schoolYear) {
		this.schoolYear = schoolYear;
	}

	public String getLearningProgram() {
		return learningProgram;
	}

	public void setLearningProgram(String learningProgram) {
		this.learningProgram = learningProgram;
	}

}
