package com.iktpreobuka.projekat_za_kraj.entities.dto;

import java.util.List;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class ClassDto {

	@JsonView(Views.Student.class)
	@Pattern(regexp = "^(IV|IX|X|V?I{0,3})$", message="Student class is not valid, must be I, II, III, IV, V, VI, VII, VIII, IX or X.")
	private String classLabel;
	@JsonView(Views.Student.class)
	//@Pattern(regexp = "^([_A-Za-z0-9- _])+$", message="Subject is not valid.")
	private List<String> subjects;
	@JsonView(Views.Admin.class)
	//@Pattern(regexp = "^[a-zA-Z]$", message="Department label is not valid, can contain only one letter.")
	private List<String> departments;
	@JsonView(Views.Student.class)
	//@Pattern(regexp = "^([_A-Za-z0-9- _])+$", message="Subject is not valid.")
	private SubjectEntity subject;
	@JsonView(Views.Student.class)
	//@Pattern(regexp = "^[a-zA-Z]$", message="Department label is not valid, can contain only one letter.")
	private DepartmentEntity department;

	public ClassDto() {
		super();
	}

	public ClassDto(
			@Pattern(regexp = "^(IV|V?I{1,4})$", message = "Student class is not valid, must be I, II, III, IV, V, VI, VII, VIII, IX or X.") String classLabel,
			//@Pattern(regexp = "^([_A-Za-z0-9- _])+$", message = "Subject is not valid.") 
			SubjectEntity subject,
			//@Pattern(regexp = "^[a-zA-Z]$", message = "Department label is not valid, can contain only one letter.") 
			DepartmentEntity department) {
		super();
		this.classLabel = classLabel;
		this.subject = subject;
		this.department = department;
	}

	public ClassDto(
			@Pattern(regexp = "^(IV|V?I{1,4})$", message = "Student class is not valid, must be I, II, III, IV, V, VI, VII, VIII, IX or X.") String classLabel,
			//@Pattern(regexp = "^([_A-Za-z0-9- _])+$", message = "Subject is not valid.") 
			List<String> subjects,
			//@Pattern(regexp = "^[a-zA-Z]$", message = "Department label is not valid, can contain only one letter.") 
			DepartmentEntity department) {
		super();
		this.classLabel = classLabel;
		this.subjects = subjects;
		this.department = department;
	}

	public ClassDto(
			@Pattern(regexp = "^(IV|V?I{1,4})$", message = "Student class is not valid, must be I, II, III, IV, V, VI, VII, VIII, IX or X.") String classLabel,
			//@Pattern(regexp = "^([_A-Za-z0-9- _])+$", message = "Subject is not valid.") 
			SubjectEntity subject,
			//@Pattern(regexp = "^[a-zA-Z]$", message = "Department label is not valid, can contain only one letter.") 
			List<String> departments) {
		super();
		this.classLabel = classLabel;
		this.subject = subject;
		this.departments = departments;
	}

	public ClassDto(
			@Pattern(regexp = "^(IV|V?I{1,4})$", message = "Student class is not valid, must be I, II, III, IV, V, VI, VII, VIII, IX or X.") String classLabel,
			//@Pattern(regexp = "^([_A-Za-z0-9- _])+$", message = "Subject is not valid.") 
			List<String> subjects) {
		super();
		this.classLabel = classLabel;
		this.subjects = subjects;
	}

	public ClassDto(
			//@Pattern(regexp = "^[a-zA-Z]$", message = "Department label is not valid, can contain only one letter.") 
			List<String> departments,
			@Pattern(regexp = "^(IV|V?I{1,4})$", message = "Student class is not valid, must be I, II, III, IV, V, VI, VII, VIII, IX or X.") String classLabel) {
		super();
		this.departments = departments;
		this.classLabel = classLabel;
	}

	public String getClassLabel() {
		return classLabel;
	}

	public void setClassLabel(String classLabel) {
		this.classLabel = classLabel;
	}

	public List<String> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<String> subjects) {
		this.subjects = subjects;
	}

	public List<String> getDepartments() {
		return departments;
	}

	public void setDepartments(List<String> departments) {
		this.departments = departments;
	}

	public SubjectEntity getSubject() {
		return subject;
	}

	public void setSubject(SubjectEntity subject) {
		this.subject = subject;
	}

	public DepartmentEntity getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentEntity department) {
		this.department = department;
	}
	
}
