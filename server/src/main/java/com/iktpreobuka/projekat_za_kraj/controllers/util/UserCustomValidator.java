package com.iktpreobuka.projekat_za_kraj.controllers.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.iktpreobuka.projekat_za_kraj.entities.dto.AdminDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.ParentDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.StudentDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.TeacherDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.UserAccountDto;

@Component 
public class UserCustomValidator implements Validator {
		
	@Override 
	public boolean supports(Class <?> myClass) { 
		return AdminDto.class.equals(myClass) || TeacherDto.class.equals(myClass) || ParentDto.class.equals(myClass) || StudentDto.class.equals(myClass) || UserAccountDto.class.equals(myClass);
		}
	
	@Override public void validate(Object target, Errors errors) { 
		if (target instanceof AdminDto) {
			AdminDto user = (AdminDto) target;
			if(user.getPassword() != null && !user.getPassword().equals(user.getConfirmedPassword())) { 
				errors.reject("400", "Passwords must be the same."); 
				}
			} else if (target instanceof TeacherDto) {
				TeacherDto user = (TeacherDto) target;
				if(user.getPassword() != null && !user.getPassword().equals(user.getConfirmedPassword())) { 
					errors.reject("400", "Passwords must be the same."); 
					}
			} else if (target instanceof ParentDto) {
				ParentDto user = (ParentDto) target;
				if(user.getPassword() != null && !user.getPassword().equals(user.getConfirmedPassword())) { 
					errors.reject("400", "Passwords must be the same."); 
					}
			} else if (target instanceof StudentDto) {
				StudentDto user = (StudentDto) target;
				if(user.getPassword() != null && !user.getPassword().equals(user.getConfirmedPassword())) { 
					errors.reject("400", "Passwords must be the same."); 
					}
			} else if (target instanceof UserAccountDto) {
				UserAccountDto user = (UserAccountDto) target;
				if(user.getPassword() != null && !user.getPassword().equals(user.getConfirmedPassword())) { 
					errors.reject("400", "Passwords must be the same."); 
					}
			}

		}
}
