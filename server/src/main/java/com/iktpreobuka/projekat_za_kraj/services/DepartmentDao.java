package com.iktpreobuka.projekat_za_kraj.services;

import com.iktpreobuka.projekat_za_kraj.entities.ClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.PrimaryTeacherDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.DepartmentDto;

public interface DepartmentDao {

	public StudentDepartmentEntity addStudentToDepartment(UserEntity loggedUser, StudentEntity student, DepartmentEntity department, String transfer_date) throws Exception;

	public DepartmentEntity addNewDepartment(UserEntity loggedUser, DepartmentDto newDepartment) throws Exception;

	public void modifyDepartment(UserEntity loggedUser, DepartmentEntity department, DepartmentDto updateDepartment) throws Exception;

	public void deleteDepartment(UserEntity loggedUser, DepartmentEntity department) throws Exception;

	public void undeleteDepartment(UserEntity loggedUser, DepartmentEntity department) throws Exception;

	public void archiveDepartment(UserEntity loggedUser, DepartmentEntity department) throws Exception;

	public DepartmentClassEntity addClassToDepartment(UserEntity loggedUser, ClassEntity class_, DepartmentEntity department, String schoolYear)  throws Exception;

	public PrimaryTeacherDepartmentEntity addPrimaryTeacherToDepartment(UserEntity loggedUser, TeacherEntity teacher, DepartmentEntity department, String assignmentDate) throws Exception;

	public TeacherSubjectDepartmentEntity addTeacherAndSubjectToDepartment(UserEntity loggedUser, TeacherEntity teacher, DepartmentEntity department, ClassEntity clas, SubjectEntity subject, String school_year) throws Exception;

	public PrimaryTeacherDepartmentEntity removePrimaryTeacherFromDepartment(UserEntity loggedUser, TeacherEntity teacher, DepartmentEntity department) throws Exception;

	public TeacherSubjectDepartmentEntity removeTeacherAndSubjectFromDepartment(UserEntity loggedUser, TeacherEntity teacher, DepartmentEntity department, ClassEntity clas, SubjectEntity subject) throws Exception;

	public DepartmentClassEntity removeClassFromDepartment(UserEntity loggedUser, ClassEntity class_, DepartmentEntity department) throws Exception;

	public StudentDepartmentEntity removeStudentFromDepartment(UserEntity loggedUser, StudentEntity student, DepartmentEntity department) throws Exception;

}
