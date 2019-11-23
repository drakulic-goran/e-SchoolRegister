package com.iktpreobuka.projekat_za_kraj.entities.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class SubjectDepartmentsDto {

	@JsonView(Views.Student.class)
	private SubjectEntity subject;
	@JsonView(Views.Student.class)
	private List<TeacherSubjectDepartmentEntity> teachingDepartments;
	
	
	public SubjectDepartmentsDto() {
		super();
	}

	public SubjectDepartmentsDto(SubjectEntity subject, List<TeacherSubjectDepartmentEntity> teachingDepartments) {
		super();
		this.subject = subject;
		this.teachingDepartments = teachingDepartments;
	}

	
	public SubjectEntity getSubject() {
		return subject;
	}

	public void setSubject(SubjectEntity subject) {
		this.subject = subject;
	}

	public List<TeacherSubjectDepartmentEntity> getTeachingDepartments() {
		return teachingDepartments;
	}

	public void setTeachingDepartments(List<TeacherSubjectDepartmentEntity> teachingDepartments) {
		this.teachingDepartments = teachingDepartments;
	}

}
