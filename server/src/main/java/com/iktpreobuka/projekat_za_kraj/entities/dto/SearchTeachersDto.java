package com.iktpreobuka.projekat_za_kraj.entities.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserAccountEntity;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class SearchTeachersDto {

	@JsonView(Views.Student.class)
	private TeacherEntity user;
	@JsonView(Views.Student.class)
	private UserAccountEntity account;
	@JsonView(Views.Student.class)
	private List<SubjectDepartmentsDto> teachingSubjectDepartments;

	
	public SearchTeachersDto() {
		super();
	}
	
	public SearchTeachersDto(TeacherEntity user, UserAccountEntity account) {
		super();
		this.user = user;
		this.account = account;
	}
	
	public SearchTeachersDto(TeacherEntity user, UserAccountEntity account,
			List<SubjectDepartmentsDto> teachingSubjectDepartments) {
		super();
		this.user = user;
		this.account = account;
		this.teachingSubjectDepartments = teachingSubjectDepartments;
	}

	
	public List<SubjectDepartmentsDto> getTeachingSubjectDepartments() {
		return teachingSubjectDepartments;
	}

	public void setTeachingSubjectDepartments(List<SubjectDepartmentsDto> teachingSubjectDepartments) {
		this.teachingSubjectDepartments = teachingSubjectDepartments;
	}

	public TeacherEntity getUser() {
		return user;
	}
	
	public void setUser(TeacherEntity user) {
		this.user = user;
	}
	
	public UserAccountEntity getAccount() {
		return account;
	}
	
	public void setAccount(UserAccountEntity account) {
		this.account = account;
	}

}
