package com.iktpreobuka.projekat_za_kraj.entities.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.GradeEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class TrioStudentSubjecGradeDto {

	@JsonView(Views.Student.class)
	@NotNull (message = "Student must be provided.")
	private StudentEntity student;
	@JsonView(Views.Student.class)
	@NotNull (message = "Subject must be provided.")
	private SubjectEntity subject;
	@JsonView(Views.Student.class)
	@NotNull (message = "Grade must be provided.")
	private GradeEntity grade;

	
	public TrioStudentSubjecGradeDto() {
		super();
	}

	public TrioStudentSubjecGradeDto(@NotNull(message = "Student must be provided.") StudentEntity student,
			@NotNull(message = "Subject must be provided.") SubjectEntity subject,
			@NotNull(message = "Grade must be provided.") GradeEntity grade) {
		super();
		this.student = student;
		this.subject = subject;
		this.grade = grade;
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

	public GradeEntity getGrade() {
		return grade;
	}

	public void setGrade(GradeEntity grade) {
		this.grade = grade;
	}

}
