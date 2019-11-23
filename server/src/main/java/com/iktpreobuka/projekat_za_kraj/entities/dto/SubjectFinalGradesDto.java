package com.iktpreobuka.projekat_za_kraj.entities.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class SubjectFinalGradesDto {

	@JsonView(Views.Student.class)
	@NotNull (message = "Grade must be provided.")
	private Double grade;
	@JsonView(Views.Student.class)
	@NotNull (message = "Subject must be provided.")
	private SubjectEntity subject;
	
	public SubjectFinalGradesDto() {
		super();
	}

	public SubjectFinalGradesDto(@NotNull(message = "Subject must be provided.") SubjectEntity subject, 
			@NotNull(message = "Grade must be provided.") Double grade) {
		super();
		this.grade = grade;
		this.subject = subject;
	}

	public Double getGrade() {
		return grade;
	}

	public void setGrade(Double grade) {
		this.grade = grade;
	}

	public SubjectEntity getSubject() {
		return subject;
	}

	public void setSubject(SubjectEntity subject) {
		this.subject = subject;
	}

}
