package com.iktpreobuka.projekat_za_kraj.entities.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.ClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserAccountEntity;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class SearchStudentsDto {

	@JsonView(Views.Student.class)
	private StudentEntity user;
	@JsonView(Views.Student.class)
	private UserAccountEntity account;
	@JsonView(Views.Student.class)
	private DepartmentEntity department;
	@JsonView(Views.Student.class)
	private ClassEntity clas;
	@JsonView(Views.Teacher.class)
	private DepartmentClassEntity departmentClass;

	
	public SearchStudentsDto() {
		super();
	}
		
	public SearchStudentsDto(StudentEntity user, UserAccountEntity account, DepartmentEntity department,
			ClassEntity clas, DepartmentClassEntity departmentClass) {
		super();
		this.user = user;
		this.account = account;
		this.department = department;
		this.clas = clas;
		this.departmentClass = departmentClass;
	}

	public SearchStudentsDto(StudentEntity user, UserAccountEntity account) {
		super();
		this.user = user;
		this.account = account;
	}


	public DepartmentClassEntity getDepartmentClass() {
		return departmentClass;
	}

	public void setDepartmentClass(DepartmentClassEntity departmentClass) {
		this.departmentClass = departmentClass;
	}

	public DepartmentEntity getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentEntity department) {
		this.department = department;
	}

	public ClassEntity getClas() {
		return clas;
	}

	public void setClas(ClassEntity clas) {
		this.clas = clas;
	}

	public StudentEntity getUser() {
		return user;
	}

	public void setUser(StudentEntity user) {
		this.user = user;
	}

	public UserAccountEntity getAccount() {
		return account;
	}

	public void setAccount(UserAccountEntity account) {
		this.account = account;
	}
	
}
