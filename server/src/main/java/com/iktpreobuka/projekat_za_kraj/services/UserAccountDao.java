package com.iktpreobuka.projekat_za_kraj.services;

import com.iktpreobuka.projekat_za_kraj.entities.UserAccountEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.AdminDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.ParentDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;

public interface UserAccountDao {

	public UserAccountEntity addNewUserAccount(UserEntity loggedUser, UserEntity user, String username, EUserRole role, String password) throws Exception;
	public void modifyAccount(UserEntity loggedUser, UserAccountEntity account, AdminDto updateAdmin) throws Exception;
	public void modifyAccountUsername(UserEntity loggedUser, UserAccountEntity account, String username) throws Exception;
	public void modifyAccountPassword(UserEntity loggedUser, UserAccountEntity account, String password) throws Exception;
	public void deleteAccount(UserEntity loggedUser, UserAccountEntity account) throws Exception;
	public void undeleteAccount(UserEntity loggedUser, UserAccountEntity account) throws Exception;
	public void archiveAccount(UserEntity loggedUser, UserAccountEntity account) throws Exception;
	public void modifyAccount(UserEntity loggedUser, UserAccountEntity account, ParentDto updateParent) throws Exception;
	public void modifyAccount(UserEntity loggedUser, UserAccountEntity account, String username, String password) throws Exception;
	public void modifyAccountUserAndAccessRole(UserEntity loggedUser, UserAccountEntity account, UserEntity user, EUserRole role) throws Exception;
	public void modifyAccountUser(UserEntity loggedUser, UserAccountEntity account, UserEntity user) throws Exception;
	public void modifyAccountAccessRole(UserEntity loggedUser, UserAccountEntity account, EUserRole role) throws Exception;
	
}
