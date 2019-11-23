package com.iktpreobuka.projekat_za_kraj.entities.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class TeacherSubjectDepartmentEntityGradeDTO {

	@JsonView(Views.Parent.class)
	@NotNull (message = "TeacherSubjectDepartmentEntity must be provided.")
	private TeacherSubjectDepartmentEntity teacher_subject_department;

	
	public TeacherSubjectDepartmentEntityGradeDTO() {
		super();
	}

	public TeacherSubjectDepartmentEntityGradeDTO(
			@NotNull(message = "TeacherSubjectDepartmentEntity must be provided.") TeacherSubjectDepartmentEntity teacher_subject_department) {
		super();
		this.teacher_subject_department = teacher_subject_department;
	}

	
	public TeacherSubjectDepartmentEntity getTeacher_subject_department() {
		return teacher_subject_department;
	}

	public void setTeacher_subject_department(TeacherSubjectDepartmentEntity teacher_subject_department) {
		this.teacher_subject_department = teacher_subject_department;
	}

}
