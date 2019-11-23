package com.iktpreobuka.projekat_za_kraj.repositories;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.iktpreobuka.projekat_za_kraj.entities.AdminEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SearchAdminsDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;

@Repository
public interface AdminRepository extends CrudRepository<AdminEntity, Integer> {
	
	public Optional<AdminEntity> findById(Integer id);
	public AdminEntity getById(Integer id);
	//public AdminEntity getByUserAccount(UserAccountEntity id);
	
	public AdminEntity getByEmail(String email);
	
	public AdminEntity findByIdAndStatusLike(Integer id, Integer status);
	
	public AdminEntity getByJMBG(String getjMBG);
	
	public Iterable<AdminEntity> findByStatusLike(Integer i);

	//@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.AdminDto(u.firstName, u.lastName, u.jMBG, u.gender.toString(), u.mobilePhoneNumber, u.email, ua.username, ua.accessRole.toString(), ua.password, ua.password) from AdminEntity u join u.accounts ua where ua.accessRole=:role and u.status=:status")
	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.SearchAdminsDto(u, ua) from AdminEntity u join u.accounts ua where ua.accessRole=:role and u.status=:status and ua.status=1")
	public Iterable<SearchAdminsDto> findByStatusWithUserAccount(@Param("status") Integer status, @Param("role") EUserRole role);
	
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

	public void save(@Valid UserEntity user);
	public Object getByEmailAndStatusLike(String email, Integer status);
	public Object getByJMBGAndStatusLike(String jMBG, Integer status);
	public Object getByJMBGAndRoleAndStatusLike(String jmbg, EUserRole role, Integer status);
	@Transactional
    @Modifying
    @Query (value ="INSERT INTO admin (user_id, e_mail, mobile_phone_number, status, created_by) VALUES (:user, :email, :mobile, 1, :logged)", nativeQuery = true)
	public void addAdminFromExistUser(@Param("mobile") String mobile, @Param("email") String email, @Param("user") Integer user, @Param("logged") Integer logged);
	public AdminEntity getByIdAndStatusLike(Integer userId, Integer status);

}
