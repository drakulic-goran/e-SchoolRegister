package com.iktpreobuka.projekat_za_kraj.repositories;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;

public interface StudentDepartmentRepository extends CrudRepository<StudentDepartmentEntity, Integer> {

	public StudentDepartmentEntity getByStudentAndDepartment(Integer id, Integer id1);

	public Object findByStudentAndDepartmentAndTransferDate(StudentEntity user, DepartmentEntity department, Date transferdate);

}
