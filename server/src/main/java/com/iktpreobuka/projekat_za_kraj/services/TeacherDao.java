package com.iktpreobuka.projekat_za_kraj.services;

import java.util.List;

import com.iktpreobuka.projekat_za_kraj.entities.PrimaryTeacherDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.TeacherDto;

public interface TeacherDao {

	//public TeacherEntity findById(Integer id) throws Exception;
	
	//public TeacherEntity findByIdAndStatusLike(Integer id, Integer status) throws Exception;

	//public Iterable<TeacherEntity> findByStatusLike(Integer status) throws Exception;

	public UserEntity addNewTeacher(UserEntity loggedUser, TeacherDto newTeacher) throws Exception;

	public void modifyTeacher(UserEntity loggedUser, TeacherEntity teacher, TeacherDto newTeacher) throws Exception;

	public void deleteTeacher(UserEntity loggedUser, TeacherEntity teacher) throws Exception;

	public void undeleteTeacher(UserEntity loggedUser, TeacherEntity teacher) throws Exception;

	public void archiveTeacher(UserEntity loggedUser, TeacherEntity teacher) throws Exception;

	public List<TeacherSubjectEntity> addSubjectsToTeacher(UserEntity loggedUser, TeacherEntity user, List<String> subjects) throws Exception;
	
	public List<TeacherSubjectEntity> removeSubjectsFromTeacher(UserEntity loggedUser, TeacherEntity user, List<String> subjects) throws Exception;

	public PrimaryTeacherDepartmentEntity addPrimaryDepartmentToTeacher(UserEntity loggedUser, TeacherEntity user, String primaryDepartment) throws Exception;
	
	public PrimaryTeacherDepartmentEntity removePrimaryDepartmentFromTeacher(UserEntity loggedUser, TeacherEntity user, String primaryDepartment) throws Exception;

	public TeacherSubjectDepartmentEntity addSubjectsInDepartmentsToTeacher(UserEntity loggedUser, TeacherEntity user, String teachingDepartment, String teachingSubject, String schoolYear) throws Exception;
	
	public TeacherSubjectDepartmentEntity removeSubjectsInDepartmentsFromTeacher(UserEntity loggedUser, TeacherEntity user, String teachingDepartment, String teachingSubject) throws Exception;

	public List<TeacherSubjectDepartmentEntity> addDepartmentsToSubjectForTeacher(UserEntity loggedUser, TeacherEntity user, List<String> teachingDepartments, String teachingSubject, String schoolYear) throws Exception;

	public List<TeacherSubjectDepartmentEntity> removeDepartmentsFromSubjectForTeacher(UserEntity loggedUser, TeacherEntity user, List<String> teachingDepartments, String teachingSubject) throws Exception;

}
