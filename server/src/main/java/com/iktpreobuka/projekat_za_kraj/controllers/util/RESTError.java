package com.iktpreobuka.projekat_za_kraj.controllers.util;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class RESTError {
	
	@JsonView(Views.Student.class)
	private int code;
	@JsonView(Views.Student.class)
	private String message;
	
	public RESTError(int code, String message) {
		this.code= code;
		this.message= message;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}

}
