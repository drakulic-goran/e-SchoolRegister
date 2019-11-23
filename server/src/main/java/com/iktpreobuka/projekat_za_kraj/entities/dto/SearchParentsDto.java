package com.iktpreobuka.projekat_za_kraj.entities.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.ParentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserAccountEntity;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class SearchParentsDto {

	@JsonView(Views.Student.class)
	private ParentEntity user;
	@JsonView(Views.Student.class)
	private UserAccountEntity account;
	
	
	public SearchParentsDto() {
		super();
	}
	
	public SearchParentsDto(ParentEntity user, UserAccountEntity account) {
		super();
		this.user = user;
		this.account = account;
	}
	
	
	public ParentEntity getUser() {
		return user;
	}
	
	public void setUser(ParentEntity user) {
		this.user = user;
	}
	
	public UserAccountEntity getAccount() {
		return account;
	}
	
	public void setAccount(UserAccountEntity account) {
		this.account = account;
	}

}
