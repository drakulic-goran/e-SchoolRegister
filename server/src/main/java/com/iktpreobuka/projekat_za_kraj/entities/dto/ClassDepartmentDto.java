package com.iktpreobuka.projekat_za_kraj.entities.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.ClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class ClassDepartmentDto {

	@JsonView(Views.Teacher.class)
	@NotNull (message = "Class must be provided.")
	private ClassEntity clas;
	@JsonView(Views.Teacher.class)
	@NotNull (message = "Subject must be provided.")
	private DepartmentEntity department;

	public ClassDepartmentDto() {
		super();
	}

	public ClassDepartmentDto(@NotNull(message = "Class must be provided.") ClassEntity clas,
			@NotNull(message = "Subject must be provided.") DepartmentEntity department) {
		super();
		this.clas = clas;
		this.department = department;
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
