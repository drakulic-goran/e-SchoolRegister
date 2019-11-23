package com.iktpreobuka.projekat_za_kraj.entities.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.ClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class ClassSubjectDto {

	@JsonView(Views.Teacher.class)
	//@Pattern(regexp = "^(IV|V?I{1,4})$", message="Student class is not valid, must be I, II, III, IV, V, VI, VII or VIII.")
	@NotNull (message = "Class must be provided.")
	private ClassEntity clas;
	@JsonView(Views.Teacher.class)
	//@Pattern(regexp = "^([_A-Za-z0-9- _])+$", message="Subject is not valid.")
	@NotNull (message = "Subject must be provided.")
	private SubjectEntity subject;
	@JsonView(Views.Teacher.class)
	@NotNull (message = "Learning program must be provided.")
	private String learningProgram;


	public ClassSubjectDto() {
		super();
	}

	public ClassSubjectDto(
			//@Pattern(regexp = "^(IV|V?I{1,4})$", message = "Student class is not valid, must be I, II, III, IV, V, VI, VII or VIII.") 
			@NotNull(message = "Class label must be provided.") ClassEntity classLabel,
			//@Pattern(regexp = "^([_A-Za-z0-9- _])+$", message = "Subject is not valid.") 
			@NotNull (message = "Subject must be provided.") SubjectEntity subject,
			@NotNull(message = "Learning program must be provided.") String learningProgram) {
		super();
		this.clas = classLabel;
		this.subject = subject;
		this.learningProgram = learningProgram;
	}

	public ClassEntity getClassLabel() {
		return clas;
	}

	public void setClassLabel(ClassEntity classLabel) {
		this.clas = classLabel;
	}

	public SubjectEntity getSubject() {
		return subject;
	}

	public void setSubject(SubjectEntity subject) {
		this.subject = subject;
	}

	public String getLearningProgram() {
		return learningProgram;
	}

	public void setLearningProgram(String learningProgram) {
		this.learningProgram = learningProgram;
	}
	
}
