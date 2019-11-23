package com.iktpreobuka.projekat_za_kraj.entities.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.ClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.GradeEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class StudentSubjectGradesClassDTO {

	@JsonView(Views.Parent.class)
	@NotNull (message = "Grade must be provided.")
	private GradeEntity grade;
	@JsonView(Views.Parent.class)
	@NotNull (message = "Student must be provided.")
	private StudentEntity student;
	@JsonView(Views.Parent.class)
	@NotNull (message = "Subject must be provided.")
	private SubjectEntity subject;
	@JsonView(Views.Parent.class)
	@NotNull (message = "Class must be provided.")
	private ClassEntity clas;
	@JsonView(Views.Parent.class)
	@NotNull (message = "Department must be provided.")
	private DepartmentEntity department;

	
	public StudentSubjectGradesClassDTO() {
		super();
	}

	public StudentSubjectGradesClassDTO(@NotNull(message = "Grade must be provided.") GradeEntity grade,
			@NotNull(message = "Student must be provided.") StudentEntity student,
			@NotNull(message = "Subject must be provided.") SubjectEntity subject,
			@NotNull(message = "Class must be provided.") ClassEntity clas,
			@NotNull(message = "Department must be provided.") DepartmentEntity department) {
		super();
		this.grade = grade;
		this.student = student;
		this.subject = subject;
		this.clas = clas;
		this.department = department;
	}


	public StudentEntity getStudent() {
		return student;
	}

	public void setStudent(StudentEntity student) {
		this.student = student;
	}

	public GradeEntity getGrade() {
		return grade;
	}

	public void setGrade(GradeEntity grade) {
		this.grade = grade;
	}

	public SubjectEntity getSubject() {
		return subject;
	}

	public void setSubject(SubjectEntity subject) {
		this.subject = subject;
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