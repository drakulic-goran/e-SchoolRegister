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
import com.iktpreobuka.projekat_za_kraj.entities.ParentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserAccountEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.UserAccountDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;
import com.iktpreobuka.projekat_za_kraj.repositories.AdminRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.ParentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.StudentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserAccountRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserRepository;
import com.iktpreobuka.projekat_za_kraj.security.Views;
import com.iktpreobuka.projekat_za_kraj.services.UserAccountDao;

@Controller
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value= "/project/account")
public class UserAccountController {

	@Autowired
	private UserAccountDao userAccountDao;

	@Autowired
	private UserAccountRepository userAccountRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private ParentRepository parentRepository;
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private TeacherRepository teacherRepository;
	
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
		logger.info("################ /project/account/getAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<UserAccountEntity> accounts= userAccountRepository.findByStatusLike(1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<UserAccountEntity>>(accounts, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getAll(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/account/getAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			UserAccountEntity account= userAccountRepository.findByIdAndStatusLike(id, 1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<UserAccountEntity>(account, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/byaccessrole/{role}")
	public ResponseEntity<?> getAllByAccessRole(@PathVariable String role, Principal principal) {
		logger.info("################ /project/account/user/getAllByAccessRole started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<UserAccountEntity> accounts= userAccountRepository.findByAccessRoleLikeAndStatusLike(EUserRole.valueOf(role), 1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<UserAccountEntity>>(accounts, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/deleted")
	public ResponseEntity<?> getAllDeleted(Principal principal) {
		logger.info("################ /project/account/deleted/getAllDeleted started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<UserAccountEntity> accounts= userAccountRepository.findByStatusLike(0);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<UserAccountEntity>>(accounts, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/deleted/{id}")
	public ResponseEntity<?> getAllDeleted(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/account/deleted/getAllDeleted started.");
		logger.info("Logged username: " + principal.getName());
		try {
			UserAccountEntity account= userAccountRepository.findByIdAndStatusLike(id, 0);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<UserAccountEntity>(account, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived")
	public ResponseEntity<?> getAllArchived(Principal principal) {
		logger.info("################ /project/account/archived/getAllArchived started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<UserAccountEntity> accounts= userAccountRepository.findByStatusLike(-1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<UserAccountEntity>>(accounts, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/{id}")
	public ResponseEntity<?> getAllArchived(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/account/archived/getAllArchived started.");
		logger.info("Logged username: " + principal.getName());
		try {
			UserAccountEntity account= userAccountRepository.findByIdAndStatusLike(id, -1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<UserAccountEntity>(account, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNew(@Valid @RequestBody UserAccountDto newUserAccount, Principal principal, BindingResult result) {
		logger.info("################ /project/account/addNew started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newUserAccount == null) {
			logger.info("---------------- New user account is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "New user account is null"), HttpStatus.BAD_REQUEST);
	      }
		if (newUserAccount.getUsername() == null || newUserAccount.getAccessRole() == null || (newUserAccount.getPassword() == null && newUserAccount.getConfirmedPassword() == null) || newUserAccount.getUserId() == null) {
			logger.info("---------------- Some or all atributes is null.");
			return new ResponseEntity<RESTError>(new RESTError(5, "Some or all atributes is null."), HttpStatus.BAD_REQUEST);
		}
		UserAccountEntity account = new UserAccountEntity();
		try {
			UserEntity user = new UserEntity();
			EUserRole role = null;
			if (newUserAccount.getUsername() != null && userAccountRepository.getByUsername(newUserAccount.getUsername()) != null) {
				logger.info("---------------- Username already exist.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "Username already exist."), HttpStatus.NOT_ACCEPTABLE);
		    }
			if (newUserAccount.getUserId() != null) {
				user = userRepository.getById(Integer.parseInt(newUserAccount.getUserId()));
				if (user == null) {
					logger.info("---------------- User not found.");
					return new ResponseEntity<RESTError>(new RESTError(4, "User not found."), HttpStatus.NOT_FOUND);
				}
				Integer userStatus = null;
				if (user != null) {
					if (newUserAccount.getAccessRole().equals("ROLE_ADMIN")) {
						AdminEntity userWithRole = adminRepository.getByIdAndStatusLike(Integer.parseInt(newUserAccount.getUserId()), 1);
						if (userWithRole != null) {
							userStatus = userWithRole.getStatus();
							role = EUserRole.ROLE_ADMIN;
						}
					} else if (newUserAccount.getAccessRole().equals("ROLE_TEACHER")) {
						TeacherEntity userWithRole = teacherRepository.getByIdAndStatusLike(Integer.parseInt(newUserAccount.getUserId()), 1);
						if (userWithRole != null) {
							userStatus = userWithRole.getStatus();
							role = EUserRole.ROLE_TEACHER;
						}
					} else if (newUserAccount.getAccessRole().equals("ROLE_PARENT")) {
						ParentEntity userWithRole = parentRepository.findByIdAndStatusLike(Integer.parseInt(newUserAccount.getUserId()), 1);
						if (userWithRole != null) {
							userStatus = userWithRole.getStatus();
							role = EUserRole.ROLE_PARENT;
						}
					} else if (newUserAccount.getAccessRole().equals("ROLE_STUDENT")) {
						StudentEntity userWithRole = studentRepository.findByIdAndStatusLike(Integer.parseInt(newUserAccount.getUserId()), 1);
						if (userWithRole != null) {
							userStatus = userWithRole.getStatus();
							role = EUserRole.ROLE_STUDENT;		
						}
					} else {
						logger.info("---------------- User not exist or wrong access role.");
						return new ResponseEntity<RESTError>(new RESTError(5, "User not exist or wrong access role."), HttpStatus.BAD_REQUEST);
					}
				}
				if (user == null || userStatus == null || userStatus != 1) {
					logger.info("---------------- User not found for that role.");
					return new ResponseEntity<RESTError>(new RESTError(4, "User not found for that role."), HttpStatus.NOT_FOUND);
				}
			}		
			if (user != null && role !=null && userAccountRepository.findByUserAndAccessRoleAndStatusLike(user, role, 1) != null) {
				logger.info("---------------- User already have account for that role.");
		        return new ResponseEntity<RESTError>(new RESTError(3, "User already have account for that role."), HttpStatus.FORBIDDEN);
		    }
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (newUserAccount.getUsername() != null && newUserAccount.getPassword() != null && newUserAccount.getConfirmedPassword() != null && newUserAccount.getPassword().equals(newUserAccount.getConfirmedPassword())) {
				account = userAccountDao.addNewUserAccount(loggedUser, user, newUserAccount.getUsername(), role, newUserAccount.getPassword());
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(account, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ This is an exception message: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> modify(@PathVariable Integer id, @Valid @RequestBody UserAccountDto updateUserAccount, Principal principal, BindingResult result) {
		logger.info("################ /project/account/{id}/modify started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateUserAccount == null) {
			logger.info("---------------- New user account is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "New user account is null"), HttpStatus.BAD_REQUEST);
	      }
		if (updateUserAccount.getUsername() == null && updateUserAccount.getAccessRole() == null && (updateUserAccount.getPassword() == null || updateUserAccount.getConfirmedPassword() == null) && updateUserAccount.getUserId() == null) {
			logger.info("---------------- All atributes is null.");
			return new ResponseEntity<RESTError>(new RESTError(5, "All atributes is null."), HttpStatus.BAD_REQUEST);
		}
		UserAccountEntity account = new UserAccountEntity();
		try {
			account = userAccountRepository.findByIdAndStatusLike(id, 1);
			if (account == null) {
				logger.info("---------------- User account not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "User account not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("User account identified.");			
			UserEntity user = new UserEntity();
			EUserRole role = null;
			if (updateUserAccount.getUsername() != null && userAccountRepository.getByUsername(updateUserAccount.getUsername()) != null) {
				logger.info("---------------- Username already exist.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "Username already exist."), HttpStatus.NOT_ACCEPTABLE);
		    }
			if (updateUserAccount.getUserId() != null && Integer.parseInt(updateUserAccount.getUserId()) != account.getUser().getId()) {
				user = userRepository.getById(Integer.parseInt(updateUserAccount.getUserId()));
				Integer userStatus = null;
				if (user != null) {
					if (((account.getAccessRole().equals(EUserRole.ROLE_ADMIN) && updateUserAccount.getAccessRole() == null) || updateUserAccount.getAccessRole().equals("ROLE_ADMIN"))) {
						AdminEntity userWithRole = adminRepository.getByIdAndStatusLike(Integer.parseInt(updateUserAccount.getUserId()), 1);
						if (userWithRole != null) {
							userStatus = userWithRole.getStatus();
							role = EUserRole.ROLE_ADMIN;
						}
					} else if (((account.getAccessRole().equals(EUserRole.ROLE_TEACHER) && updateUserAccount.getAccessRole() == null) || updateUserAccount.getAccessRole().equals("ROLE_TEACHER"))) {
						TeacherEntity userWithRole = teacherRepository.getByIdAndStatusLike(Integer.parseInt(updateUserAccount.getUserId()), 1);
						if (userWithRole != null) {
							userStatus = userWithRole.getStatus();
							role = EUserRole.ROLE_TEACHER;
						}
					} else if (((account.getAccessRole().equals(EUserRole.ROLE_PARENT) && updateUserAccount.getAccessRole() == null) || updateUserAccount.getAccessRole().equals("ROLE_PARENT"))) {
						ParentEntity userWithRole = parentRepository.findByIdAndStatusLike(Integer.parseInt(updateUserAccount.getUserId()), 1);
						if (userWithRole != null) {
							userStatus = userWithRole.getStatus();
							role = EUserRole.ROLE_PARENT;
						}
					} else if (((account.getAccessRole().equals(EUserRole.ROLE_STUDENT) && updateUserAccount.getAccessRole() == null) || updateUserAccount.getAccessRole().equals("ROLE_STUDENT"))) {
						StudentEntity userWithRole = studentRepository.findByIdAndStatusLike(Integer.parseInt(updateUserAccount.getUserId()), 1);
						if (userWithRole != null) {
							userStatus = userWithRole.getStatus();
							role = EUserRole.ROLE_STUDENT;		
						}
					} else {
						logger.info("---------------- User not exist or wrong access role.");
						return new ResponseEntity<RESTError>(new RESTError(5, "User not exist or wrong access role."), HttpStatus.BAD_REQUEST);
					}
				}
				if (user == null || userStatus == null || userStatus != 1) {
					logger.info("---------------- User not found with that role.");
					return new ResponseEntity<RESTError>(new RESTError(5, "User not found with that role."), HttpStatus.BAD_REQUEST);
				}
			}		
			if (user != null && role !=null && userAccountRepository.findByUserAndAccessRoleAndStatusLike(user, EUserRole.valueOf(updateUserAccount.getAccessRole()), 1) != null) {
				logger.info("---------------- User already have account with that role.");
		        return new ResponseEntity<RESTError>(new RESTError(3, "User already have account with that role."), HttpStatus.FORBIDDEN);
		    }	
			if (updateUserAccount.getUserId() == null && updateUserAccount.getAccessRole() != null && userAccountRepository.findByUserAndAccessRoleAndStatusLike(account.getUser(), EUserRole.valueOf(updateUserAccount.getAccessRole()), 1) != null) {
				logger.info("---------------- Other account of same user with that role already exist.");
		        return new ResponseEntity<RESTError>(new RESTError(3, "Other account of same user with that role already exist."), HttpStatus.FORBIDDEN);
		    }	
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updateUserAccount.getUsername() != null && !updateUserAccount.getUsername().equals("") && !updateUserAccount.getUsername().equals(" ") && userAccountRepository.getByUsername(updateUserAccount.getUsername()) == null) {
				userAccountDao.modifyAccountUsername(loggedUser, account, updateUserAccount.getUsername());
				logger.info("Username modified.");					
			}
			if (updateUserAccount.getPassword() != null && !updateUserAccount.getPassword().equals("") && !updateUserAccount.getPassword().equals(" ") && updateUserAccount.getConfirmedPassword() != null && updateUserAccount.getPassword().equals(updateUserAccount.getConfirmedPassword())) {
				userAccountDao.modifyAccountPassword(loggedUser, account, updateUserAccount.getPassword());
				logger.info("Password modified.");
			}
			if (updateUserAccount.getUserId() != null && updateUserAccount.getAccessRole() != null && user != account.getUser() && !role.equals(account.getAccessRole())) {
				userAccountDao.modifyAccountUserAndAccessRole(loggedUser, account, user, role);
				logger.info("User and access role modified.");
			} else if (updateUserAccount.getUserId() != null && user != account.getUser()) {
				userAccountDao.modifyAccountUser(loggedUser, account, user);
				logger.info("User modified.");
			} else if (updateUserAccount.getAccessRole() != null && !role.equals(account.getAccessRole())) {
				userAccountDao.modifyAccountAccessRole(loggedUser, account, EUserRole.valueOf(updateUserAccount.getAccessRole()));
				logger.info("Access role modified.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(account, HttpStatus.OK);
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
		logger.info("################ /project/account/archive/archive started.");
		logger.info("Logged user: " + principal.getName());
		UserAccountEntity account = new UserAccountEntity();
		try {
			account = userAccountRepository.getById(id);
			if (account == null || account.getStatus() == -1) {
				logger.info("---------------- User account not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "User account not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("User account for archiving identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (account.getUser().getId() == loggedUser.getId()) {
				logger.info("---------------- Selected Id is ID of logged User account: Cann't archive your account.");
				return new ResponseEntity<RESTError>(new RESTError(3, "Selected Id is ID of logged User account: Cann't archive your account."), HttpStatus.FORBIDDEN);
		      }	
			userAccountDao.archiveAccount(loggedUser, account);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<UserAccountEntity>(account, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/undelete/{id}")
	public ResponseEntity<?> unDelete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/account/undelete/{id}/unDelete started.");
		logger.info("Logged user: " + principal.getName());
		UserAccountEntity account = new UserAccountEntity();
		try {
			account = userAccountRepository.findByIdAndStatusLike(id, 0);
			if (account == null) {
				logger.info("---------------- User account not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "User account not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("User account for undeleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			userAccountDao.undeleteAccount(loggedUser, account);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<UserAccountEntity>(account, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/account/{id}/delete started.");
		logger.info("Logged user: " + principal.getName());
		UserAccountEntity account = new UserAccountEntity();
		try {
			account = userAccountRepository.findByIdAndStatusLike(id, 1);
			if (account == null) {
				logger.info("---------------- User account not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "User account not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("User account for deleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (account.getUser().getId() == loggedUser.getId()) {
				logger.info("---------------- Selected Id is ID of logged User account: Cann't delete your account.");
				return new ResponseEntity<RESTError>(new RESTError(3, "Selected Id is ID of logged User account: Cann't delete your account."), HttpStatus.FORBIDDEN);
		      }	
			userAccountDao.deleteAccount(loggedUser, account);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<UserAccountEntity>(account, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
