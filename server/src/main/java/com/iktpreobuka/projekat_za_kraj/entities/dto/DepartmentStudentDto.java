package com.iktpreobuka.projekat_za_kraj.entities.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class DepartmentStudentDto {

	@JsonView(Views.Teacher.class)
	@NotNull (message = "Student must be provided.")
	private StudentEntity student;
	@JsonView(Views.Teacher.class)
	@NotNull (message = "Subject must be provided.")
	private DepartmentEntity department;

	public DepartmentStudentDto() {
		super();
	}

	public DepartmentStudentDto(@NotNull(message = "Subject must be provided.") DepartmentEntity department, @NotNull(message = "Class must be provided.") StudentEntity clas) {
		super();
		this.student = clas;
		this.department = department;
	}

	public StudentEntity getStudent() {
		return student;
	}

	public void setStudent(StudentEntity student) {
		this.student = student;
	}

	public DepartmentEntity getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentEntity department) {
		this.department = department;
	}
	
}
