package com.iktpreobuka.projekat_za_kraj.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.projekat_za_kraj.entities.AdminEntity;
import com.iktpreobuka.projekat_za_kraj.entities.ParentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserAccountEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;

public interface UserAccountRepository extends CrudRepository<UserAccountEntity, Integer> {
	
	public UserAccountEntity getById(Integer id);
	public UserAccountEntity getByUsername(String username);
	public UserAccountEntity getByUser(UserEntity user);
	@Query("select ua.user from UserAccountEntity ua where ua.username=:username and ua.status=:status")
	public UserEntity findUserByUsernameAndStatusLike(String username, Integer status);
	@Query("select ua.user from UserAccountEntity ua where ua.username=:username and ua.status=:status")
	public TeacherEntity findTeacherByUsernameAndStatusLike(String username, Integer status);
	public UserAccountEntity findByUserAndAccessRoleLikeAndStatusLike(UserEntity user, EUserRole eUserRole, Integer status);
	@Query("select ua from UserAccountEntity ua join ua.user us where us.id=:userId and ua.accessRole=:eUserRole and ua.status=:status")
	public UserAccountEntity findByUserIdAndAccessRoleLikeAndStatusLike(Integer userId, EUserRole eUserRole, Integer status);
	public Iterable<UserAccountEntity> findByAccessRoleLikeAndStatusLike(EUserRole eUserRole, Integer status);
	public Iterable<UserAccountEntity> findByStatusLike(Integer status);
	public UserAccountEntity findByIdAndStatusLike(Integer id, Integer status);
	public UserAccountEntity findByUserAndAccessRoleAndStatusLike(UserEntity user, EUserRole role, Integer status);
	public UserAccountEntity findByUsernameAndStatusLike(String name, Integer Status);
	public UserAccountEntity findByUserAndAccessRoleLike(TeacherEntity user, EUserRole role);
	public UserAccountEntity findByUserAndAccessRoleLike(StudentEntity user, EUserRole role);
	public UserAccountEntity findByUserAndAccessRoleLike(ParentEntity user, EUserRole role);
	public UserAccountEntity findByUserAndAccessRoleLike(AdminEntity user, EUserRole role);
}
