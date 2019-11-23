package com.iktpreobuka.projekat_za_kraj.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectEntity;

@Repository
public interface TeacherSubjectRepository extends CrudRepository<TeacherSubjectEntity, Integer> {

	public TeacherSubjectEntity getByTeacherAndSubject(Integer id, Integer id1);
	
}
