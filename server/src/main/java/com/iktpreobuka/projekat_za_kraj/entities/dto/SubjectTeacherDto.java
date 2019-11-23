package com.iktpreobuka.projekat_za_kraj.entities.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class SubjectTeacherDto {

	@JsonView(Views.Student.class)
	@NotNull (message = "Teacher must be provided.")
	private TeacherEntity teacher;
	@JsonView(Views.Student.class)
	@NotNull (message = "Subject must be provided.")
	private SubjectEntity subject;
	
	public SubjectTeacherDto() {
		super();
	}

	public SubjectTeacherDto(@NotNull(message = "Grade must be provided.") TeacherEntity grade, @NotNull(message = "Subject must be provided.") SubjectEntity subject) {
		super();
		this.teacher = grade;
		this.subject = subject;
	}


	public SubjectEntity getSubject() {
		return subject;
	}

	public TeacherEntity getTeacher() {
		return teacher;
	}

	public void setTeacher(TeacherEntity teacher) {
		this.teacher = teacher;
	}

	public void setSubject(SubjectEntity subject) {
		this.subject = subject;
	}

}
