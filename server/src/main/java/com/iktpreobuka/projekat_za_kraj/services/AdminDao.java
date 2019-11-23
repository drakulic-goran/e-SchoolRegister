package com.iktpreobuka.projekat_za_kraj.services;

import com.iktpreobuka.projekat_za_kraj.entities.AdminEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.AdminDto;

public interface AdminDao {

	public UserEntity addNewAdmin(UserEntity loggedUser, AdminDto newAdmin) throws Exception;

	public void modifyAdmin(UserEntity loggedUser, AdminEntity admin, AdminDto newAdmin) throws Exception;

	public void deleteAdmin(UserEntity loggedUser, AdminEntity admin) throws Exception;

	public void undeleteAdmin(UserEntity loggedUser, AdminEntity admin) throws Exception;

	public void archiveAdmin(UserEntity loggedUser, AdminEntity admin) throws Exception;

}
