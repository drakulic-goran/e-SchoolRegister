package com.iktpreobuka.projekat_za_kraj.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
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
import com.iktpreobuka.projekat_za_kraj.entities.PrimaryTeacherDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserAccountEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SearchTeachersDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectDepartmentsDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.TeacherDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;
import com.iktpreobuka.projekat_za_kraj.repositories.DepartmentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.SubjectRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherSubjectDepartmentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserAccountRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserRepository;
import com.iktpreobuka.projekat_za_kraj.security.Views;
import com.iktpreobuka.projekat_za_kraj.services.TeacherDao;
import com.iktpreobuka.projekat_za_kraj.services.UserAccountDao;

@Controller
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value= "/project/teacher")
public class TeacherController {

	@Autowired
	private UserAccountDao userAccountDao;

	@Autowired
	private TeacherDao teacherDao;

	@Autowired
	private TeacherRepository teacherRepository;
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private TeacherSubjectDepartmentRepository teacherSubjectDepartmentRepository;

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
		logger.info("################ /project/teacher/getAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<TeacherEntity> users= teacherRepository.findByStatusLike(1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<TeacherEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/teacher/getById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			TeacherEntity user= teacherRepository.findByIdAndStatusLike(id, 1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<TeacherEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/withaccount")
	public ResponseEntity<?> getAllWithUserAccount(Principal principal) {
		logger.info("################ /project/teacher/withaccount/getAllWithUserAccount started.");
		logger.info("Logged username: " + principal.getName());
		try {
			//Iterable<TeacherEntity> users= teacherRepository.findByStatusLike(1);
			Iterable<SearchTeachersDto> users= teacherRepository.findByStatusWithUserAccount(1, EUserRole.ROLE_TEACHER);
			Iterable<TeacherSubjectDepartmentEntity> subjects_departments= teacherSubjectDepartmentRepository.findByStatusLike(1);
			for (SearchTeachersDto s : users) {
				List<TeacherSubjectEntity> tempSubjects = new ArrayList<TeacherSubjectEntity>();
				List<SubjectDepartmentsDto> teachingSubjectDepartments = new ArrayList<SubjectDepartmentsDto>();
				for (TeacherSubjectEntity tse : s.getUser().getSubjects()) {
					if (tse.getStatus()==1) {
						SubjectDepartmentsDto subjectDepartments = new SubjectDepartmentsDto();
						List<TeacherSubjectDepartmentEntity> tempDepartments = new ArrayList<TeacherSubjectDepartmentEntity>();
						subjectDepartments.setSubject(tse.getSubject());
						for (TeacherSubjectDepartmentEntity tsd : subjects_departments) {
							if (tsd.getStatus() == 1 && tsd.getTeachingTeacher() == s.getUser() && tsd.getTeachingSubject() == tse.getSubject()) {
								tempDepartments.add(tsd);
							}
						}
						subjectDepartments.setTeachingDepartments(tempDepartments);						
						teachingSubjectDepartments.add(subjectDepartments);
						tempSubjects.add(tse);
					}
				}
				s.getUser().setSubjects(tempSubjects);
				s.setTeachingSubjectDepartments(teachingSubjectDepartments);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<SearchTeachersDto>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_TEACHER")
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.GET, value = "/logged")
	public ResponseEntity<?> getLoggedTeacher(Principal principal) {
		logger.info("################ /project/teacher/getLoggedTeacher started.");
		logger.info("Logged username: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			TeacherEntity user= teacherRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<TeacherEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/deleted")
	public ResponseEntity<?> getAllDeleted(Principal principal) {
		logger.info("################ /project/teacher/deleted/getAllDeleted started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<TeacherEntity> users= teacherRepository.findByStatusLike(0);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<TeacherEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/deleted/{id}")
	public ResponseEntity<?> getDeletedById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/teacher/deleted/getDeletedById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			TeacherEntity user= teacherRepository.findByIdAndStatusLike(id, 0);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<TeacherEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived")
	public ResponseEntity<?> getAllArchived(Principal principal) {
		logger.info("################ /project/teacher/archived/getAllArchived started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<TeacherEntity> users= teacherRepository.findByStatusLike(-1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<TeacherEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/{id}")
	public ResponseEntity<?> getArchivedById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/teacher/archived/getArchivedById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			TeacherEntity user= teacherRepository.findByIdAndStatusLike(id, -1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<TeacherEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewTeacher(@Valid @RequestBody TeacherDto newTeacher, Principal principal, BindingResult result) {
		logger.info("################ /project/teacher/addNewTeacher started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newTeacher == null) {
			logger.info("---------------- New teacher is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "New teacher is null."), HttpStatus.BAD_REQUEST);
	      }
		if (newTeacher.getFirstName() == null || newTeacher.getLastName() == null || newTeacher.getCertificate() == null || newTeacher.getEmploymentDate() == null || newTeacher.getGender() == null || newTeacher.getjMBG() == null) {
			logger.info("---------------- Some atributes is null.");
			return new ResponseEntity<RESTError>(new RESTError(5, "Some atributes is null"), HttpStatus.BAD_REQUEST);
		}
		UserEntity user = new TeacherEntity();
		try {
			if (newTeacher.getjMBG() != null && teacherRepository.getByJMBG(newTeacher.getjMBG()) != null) {
				logger.info("---------------- JMBG already exists.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "JMBG already exists."), HttpStatus.NOT_ACCEPTABLE);
			}
			if (newTeacher.getAccessRole() != null && !newTeacher.getAccessRole().equals("ROLE_TEACHER")) {
				logger.info("---------------- Access role must be ROLE_TEACHER.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "Access role must be ROLE_TEACHER."), HttpStatus.NOT_ACCEPTABLE);
			}		
			if (newTeacher.getUsername() != null && userAccountRepository.getByUsername(newTeacher.getUsername()) != null) {
				logger.info("---------------- Username already exists.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "Username already exists."), HttpStatus.NOT_ACCEPTABLE);
		      }
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			user = teacherDao.addNewTeacher(loggedUser, newTeacher);
			logger.info("Teacher created.");
			if (newTeacher.getUsername() != null && newTeacher.getPassword() != null && newTeacher.getConfirmedPassword() != null && newTeacher.getPassword().equals(newTeacher.getConfirmedPassword())) {
				UserAccountEntity account = userAccountDao.addNewUserAccount(loggedUser, user, newTeacher.getUsername(), EUserRole.ROLE_TEACHER, newTeacher.getPassword());
				logger.info("Account created.");
				return new ResponseEntity<>(account, HttpStatus.OK);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ This is an exception message: " + e.getMessage());
			if (user != null && teacherRepository.findByIdAndStatusLike(user.getId(), 1) != null) {
				teacherRepository.deleteById(user.getId());
				logger.error("++++++++++++++++ Because of exeption Teacher with Id " + user.getId().toString() + " deleted.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_TEACHER", "ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> modifyTeacher(@PathVariable Integer id, @Valid @RequestBody TeacherDto updateTeacher, Principal principal, BindingResult result) {
		logger.info("################ /project/teacher/{id}/modifyTeacher started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateTeacher == null) {
			logger.info("---------------- New teacher is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "New teacher data is null."), HttpStatus.BAD_REQUEST);
	      }
		TeacherEntity user = new TeacherEntity();
		try {
			if (updateTeacher.getjMBG() != null && userRepository.getByJMBG(updateTeacher.getjMBG()) != null) {
				logger.info("---------------- JMBG already exists.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "JMBG already exists."), HttpStatus.NOT_ACCEPTABLE);
			}
			if (updateTeacher.getAccessRole() != null && !updateTeacher.getAccessRole().equals("ROLE_TEACHER")) {
				logger.info("---------------- Access role must be ROLE_TEACHER.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "Access role must be ROLE_TEACHER."), HttpStatus.NOT_ACCEPTABLE);
			}		
			if (updateTeacher.getUsername() != null && userAccountRepository.getByUsername(updateTeacher.getUsername()) != null) {
				logger.info("---------------- Username already exists.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "Username already exists."), HttpStatus.NOT_ACCEPTABLE);
		      }
			user = teacherRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Teacher not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Teacher not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Teacher identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			logger.info(updateTeacher.getEmploymentDate());
			if (updateTeacher.getFirstName() != null || updateTeacher.getLastName() != null || updateTeacher.getCertificate() != null || updateTeacher.getEmploymentDate() != null|| updateTeacher.getGender() != null || updateTeacher.getjMBG() != null) {
				teacherDao.modifyTeacher(loggedUser, user, updateTeacher);
				logger.info("Teacher modified.");
			}
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_TEACHER, 1);
			logger.info("Admin's user account identified.");
			if (account != null) {
				if (updateTeacher.getUsername() != null && !updateTeacher.getUsername().equals("") && !updateTeacher.getUsername().equals(" ") && userAccountRepository.getByUsername(updateTeacher.getUsername()) == null) {
					userAccountDao.modifyAccountUsername(loggedUser, account, updateTeacher.getUsername());
					logger.info("Username modified.");					
				}
				if (updateTeacher.getPassword() != null && !updateTeacher.getPassword().equals("") && !updateTeacher.getPassword().equals(" ") && updateTeacher.getConfirmedPassword() != null && updateTeacher.getPassword().equals(updateTeacher.getConfirmedPassword())) {
					userAccountDao.modifyAccountPassword(loggedUser, account, updateTeacher.getPassword());
					logger.info("Password modified.");
				}
				logger.info("Account modified.");
				return new ResponseEntity<>(account, HttpStatus.OK);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(user, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/add-subject")
	public ResponseEntity<?> addSubjectsToTeacher(@PathVariable Integer id, @Valid @RequestBody TeacherDto updateTeacher, Principal principal, BindingResult result) {
		logger.info("################ /project/teacher/{id}/add-subject/addSubjectsToTeacher started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateTeacher == null) {
			logger.info("---------------- Data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Data is null."), HttpStatus.BAD_REQUEST);
	      }
		if (updateTeacher.getFirstName() != null || updateTeacher.getLastName() != null || updateTeacher.getCertificate() != null || updateTeacher.getEmploymentDate() != null|| updateTeacher.getGender() != null || updateTeacher.getjMBG() != null || updateTeacher.getUsername() != null || updateTeacher.getPassword() != null || updateTeacher.getConfirmedPassword() != null) {
			logger.info("---------------- Update have non acceptable atrributes.");
	        return new ResponseEntity<RESTError>(new RESTError(2, "Update have non acceptable atrributes."), HttpStatus.NOT_ACCEPTABLE);
		}
		for (String t : updateTeacher.getSubjects()) {
			if (t ==null || t.equals("") || t.equals(" ")) {
				logger.info("---------------- New subject/s is null.");
		        return new ResponseEntity<RESTError>(new RESTError(5, "New subject/s is null."), HttpStatus.BAD_REQUEST);
			}
		}
		List<TeacherSubjectEntity> st = new ArrayList<TeacherSubjectEntity>();
		try {
			for (String s : updateTeacher.getSubjects()) {
				if (subjectRepository.findByIdAndStatusLike(Integer.parseInt(s), 1) == null ) {
					logger.info("---------------- Subject/s not found.");
			        return new ResponseEntity<RESTError>(new RESTError(4, "Subject/s not found."), HttpStatus.NOT_FOUND);
				}
			}
			TeacherEntity user = teacherRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Teacher not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Teacher not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Teacher identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updateTeacher.getSubjects() != null) {
				st = teacherDao.addSubjectsToTeacher(loggedUser, user, updateTeacher.getSubjects());
				logger.info("Subject/s added.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(st, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/add-primarydepartment")
	public ResponseEntity<?> addPrimaryDepartmentToTeacher(@PathVariable Integer id, @Valid @RequestBody TeacherDto updateTeacher, Principal principal, BindingResult result) {
		logger.info("################ /project/teacher/{id}/add-primarydepartment/addPrimaryDepartmentToTeacher started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateTeacher == null) {
			logger.info("---------------- Data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Data is null."), HttpStatus.BAD_REQUEST);
	      }
		if (updateTeacher.getFirstName() != null || updateTeacher.getLastName() != null || updateTeacher.getCertificate() != null || updateTeacher.getEmploymentDate() != null|| updateTeacher.getGender() != null || updateTeacher.getjMBG() != null || updateTeacher.getUsername() != null || updateTeacher.getPassword() != null || updateTeacher.getConfirmedPassword() != null) {
			logger.info("---------------- Update have non acceptable atrributes.");
	        return new ResponseEntity<RESTError>(new RESTError(2, "Update have non acceptable atrributes."), HttpStatus.NOT_ACCEPTABLE);
		}
		if (updateTeacher.getPrimaryDepartment() ==null || updateTeacher.getPrimaryDepartment().equals("") || updateTeacher.getPrimaryDepartment().equals(" ")) {
			logger.info("---------------- New primary department is null.");
		    return new ResponseEntity<RESTError>(new RESTError(5, "New primary department is null."), HttpStatus.BAD_REQUEST);
		}
		PrimaryTeacherDepartmentEntity ptd = new PrimaryTeacherDepartmentEntity();
		try {
			if (departmentRepository.findByIdAndStatusLike(Integer.parseInt(updateTeacher.getPrimaryDepartment()), 1) == null ) {
				logger.info("---------------- Primary department not found.");
			    return new ResponseEntity<RESTError>(new RESTError(4, "Primary department not found."), HttpStatus.NOT_FOUND);
			}
			TeacherEntity user = teacherRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Teacher not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Teacher not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Teacher identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updateTeacher.getPrimaryDepartment() != null) {
				ptd = teacherDao.addPrimaryDepartmentToTeacher(loggedUser, user, updateTeacher.getPrimaryDepartment());
				logger.info("Primary department added.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(ptd, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/add-subjects-with-departments")
	public ResponseEntity<?> addSubjectsWithDepartmentsToTeacher(@PathVariable Integer id, @Valid @RequestBody TeacherDto updateTeacher, Principal principal, BindingResult result) {
		logger.info("################ /project/teacher/{id}/add-subjects-with-departments/addSubjectsWithDepartmentsToTeacher started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateTeacher == null) {
			logger.info("---------------- Data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Data is null."), HttpStatus.BAD_REQUEST);
	      }
		if (updateTeacher.getFirstName() != null || updateTeacher.getLastName() != null || updateTeacher.getCertificate() != null || updateTeacher.getEmploymentDate() != null|| updateTeacher.getGender() != null || updateTeacher.getjMBG() != null || updateTeacher.getUsername() != null || updateTeacher.getPassword() != null || updateTeacher.getConfirmedPassword() != null) {
			logger.info("---------------- Update have non acceptable atrributes.");
	        return new ResponseEntity<RESTError>(new RESTError(2, "Update have non acceptable atrributes."), HttpStatus.NOT_ACCEPTABLE);
		}
		if (updateTeacher.getTeachingDepartment() ==null || updateTeacher.getTeachingDepartment().equals("") || updateTeacher.getTeachingDepartment().equals(" ")) {
			logger.info("---------------- New department is null.");
		    return new ResponseEntity<RESTError>(new RESTError(5, "New department is null."), HttpStatus.BAD_REQUEST);
		}
		if (updateTeacher.getTeachingSubject() ==null || updateTeacher.getTeachingSubject().equals("") || updateTeacher.getTeachingSubject().equals(" ")) {
			logger.info("---------------- New subject is null.");
		    return new ResponseEntity<RESTError>(new RESTError(5, "New subject is null."), HttpStatus.BAD_REQUEST);
		}
		if (updateTeacher.getSchoolYear() ==null || updateTeacher.getSchoolYear().equals("") || updateTeacher.getSchoolYear().equals(" ")) {
			logger.info("---------------- New school year is null.");
		       return new ResponseEntity<RESTError>(new RESTError(5, "New school year is null."), HttpStatus.BAD_REQUEST);
		}
		TeacherSubjectDepartmentEntity tsd = new TeacherSubjectDepartmentEntity();
		try {
			if (subjectRepository.findByIdAndStatusLike(Integer.parseInt(updateTeacher.getTeachingSubject()), 1) == null ) {
				logger.info("---------------- Subject not found.");
			    return new ResponseEntity<RESTError>(new RESTError(4, "Subject not found."), HttpStatus.NOT_FOUND);
			}
			if (departmentRepository.findByIdAndStatusLike(Integer.parseInt(updateTeacher.getTeachingDepartment()), 1) == null ) {
				logger.info("---------------- Department not found.");
			    return new ResponseEntity<RESTError>(new RESTError(4, "Department not found."), HttpStatus.NOT_FOUND);
			}
			TeacherEntity user = teacherRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Teacher not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Teacher not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Teacher identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updateTeacher.getTeachingDepartment()!= null && updateTeacher.getTeachingSubject()!= null && updateTeacher.getSchoolYear()!= null) {
				tsd = teacherDao.addSubjectsInDepartmentsToTeacher(loggedUser, user, updateTeacher.getTeachingDepartment(), updateTeacher.getTeachingSubject(), updateTeacher.getSchoolYear());
				logger.info("Subject/s in department/s added.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(tsd, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/add-departments-to-subject")
	public ResponseEntity<?> addDepartmentstoSubjectForTeacher(@PathVariable Integer id, @Valid @RequestBody TeacherDto updateTeacher, Principal principal, BindingResult result) {
		logger.info("################ /project/teacher/{id}/add-departments-to-subject/addDepartmentstoSubjectForTeacher started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateTeacher == null) {
			logger.info("---------------- Data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Data is null."), HttpStatus.BAD_REQUEST);
	      }
		if (updateTeacher.getFirstName() != null || updateTeacher.getLastName() != null || updateTeacher.getCertificate() != null || updateTeacher.getEmploymentDate() != null|| updateTeacher.getGender() != null || updateTeacher.getjMBG() != null || updateTeacher.getUsername() != null || updateTeacher.getPassword() != null || updateTeacher.getConfirmedPassword() != null) {
			logger.info("---------------- Update have non acceptable atrributes.");
	        return new ResponseEntity<RESTError>(new RESTError(2, "Update have non acceptable atrributes."), HttpStatus.NOT_ACCEPTABLE);
		}
		for (String t : updateTeacher.getTeachingDepartments()) {
			if (t ==null || t.equals("") || t.equals(" ")) {
				logger.info("---------------- New department/s is null.");
		        return new ResponseEntity<RESTError>(new RESTError(5, "New department/s is null."), HttpStatus.BAD_REQUEST);
			}
		}
		if (updateTeacher.getTeachingSubject() ==null || updateTeacher.getTeachingSubject().equals("") || updateTeacher.getTeachingSubject().equals(" ")) {
			logger.info("---------------- Subject is null.");
		    return new ResponseEntity<RESTError>(new RESTError(5, "New subject is null."), HttpStatus.BAD_REQUEST);
		}
		if (updateTeacher.getSchoolYear() ==null || updateTeacher.getSchoolYear().equals("") || updateTeacher.getSchoolYear().equals(" ")) {
			logger.info("---------------- New school year is null.");
		       return new ResponseEntity<RESTError>(new RESTError(5, "New school year is null."), HttpStatus.BAD_REQUEST);
		}
		List<TeacherSubjectDepartmentEntity> tsd = new ArrayList<TeacherSubjectDepartmentEntity>();
		try {
			if (subjectRepository.findByIdAndStatusLike(Integer.parseInt(updateTeacher.getTeachingSubject()), 1) == null ) {
				logger.info("---------------- Subject not found.");
			    return new ResponseEntity<RESTError>(new RESTError(4, "Subject not found."), HttpStatus.NOT_FOUND);
			}
			for (String s : updateTeacher.getTeachingDepartments()) {
				if (departmentRepository.findByIdAndStatusLike(Integer.parseInt(s), 1) == null) {
					logger.info("---------------- Department/s not found.");
			        return new ResponseEntity<RESTError>(new RESTError(4, "Department/s not found."), HttpStatus.NOT_FOUND);
				}
			}
			TeacherEntity user = teacherRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Teacher not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Teacher not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Teacher identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updateTeacher.getTeachingDepartments()!= null && updateTeacher.getTeachingSubject()!= null && updateTeacher.getSchoolYear()!= null) {
				tsd = teacherDao.addDepartmentsToSubjectForTeacher(loggedUser, user, updateTeacher.getTeachingDepartments(), updateTeacher.getTeachingSubject(), updateTeacher.getSchoolYear());
				logger.info("Subject/s in department/s added.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(tsd, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/remove-subject")
	public ResponseEntity<?> removeSubjectsFromTeacher(@PathVariable Integer id, @Valid @RequestBody TeacherDto updateTeacher, Principal principal, BindingResult result) {
		logger.info("################ /project/teacher/{id}/remove-subject/removeSubjectsFromTeacher started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateTeacher == null) {
			logger.info("---------------- Data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Data is null."), HttpStatus.BAD_REQUEST);
	      }
		if (updateTeacher.getFirstName() != null || updateTeacher.getLastName() != null || updateTeacher.getCertificate() != null || updateTeacher.getEmploymentDate() != null|| updateTeacher.getGender() != null || updateTeacher.getjMBG() != null || updateTeacher.getUsername() != null || updateTeacher.getPassword() != null || updateTeacher.getConfirmedPassword() != null) {
			logger.info("---------------- Update have non acceptable atrributes.");
	        return new ResponseEntity<RESTError>(new RESTError(2, "Update have non acceptable atrributes."), HttpStatus.NOT_ACCEPTABLE);
		}
		for (String t : updateTeacher.getSubjects()) {
			if (t ==null || t.equals("") || t.equals(" ")) {
				logger.info("---------------- Remove subject/s is null.");
		        return new ResponseEntity<RESTError>(new RESTError(5, "Remove subject/s is null."), HttpStatus.BAD_REQUEST);
			}
		}
		List<TeacherSubjectEntity> st = new ArrayList<TeacherSubjectEntity>();
		try {
			for (String s : updateTeacher.getSubjects()) {
				if (subjectRepository.findByIdAndStatusLike(Integer.parseInt(s), 1) == null ) {
					logger.info("---------------- Subject/s not found.");
			        return new ResponseEntity<RESTError>(new RESTError(4, "Subject/s not found."), HttpStatus.NOT_FOUND);
				}
			}
			TeacherEntity user = teacherRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Teacher not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Teacher not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Teacher identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updateTeacher.getSubjects() != null) {
				st = teacherDao.removeSubjectsFromTeacher(loggedUser, user, updateTeacher.getSubjects());
				logger.info("Subject/s added.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(st, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/remove-primarydepartment")
	public ResponseEntity<?> removePrimaryDepartmentFromTeacher(@PathVariable Integer id, @Valid @RequestBody TeacherDto updateTeacher, Principal principal, BindingResult result) {
		logger.info("################ /project/teacher/{id}/remove/remove-primarydepartment/removePrimaryDepartmentFromTeacher started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateTeacher == null) {
			logger.info("---------------- Data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Data is null."), HttpStatus.BAD_REQUEST);
	      }
		if (updateTeacher.getFirstName() != null || updateTeacher.getLastName() != null || updateTeacher.getCertificate() != null || updateTeacher.getEmploymentDate() != null|| updateTeacher.getGender() != null || updateTeacher.getjMBG() != null || updateTeacher.getUsername() != null || updateTeacher.getPassword() != null || updateTeacher.getConfirmedPassword() != null) {
			logger.info("---------------- Update have non acceptable atrributes.");
	        return new ResponseEntity<RESTError>(new RESTError(2, "Update have non acceptable atrributes."), HttpStatus.NOT_ACCEPTABLE);
		}
		if (updateTeacher.getPrimaryDepartment() ==null || updateTeacher.getPrimaryDepartment().equals("") || updateTeacher.getPrimaryDepartment().equals(" ")) {
			logger.info("---------------- Remove primary department is null.");
		    return new ResponseEntity<RESTError>(new RESTError(5, "Remove primary department is null."), HttpStatus.BAD_REQUEST);
		}
		PrimaryTeacherDepartmentEntity ptd = new PrimaryTeacherDepartmentEntity();
		try {
			if (departmentRepository.findByIdAndStatusLike(Integer.parseInt(updateTeacher.getPrimaryDepartment()), 1) == null ) {
				logger.info("---------------- Primary department not found.");
			    return new ResponseEntity<RESTError>(new RESTError(4, "Primary department not found."), HttpStatus.NOT_FOUND);
			}
			TeacherEntity user = teacherRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Teacher not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Teacher not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Teacher identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updateTeacher.getPrimaryDepartment() != null) {
				ptd = teacherDao.removePrimaryDepartmentFromTeacher(loggedUser, user, updateTeacher.getPrimaryDepartment());
				logger.info("Primary department added.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(ptd, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/remove-departments-from-subject")
	public ResponseEntity<?> removeDepartmentsFromSubjectForTeacher(@PathVariable Integer id, @Valid @RequestBody TeacherDto updateTeacher, Principal principal, BindingResult result) {
		logger.info("################ /project/teacher/{id}/remove/remove-departments-from-subject/removeDepartmentsFromSubjectForTeacher started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateTeacher == null) {
			logger.info("---------------- Data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Data is null."), HttpStatus.BAD_REQUEST);
	      }
		if (updateTeacher.getFirstName() != null || updateTeacher.getLastName() != null || updateTeacher.getCertificate() != null || updateTeacher.getEmploymentDate() != null|| updateTeacher.getGender() != null || updateTeacher.getjMBG() != null || updateTeacher.getUsername() != null || updateTeacher.getPassword() != null || updateTeacher.getConfirmedPassword() != null) {
			logger.info("---------------- Update have non acceptable atrributes.");
	        return new ResponseEntity<RESTError>(new RESTError(2, "Update have non acceptable atrributes."), HttpStatus.NOT_ACCEPTABLE);
		}
		for (String t : updateTeacher.getTeachingDepartments()) {
			if (t ==null || t.equals("") || t.equals(" ")) {
				logger.info("---------------- New department/s is null.");
		        return new ResponseEntity<RESTError>(new RESTError(5, "New department/s is null."), HttpStatus.BAD_REQUEST);
			}
		}
		if (updateTeacher.getTeachingSubject() ==null || updateTeacher.getTeachingSubject().equals("") || updateTeacher.getTeachingSubject().equals(" ")) {
			logger.info("---------------- Subject is null.");
		    return new ResponseEntity<RESTError>(new RESTError(5, "Subject is null."), HttpStatus.BAD_REQUEST);
		}
		List<TeacherSubjectDepartmentEntity> tsd = new ArrayList<TeacherSubjectDepartmentEntity>();
		try {
			if (subjectRepository.findByIdAndStatusLike(Integer.parseInt(updateTeacher.getTeachingSubject()), 1) == null ) {
				logger.info("---------------- Subject not found.");
			    return new ResponseEntity<RESTError>(new RESTError(4, "Subject not found."), HttpStatus.NOT_FOUND);
			}
			for (String s : updateTeacher.getTeachingDepartments()) {
				if (departmentRepository.findByIdAndStatusLike(Integer.parseInt(s), 1) == null) {
					logger.info("---------------- Department/s not found.");
			        return new ResponseEntity<RESTError>(new RESTError(4, "Department/s not found."), HttpStatus.NOT_FOUND);
				}
			}
			TeacherEntity user = teacherRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Teacher not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Teacher not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Teacher identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updateTeacher.getTeachingDepartments()!= null && updateTeacher.getTeachingSubject()!= null) {
				tsd = teacherDao.removeDepartmentsFromSubjectForTeacher(loggedUser, user, updateTeacher.getTeachingDepartments(), updateTeacher.getTeachingSubject());
				logger.info("Subject/s in department/s added.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(tsd, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/remove-subjects-with-departments")
	public ResponseEntity<?> removeSubjectsWithDepartmentsFromTeacher(@PathVariable Integer id, @Valid @RequestBody TeacherDto updateTeacher, Principal principal, BindingResult result) {
		logger.info("################ /project/teacher/{id}/remove/remove-subjects-with-departments/removeSubjectsWithDepartmentsFromTeacher started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateTeacher == null) {
			logger.info("---------------- Data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Data is null."), HttpStatus.BAD_REQUEST);
	      }
		if (updateTeacher.getFirstName() != null || updateTeacher.getLastName() != null || updateTeacher.getCertificate() != null || updateTeacher.getEmploymentDate() != null|| updateTeacher.getGender() != null || updateTeacher.getjMBG() != null || updateTeacher.getUsername() != null || updateTeacher.getPassword() != null || updateTeacher.getConfirmedPassword() != null) {
			logger.info("---------------- Update have non acceptable atrributes.");
	        return new ResponseEntity<RESTError>(new RESTError(2, "Update have non acceptable atrributes."), HttpStatus.NOT_ACCEPTABLE);
		}
		if (updateTeacher.getTeachingDepartment() ==null || updateTeacher.getTeachingDepartment().equals("") || updateTeacher.getTeachingDepartment().equals(" ")) {
			logger.info("---------------- Remove department is null.");
		    return new ResponseEntity<RESTError>(new RESTError(5, "Remove department is null."), HttpStatus.BAD_REQUEST);
		}
		if (updateTeacher.getTeachingSubject() ==null || updateTeacher.getTeachingSubject().equals("") || updateTeacher.getTeachingSubject().equals(" ")) {
			logger.info("---------------- Remove subject is null.");
		    return new ResponseEntity<RESTError>(new RESTError(5, "Remove subject is null."), HttpStatus.BAD_REQUEST);
		}
		TeacherSubjectDepartmentEntity tsd = new TeacherSubjectDepartmentEntity();
		try {
			if (subjectRepository.findByIdAndStatusLike(Integer.parseInt(updateTeacher.getTeachingSubject()), 1) == null ) {
				logger.info("---------------- Subject not found.");
			    return new ResponseEntity<RESTError>(new RESTError(4, "Subject not found."), HttpStatus.NOT_FOUND);
			}
			if (departmentRepository.findByIdAndStatusLike(Integer.parseInt(updateTeacher.getTeachingDepartment()), 1) == null ) {
				logger.info("---------------- Department not found.");
			    return new ResponseEntity<RESTError>(new RESTError(4, "Department not found."), HttpStatus.NOT_FOUND);
			}
			TeacherEntity user = teacherRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Teacher not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Teacher not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Teacher identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updateTeacher.getTeachingDepartment()!= null && updateTeacher.getTeachingSubject()!= null) {
				tsd = teacherDao.removeSubjectsInDepartmentsFromTeacher(loggedUser, user, updateTeacher.getTeachingDepartment(), updateTeacher.getTeachingSubject());
				logger.info("Subject/s in department/s added.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(tsd, HttpStatus.OK);
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
		logger.info("################ /project/teacher/archive/archive started.");
		logger.info("Logged user: " + principal.getName());
		TeacherEntity user = new TeacherEntity();
		try {
			user = teacherRepository.getById(id);
			if (user == null || user.getStatus() == -1) {
				logger.info("---------------- Teacher not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Teacher not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Teacher for archiving identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (id == loggedUser.getId()) {
				logger.info("---------------- Selected Id is ID of logged User: Cann't archive yourself.");
				return new ResponseEntity<RESTError>(new RESTError(3, "Selected Id is ID of logged User: Cann't archive yourself."), HttpStatus.FORBIDDEN);
		      }	
			teacherDao.archiveTeacher(loggedUser, user);
			logger.info("Teacher archived.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLike(user, EUserRole.ROLE_TEACHER);
			logger.info("Teacher's user account identified.");
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
				teacherRepository.save(user);
				logger.error("++++++++++++++++ Because of exeption Teacher with Id " + user.getId().toString() + " deleted.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/undelete/{id}")
	public ResponseEntity<?> unDelete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/teacher/undelete/{id}/unDelete started.");
		logger.info("Logged user: " + principal.getName());
		TeacherEntity user = new TeacherEntity();
		try {
			user = teacherRepository.findByIdAndStatusLike(id, 0);
			if (user == null) {
				logger.info("---------------- Teacher not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Teacher not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Teacher for undeleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			teacherDao.undeleteTeacher(loggedUser, user);
			logger.info("Teacher undeleted.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_TEACHER, 1);
			logger.info("Teacher's user account identified.");
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
				teacherRepository.save(user);
				logger.error("++++++++++++++++ Because of exeption Teacher with Id " + user.getId().toString() + " deleted.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/teacher/{id}/delete started.");
		logger.info("Logged user: " + principal.getName());
		TeacherEntity user = new TeacherEntity();
		try {
			user = teacherRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Teacher not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Teacher not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Teacher for deleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (id == loggedUser.getId()) {
				logger.info("---------------- Selected Id is ID of logged User: Cann't delete yourself.");
				return new ResponseEntity<RESTError>(new RESTError(3, "Selected Id is ID of logged User: Cann't delete yourself."), HttpStatus.FORBIDDEN);
		      }	
			teacherDao.deleteTeacher(loggedUser, user);
			logger.info("Teacher deleted.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_TEACHER, 1);
			logger.info("Teacher's user account identified.");
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
				teacherRepository.save(user);
				logger.error("++++++++++++++++ Because of exeption Teacher with Id " + user.getId().toString() + " activated.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

}
