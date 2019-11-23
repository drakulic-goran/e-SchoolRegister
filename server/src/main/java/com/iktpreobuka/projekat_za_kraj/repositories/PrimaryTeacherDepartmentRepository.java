package com.iktpreobuka.projekat_za_kraj.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.projekat_za_kraj.entities.PrimaryTeacherDepartmentEntity;

public interface PrimaryTeacherDepartmentRepository extends CrudRepository<PrimaryTeacherDepartmentEntity, Integer> {

	public Optional<PrimaryTeacherDepartmentEntity> findById(Integer id);
	public PrimaryTeacherDepartmentEntity getById(Integer id);
	
}
