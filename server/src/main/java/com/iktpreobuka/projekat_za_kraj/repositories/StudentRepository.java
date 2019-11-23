package com.iktpreobuka.projekat_za_kraj.repositories;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SearchStudentsDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.StudentDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;

@Repository
public interface StudentRepository extends CrudRepository<StudentEntity, Integer> {

	public Optional<StudentEntity> findById(Integer id);
	public StudentEntity getById(Integer id);
	//public StudentEntity getByUserAccount(UserAccountEntity id);
	
	//public StudentEntity findByUserAccountAndStatusLike(UserAccountEntity loggedUser, Integer status);

	public StudentEntity getByJMBG(String getjMBG);

	/* public UserEntity findByEmail(String email);
	public List<UserEntity> findByFirstNameOrderByEmailAsc(String name); */
	// public List<UserEntity> findByNameStartingWith(String letter);
	// @Query("select distinct u.name from UserEntity u where (u.name) like ?1%")
	/* @Query("select distinct u.firstname from UserEntity u where u.firstname like :letter%")
	public List<UserEntity> findAllByFirstLetter(String letter); */
	@Query("select s from StudentEntity s join s.parents ps where ps.id=:parentId and s.status=1")
	public List<StudentEntity> findByParent(@Param("parentId") Integer parentId);
	
	// starije @Query("select s from StudentEntity s join s.student_department sd join PrimaryTeacherEntity pt join TeacherEntity t where pt.primary_department=d.id and pt.primary_teacher=t.id and t.id=:teacherId")
	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.StudentDto(s.firstName, s.lastName, s.schoolIdentificationNumber, c.classLabel, de.departmentLabel) from StudentEntity s join s.departments dep join dep.department de join de.classes cl join cl.clas c join de.teachers d join d.primaryTeacher t where t.id=:teacher and dep.status=1 and s.status=1 and cl.status=1 and d.status=1")
	public List<StudentDto> findByPrimaryTeacher(@Param("teacher") Integer teacher);

	public void save(@Valid StudentDto newUser);
	public void save(UserEntity user);
	public StudentEntity findByIdAndStatusLike(Integer loggedUser, Integer i);
	public Iterable<StudentEntity> findByStatusLike(Integer status);
	public StudentEntity getBySchoolIdentificationNumber(String schoolIdentificationNumber);
	@Transactional
    @Modifying
    @Query (value ="INSERT INTO student (user_id, enrollment_date, school_identification_number, status, created_by) VALUES (:user, :enrollment, :number, 1, :logged)", nativeQuery = true)
	public void addAdminFromExistUser(@Param("enrollment") String enrollment, @Param("number") String number, @Param("user") Integer user, @Param("logged") Integer logged);
	public Object getBySchoolIdentificationNumberAndStatusLike(String schoolIdentificationNumber, Integer status);
	public Object getByJMBGAndStatusLike(String jMBG, Integer status);
	
	@Query("select s from StudentEntity s join s.departments dep join dep.department de join de.teachers d join d.primaryTeacher t where t.id=:teacher and t.status=1 and dep.status=1 and s.status=1 and de.status=1 and d.status=1")
	public List<StudentEntity> findByPrimaryTeacherId(@Param("teacher") Integer teacher);
	
	@Query("select s from StudentEntity s join s.departments dep join dep.department de join de.teachers_subjects ts join ts.teachingTeacher tt where tt.id=:teacher and s.status=1 and dep.status=1 and de.status=1 and ts.status=1 and tt.status=1")
	public List<StudentEntity> findByTeachingTeacher(@Param("teacher") Integer teacher);
	
	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.SearchStudentsDto(u, ua) from StudentEntity u join u.accounts ua where ua.accessRole=:role and u.status=:status and ua.status=1")
	public Iterable<SearchStudentsDto> findByStatusWithUserAccount(@Param("status") Integer status, @Param("role") EUserRole role);
	
	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.SearchStudentsDto(u, ua, d, cl, clss) from StudentEntity u join u.accounts ua join u.departments deps join deps.department d join d.classes clss join clss.clas cl where ua.accessRole=:role and u.status=:status and ua.status=1 and deps.status=1 and d.status=1 and clss.status=1 and cl.status=1")
	public Iterable<SearchStudentsDto> findByStatusWithUserAccountAndDepartmentAndClass(@Param("status") Integer status, @Param("role") EUserRole role);


}