package com.iktpreobuka.projekat_za_kraj.controllers;

import java.security.Principal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.ParentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserAccountEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SearchStudentsDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.StudentDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.StudentSubjectTeacherDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectTeacherDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.TrioStudentSubjecTeachertDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;
import com.iktpreobuka.projekat_za_kraj.repositories.DepartmentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.ParentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.StudentDepartmentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.StudentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.SubjectRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserAccountRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserRepository;
import com.iktpreobuka.projekat_za_kraj.security.Views;
import com.iktpreobuka.projekat_za_kraj.services.StudentDao;
import com.iktpreobuka.projekat_za_kraj.services.UserAccountDao;

@Controller
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value= "/project/student")
public class StudentController {

	@Autowired
	private UserAccountDao userAccountDao;

	@Autowired
	private StudentDao studentDao;

	@Autowired
	private ParentRepository parentRepository;

	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	@Autowired
	private UserAccountRepository userAccountRepository;
	
	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	private StudentDepartmentRepository studentDepartmentRepository;

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
		logger.info("################ /project/student/getAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<StudentEntity> users= studentRepository.findByStatusLike(1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<StudentEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/withaccount")
	public ResponseEntity<?> getAllWithUserAccount(Principal principal) {
		logger.info("################ /project/student/getAllWithUserAccount started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<SearchStudentsDto> users= studentRepository.findByStatusWithUserAccount(1, EUserRole.ROLE_STUDENT);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<SearchStudentsDto>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/withaccount/withdepartment")
	public ResponseEntity<?> getAllWithUserAccountAndDepartmentAndClass(Principal principal) {
		logger.info("################ /project/student/getAllWithUserAccountAndDepartmentAndClass started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<SearchStudentsDto> users= studentRepository.findByStatusWithUserAccountAndDepartmentAndClass(1, EUserRole.ROLE_STUDENT);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<SearchStudentsDto>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/student/getById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			StudentEntity user= studentRepository.findByIdAndStatusLike(id, 1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<StudentEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_STUDENT")
	@JsonView(Views.Student.class)
	@RequestMapping(method = RequestMethod.GET, value = "/logged")
	public ResponseEntity<?> getLoggedStudent(Principal principal) {
		logger.info("################ /project/student/getLoggedStudent started.");
		logger.info("Logged username: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			StudentEntity user= studentRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<StudentEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/deleted")
	public ResponseEntity<?> getAllDeleted(Principal principal) {
		logger.info("################ /project/student/deleted/getAllDeleted started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<StudentEntity> users= studentRepository.findByStatusLike(0);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<StudentEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/deleted/{id}")
	public ResponseEntity<?> getDeletedById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/student/deleted/getDeletedById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			StudentEntity user= studentRepository.findByIdAndStatusLike(id, 0);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<StudentEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived")
	public ResponseEntity<?> getAllArchived(Principal principal) {
		logger.info("################ /project/student/archived/getAllArchived started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<StudentEntity> users= studentRepository.findByStatusLike(-1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<StudentEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/{id}")
	public ResponseEntity<?> getArchivedById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/student/archived/getArchivedById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			StudentEntity user= studentRepository.findByIdAndStatusLike(id, -1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<StudentEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/teaching")
	public ResponseEntity<?> getAllWithTeachersAndSubjects(Principal principal) {
		logger.info("################ /project/student/teaching/getAllWithTeachersAndSubjects started.");
		logger.info("Logged username: " + principal.getName());
		try {			
			List<TrioStudentSubjecTeachertDto> students= subjectRepository.findStudentAndSubjectAndTeacher();
			List<StudentSubjectTeacherDto> subjectsAndTeachers = new ArrayList<StudentSubjectTeacherDto>();
			for(final TrioStudentSubjecTeachertDto t : students) {
			   StudentSubjectTeacherDto temp = new StudentSubjectTeacherDto();
			   SubjectTeacherDto temp1 = new SubjectTeacherDto();
			   temp1.setSubject(t.getSubject());
			   temp1.setTeacher(t.getTeacher());
			   temp.setStudent(t.getStudent());
			   temp.setSubject(temp1);
			   subjectsAndTeachers.add(temp);
			}

			Map<StudentEntity, List<SubjectTeacherDto>> subjectsByStudent = new HashMap<StudentEntity, List<SubjectTeacherDto>>();
			for (StudentSubjectTeacherDto entry : subjectsAndTeachers) {
				SubjectTeacherDto subject = entry.getSubject();
			    StudentEntity student = entry.getStudent();
			    List<SubjectTeacherDto> subjectsss = subjectsByStudent.get(student);
			    if (subjectsss == null) {
			        subjectsss = new ArrayList<SubjectTeacherDto>();
			        subjectsByStudent.put(student, subjectsss);
			    }
			    subjectsss.add(subject);
			}
			
			Map<String, List<SubjectTeacherDto>> subjectsAndStudent = new TreeMap<String, List<SubjectTeacherDto>>();
			for (Map.Entry<StudentEntity, List<SubjectTeacherDto>> entry : subjectsByStudent.entrySet()) {
				subjectsAndStudent.put("Id: " + entry.getKey().getId() + ", " + entry.getKey().getFirstName() + " " + entry.getKey().getLastName(), entry.getValue());
			}			
						
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(subjectsAndStudent, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/teaching")
	public ResponseEntity<?> getTeachersAndSubjects(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/student/{id}/teaching/getTeachersAndSubjects started.");
		logger.info("Logged username: " + principal.getName());
		try {			
			List<TrioStudentSubjecTeachertDto> students= subjectRepository.findStudentAndSubjectAndTeacherForStudent(id);
			List<StudentSubjectTeacherDto> subjectsAndTeachers = new ArrayList<StudentSubjectTeacherDto>();
			for(final TrioStudentSubjecTeachertDto t : students) {
			   StudentSubjectTeacherDto temp = new StudentSubjectTeacherDto();
			   SubjectTeacherDto temp1 = new SubjectTeacherDto();
			   temp1.setSubject(t.getSubject());
			   temp1.setTeacher(t.getTeacher());
			   temp.setStudent(t.getStudent());
			   temp.setSubject(temp1);
			   subjectsAndTeachers.add(temp);
			}

			Map<StudentEntity, List<SubjectTeacherDto>> subjectsByStudent = new HashMap<StudentEntity, List<SubjectTeacherDto>>();
			for (StudentSubjectTeacherDto entry : subjectsAndTeachers) {
				SubjectTeacherDto subject = entry.getSubject();
			    StudentEntity student = entry.getStudent();
			    List<SubjectTeacherDto> subjectsss = subjectsByStudent.get(student);
			    if (subjectsss == null) {
			        subjectsss = new ArrayList<SubjectTeacherDto>();
			        subjectsByStudent.put(student, subjectsss);
			    }
			    subjectsss.add(subject);
			}
			
			Map<String, List<SubjectTeacherDto>> subjectsAndStudent = new TreeMap<String, List<SubjectTeacherDto>>();
			for (Map.Entry<StudentEntity, List<SubjectTeacherDto>> entry : subjectsByStudent.entrySet()) {
				subjectsAndStudent.put("Id: " + entry.getKey().getId() + ", " + entry.getKey().getFirstName() + " " + entry.getKey().getLastName(), entry.getValue());
			}			
						
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(subjectsAndStudent, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewSudent(@Valid @RequestBody StudentDto newStudent, Principal principal, BindingResult result) {
		logger.info("################ /project/student/addNewSudent started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newStudent == null) {
			logger.info("---------------- New student is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "New student is null."), HttpStatus.BAD_REQUEST);
	      }
		if (newStudent.getFirstName() == null || newStudent.getLastName() == null || newStudent.getSchoolIdentificationNumber() == null || newStudent.getEnrollmentDate() == null|| newStudent.getGender() == null || newStudent.getjMBG() == null) {
			logger.info("---------------- Some atributes is null.");
			return new ResponseEntity<RESTError>(new RESTError(5, "Some atributes is null."), HttpStatus.BAD_REQUEST);
		}
		UserEntity user = new StudentEntity();
		try {
			if (newStudent.getjMBG() != null && studentRepository.getByJMBG(newStudent.getjMBG()) != null) {
				logger.info("---------------- JMBG already exist.");
				return new ResponseEntity<RESTError>(new RESTError(2, "JMBG already exist."), HttpStatus.NOT_ACCEPTABLE);
			}
			if (newStudent.getSchoolIdentificationNumber() != null && studentRepository.getBySchoolIdentificationNumber(newStudent.getSchoolIdentificationNumber()) != null) {
				logger.info("---------------- School identification number already exist.");
				return new ResponseEntity<RESTError>(new RESTError(2, "School identification number already exist."), HttpStatus.NOT_ACCEPTABLE);
			}
			if (newStudent.getAccessRole() != null && !newStudent.getAccessRole().equals("ROLE_STUDENT")) {
				logger.info("---------------- Access role must be ROLE_STUDENT.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "Access role must be ROLE_STUDENT."), HttpStatus.NOT_ACCEPTABLE);
			}		
			if (newStudent.getUsername() != null && userAccountRepository.getByUsername(newStudent.getUsername()) != null) {
				logger.info("---------------- Username already exist.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "Username already exist."), HttpStatus.NOT_ACCEPTABLE);
		    }
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			user = studentDao.addNewStudent(loggedUser, newStudent);
			logger.info("Student created.");
			if (newStudent.getUsername() != null && newStudent.getPassword() != null && newStudent.getConfirmedPassword() != null && newStudent.getPassword().equals(newStudent.getConfirmedPassword())) {
				UserAccountEntity account = userAccountDao.addNewUserAccount(loggedUser, user, newStudent.getUsername(), EUserRole.ROLE_STUDENT, newStudent.getPassword());
				logger.info("Account created.");
				return new ResponseEntity<>(account, HttpStatus.OK);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ This is an exception message: " + e.getMessage());
			if (user != null && studentRepository.findByIdAndStatusLike(user.getId(), 1) != null) {
				studentRepository.deleteById(user.getId());
				logger.error("++++++++++++++++ Because of exeption Teacher with Id " + user.getId().toString() + " deleted.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_STUDENT", "ROLE_ADMIN"})
	@JsonView(Views.Student.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> modifyStudent(@PathVariable Integer id, @Valid @RequestBody StudentDto updateStudent, Principal principal, BindingResult result) {
		logger.info("################ /project/student/{id}/modifyStudent started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateStudent == null) {
			logger.info("---------------- New student is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "New student is null."), HttpStatus.BAD_REQUEST);
	      }
		StudentEntity user = new StudentEntity();
		try {
			user = studentRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Student not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Student not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Student identified.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_STUDENT, 1);
			logger.info("Student's user account identified.");
			if (updateStudent.getjMBG() != null && updateStudent.getjMBG().equals(user.getjMBG()) ) {
				logger.info("---------------- Same JMBG.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "Same JMBG."), HttpStatus.NOT_ACCEPTABLE);
			}
			if (updateStudent.getSchoolIdentificationNumber() != null && updateStudent.getSchoolIdentificationNumber().equals(user.getSchoolIdentificationNumber()) ) {
				logger.info("---------------- Same school identification number.");
				return new ResponseEntity<RESTError>(new RESTError(2, "Same school identification number."), HttpStatus.NOT_ACCEPTABLE);
			}
			if (updateStudent.getUsername() != null && updateStudent.getUsername().equals(account.getUsername()) ) {
				logger.info("---------------- Same username.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "Same username."), HttpStatus.NOT_ACCEPTABLE);
			}
			if (updateStudent.getjMBG() != null && userRepository.getByJMBG(updateStudent.getjMBG()) != null) {
				logger.info("---------------- JMBG already exist.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "JMBG already exist."), HttpStatus.NOT_ACCEPTABLE);
			}
			if (updateStudent.getSchoolIdentificationNumber() != null && studentRepository.getBySchoolIdentificationNumber(updateStudent.getSchoolIdentificationNumber()) != null) {
				logger.info("---------------- School identification number already exist.");
				return new ResponseEntity<RESTError>(new RESTError(2, "School identification number already exist."), HttpStatus.NOT_ACCEPTABLE);
			}
			if (updateStudent.getAccessRole() != null && !updateStudent.getAccessRole().equals("ROLE_STUDENT")) {
				logger.info("---------------- Access role must be ROLE_STUDENT.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "Access role must be ROLE_STUDENT."), HttpStatus.NOT_ACCEPTABLE);
			}		
			if (updateStudent.getUsername() != null && userAccountRepository.getByUsername(updateStudent.getUsername()) != null) {
				logger.info("---------------- Username already exist.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "Username already exist."), HttpStatus.NOT_ACCEPTABLE);
		      }
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updateStudent.getFirstName() != null || updateStudent.getLastName() != null || updateStudent.getSchoolIdentificationNumber() != null || updateStudent.getEnrollmentDate() != null|| updateStudent.getGender() != null || updateStudent.getjMBG() != null) {
				studentDao.modifyStudent(loggedUser, user, updateStudent);
				logger.info("Student modified.");
			}
			if (account != null) {
				if (updateStudent.getUsername() != null && !updateStudent.getUsername().equals("") && !updateStudent.getUsername().equals(" ") && userAccountRepository.getByUsername(updateStudent.getUsername()) == null) {
					userAccountDao.modifyAccountUsername(loggedUser, account, updateStudent.getUsername());
					logger.info("Username modified.");					
				}
				if (updateStudent.getPassword() != null && !updateStudent.getPassword().equals("") && !updateStudent.getPassword().equals(" ") && updateStudent.getConfirmedPassword() != null && updateStudent.getPassword().equals(updateStudent.getConfirmedPassword())) {
					userAccountDao.modifyAccountPassword(loggedUser, account, updateStudent.getPassword());
					logger.info("Password modified.");
				}
				logger.info("Account modified.");
				logger.info("---------------- Finished OK.");
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/parent/{p_id}")
	public ResponseEntity<?> addParent(@PathVariable Integer id, @PathVariable Integer p_id, Principal principal) {
		logger.info("################ /project/student/{id}/parent/{p_id}/addParent started.");
		logger.info("Logged user: " + principal.getName());
		StudentEntity user = new StudentEntity();
		try {
			user = studentRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Student not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Student not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Student identified.");
			ParentEntity parent = parentRepository.findByIdAndStatusLike(p_id, 1);
			if (parent == null) {
				logger.info("---------------- Parent not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Parent not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Parent identified.");		
			if (user.getParents().contains(parent)) {
				logger.info("---------------- Parent already exist.");
				return new ResponseEntity<RESTError>(new RESTError(2, "Parent already exist."), HttpStatus.NOT_ACCEPTABLE);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			studentDao.addParentToStudent(loggedUser, user, parent);		
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/remove/parent/{p_id}")
	public ResponseEntity<?> removeParent(@PathVariable Integer id, @PathVariable Integer p_id, Principal principal) {
		logger.info("################ /project/student/{id}/remove/parent/{p_id}/removeParent started.");
		logger.info("Logged user: " + principal.getName());
		StudentEntity user = new StudentEntity();
		try {
			user = studentRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Student not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Student not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Student identified.");
			ParentEntity parent = parentRepository.findByIdAndStatusLike(p_id, 1);
			if (parent == null) {
				logger.info("---------------- Parent not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Parent not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Parent identified.");		
			if (!user.getParents().contains(parent)) {
				logger.info("---------------- Parent not student parent.");
				return new ResponseEntity<RESTError>(new RESTError(4, "Parent not student parent."), HttpStatus.NOT_FOUND);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			studentDao.removeParentFromStudent(loggedUser, user, parent);		
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/department/{d_id}/transferdate/{transferdate}")
	public ResponseEntity<?> addDepartmentToStudent(@PathVariable Integer id, @PathVariable Integer d_id, @PathVariable String transferdate, Principal principal) {
		logger.info("################ /project/student/{id}/department/{d_id}/transferdate/{transferdate}/addDepartmentToStudent started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null || d_id == null || transferdate == null) {
			logger.info("---------------- Some data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Some data is null."), HttpStatus.BAD_REQUEST);
	      }
		StudentDepartmentEntity sde = new StudentDepartmentEntity();
		try {
			StudentEntity user = studentRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Student not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Student not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Student identified.");
			DepartmentEntity department = departmentRepository.findByIdAndStatusLike(d_id, 1);
			if (department == null) {
				logger.info("---------------- Department not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Department not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Department identified.");
			if (transferdate == null || transferdate.equals(" ") || transferdate.equals("") || studentDepartmentRepository.findByStudentAndDepartmentAndTransferDate(user, department, Date.valueOf(transferdate)) != null) {
				logger.info("---------------- Transfer date same as last transfer date.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "Transfer date same as last transfer date."), HttpStatus.NOT_ACCEPTABLE);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			sde = studentDao.addDepartmentToStudent(loggedUser, user, department, transferdate);
			logger.info("Department added to student.");
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<StudentDepartmentEntity>(sde, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/remove/department/{d_id}")
	public ResponseEntity<?> removeDepartmentFromStudent(@PathVariable Integer id, @PathVariable Integer d_id, Principal principal) {
		logger.info("################ /project/student/{id}/remove/department/{d_id}/removeDepartmentFromStudent started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null || d_id == null) {
			logger.info("---------------- Some data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Some data is null."), HttpStatus.BAD_REQUEST);
	      }
		StudentDepartmentEntity sde = new StudentDepartmentEntity();
		try {
			StudentEntity user = studentRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Student not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Student not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Student identified.");
			DepartmentEntity department = departmentRepository.findByIdAndStatusLike(d_id, 1);
			if (department == null) {
				logger.info("---------------- Department not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Department not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Department identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			sde = studentDao.removeDepartmentFromStudent(loggedUser, user, department);
			logger.info("Department removed from student.");
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<StudentDepartmentEntity>(sde, HttpStatus.OK);
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
		logger.info("################ /project/student/archive/archive started.");
		logger.info("Logged user: " + principal.getName());
		StudentEntity user = new StudentEntity();
		try {
			user = studentRepository.getById(id);
			if (user == null || user.getStatus() == -1) {
				logger.info("---------------- Student not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Student not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Student for archiving identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (id == loggedUser.getId()) {
				logger.info("---------------- Selected Id is ID of logged User: Cann't archive yourself.");
				return new ResponseEntity<RESTError>(new RESTError(3, "Selected Id is ID of logged User: Cann't archive yourself."), HttpStatus.FORBIDDEN);
		      }	
			studentDao.archiveStudent(loggedUser, user);
			logger.info("Student archived.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLike(user, EUserRole.ROLE_STUDENT);
			logger.info("Student's user account identified.");
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
				studentRepository.save(user);
				logger.error("++++++++++++++++ Because of exeption Teacher with Id " + user.getId().toString() + " deleted.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/undelete/{id}")
	public ResponseEntity<?> unDelete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/student/undelete/{id}/unDelete started.");
		logger.info("Logged user: " + principal.getName());
		StudentEntity user = new StudentEntity();
		try {
			user = studentRepository.findByIdAndStatusLike(id, 0);
			if (user == null) {
				logger.info("---------------- Student not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Student not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Student for undeleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			studentDao.undeleteStudent(loggedUser, user);
			logger.info("Student undeleted.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_STUDENT, 1);
			logger.info("Student's user account identified.");
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
				studentRepository.save(user);
				logger.error("++++++++++++++++ Because of exeption Teacher with Id " + user.getId().toString() + " deleted.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/student/{id}/delete started.");
		logger.info("Logged user: " + principal.getName());
		StudentEntity user = new StudentEntity();
		try {
			user = studentRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Student not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Student not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Student for deleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (id == loggedUser.getId()) {
				logger.info("---------------- Selected Id is ID of logged User: Cann't delete yourself.");
				return new ResponseEntity<RESTError>(new RESTError(3, "Selected Id is ID of logged User: Cann't delete yourself."), HttpStatus.FORBIDDEN);
		      }	
			studentDao.deleteStudent(loggedUser, user);
			logger.info("Student deleted.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_STUDENT, 1);
			logger.info("Student's user account identified.");
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
				studentRepository.save(user);
				logger.error("++++++++++++++++ Because of exeption Teacher with Id " + user.getId().toString() + " activated.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

}
