package com.iktpreobuka.projekat_za_kraj.entities.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.ClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class SubjectClassDto {

	@JsonView(Views.Student.class)
	@NotNull (message = "Class must be provided.")
	private ClassEntity clas;
	@JsonView(Views.Student.class)
	@NotNull (message = "Subject must be provided.")
	private SubjectEntity subject;
	
	public SubjectClassDto() {
		super();
	}

	public SubjectClassDto(@NotNull(message = "Grade must be provided.") ClassEntity grade, @NotNull(message = "Subject must be provided.") SubjectEntity subject) {
		super();
		this.clas = grade;
		this.subject = subject;
	}


	public SubjectEntity getSubject() {
		return subject;
	}

	public ClassEntity getClas() {
		return clas;
	}

	public void setClas(ClassEntity clas) {
		this.clas = clas;
	}

	public void setSubject(SubjectEntity subject) {
		this.subject = subject;
	}

	
}
