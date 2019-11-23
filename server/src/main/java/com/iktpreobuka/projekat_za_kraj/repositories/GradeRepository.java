package com.iktpreobuka.projekat_za_kraj.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.projekat_za_kraj.entities.GradeEntity;
import com.iktpreobuka.projekat_za_kraj.entities.ParentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.StudentSubjectGradesClassDTO;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectFinalGradesDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectGradesClassDTO;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectGradesDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.TrioStudentSubjecGradeDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.ESemester;

public interface GradeRepository extends CrudRepository<GradeEntity, Integer> {
	
	public GradeEntity getById(Integer id);
	
	public List<GradeEntity> findByStudentAndStatusLike(StudentEntity studentId, Integer status);
	
	public GradeEntity findByStudentAndIdAndStatusLike(StudentEntity user, Integer grade, Integer status);

	public Iterable<GradeEntity> findByStatusLike(Integer status);
	
	public GradeEntity findByIdAndStatusLike(Integer id, Integer status);

	@Query("select g from GradeEntity g join g.student s join s.parents p where p=:parent and g.status=1 and s.status=1")
	public List<GradeEntity> findByParent(ParentEntity parent);

	@Query("select g from GradeEntity g join g.student s join s.parents p where p=:parent and g.status=1 and g.id=:grade and s.status=1")
	public GradeEntity findByParentAndGradeId(ParentEntity parent, Integer grade);

	//@Query("select g from GradeEntity g join g.teacher_subject_department tsd where tsd.teachingTeacher=:teacher and g.status=1 and tsd.status=1")
	@Query("select g from GradeEntity g join g.teacher_subject_department tsd where tsd.teachingTeacher=:teacher and g.status=1")
	public List<GradeEntity> findByTeacher(TeacherEntity teacher);
	
	/*@Query("select tsd.grades from TeacherEntity t join t.subjects_departments tsd join tsd.teachingDepartment d join d.students where t=:teacher")
	public List<GradeEntity> findByTeacherWithNullGrades(TeacherEntity teacher);*/

	//@Query("select g from GradeEntity g join g.teacher_subject_department tsd where tsd.teachingTeacher=:teacher and g.status=1 and g.id=:grade and tsd.status=1")
	@Query("select g from GradeEntity g join g.teacher_subject_department tsd where tsd.teachingTeacher=:teacher and g.status=1 and g.id=:grade")
	public GradeEntity findByTeacherAndGradeId(TeacherEntity teacher, Integer grade);

	//@Query("select sub, s from ParentEntity p join p.students s join s.student_department sd join sd.class_department cd join cd.subjects su join su.subject sub where p=:parent")
	//public Map<SubjectEntity, StudentEntity> findByParent(UserEntity parent);
	//@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectGradesDto(tsd.teachingSubject, g) from GradeEntity g join g.teacher_subject_department tsd where g.student.id=:student and g.status=1 and tsd.status=1")
	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectGradesDto(tsd.teachingSubject, g) from GradeEntity g join g.teacher_subject_department tsd where g.student.id=:student and g.status=1")
	public List<SubjectGradesDto> findGradesWithSubjectByStudent(Integer student);
	
	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectGradesClassDTO(g, tsd.teachingSubject, tsd.teachingClass, tsd.teachingDepartment) from GradeEntity g join g.teacher_subject_department tsd where g.student.id=:student and g.status=1")
	public List<SubjectGradesClassDTO> findGradesWithClassAndSubjectByStudent(Integer student);
	
	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.StudentSubjectGradesClassDTO(g, s, tsd.teachingSubject, tsd.teachingClass, tsd.teachingDepartment) from ParentEntity p join p.students s join s.grades g join g.teacher_subject_department tsd join g.student s where p=:parent and g.status=1")
	public List<StudentSubjectGradesClassDTO> findGradesWithStudentAndClassAndSubjectByParent(ParentEntity parent);

	//@Query("select g from GradeEntity g join g.teacher_subject_department tsd where g.student=:student and tsd.teachingSubject=:subject and g.status=1 and tsd.status=1")
	@Query("select g from GradeEntity g join g.teacher_subject_department tsd where g.student=:student and tsd.teachingSubject=:subject and g.status=1")
	public List<GradeEntity> findByStudentAndSubject(StudentEntity student, SubjectEntity subject);

	@Query("select g from GradeEntity g join g.teacher_subject_department tsd where g.student=:student and g.semester=:semester and tsd.teachingSubject=:subject and g.status=1")
	public List<GradeEntity> findByStudentAndSemesterAndSubject(StudentEntity student, SubjectEntity subject, ESemester semester);

	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectGradesDto(tsd.teachingSubject, g) from GradeEntity g join g.teacher_subject_department tsd where g.semester=:semester and g.student.id=:student and g.status=1")
	public List<SubjectGradesDto> findGradesWithSubjectBySemesterAndStudent(Integer student, ESemester semester);

	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.TrioStudentSubjecGradeDto(s, tsd.teachingSubject, g) from GradeEntity g join g.teacher_subject_department tsd join g.student s join s.parents p where p.id=:parent and g.semester=:semester and s.status=1 and g.status=1")
	public List<TrioStudentSubjecGradeDto> findGradesWithSubjectBySemesterAndStudentForParent(Integer parent, ESemester semester);

	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectGradesDto(tsd.teachingSubject, g) from GradeEntity g join g.teacher_subject_department tsd where g.semester=:semester and g.student.id=:student and g.status=1 and tsd.teachingSubject.id=:subject")
	public List<SubjectGradesDto> findGradesWithSubjectBySemesterAndStudentAndSubject(Integer student, Integer subject, ESemester semester);

	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectGradesDto(tsd.teachingSubject, g) from GradeEntity g join g.teacher_subject_department tsd where g.semester=:semester and g.student.id=:student and tsd.teachingTeacher.id=:teacher and g.status=1")
	public List<SubjectGradesDto> findGradesWithSubjectBySemesterAndStudentAndTeacher(Integer student, Integer teacher, ESemester semester);

	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectGradesDto(tsd.teachingSubject, g) from GradeEntity g join g.teacher_subject_department tsd where g.semester=:semester and g.student.id=:student and g.status=1 and tsd.teachingTeacher.id=:teacher and tsd.teachingSubject.id=:subject")
	public List<SubjectGradesDto> findGradesWithSubjectBySemesterAndStudentAndSubjectAndTeacher(Integer student, Integer teacher, Integer subject, ESemester semester);

	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectFinalGradesDto(tsd.teachingSubject, ROUND(AVG(g.gradeValue))) from GradeEntity g join g.teacher_subject_department tsd where g.semester=:semester and g.student.id=:student and g.status=1 group by g.student, tsd.teachingSubject")
	public List<SubjectFinalGradesDto> findFinalGradesWithSubjectBySemesterAndStudent(Integer student, ESemester semester);

}
