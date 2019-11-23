package com.iktpreobuka.projekat_za_kraj.repositories;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SearchTeachersDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.TeacherDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;

public interface TeacherRepository extends CrudRepository<TeacherEntity, Integer> {

	public TeacherEntity getById(Integer id);
	public Optional<TeacherEntity> findById(Integer id);
	//public TeacherEntity getByUserAccount(UserAccountEntity loggedUser);
	public TeacherEntity findByIdAndStatusLike(Integer id, Integer status);
	
	public TeacherEntity getByJMBG(String getjMBG);

	
	//public TeacherEntity findByUserAccountAndStatusLike(UserAccountEntity loggedUser, int i);

	/* public UserEntity findByEmail(String email);
	public List<UserEntity> findByFirstNameOrderByEmailAsc(String name); */
	// public List<UserEntity> findByNameStartingWith(String letter);
	// @Query("select distinct u.name from UserEntity u where (u.name) like ?1%")
	/* @Query("select distinct u.firstname from UserEntity u where u.firstname like :letter%")
	public List<UserEntity> findAllByFirstLetter(String letter); 
	@Query("select s from StudentEntity s join s.parents ps where ps.id=:parentId")
	public List<UserEntity> findByParent(Integer parentId);
	
	@Query("select s from StudentEntity s join s.student_department d join PrimaryTeacherEntity pt where pt.primary_department=d.id and pt.primary_teacher=:teacherId")
	public List<UserEntity> findByPrimaryTeacher(Integer teacherId);*/

	public void save(@Valid TeacherDto newUser);
	public void save(UserEntity user);
	public Iterable<TeacherEntity> findByStatusLike(Integer status);
	public TeacherEntity getByJMBGAndStatusLike(String jMBG, Integer status);
	@Transactional
    @Modifying
    @Query (value ="INSERT INTO teacher (user_id, certificate, employment_date, status, created_by) VALUES (:user, :certificate, :employment, 1, :logged)", nativeQuery = true)
	public void addAdminFromExistUser(String certificate, String employment, Integer user, Integer logged);
	public TeacherEntity getByIdAndStatusLike(Integer userId, Integer status);
	
	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.SearchTeachersDto(u, ua) from TeacherEntity u join u.accounts ua where ua.accessRole=:role and u.status=:status and ua.status=1")
	public Iterable<SearchTeachersDto> findByStatusWithUserAccount(Integer status, EUserRole role);

}
