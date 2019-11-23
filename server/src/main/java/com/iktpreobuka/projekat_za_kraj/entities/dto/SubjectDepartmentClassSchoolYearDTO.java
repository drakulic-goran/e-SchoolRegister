package com.iktpreobuka.projekat_za_kraj.entities.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class SubjectDepartmentClassSchoolYearDTO {
	
	@JsonView(Views.Student.class)
	@NotNull (message = "Student must be provided.")
	private StudentEntity student;
	@JsonView(Views.Student.class)
	@NotNull (message = "TeacherSubjectDepartmentEntity must be provided.")
	private TeacherSubjectDepartmentEntity teacher_subject_department;
	/*@JsonView(Views.Student.class)
	@NotNull (message = "TeacherSubjectDepartmentEntity must be provided.")
	private TeacherSubjectDepartmentEntityGradeDTO grade;
	@JsonView(Views.Student.class)
	@NotNull (message = "Subject must be provided.")
	private SubjectEntity subject;
	@JsonView(Views.Student.class)
	@NotNull (message = "Class must be provided.")
	private ClassEntity clas;
	@JsonView(Views.Student.class)
	@NotNull (message = "Department must be provided.")
	private DepartmentEntity department;*/
	
	
	public SubjectDepartmentClassSchoolYearDTO() {
		super();
	}

	
	public SubjectDepartmentClassSchoolYearDTO(@NotNull(message = "Student must be provided.") StudentEntity student,
			@NotNull(message = "TeacherSubjectDepartmentEntity must be provided.") TeacherSubjectDepartmentEntity teacher_subject_department) {
		super();
		this.student = student;
		this.teacher_subject_department = teacher_subject_department;
	}


	/*public SubjectDepartmentClassSchoolYearDTO(@NotNull(message = "Student must be provided.") StudentEntity student,
			@NotNull(message = "TeacherSubjectDepartmentEntity must be provided.") TeacherSubjectDepartmentEntity teacher_subject_department,
			@NotNull(message = "Subject must be provided.") SubjectEntity subject,
			@NotNull(message = "Class must be provided.") ClassEntity clas,
			@NotNull(message = "Department must be provided.") DepartmentEntity department) {
		super();
		this.student = student;
		this.grade = new TeacherSubjectDepartmentEntityGradeDTO();
		this.grade.setTeacher_subject_department(teacher_subject_department);
		this.subject = subject;
		this.clas = clas;
		this.department = department;
	}*/

	
	public StudentEntity getStudent() {
		return student;
	}

	public void setStudent(StudentEntity student) {
		this.student = student;
	}

	public TeacherSubjectDepartmentEntity getTeacher_subject_department() {
		return teacher_subject_department;
	}

	public void setTeacher_subject_department(TeacherSubjectDepartmentEntity teacher_subject_department) {
		this.teacher_subject_department = teacher_subject_department;
	}

	/*public TeacherSubjectDepartmentEntityGradeDTO getGrade() {
		return grade;
	}

	public void setGrade(TeacherSubjectDepartmentEntityGradeDTO grade) {
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
	}*/

}
