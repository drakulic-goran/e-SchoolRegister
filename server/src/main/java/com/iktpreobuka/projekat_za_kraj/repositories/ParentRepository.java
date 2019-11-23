package com.iktpreobuka.projekat_za_kraj.repositories;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.iktpreobuka.projekat_za_kraj.entities.ParentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.ParentDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SearchParentsDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;

public interface ParentRepository extends CrudRepository<ParentEntity, Integer> {

	public Optional<ParentEntity> findById(Integer id);
	public ParentEntity getById(Integer id);
	//public ParentEntity getByUserAccount(UserAccountEntity id);
	
	//public ParentEntity findByUserAccountAndStatusLike(UserAccountEntity loggedUser, Integer status);

	public ParentEntity getByJMBG(String JMBG);

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

	public void save(@Valid ParentDto newUser);
	public void save(UserEntity user);
	public ParentEntity findByIdAndStatusLike(Integer id, Integer i);
	public Iterable<ParentEntity> findByStatusLike(Integer status);
	public Object getByEmail(String email);
	public Object getByJMBGAndStatusLike(String jMBG, Integer status);
	public Object getByEmailAndStatusLike(String email, Integer status);
	@Transactional
    @Modifying
    @Query (value ="INSERT INTO parent (user_id, e_mail, status, created_by) VALUES (:user, :email, 1, :logged)", nativeQuery = true)
	public void addAdminFromExistUser(String email, Integer user, Integer logged);
	
	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.SearchParentsDto(u, ua) from ParentEntity u join u.accounts ua where ua.accessRole=:role and u.status=:status and ua.status=1")
	public Iterable<SearchParentsDto> findByStatusWithUserAccount(Integer status, EUserRole role);

}
