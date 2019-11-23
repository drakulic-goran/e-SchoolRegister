package com.iktpreobuka.projekat_za_kraj.entities.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class StudentSubjectGradesDto {

	@JsonView(Views.Student.class)
	@NotNull (message = "Grade must be provided.")
	private StudentEntity student;
	@JsonView(Views.Student.class)
	@NotNull (message = "Subject must be provided.")
	private SubjectGradesDto gradeBySubject;
	
	public StudentSubjectGradesDto() {
		super();
	}

	public StudentSubjectGradesDto(@NotNull(message = "Grade must be provided.") StudentEntity student,
			@NotNull(message = "Subject must be provided.") SubjectGradesDto gradeBySubject) {
		super();
		this.student = student;
		this.gradeBySubject = gradeBySubject;
	}

	public StudentEntity getStudent() {
		return student;
	}

	public void setStudent(StudentEntity student) {
		this.student = student;
	}

	public SubjectGradesDto getGradeBySubject() {
		return gradeBySubject;
	}

	public void setGradeBySubject(SubjectGradesDto gradeBySubject) {
		this.gradeBySubject = gradeBySubject;
	}

}
