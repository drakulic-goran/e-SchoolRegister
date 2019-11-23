package com.iktpreobuka.projekat_za_kraj.entities.dto;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class UserSearchDTO {
	
	@JsonView(Views.Student.class)
	protected Integer id;
	@JsonView(Views.Student.class)
	@Pattern(regexp="^(ROLE_ADMIN|ROLE_TEACHER|ROLE_PARENT|ROLE_STUDENT)$",message="Role is not valid, must be ROLE_ADMIN, ROLE_TEACHER, ROLE_PARENT or ROLE_STUDENT")
	private String accessRole;
	
	
	public UserSearchDTO() {
		super();
	}
	
	public UserSearchDTO(Integer id,
			@Pattern(regexp = "^(ROLE_ADMIN|ROLE_TEACHER|ROLE_PARENT|ROLE_STUDENT)$", message = "Role is not valid, must be ROLE_ADMIN, ROLE_TEACHER, ROLE_PARENT or ROLE_STUDENT") String accessRole) {
		super();
		this.id = id;
		this.accessRole = accessRole;
	}
	
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getAccessRole() {
		return accessRole;
	}
	
	public void setAccessRole(String accessRole) {
		this.accessRole = accessRole;
	}

}
