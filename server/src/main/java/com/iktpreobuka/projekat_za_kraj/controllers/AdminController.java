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
import com.iktpreobuka.projekat_za_kraj.entities.AdminEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserAccountEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.AdminDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SearchAdminsDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;
import com.iktpreobuka.projekat_za_kraj.repositories.AdminRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserAccountRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserRepository;
import com.iktpreobuka.projekat_za_kraj.security.Views;
import com.iktpreobuka.projekat_za_kraj.services.AdminDao;
import com.iktpreobuka.projekat_za_kraj.services.UserAccountDao;

@Controller
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value= "/project/admin")
public class AdminController {

	@Autowired
	private UserAccountDao userAccountDao;

	@Autowired
	private AdminDao adminDao;

	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private UserAccountRepository userAccountRepository;
	
	@Autowired
	private UserRepository userRepository;

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
		logger.info("################ /project/admin/getAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<AdminEntity> users= adminRepository.findByStatusLike(1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<AdminEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/withaccount")
	public ResponseEntity<?> getAllWithUserAccount(Principal principal) {
		logger.info("################ /project/admin/getAllWithUserAccount started.");
		logger.info("Logged username: " + principal.getName());
		try {
			//Iterable<AdminEntity> users= adminRepository.findByStatusLike(1);
			Iterable<SearchAdminsDto> users= adminRepository.findByStatusWithUserAccount(1, EUserRole.ROLE_ADMIN);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<SearchAdminsDto>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/admin/getById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			AdminEntity user= adminRepository.findByIdAndStatusLike(id, 1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/logged")
	public ResponseEntity<?> getLoggedAdmin(Principal principal) {
		logger.info("################ /project/admin/getLoggedAdmin started.");
		logger.info("Logged username: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			AdminEntity user= adminRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(user, HttpStatus.OK);
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
			Iterable<AdminEntity> users= adminRepository.findByStatusLike(0);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<AdminEntity>>(users, HttpStatus.OK);
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
			AdminEntity user= adminRepository.findByIdAndStatusLike(id, 0);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<AdminEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived")
	public ResponseEntity<?> getAllArchived(Principal principal) {
		logger.info("################ /project/admin/archived/getAllArchived started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<AdminEntity> users= adminRepository.findByStatusLike(-1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<AdminEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/{id}")
	public ResponseEntity<?> getArchivedById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/admin/archived/getArchivedById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			AdminEntity user= adminRepository.findByIdAndStatusLike(id, -1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<AdminEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewAdmin(@Valid @RequestBody AdminDto newAdmin, Principal principal, BindingResult result) {
		logger.info("################ /project/admin/addNewAdmin started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newAdmin == null) {
			logger.info("---------------- New admin is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "New admin is null"), HttpStatus.BAD_REQUEST);
	      }
		if (newAdmin.getFirstName() == null || newAdmin.getLastName() == null || newAdmin.getEmail() == null || newAdmin.getMobilePhoneNumber() == null|| newAdmin.getGender() == null || newAdmin.getjMBG() == null ) {
			logger.info("---------------- Some or all atributes is null.");
			return new ResponseEntity<RESTError>(new RESTError(5, "Some or all atributes is null."), HttpStatus.BAD_REQUEST);
		}
		UserEntity user = new AdminEntity();
		try {
			if (newAdmin.getjMBG() != null && adminRepository.getByJMBG(newAdmin.getjMBG()) != null && userRepository.getByJMBG(newAdmin.getjMBG()) == null) {
				logger.info("---------------- JMBG already exist.");
				return new ResponseEntity<RESTError>(new RESTError(2, "JMBG already exist."), HttpStatus.NOT_ACCEPTABLE);
			}
			if (newAdmin.getEmail() != null && adminRepository.getByEmail(newAdmin.getEmail()) != null ) {
				logger.info("---------------- Email already exist.");
				return new ResponseEntity<RESTError>(new RESTError(2, "Email already exist."), HttpStatus.NOT_ACCEPTABLE);
			}
			if (newAdmin.getAccessRole() != null && !newAdmin.getAccessRole().equals("ROLE_ADMIN")) {
				logger.info("---------------- Access role must be ROLE_ADMIN.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "Access role must be ROLE_ADMIN."), HttpStatus.NOT_ACCEPTABLE);
			}		
			if (newAdmin.getUsername() != null && userAccountRepository.getByUsername(newAdmin.getUsername()) != null) {
				logger.info("---------------- Username already exist.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "Username already exist."), HttpStatus.NOT_ACCEPTABLE);
		    }
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			user = adminDao.addNewAdmin(loggedUser, newAdmin);
			logger.info("Admin created.");
			if (newAdmin.getUsername() != null && newAdmin.getPassword() != null && newAdmin.getConfirmedPassword() != null && newAdmin.getPassword().equals(newAdmin.getConfirmedPassword())) {
				UserAccountEntity account = userAccountDao.addNewUserAccount(loggedUser, user, newAdmin.getUsername(), EUserRole.ROLE_ADMIN, newAdmin.getPassword());
				logger.info("---------------- Finished OK.");
				return new ResponseEntity<>(account, HttpStatus.OK);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ This is an exception message: " + e.getMessage());
			if (user != null && adminRepository.findByIdAndStatusLike(user.getId(), 1) != null) {
				adminRepository.deleteById(user.getId());
				logger.error("++++++++++++++++ Because of exeption Admin with Id " + user.getId().toString() + " deleted.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> modifyAdmin(@PathVariable Integer id, @Valid @RequestBody AdminDto updateAdmin, Principal principal, BindingResult result) {
		logger.info("################ /project/admin/{id}/modifyAdmin started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateAdmin == null) {
			logger.info("---------------- New data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "New data is null."), HttpStatus.BAD_REQUEST);
	      }
		AdminEntity user = new AdminEntity();
		try {
			user = adminRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Admin not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Admin not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Admin identified.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_ADMIN, 1);
			logger.info("Admin's user account identified.");
			if (updateAdmin.getjMBG() != null && updateAdmin.getjMBG().equals(user.getjMBG()) ) {
				updateAdmin.setjMBG(null);
			}
			if (updateAdmin.getEmail() != null && updateAdmin.getEmail().equals(user.getEmail()) ) {
				updateAdmin.setEmail(null);
			}
			if (updateAdmin.getUsername() != null && updateAdmin.getUsername().equals(account.getUsername()) ) {
				updateAdmin.setUsername(null);
			}
			if (updateAdmin.getjMBG() != null && userRepository.getByJMBG(updateAdmin.getjMBG()) != null) {
				logger.info("---------------- JMBG already exist.");
				return new ResponseEntity<RESTError>(new RESTError(2, "JMBG already exist."), HttpStatus.NOT_ACCEPTABLE);
			}
			if (updateAdmin.getEmail() != null && adminRepository.getByEmail(updateAdmin.getEmail()) != null) {
				logger.info("---------------- Email already exist.");
				return new ResponseEntity<RESTError>(new RESTError(2, "Email already exist."), HttpStatus.NOT_ACCEPTABLE);
			}
			if (updateAdmin.getAccessRole() != null && !updateAdmin.getAccessRole().equals("ROLE_ADMIN")) {
				logger.info("---------------- Access role must be ROLE_ADMIN.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "Access role must be ROLE_ADMIN."), HttpStatus.NOT_ACCEPTABLE);
			}		
			if (updateAdmin.getUsername() != null && userAccountRepository.getByUsername(updateAdmin.getUsername()) != null) {
				logger.info("---------------- Username already exist.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "Username already exist."), HttpStatus.NOT_ACCEPTABLE);
		    }
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updateAdmin.getFirstName() != null || updateAdmin.getLastName() != null || updateAdmin.getEmail() != null || updateAdmin.getMobilePhoneNumber() != null|| updateAdmin.getGender() != null || updateAdmin.getjMBG() != null) {
				adminDao.modifyAdmin(loggedUser, user, updateAdmin);
				logger.info("Admin modified.");
			}
			if (account != null) {
				logger.info("Admin's user account identified.");
				if (updateAdmin.getUsername() != null && !updateAdmin.getUsername().equals("") && !updateAdmin.getUsername().equals(" ") && userAccountRepository.getByUsername(updateAdmin.getUsername()) == null) {
					userAccountDao.modifyAccountUsername(loggedUser, account, updateAdmin.getUsername());
					logger.info("Username modified.");					
				}
				if (updateAdmin.getPassword() != null && !updateAdmin.getPassword().equals("") && !updateAdmin.getPassword().equals(" ") && updateAdmin.getConfirmedPassword() != null && updateAdmin.getPassword().equals(updateAdmin.getConfirmedPassword())) {
					userAccountDao.modifyAccountPassword(loggedUser, account, updateAdmin.getPassword());
					logger.info("Password modified.");
				}
				logger.info("Account modified.");
				return new ResponseEntity<>(account, HttpStatus.OK);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/archive/{id}")
	public ResponseEntity<?> archive(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/admin/archivedeleted/archive started.");
		logger.info("Logged user: " + principal.getName());
		AdminEntity user = new AdminEntity();
		try {
			user = adminRepository.getById(id);
			if (user == null || user.getStatus() == -1) {
				logger.info("---------------- Admin not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Admin not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Admin for archiving identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (id == loggedUser.getId()) {
				logger.info("---------------- Selected Id is ID of logged User: Cann't archive yourself.");
				return new ResponseEntity<RESTError>(new RESTError(5, "Selected Id is ID of logged User: Cann't archive yourself."), HttpStatus.BAD_REQUEST);
		      }	
			adminDao.archiveAdmin(loggedUser, user);
			logger.info("Admin archived.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLike(user, EUserRole.ROLE_ADMIN);
			logger.info("Admin's user account identified.");
			if (account != null && account.getStatus() != -1) {
				userAccountDao.archiveAccount(loggedUser, account);
				logger.info("---------------- Finished OK.");
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
				adminRepository.save(user);
				logger.error("++++++++++++++++ Because of exeption Admin with Id " + user.getId().toString() + " deleted.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/undelete/{id}")
	public ResponseEntity<?> unDelete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/admin/undelete/{id}/unDelete started.");
		logger.info("Logged user: " + principal.getName());
		AdminEntity user = new AdminEntity();
		try {
			user = adminRepository.findByIdAndStatusLike(id, 0);
			if (user == null) {
				logger.info("---------------- Admin not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Admin not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Admin for undeleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			adminDao.undeleteAdmin(loggedUser, user);
			logger.info("Admin undeleted.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_ADMIN, 1);
			logger.info("Admin's user account identified.");
			if (account != null) {
				userAccountDao.undeleteAccount(loggedUser, account);
				logger.info("---------------- Finished OK.");
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
				adminRepository.save(user);
				logger.error("++++++++++++++++ Because of exeption Admin with Id " + user.getId().toString() + " deleted.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/admin/{id}/delete started.");
		logger.info("Logged user: " + principal.getName());
		AdminEntity user = new AdminEntity();
		try {
			user = adminRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Admin not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Admin not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Admin for deleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (id == loggedUser.getId()) {
				logger.info("---------------- Selected Id is ID of logged User: Cann't delete yourself.");
				return new ResponseEntity<RESTError>(new RESTError(5, "Selected Id is ID of logged User: Cann't delete yourself."), HttpStatus.BAD_REQUEST);
		      }	
			adminDao.deleteAdmin(loggedUser, user);
			logger.info("Admin deleted.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_ADMIN, 1);
			logger.info("Admin's user account identified.");
			if (account != null) {
				userAccountDao.deleteAccount(loggedUser, account);
				logger.info("---------------- Finished OK.");
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
				adminRepository.save(user);
				logger.error("++++++++++++++++ Because of exeption Admin with Id " + user.getId().toString() + " activated.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
