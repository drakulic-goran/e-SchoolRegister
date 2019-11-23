package com.iktpreobuka.projekat_za_kraj.entities.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.ClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.GradeEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class SubjectGradesClassDTO {

	@JsonView(Views.Student.class)
	@NotNull (message = "Grade must be provided.")
	private GradeEntity grade;
	@JsonView(Views.Student.class)
	@NotNull (message = "Subject must be provided.")
	private SubjectEntity subject;
	@JsonView(Views.Student.class)
	@NotNull (message = "Class must be provided.")
	private ClassEntity clas;
	@JsonView(Views.Student.class)
	@NotNull (message = "Department must be provided.")
	private DepartmentEntity department;

	
	public SubjectGradesClassDTO() {
		super();
	}

	public SubjectGradesClassDTO(@NotNull(message = "Grade must be provided.") GradeEntity grade,
			@NotNull(message = "Subject must be provided.") SubjectEntity subject,
			@NotNull(message = "Class must be provided.") ClassEntity clas,
			@NotNull(message = "Department must be provided.") DepartmentEntity department) {
		super();
		this.grade = grade;
		this.subject = subject;
		this.clas = clas;
		this.department = department;
	}

	public GradeEntity getGrade() {
		return grade;
	}

	public void setGrade(GradeEntity grade) {
		this.grade = grade;
	}

	public SubjectEntity getSubject() {
		return subject;
	}

	public void setSubject(SubjectEntity subject) {
		this.subject = subject;
	}

	public ClassEntity getClas() {
		return clas;
	}

	public void setClas(ClassEntity clas) {
		this.clas = clas;
	}

	public DepartmentEntity getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentEntity department) {
		this.department = department;
	}

}