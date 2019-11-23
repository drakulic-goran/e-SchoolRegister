package com.iktpreobuka.projekat_za_kraj.entities.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class TeacherSubjectDepartmentDTO {

	@JsonView(Views.Student.class)
	@NotNull (message = "Subject must be provided.")
	private SubjectEntity subject;
	@JsonView(Views.Student.class)
	@NotNull (message = "TeacherSubjectDepartmentEntity must be provided.")
	private TeacherSubjectDepartmentEntity teacher_subject_department;
	
	
	public TeacherSubjectDepartmentDTO() {
		super();
	}
	
	public TeacherSubjectDepartmentDTO(@NotNull(message = "Subject must be provided.") SubjectEntity subject,
			@NotNull(message = "TeacherSubjectDepartmentEntity must be provided.") TeacherSubjectDepartmentEntity teacher_subject_department) {
		super();
		this.subject = subject;
		this.teacher_subject_department = teacher_subject_department;
	}

	
	public SubjectEntity getSubject() {
		return subject;
	}

	public void setSubject(SubjectEntity subject) {
		this.subject = subject;
	}

	public TeacherSubjectDepartmentEntity getTeacher_subject_department() {
		return teacher_subject_department;
	}

	public void setTeacher_subject_department(TeacherSubjectDepartmentEntity teacher_subject_department) {
		this.teacher_subject_department = teacher_subject_department;
	}
	
}
