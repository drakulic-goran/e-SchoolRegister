package com.iktpreobuka.projekat_za_kraj.entities.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.AdminEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserAccountEntity;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class SearchAdminsDto {

	@JsonView(Views.Student.class)
	private AdminEntity admin;
	@JsonView(Views.Student.class)
	private UserAccountEntity account;
	
	public SearchAdminsDto() {
		super();
	}

	public SearchAdminsDto(AdminEntity admin, UserAccountEntity account) {
		super();
		this.admin = admin;
		this.account = account;
	}
	

	public AdminEntity getAdmin() {
		return admin;
	}

	public void setAdmin(AdminEntity admin) {
		this.admin = admin;
	}

	public UserAccountEntity getAccount() {
		return account;
	}

	public void setAccount(UserAccountEntity account) {
		this.account = account;
	}
}
