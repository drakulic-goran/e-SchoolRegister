package com.iktpreobuka.projekat_za_kraj.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.projekat_za_kraj.entities.ClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.enumerations.EClass;

public interface ClassRepository extends CrudRepository<ClassEntity, Integer> {

	public ClassEntity getById(Integer id);
	
	public Iterable<ClassEntity> findByStatusLike(Integer status);
	
	public ClassEntity findByIdAndStatusLike(Integer id, Integer status);
	
	@Query("select s.subject from ClassEntity c join c.subjects s where s.clas=:clas and s.status=1")
	public Iterable<SubjectEntity> findSubjectsByClass(ClassEntity clas);

	public ClassEntity getByClassLabel(EClass classLabel);

	public ClassEntity findByClassLabelAndStatusLike(EClass valueOf, Integer status);
	
}
