package com.iktpreobuka.projekat_za_kraj.controllers;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.controllers.util.RESTError;
import com.iktpreobuka.projekat_za_kraj.entities.UserAccountEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.repositories.UserAccountRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserRepository;
import com.iktpreobuka.projekat_za_kraj.security.Views;

//@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value= "/project/users")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserAccountRepository userAccountRepository;

	
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	

	//@CrossOrigin
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll(Principal principal) {
		logger.info("################ /project/users/getAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<UserEntity> users = userRepository.findAll();
			logger.info("All users (active, deleted, archived).");
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<UserEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("---------------- This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}		
	
	//@Secured("ROLE_STUDENT")
	@JsonView(Views.Student.class)
	@RequestMapping(method = RequestMethod.GET, value = "/LogIn")
	//public boolean LogIn(Principal principal) {
	public ResponseEntity<?> LogIn(Principal principal) {
		logger.info("################ /project/users/LogIn started.");
		logger.info("Logged username: " + principal.getName());
		try {
			/*UserAccountEntity loggedUser = userAccountRepository.getByUsername(principal.getName());
			UserAccountDto userLogged = new UserAccountDto();
			userLogged.setUsername(loggedUser.getUsername());
			userLogged.setPassword(loggedUser.getPassword());
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<UserAccountDto>(userLogged, HttpStatus.OK);*/
			//return true;
			UserAccountEntity loggedUser= userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1);
			if (loggedUser == null) {
				logger.info("---------------- User not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "User not found."), HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- LogIn finished OK.");
			return new ResponseEntity<UserAccountEntity>(loggedUser, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("---------------- This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}		

}



	
	/*########################
	@Secured({"ROLE_STUDENT", "ROLE_PARENT", "ROLE_TEACHER", "ROLE_ADMIN"})
	//@Secured("ROLE_STUDENT")
	@JsonView(Views.Student.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll(Principal principal) {
		logger.info("This is an info message: /project/users/getAll started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		try {
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			if (loggedUser.getAccessRole().equals(EUserRole.ROLE_STUDENT)) {
				//StudentEntity user= studentRepository.getByUserAccount(loggedUser);
				StudentEntity user= studentRepository.getById(loggedUser.getUser().getId());
				logger.info("This is an info message: getAll Student finished OK.");
				return new ResponseEntity<StudentEntity>(user, HttpStatus.OK);
			} else if (loggedUser.getAccessRole().equals(EUserRole.ROLE_PARENT)) {
				List<StudentEntity> user= studentRepository.findByParent(loggedUser.getUser().getId());
				logger.info("This is an info message: getAll Parent finished OK.");
				return new ResponseEntity<List<StudentEntity>>(user, HttpStatus.OK);
			} else if (loggedUser.getAccessRole().equals(EUserRole.ROLE_TEACHER)) {
				//TeacherEntity teacher = teacherRepository.getById(loggedUser.getUser().getId());
				List<StudentDto> user= studentRepository.findByPrimaryTeacher(loggedUser.getUser().getId());
				logger.info("This is an info message: getAll Primary teacher finished OK." + loggedUser.getUser().getId());
				return new ResponseEntity<List<StudentDto>>(user, HttpStatus.OK);
			}
			Iterable<UserEntity> users= userRepository.findAll();
			logger.info("This is an info message: getAll Admin finished OK.");
			return new ResponseEntity<Iterable<UserEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/