package com.iktpreobuka.projekat_za_kraj.entities.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class StudentSubjectTeacherDto {

	@JsonView(Views.Student.class)
	@NotNull (message = "Grade must be provided.")
	private StudentEntity student;
	@JsonView(Views.Student.class)
	@NotNull (message = "Subject must be provided.")
	private SubjectTeacherDto subject;
	
	public StudentSubjectTeacherDto() {
		super();
	}

	public StudentSubjectTeacherDto(@NotNull(message = "Grade must be provided.") StudentEntity grade, @NotNull(message = "Subject must be provided.") SubjectTeacherDto subject) {
		super();
		this.student = grade;
		this.subject = subject;
	}


	public StudentEntity getStudent() {
		return student;
	}

	public void setStudent(StudentEntity student) {
		this.student = student;
	}

	public SubjectTeacherDto getSubject() {
		return subject;
	}

	public void setSubject(SubjectTeacherDto subject) {
		this.subject = subject;
	}

}
