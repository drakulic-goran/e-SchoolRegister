package com.iktpreobuka.projekat_za_kraj.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.projekat_za_kraj.entities.ClassSubjectEntity;

@Repository
public interface ClassSubjectRepository extends CrudRepository<ClassSubjectEntity, Integer> {

	public ClassSubjectEntity getByClasAndSubject(Integer id, Integer id1);
}
