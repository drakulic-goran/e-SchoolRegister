package com.iktpreobuka.projekat_za_kraj.controllers;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.controllers.util.RESTError;
import com.iktpreobuka.projekat_za_kraj.entities.ParentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.models.EmailObject;
import com.iktpreobuka.projekat_za_kraj.repositories.StudentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserAccountRepository;
import com.iktpreobuka.projekat_za_kraj.security.Views;
import com.iktpreobuka.projekat_za_kraj.services.EmailService;

@Controller
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "/project/email")
public class EmailController{

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private UserAccountRepository userAccountRepository;

	//private static String PATH_TO_ATTACHMENT= "D://proba//slika.jpg";
	
	private final Logger logger= (Logger) LoggerFactory.getLogger(this.getClass());


	@Secured("ROLE_TEACHER")
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.POST, value = "/teacher")
	public ResponseEntity<?> sendMessageToParents(@RequestBody EmailObject object, Principal principal) {
		logger.info("################ /project/grades/parent/{id}/getByIdParent started.");
		logger.info("Logged user: " + principal.getName());
		UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
		logger.info("Logged user identified.");

		if(object==null|| object.getSubject()==null|| object.getText()==null) {
			//return null;
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
		try {
			if (object.getTo() == null) {
				List<StudentEntity> students = studentRepository.findByPrimaryTeacherId(loggedUser.getId());
				for (StudentEntity student : students) {
					for (ParentEntity p : student.getParents()) 
						if (p.getStatus() == 1) {
							object.setTo(p.getEmail());
							emailService.sendSimpleMessage(object);
						}
				}
			} else {
				StudentEntity student = studentRepository.findByIdAndStatusLike(Integer.parseInt(object.getTo()), 1);
				for (ParentEntity p : student.getParents()) 
						if (p.getStatus() == 1) {
							object.setTo(p.getEmail());
							emailService.sendSimpleMessage(object);
						}
			}
			
			return new ResponseEntity<>("Your mail has been sent!", HttpStatus.OK);
			//return "Your mail has been sent!";
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST, value = "/admin")
	public ResponseEntity<?> sendMessageToAll(@RequestBody EmailObject object, Principal principal) {
		logger.info("################ /project/grades/parent/{id}/getByIdParent started.");
		logger.info("Logged user: " + principal.getName());

		if(object==null|| object.getSubject()==null|| object.getText()==null) {
			//return null;
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
		try {
			if (object.getTo() == null) {
				Iterable<StudentEntity> students = studentRepository.findByStatusLike(1);
				for (StudentEntity student : students) {
					for (ParentEntity p : student.getParents()) 
						if (p.getStatus() == 1) {
							object.setTo(p.getEmail());
							emailService.sendSimpleMessage(object);
						}
				}
			} else {
				StudentEntity student = studentRepository.findByIdAndStatusLike(Integer.parseInt(object.getTo()), 1);
				for (ParentEntity p : student.getParents())
						if (p.getStatus() == 1) {
							object.setTo(p.getEmail());
							emailService.sendSimpleMessage(object);
						}
			}
			
			return new ResponseEntity<>("Your mail has been sent!", HttpStatus.OK);
			//return "Your mail has been sent!";
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}