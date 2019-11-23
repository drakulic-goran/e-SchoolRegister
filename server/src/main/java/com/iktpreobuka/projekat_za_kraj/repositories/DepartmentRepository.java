package com.iktpreobuka.projekat_za_kraj.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.ClassDepartmentClDepDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.DepartmentClassDto;

@Repository
public interface DepartmentRepository extends CrudRepository<DepartmentEntity, Integer> {

	public DepartmentEntity getById(Integer id);
	
	public Iterable<DepartmentEntity> findByStatusLike(Integer status);
	
	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.DepartmentClassDto(c.classLabel, d.departmentLabel, d.status, d.createdById, d.updatedById) from DepartmentEntity d join d.classes cl join cl.clas c where d.status=:status and cl.status=:status")
	public Iterable<DepartmentClassDto> findWithClass_departmentByStatusLike(@Param("status") Integer status);

	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.ClassDepartmentClDepDto(c, d, cl) from DepartmentEntity d join d.classes cl join cl.clas c where d.status=:status and cl.status=1 and c.status=1")
	public Iterable<ClassDepartmentClDepDto> findWithClassByStatusLike(@Param("status") Integer status);

	public DepartmentEntity findByIdAndStatusLike(Integer depatmentId, Integer status);

	public DepartmentEntity findByDepartmentLabelAndEnrollmentYearAndStatusLike(String departmentLabel, String enrollmentYear, Integer status);

	public DepartmentEntity findByDepartmentLabelAndStatusLike(String departmentLabel, Integer status);

	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.DepartmentClassDto(c.classLabel, d.departmentLabel, d.status, d.createdById, d.updatedById) from DepartmentEntity d join d.classes cl join cl.clas c where d.status=:status and d.id=:id and cl.status=:status")
	public Iterable<DepartmentClassDto> findWithClass_departmentByIdAndStatusLike(@Param("id") Integer id, @Param("status") Integer status);
	
}
