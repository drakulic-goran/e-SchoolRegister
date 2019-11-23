package com.iktpreobuka.projekat_za_kraj.entities.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.ClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class ClassDepartmentClDepDto {
	
	@JsonView(Views.Teacher.class)
	@NotNull (message = "Class must be provided.")
	private ClassEntity clas;
	@JsonView(Views.Teacher.class)
	@NotNull (message = "Subject must be provided.")
	private DepartmentEntity department;
	@JsonView(Views.Teacher.class)
	@NotNull (message = "Subject must be provided.")
	private DepartmentClassEntity departmentClass;
	
	
	public ClassDepartmentClDepDto() {
		super();
	}

	public ClassDepartmentClDepDto(@NotNull(message = "Class must be provided.") ClassEntity clas,
			@NotNull(message = "Subject must be provided.") DepartmentEntity department,
			@NotNull(message = "Subject must be provided.") DepartmentClassEntity departmentClass) {
		super();
		this.clas = clas;
		this.department = department;
		this.departmentClass = departmentClass;
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

	public DepartmentClassEntity getDepartmentClass() {
		return departmentClass;
	}

	public void setDepartmentClass(DepartmentClassEntity departmentClass) {
		this.departmentClass = departmentClass;
	}

}
