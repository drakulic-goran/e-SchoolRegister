package com.iktpreobuka.projekat_za_kraj.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectDepartmentEntity;

@Repository
public interface TeacherSubjectDepartmentRepository extends CrudRepository<TeacherSubjectDepartmentEntity, Integer> {

	/*@Query("select tsd from TeacherSubjectDepartmentEntity tsd where tsd.teachingTeacher=:teacher and tsd.teachingSubject=:subject and tsd.teachingDepartment=:department and tsd.status=1")
	public TeacherSubjectDepartmentEntity getByTeachingTeacherAndTeachingSubjectAndTeachingDepartment(TeacherEntity teacher, SubjectEntity subject, DepartmentEntity department);
*/
	
	@Query("select tsd from TeacherSubjectDepartmentEntity tsd join tsd.teachingDepartment td join td.students s where tsd.teachingTeacher=:teacher and tsd.teachingSubject=:subject and s.student=:student and td.status=1 and tsd.status=1 and s.status=1")
	public TeacherSubjectDepartmentEntity getByTeachingTeacherAndTeachingSubjectAndTeachingDepartment(@Param("teacher") TeacherEntity teacher, @Param("subject") SubjectEntity subject, @Param("student") StudentEntity student);

	@Query("select tsd from TeacherSubjectDepartmentEntity tsd join tsd.teachingDepartment td join td.students s join td.classes c where tsd.teachingTeacher=:teacher and tsd.teachingSubject=:subject and s.student=:student and td.status=1 and tsd.status=1 and s.status=1 and c.status=1")
	public TeacherSubjectDepartmentEntity getByTeachingTeacherAndTeachingSubjectAndTeachingDepartmentAndTeachingClass(@Param("teacher") TeacherEntity teacher, @Param("subject") SubjectEntity subject, @Param("student") StudentEntity student);

	public Iterable<TeacherSubjectDepartmentEntity> findByStatusLike(Integer status);

}
