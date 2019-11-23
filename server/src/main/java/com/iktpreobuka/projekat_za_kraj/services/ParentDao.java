package com.iktpreobuka.projekat_za_kraj.services;

import com.iktpreobuka.projekat_za_kraj.entities.ParentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.ParentDto;

public interface ParentDao {

	//public ParentEntity findById(Integer id) throws Exception;
	
	//public ParentEntity findByIdAndStatusLike(Integer id, Integer status) throws Exception;

	//public Iterable<ParentEntity> findByStatusLike(Integer status) throws Exception;

	public UserEntity addNewParent(UserEntity loggedUser, ParentDto newParent) throws Exception;

	public void modifyParent(UserEntity loggedUser, ParentEntity parent, ParentDto newParent) throws Exception;

	public void deleteParent(UserEntity loggedUser, ParentEntity parent) throws Exception;

	public void undeleteParent(UserEntity loggedUser, ParentEntity parent) throws Exception;

	public void archiveParent(UserEntity loggedUser, ParentEntity parent) throws Exception;

	public void addStudentToParent(UserEntity loggedUser, ParentEntity parent, StudentEntity student) throws Exception;

	public void removeStudentFromParent(UserEntity loggedUser, ParentEntity parent, StudentEntity student) throws Exception;

}
