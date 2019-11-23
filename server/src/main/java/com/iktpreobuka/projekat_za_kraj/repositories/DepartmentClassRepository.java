package com.iktpreobuka.projekat_za_kraj.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.projekat_za_kraj.entities.ClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;

@Repository
public interface DepartmentClassRepository extends CrudRepository<DepartmentClassEntity, Integer> {

	public DepartmentClassEntity getByClasAndDepartment(Integer id, Integer id1);

	@Query("select c from ClassEntity c join c.departments d where d.department=:dep and c.status=1 and d.status=:status")
	public ClassEntity getClasByDepartmentAndStatusLike(@Param("dep") DepartmentEntity dep, @Param("status") Integer status);

}
