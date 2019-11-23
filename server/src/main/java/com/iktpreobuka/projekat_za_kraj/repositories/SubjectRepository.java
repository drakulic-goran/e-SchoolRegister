package com.iktpreobuka.projekat_za_kraj.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.StudentSubjectDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectDepartmentClassSchoolYearDTO;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectTeacherDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.TeacherSubjectDepartmentDTO;
import com.iktpreobuka.projekat_za_kraj.entities.dto.TrioStudentSubjecTeachertDto;

public interface SubjectRepository extends CrudRepository<SubjectEntity, Integer> {
	
	public SubjectEntity getById(Integer id);
	
	public Iterable<SubjectEntity> findByStatusLike(Integer status);

	@Query("select s from SubjectEntity s join s.teachers t where t.teacher.id=:teacher and s.status=1 and t.status=1")
	public Iterable<SubjectEntity> findByTeacher(Integer teacher);

	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.TeacherSubjectDepartmentDTO(s, tsd) from SubjectEntity s join s.teachers t join t.teacher te join te.subjects_departments tsd where s.status=:status and t.status=1 and te.status=1 and tsd.status=1 and tsd.teachingSubject=s")
	public Iterable<TeacherSubjectDepartmentDTO> findAllWithDepartmentsStatusLike(Integer status);

	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.TeacherSubjectDepartmentDTO(s, tsd) from SubjectEntity s join s.teachers t join t.teacher te join te.subjects_departments tsd where te.id=:teacher and s.status=1 and t.status=1 and te.status=1 and tsd.status=1 and tsd.teachingSubject=s")
	public Iterable<TeacherSubjectDepartmentDTO> findByTeacherWithDepartments(Integer teacher);
	
	@Query("select sub from StudentEntity s join s.departments d join d.department sd join sd.classes cl join cl.clas cd join cd.subjects su join su.subject sub where s.id=:student and s.status=1 and d.status=1 and sd.status=1 and cl.status=1 and cd.status=1 and su.status=1 and sub.status=1")
	public Iterable<SubjectEntity> findByStudent(Integer student);
	
	//@Query("select tsd from TeacherSubjectDepartmentEntity tsd join tsd.teachingDepartment td join td.students s where s.student.id=:student and tsd.teachingSubject.status=1 and s.student.status=1 and td.status=1 and tsd.teachingClass.status=1 and tsd.status=1 and s.status=1")
	@Query("select tsd from TeacherSubjectDepartmentEntity tsd join tsd.teachingDepartment td join td.students s where s.student.id=:student")
	public Iterable<TeacherSubjectDepartmentEntity> findByStudentWithClassAndDepartment(Integer student);
	
	//@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectDepartmentClassSchoolYearDTO(st, tsd) from TeacherSubjectDepartmentEntity tsd join tsd.teachingDepartment td join td.students s join s.student st join st.parents p where p.id=:parent and tsd.teachingSubject.status=1 and p.status=1 and td.status=1 and tsd.teachingClass.status=1 and tsd.status=1 and s.status=1 and st.status=1")
	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectDepartmentClassSchoolYearDTO(st, tsd) from TeacherSubjectDepartmentEntity tsd join tsd.teachingDepartment td join td.students s join s.student st join st.parents p where p.id=:parent")
	public Iterable<SubjectDepartmentClassSchoolYearDTO> findByParentWithClassAndDepartment(Integer parent);

	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectDepartmentClassSchoolYearDTO(st, tsd) from TeacherSubjectDepartmentEntity tsd join tsd.teachingDepartment td join td.students s join s.student st")
	public Iterable<SubjectDepartmentClassSchoolYearDTO> findAllWithStudentAndClassAndDepartmentStatusLike();

	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectDepartmentClassSchoolYearDTO(st, tsd) from TeacherSubjectDepartmentEntity tsd join tsd.teachingDepartment td join td.students s join s.student st join tsd.teachingTeacher t where t.id=:teacher")
	public Iterable<SubjectDepartmentClassSchoolYearDTO> findByTeacherWithStudentAndClassAndDepartment(Integer teacher);

	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectTeacherDto(tsd.teachingTeacher, sub) from StudentEntity s join s.departments d join d.department sd join sd.classes cl join cl.clas cd join cd.subjects su join su.subject sub join sub.teachers_departments tsd where s.id=:student and s.status=1 and d.status=1 and sd.status=1 and cl.status=1 and cd.status=1 and su.status=1 and sub.status=1 and tsd.status=1")
	public Iterable<SubjectTeacherDto> findSubjectAndTeacherByStudent(Integer student);
	
	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.TrioStudentSubjecTeachertDto(s, sub, tsd.teachingTeacher) from ParentEntity p join p.students s join s.departments d join d.department sd join sd.classes cl join cl.clas cd join cd.subjects su join su.subject sub join sub.teachers_departments tsd where p.id=:parent and s.status=1 and d.status=1 and cl.status=1 and su.status=1 and sub.status=1 and tsd.status=1")
	public List<TrioStudentSubjecTeachertDto> findStudentAndSubjectAndTeacherByStudent(Integer parent);

	@Query("select sub from StudentEntity s join s.departments d join d.department sd join sd.classes cl join cl.clas cd join cd.subjects su join su.subject sub where s.id=:student")
	public Iterable<SubjectEntity> findActiveAndDeletedSubjectsByStudent(Integer student);

	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.StudentSubjectDto(s, sub) from ParentEntity p join p.students s join s.departments d join d.department sd join sd.classes cl join cl.clas cd join cd.subjects su join su.subject sub where p.id=:parent and s.status=1 and d.status=1 and cl.status=1 and su.status=1 and sub.status=1")
	//@Query("select sub, s from ParentEntity p join p.students s join s.student_department sd join sd.class_department cd join cd.subjects su join su.subject sub where p=:parent")
	//public Map<SubjectEntity, StudentEntity> findByParent(UserEntity parent);
	public List<StudentSubjectDto> findByParent(Integer parent);

	public SubjectEntity findByIdAndStatusLike(Integer s_id, Integer status);

	@Query("select sub from TeacherEntity t join t.departments d join d.primaryDepartment sd join sd.classes cl join cl.clas cd join cd.subjects su join su.subject sub where t.id=:teacher and t.status=1 and d.status=1 and sd.status=1 and cl.status=1 and cd.status=1 and su.status=1 and sub.status=1")
	public Iterable<SubjectEntity> findByPrimaryTeacher(Integer teacher);

	public SubjectEntity findBySubjectName(String subjectName);

	//@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.TrioStudentSubjecTeachertDto(s, cl.teachingSubject, cl.teachingTeacher) from StudentEntity s join s.departments d join d.department sd join sd.teachers_subjects cl where s.status=1 and d.status=1 and sd.status=1 and cl.status=1")
	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.TrioStudentSubjecTeachertDto(s.student, tsd.teachingSubject, tsd.teachingTeacher) from TeacherSubjectDepartmentEntity tsd join tsd.teachingDepartment td join td.students s where tsd.teachingTeacher.status=1 and tsd.teachingSubject.status=1 and s.student.status=1 and td.status=1 and tsd.status=1 and s.status=1")
	public List<TrioStudentSubjecTeachertDto> findStudentAndSubjectAndTeacher();

	//@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.TrioStudentSubjecTeachertDto(s, sub, tsd.teachingTeacher) from StudentEntity s join s.departments d join d.department sd join sd.classes cl join cl.clas cd join cd.subjects su join su.subject sub join sub.teachers_departments tsd where s.id=:student and s.status=1 and d.status=1 and cl.status=1 and su.status=1 and sub.status=1 and tsd.status=1")
	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.TrioStudentSubjecTeachertDto(s.student, tsd.teachingSubject, tsd.teachingTeacher) from TeacherSubjectDepartmentEntity tsd join tsd.teachingDepartment td join td.students s where s.student.id=:student and tsd.teachingTeacher.status=1 and tsd.teachingSubject.status=1 and s.student.status=1 and td.status=1 and tsd.status=1 and s.status=1")
	public List<TrioStudentSubjecTeachertDto> findStudentAndSubjectAndTeacherForStudent(Integer student);

}
