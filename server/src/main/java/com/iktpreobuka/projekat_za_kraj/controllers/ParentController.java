package com.iktpreobuka.projekat_za_kraj.controllers;

import java.security.Principal;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.controllers.util.RESTError;
import com.iktpreobuka.projekat_za_kraj.controllers.util.UserCustomValidator;
import com.iktpreobuka.projekat_za_kraj.entities.ParentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserAccountEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.ParentDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SearchParentsDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;
import com.iktpreobuka.projekat_za_kraj.repositories.ParentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.StudentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserAccountRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserRepository;
import com.iktpreobuka.projekat_za_kraj.security.Views;
import com.iktpreobuka.projekat_za_kraj.services.ParentDao;
import com.iktpreobuka.projekat_za_kraj.services.UserAccountDao;

@Controller
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value= "/project/parent")
public class ParentController {

	@Autowired
	private UserAccountDao userAccountDao;

	@Autowired
	private ParentDao parentDao;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private ParentRepository parentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired 
	private UserCustomValidator userValidator;

	@InitBinder
	protected void initBinder(final WebDataBinder binder) { 
		binder.addValidators(userValidator); 
		}

	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	private String createErrorMessage(BindingResult result) { 
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
		}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll(Principal principal) {
		logger.info("################ /project/parent/getAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<ParentEntity> users= parentRepository.findByStatusLike(1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<ParentEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/parent/getById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			ParentEntity user= parentRepository.findByIdAndStatusLike(id, 1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ParentEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/withaccount")
	public ResponseEntity<?> getAllWithUserAccount(Principal principal) {
		logger.info("################ /project/parent/withaccount/getAllWithUserAccount started.");
		logger.info("Logged username: " + principal.getName());
		try {
			//Iterable<ParentEntity> users= parentRepository.findByStatusLike(1);
			Iterable<SearchParentsDto> users= parentRepository.findByStatusWithUserAccount(1, EUserRole.ROLE_PARENT);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<SearchParentsDto>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_PARENT")
	@JsonView(Views.Parent.class)
	@RequestMapping(method = RequestMethod.GET, value = "/logged")
	public ResponseEntity<?> getLoggedParent(Principal principal) {
		logger.info("################ /project/parent/getLoggedParent started.");
		logger.info("Logged username: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			ParentEntity user= parentRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ParentEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/deleted")
	public ResponseEntity<?> getAllDeleted(Principal principal) {
		logger.info("################ /project/admin/deleted/getAllDeleted started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<ParentEntity> users= parentRepository.findByStatusLike(0);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<ParentEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/deleted/{id}")
	public ResponseEntity<?> getDeletedById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/admin/deleted/getDeletedById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			ParentEntity user= parentRepository.findByIdAndStatusLike(id, 0);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ParentEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived")
	public ResponseEntity<?> getAllArchived(Principal principal) {
		logger.info("################ /project/parent/archived/getAllArchived started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<ParentEntity> users= parentRepository.findByStatusLike(-1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<ParentEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/{id}")
	public ResponseEntity<?> getArchivedById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/parent/archived/getArchivedById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			ParentEntity user= parentRepository.findByIdAndStatusLike(id, -1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ParentEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewParent(@Valid @RequestBody ParentDto newParent, Principal principal, BindingResult result) {
		logger.info("################ /project/parent/addNewParent started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newParent == null) {
			logger.info("---------------- New parent is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "New parent is null."), HttpStatus.BAD_REQUEST);
	      }
		if (newParent.getFirstName() == null || newParent.getLastName() == null || newParent.getEmail() == null || newParent.getGender() == null || newParent.getjMBG() == null) {
			logger.info("---------------- Some atributes is null.");
			return new ResponseEntity<RESTError>(new RESTError(5, "Some atributes is null."), HttpStatus.BAD_REQUEST);
		}
		UserEntity user = new ParentEntity();
		try {
			if (newParent.getjMBG() != null && parentRepository.getByJMBG(newParent.getjMBG()) != null) {
				logger.info("---------------- JMBG already exist.");
				return new ResponseEntity<RESTError>(new RESTError(2, "JMBG already exist."), HttpStatus.NOT_ACCEPTABLE);
			}
			if (newParent.getEmail() != null && parentRepository.getByEmail(newParent.getEmail()) != null) {
				logger.info("---------------- Email already exist.");
				return new ResponseEntity<RESTError>(new RESTError(2, "Email already exist."), HttpStatus.NOT_ACCEPTABLE);
			}
			if (newParent.getAccessRole() != null && !newParent.getAccessRole().equals("ROLE_PARENT")) {
				logger.info("---------------- Access role must be ROLE_PARENT.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "Access role must be ROLE_PARENT."), HttpStatus.NOT_ACCEPTABLE);
			}		
			if (newParent.getUsername() != null && userAccountRepository.getByUsername(newParent.getUsername()) != null) {
				logger.info("---------------- Username already exists.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "Username already exists."), HttpStatus.NOT_ACCEPTABLE);
		    }
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			user = parentDao.addNewParent(loggedUser, newParent);
			logger.info("Parent created.");
			if (newParent.getUsername() != null && newParent.getPassword() != null && newParent.getConfirmedPassword() != null && newParent.getPassword().equals(newParent.getConfirmedPassword())) {
				UserAccountEntity account = userAccountDao.addNewUserAccount(loggedUser, user, newParent.getUsername(), EUserRole.ROLE_PARENT, newParent.getPassword());
				logger.info("Account created.");
				return new ResponseEntity<>(account, HttpStatus.OK);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ This is an exception message: " + e.getMessage());
			if (user != null && parentRepository.findByIdAndStatusLike(user.getId(), 1) != null) {
				parentRepository.deleteById(user.getId());
				logger.error("++++++++++++++++ Because of exeption Parent with Id " + user.getId().toString() + " deleted.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_PARENT", "ROLE_ADMIN"})
	@JsonView(Views.Parent.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> modifyParent(@PathVariable Integer id, @Valid @RequestBody ParentDto updateParent, Principal principal, BindingResult result) {
		logger.info("################ /project/parent/{id}/modifyParent started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<RESTError>(new RESTError(6, createErrorMessage(result)), HttpStatus.BAD_REQUEST); 
			}
		if (updateParent == null) {
			logger.info("---------------- New parent is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "New parent is null."), HttpStatus.BAD_REQUEST);
	      }
		ParentEntity user = new ParentEntity();
		try {
			user = parentRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Parent not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Parent not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Parent identified.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_PARENT, 1);
			logger.info("Parent's user account identified.");
			if (updateParent.getjMBG() != null && updateParent.getjMBG().equals(user.getjMBG()) ) {
				logger.info("---------------- Same JMBG.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "Same JMBG."), HttpStatus.NOT_ACCEPTABLE);
			}
			if (updateParent.getEmail() != null && updateParent.getEmail().equals(user.getEmail()) ) {
				logger.info("---------------- Same email.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "Same email."), HttpStatus.NOT_ACCEPTABLE);
			}
			if (updateParent.getUsername() != null && updateParent.getUsername().equals(account.getUsername()) ) {
				logger.info("---------------- Same username.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "Same username."), HttpStatus.NOT_ACCEPTABLE);
			}
			if (updateParent.getjMBG() != null && userRepository.getByJMBG(updateParent.getjMBG()) != null) {
				logger.info("---------------- JMBG already exist.");
				return new ResponseEntity<RESTError>(new RESTError(2, "JMBG already exist."), HttpStatus.NOT_ACCEPTABLE);
			}
			if (updateParent.getEmail() != null && parentRepository.getByEmail(updateParent.getEmail()) != null) {
				logger.info("---------------- Email already exist.");
				return new ResponseEntity<RESTError>(new RESTError(2, "Email already exist."), HttpStatus.NOT_ACCEPTABLE);
			}
			if (updateParent.getAccessRole() != null && !updateParent.getAccessRole().equals("ROLE_PARENT")) {
				logger.info("---------------- Access role must be ROLE_PARENT.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "Access role must be ROLE_PARENT."), HttpStatus.NOT_ACCEPTABLE);
			}		
			if (updateParent.getUsername() != null && userAccountRepository.getByUsername(updateParent.getUsername()) != null) {
				logger.info("---------------- Username already exists.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "Username already exists."), HttpStatus.NOT_ACCEPTABLE);
		    }
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updateParent.getFirstName() != null || updateParent.getLastName() != null || updateParent.getEmail() != null || updateParent.getGender() != null || updateParent.getjMBG() != null) {
				parentDao.modifyParent(loggedUser, user, updateParent);
				logger.info("Parent modified.");
			}
			if (account != null) {
				if (updateParent.getUsername() != null && !updateParent.getUsername().equals("") && !updateParent.getUsername().equals(" ") && userAccountRepository.getByUsername(updateParent.getUsername()) == null) {
					userAccountDao.modifyAccountUsername(loggedUser, account, updateParent.getUsername());
					logger.info("Username modified.");					
				}
				if (updateParent.getPassword() != null && !updateParent.getPassword().equals("") && !updateParent.getPassword().equals(" ") && updateParent.getConfirmedPassword() != null && updateParent.getPassword().equals(updateParent.getConfirmedPassword())) {
					userAccountDao.modifyAccountPassword(loggedUser, account, updateParent.getPassword());
					logger.info("Password modified.");
				}
				logger.info("Account modified.");
				return new ResponseEntity<>(account, HttpStatus.OK);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ This is an exception message: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/child/{c_id}")
	public ResponseEntity<?> addChild(@PathVariable Integer id, @PathVariable Integer c_id, Principal principal) {
		logger.info("################ /project/parent/{id}/child/{c_id}/addChild started.");
		logger.info("Logged user: " + principal.getName());
		ParentEntity user = new ParentEntity();
		try {
			user = parentRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Parent not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Parent not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Parent identified.");
			StudentEntity student = studentRepository.findByIdAndStatusLike(c_id, 1);
			if (student == null) {
				logger.info("---------------- Student not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Student not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Student identified.");
			if (user.getStudents().contains(student)) {
				logger.info("---------------- Student already exist.");
				return new ResponseEntity<RESTError>(new RESTError(2, "Student already exist."), HttpStatus.NOT_ACCEPTABLE);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			parentDao.addStudentToParent(loggedUser, user, student);		
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/remove/child/{c_id}")
	public ResponseEntity<?> removeChild(@PathVariable Integer id, @PathVariable Integer c_id, Principal principal) {
		logger.info("################ /project/parent/{id}/remove/child/{c_id}/addChild started.");
		logger.info("Logged user: " + principal.getName());
		ParentEntity user = new ParentEntity();
		try {
			user = parentRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Parent not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Parent not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Parent identified.");
			StudentEntity student = studentRepository.findByIdAndStatusLike(c_id, 1);
			if (student == null) {
				logger.info("---------------- Student not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Student not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Student identified.");
			if (!user.getStudents().contains(student)) {
				logger.info("---------------- Student not parent child.");
				return new ResponseEntity<RESTError>(new RESTError(4, "Student not parent child."), HttpStatus.NOT_FOUND);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			parentDao.removeStudentFromParent(loggedUser, user, student);		
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/archive/{id}")
	public ResponseEntity<?> archive(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/parent/archive/archiveDeleted started.");
		logger.info("Logged user: " + principal.getName());
		ParentEntity user = new ParentEntity();
		try {
			user = parentRepository.getById(id);
			if (user == null || user.getStatus() == -1) {
				logger.info("---------------- Parent not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Parent not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Parent for archiving identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (id == loggedUser.getId()) {
				logger.info("---------------- Selected Id is ID of logged User: Cann't archive yourself.");
				return new ResponseEntity<RESTError>(new RESTError(3, "Selected Id is ID of logged User: Cann't archive yourself."), HttpStatus.FORBIDDEN);
		      }	
			parentDao.archiveParent(loggedUser, user);
			logger.info("Parent archived.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLike(user, EUserRole.ROLE_PARENT);
			logger.info("Parent's user account identified.");
			if (account != null && account.getStatus() != -1) {
				userAccountDao.archiveAccount(loggedUser, account);
				logger.info("Account archived.");
				return new ResponseEntity<UserAccountEntity>(account, HttpStatus.OK);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			if (user != null && user.getStatus() == -1) {
				user.setStatusInactive();
				parentRepository.save(user);
				logger.error("++++++++++++++++ Because of exeption Parent with Id " + user.getId().toString() + " deleted.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/undelete/{id}")
	public ResponseEntity<?> unDelete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/parent/undelete/{id}/unDelete started.");
		logger.info("Logged user: " + principal.getName());
		ParentEntity user = new ParentEntity();
		try {
			user = parentRepository.findByIdAndStatusLike(id, 0);
			if (user == null) {
				logger.info("---------------- Parent not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Parent not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Parent for undeleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			parentDao.undeleteParent(loggedUser, user);
			logger.info("Parent undeleted.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_PARENT, 1);
			logger.info("Parent's user account identified.");
			if (account != null) {
				userAccountDao.undeleteAccount(loggedUser, account);
				logger.info("Account undeleted.");
				return new ResponseEntity<UserAccountEntity>(account, HttpStatus.OK);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			if (user != null && user.getStatus() == 1) {
				user.setStatusInactive();
				parentRepository.save(user);
				logger.error("++++++++++++++++ Because of exeption Parent with Id " + user.getId().toString() + " deleted.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/parent/{id}/delete started.");
		logger.info("Logged user: " + principal.getName());
		ParentEntity user = new ParentEntity();
		try {
			user = parentRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Parent not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Parent not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Parent for deleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (id == loggedUser.getId()) {
				logger.info("---------------- Selected Id is ID of logged User: Cann't delete yourself.");
				return new ResponseEntity<RESTError>(new RESTError(3, "Selected Id is ID of logged User: Cann't delete yourself."), HttpStatus.FORBIDDEN);
		      }	
			parentDao.deleteParent(loggedUser, user);
			logger.info("Parent deleted.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_PARENT, 1);
			logger.info("Parent's user account identified.");
			if (account != null) {
				userAccountDao.deleteAccount(loggedUser, account);
				logger.info("Account deleted.");
				return new ResponseEntity<UserAccountEntity>(account, HttpStatus.OK);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			if (user != null && user.getStatus() == 0) {
				user.setStatusActive();
				parentRepository.save(user);
				logger.error("++++++++++++++++ Because of exeption Parent with Id " + user.getId().toString() + " activated.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

}
