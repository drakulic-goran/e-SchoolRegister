package com.iktpreobuka.projekat_za_kraj.entities.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class TrioStudentSubjecTeachertDto {

	@JsonView(Views.Student.class)
	@NotNull (message = "Student must be provided.")
	private StudentEntity student;
	@JsonView(Views.Student.class)
	@NotNull (message = "Subject must be provided.")
	private SubjectEntity subject;
	@JsonView(Views.Student.class)
	@NotNull (message = "Teacher must be provided.")
	private TeacherEntity teacher;

	
	public TrioStudentSubjecTeachertDto() {
		super();
	}

	public TrioStudentSubjecTeachertDto(@NotNull(message = "Student must be provided.") StudentEntity student,
			@NotNull(message = "Subject must be provided.") SubjectEntity subject,
			@NotNull(message = "Teacher must be provided.") TeacherEntity teacher) {
		super();
		this.student = student;
		this.subject = subject;
		this.teacher = teacher;
	}

	public TeacherEntity getTeacher() {
		return teacher;
	}

	public void setTeacher(TeacherEntity teacher) {
		this.teacher = teacher;
	}

	public StudentEntity getStudent() {
		return student;
	}

	public void setStudent(StudentEntity student) {
		this.student = student;
	}

	public SubjectEntity getSubject() {
		return subject;
	}

	public void setSubject(SubjectEntity subject) {
		this.subject = subject;
	}
	
}
