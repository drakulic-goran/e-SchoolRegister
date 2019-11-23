package com.iktpreobuka.projekat_za_kraj.services;

import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.ParentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.StudentDto;

public interface StudentDao {

	//public StudentEntity findById(Integer id) throws Exception;
	
	//public StudentEntity findByIdAndStatusLike(Integer id, Integer status) throws Exception;

	//public Iterable<StudentEntity> findByStatusLike(Integer status) throws Exception;

	public UserEntity addNewStudent(UserEntity loggedUser, StudentDto newParent) throws Exception;

	public void modifyStudent(UserEntity loggedUser, StudentEntity student, StudentDto newParent) throws Exception;

	public void deleteStudent(UserEntity loggedUser, StudentEntity student) throws Exception;

	public void undeleteStudent(UserEntity loggedUser, StudentEntity student) throws Exception;

	public void archiveStudent(UserEntity loggedUser, StudentEntity student) throws Exception;

	public void addParentToStudent(UserEntity loggedUser, StudentEntity student, ParentEntity parent) throws Exception;
	
	public void removeParentFromStudent(UserEntity loggedUser, StudentEntity student, ParentEntity parent) throws Exception;

	public StudentDepartmentEntity addDepartmentToStudent(UserEntity loggedUser, StudentEntity student, DepartmentEntity department, String transfer_date) throws Exception;

	public StudentDepartmentEntity removeDepartmentFromStudent(UserEntity loggedUser, StudentEntity student, DepartmentEntity department) throws Exception;
	
}
