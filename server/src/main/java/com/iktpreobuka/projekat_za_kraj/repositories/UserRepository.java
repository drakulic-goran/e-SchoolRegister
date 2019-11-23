package com.iktpreobuka.projekat_za_kraj.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {

	public UserEntity getById(Integer id);

	/*public Iterable<UserEntity> findByStatusLike(Integer i);
*/
	public UserEntity findByJMBG(String getjMBG);

	public UserEntity getByJMBG(String jMBG);

	//public Iterable<UserEntity> findAllByStatusLike(Integer status);

	/*public UserEntity findByJMBGAndStatusLike(String jmbg, Integer status);*/
}
