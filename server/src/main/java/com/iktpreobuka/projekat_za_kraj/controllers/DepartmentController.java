package com.iktpreobuka.projekat_za_kraj.controllers;

import java.security.Principal;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.controllers.util.RESTError;
import com.iktpreobuka.projekat_za_kraj.entities.ClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.PrimaryTeacherDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.ClassDepartmentClDepDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.DepartmentDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.DepartmentStudentDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EClass;
import com.iktpreobuka.projekat_za_kraj.repositories.ClassRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.DepartmentClassRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.DepartmentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.StudentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.SubjectRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserAccountRepository;
import com.iktpreobuka.projekat_za_kraj.security.Views;
import com.iktpreobuka.projekat_za_kraj.services.DepartmentDao;

@Controller
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value= "/project/department")
public class DepartmentController {

	@Autowired
	private DepartmentDao departmentDao;

	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	private DepartmentClassRepository departmentClassRepository;
	
	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private ClassRepository classRepository;

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	private String createErrorMessage(BindingResult result) { 
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
		}

	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll(Principal principal) {
		logger.info("################ /project/department/getAll started.");
		logger.info("Logged user: " + principal.getName());
		try {
			Iterable<DepartmentEntity> departments= departmentRepository.findByStatusLike(1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(departments, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/withclass")
	public ResponseEntity<?> getAllWithClass(Principal principal) {
		logger.info("################ /project/department/getAllWithClass started.");
		logger.info("Logged user: " + principal.getName());
		try {
			Iterable<ClassDepartmentClDepDto> departments= departmentRepository.findWithClassByStatusLike(1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(departments, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/department/getById started.");
		logger.info("Logged user: " + principal.getName());
		try {
			DepartmentEntity department= departmentRepository.findByIdAndStatusLike(id, 1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(department, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/deleted")
	public ResponseEntity<?> getAllDeleted(Principal principal) {
		logger.info("################ /project/department/getAllDeleted started.");
		logger.info("Logged user: " + principal.getName());
		try {
			Iterable<DepartmentEntity> departments= departmentRepository.findByStatusLike(0);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(departments, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/deleted/{id}")
	public ResponseEntity<?> getByIdDeleted(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/department/getByIdDeleted started.");
		logger.info("Logged user: " + principal.getName());
		try {
			DepartmentEntity department= departmentRepository.findByIdAndStatusLike(id, 0);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(department, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived")
	public ResponseEntity<?> getAllArchived(Principal principal) {
		logger.info("################ /project/department/archived/getAllArchived started.");
		logger.info("Logged user: " + principal.getName());
		try {
			Iterable<DepartmentEntity> departments= departmentRepository.findByStatusLike(-1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(departments, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/{id}")
	public ResponseEntity<?> getByIdArchived(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/department/archived/getByIdArchived started.");
		logger.info("Logged user: " + principal.getName());
		try {
			DepartmentEntity department= departmentRepository.findByIdAndStatusLike(id, -1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(department, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/students")
	public ResponseEntity<?> getAllAndStudents(Principal principal) {
		logger.info("################ /project/department/students/getAllAndStudents started.");
		logger.info("Logged user: " + principal.getName());
		try {
			Iterable<DepartmentEntity> departments= departmentRepository.findByStatusLike(1);
			
			List<DepartmentStudentDto> cd = new ArrayList<DepartmentStudentDto>();
			for(final DepartmentEntity c : departments) {
				for(final StudentDepartmentEntity d : c.getStudents()) {
					if (d.getStatus() == 1) {
						cd.add(new DepartmentStudentDto(d.getDepartment(), d.getStudent()));
					}
				}
			}
			
			Map<DepartmentEntity, List<StudentEntity>> departmentsByClass = new HashMap<DepartmentEntity, List<StudentEntity>>();
			for (DepartmentStudentDto entry : cd) {
				DepartmentEntity department = entry.getDepartment();
				StudentEntity clas = entry.getStudent();
			    List<StudentEntity> subjectsss = departmentsByClass.get(department);
			    if (subjectsss == null) {
			        subjectsss = new ArrayList<StudentEntity>();
			        departmentsByClass.put(department, subjectsss);
			    }
			    subjectsss.add(clas);
			}
			
			Map<String, List<StudentEntity>> departmentsAndClass = new TreeMap<String, List<StudentEntity>>();
			for (Map.Entry<DepartmentEntity, List<StudentEntity>> entry : departmentsByClass.entrySet()) {
				departmentsAndClass.put("Id: " + entry.getKey().getId() + ", department: " + entry.getKey().getDepartmentLabel(), entry.getValue());
			}			

			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(departmentsAndClass, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/students")
	public ResponseEntity<?> getDepartmentWithStudents(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/department/{id}/students/getDepartmentWithStudents started.");
		logger.info("Logged user: " + principal.getName());
		try {
			DepartmentEntity department= departmentRepository.findByIdAndStatusLike(id, 1);
			
			List<DepartmentStudentDto> cd = new ArrayList<DepartmentStudentDto>();
			for(final StudentDepartmentEntity d : department.getStudents()) {
				if (d.getStatus() == 1) {
					cd.add(new DepartmentStudentDto(d.getDepartment(), d.getStudent()));
				}
			}
			
			Map<DepartmentEntity, List<StudentEntity>> departmentsByClass = new HashMap<DepartmentEntity, List<StudentEntity>>();
			for (DepartmentStudentDto entry : cd) {
				DepartmentEntity dep = entry.getDepartment();
				StudentEntity clas = entry.getStudent();
			    List<StudentEntity> subjectsss = departmentsByClass.get(dep);
			    if (subjectsss == null) {
			        subjectsss = new ArrayList<StudentEntity>();
			        departmentsByClass.put(dep, subjectsss);
			    }
			    subjectsss.add(clas);
			}
			
			Map<String, List<StudentEntity>> departmentsAndClass = new TreeMap<String, List<StudentEntity>>();
			for (Map.Entry<DepartmentEntity, List<StudentEntity>> entry : departmentsByClass.entrySet()) {
				departmentsAndClass.put("Id: " + entry.getKey().getId() + ", department: " + entry.getKey().getDepartmentLabel(), entry.getValue());
			}			

			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(departmentsAndClass, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNew(@Valid @RequestBody DepartmentDto newDepartment, Principal principal, BindingResult result) {
		logger.info("################ /project/department/addNew started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newDepartment == null) {
			logger.info("---------------- New department is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "New department is null."), HttpStatus.BAD_REQUEST);
	      }
		if (newDepartment.getDepartment_class() == null || newDepartment.getSchoolYear() == null || newDepartment.getDepartmentLabel() == null || newDepartment.getEnrollmentYear() == null) {
			logger.info("---------------- Some data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Some data is null."), HttpStatus.BAD_REQUEST);
		}
		if (newDepartment.getDepartment_class() != null && (newDepartment.getDepartment_class().equals(" ") || newDepartment.getDepartment_class().equals("") ) ) {
			logger.info("---------------- Correct class label must be provided.");
	        return new ResponseEntity<RESTError>(new RESTError(2, "Correct class label must be provided."), HttpStatus.NOT_ACCEPTABLE);
		}
		if (newDepartment.getDepartmentLabel() != null && (newDepartment.getDepartmentLabel().equals(" ") || newDepartment.getDepartmentLabel().equals("") ) ) {
			logger.info("---------------- Correct department label must be provided.");
	        return new ResponseEntity<RESTError>(new RESTError(2, "Correct department label must be provided."), HttpStatus.NOT_ACCEPTABLE);
		}
		if (newDepartment.getEnrollmentYear() != null && (newDepartment.getEnrollmentYear().equals(" ") || newDepartment.getEnrollmentYear().equals("") ) ) {
			logger.info("---------------- Correct enrollment year must be provided.");
	        return new ResponseEntity<RESTError>(new RESTError(2, "Correct enrollment year must be provided."), HttpStatus.NOT_ACCEPTABLE);
		}
		if (newDepartment.getSchoolYear() != null && (newDepartment.getSchoolYear().equals(" ") || newDepartment.getSchoolYear().equals("") ) ) {
			logger.info("---------------- Correct school year must be provided.");
	        return new ResponseEntity<RESTError>(new RESTError(2, "Correct school year must be provided."), HttpStatus.NOT_ACCEPTABLE);
		}
		DepartmentEntity department = new DepartmentEntity();
		try {
			if (newDepartment.getDepartmentLabel() != null && newDepartment.getEnrollmentYear() != null && departmentRepository.findByDepartmentLabelAndEnrollmentYearAndStatusLike(newDepartment.getDepartmentLabel(), newDepartment.getEnrollmentYear(), 1) != null) {
				logger.info("---------------- Department label for that enrollment year already exist.");
				return new ResponseEntity<RESTError>(new RESTError(2, "Department label for that enrollment year already exist."), HttpStatus.NOT_ACCEPTABLE);
			}
			ClassEntity class_ = classRepository.findByClassLabelAndStatusLike(EClass.valueOf(newDepartment.getDepartment_class()), 1);
			if (class_==null || class_.getStatus()!=1) {
				logger.info("---------------- Class not found.");
				return new ResponseEntity<RESTError>(new RESTError(4, "Class not found."), HttpStatus.NOT_FOUND);
			}
			logger.info("Class identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			department = departmentDao.addNewDepartment(loggedUser, newDepartment);		
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(department, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> modify(@PathVariable String id, @Valid @RequestBody DepartmentDto updateDepartment, Principal principal, BindingResult result) {
		logger.info("################ /project/department/modify started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (id == null) {
			logger.info("---------------- Department is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Department is null."), HttpStatus.BAD_REQUEST);
	      }
		if (updateDepartment == null) {
			logger.info("---------------- New department is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "New department is null."), HttpStatus.BAD_REQUEST);
	      }
		if (updateDepartment.getDepartment_class() != null && (updateDepartment.getDepartment_class().equals(" ") || updateDepartment.getDepartment_class().equals("") ) ) {
			logger.info("---------------- Correct class label must be provided.");
	        return new ResponseEntity<RESTError>(new RESTError(2, "Correct class label must be provided."), HttpStatus.NOT_ACCEPTABLE);
		}
		if (updateDepartment.getDepartmentLabel() != null && (updateDepartment.getDepartmentLabel().equals(" ") || updateDepartment.getDepartmentLabel().equals("") ) ) {
			logger.info("---------------- Correct department label must be provided.");
	        return new ResponseEntity<RESTError>(new RESTError(2, "Correct department label must be provided."), HttpStatus.NOT_ACCEPTABLE);
		}
		if (updateDepartment.getEnrollmentYear() != null && (updateDepartment.getEnrollmentYear().equals(" ") || updateDepartment.getEnrollmentYear().equals("") ) ) {
			logger.info("---------------- Correct enrollment year must be provided.");
	        return new ResponseEntity<RESTError>(new RESTError(2, "Correct enrollment year must be provided."), HttpStatus.NOT_ACCEPTABLE);
		}
		if (updateDepartment.getSchoolYear() != null && (updateDepartment.getSchoolYear().equals(" ") || updateDepartment.getSchoolYear().equals("") ) ) {
			logger.info("---------------- Correct school year must be provided.");
	        return new ResponseEntity<RESTError>(new RESTError(2, "Correct school year must be provided."), HttpStatus.NOT_ACCEPTABLE);
		}
		DepartmentEntity department = new DepartmentEntity();
		try {
			if (updateDepartment.getDepartmentLabel() != null && updateDepartment.getEnrollmentYear() != null && departmentRepository.findByDepartmentLabelAndEnrollmentYearAndStatusLike(updateDepartment.getDepartmentLabel(), updateDepartment.getEnrollmentYear(), 1) != null) {
				logger.info("---------------- Department label and enrollment year already exist.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "Department label and enrollment year already exist."), HttpStatus.NOT_ACCEPTABLE);
			}
			if (updateDepartment.getDepartmentLabel() != null && updateDepartment.getEnrollmentYear() == null && departmentRepository.findByDepartmentLabelAndEnrollmentYearAndStatusLike(updateDepartment.getDepartmentLabel(), department.getEnrollmentYear(), 1) != null) {
				logger.info("---------------- Department label already exist.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "Department label already exist."), HttpStatus.NOT_ACCEPTABLE);
			}
			department = departmentRepository.findByIdAndStatusLike(Integer.parseInt(id), 1);
			if (department==null || department.getStatus()!=1) {
				logger.info("---------------- Department not found.");
				return new ResponseEntity<RESTError>(new RESTError(4, "Department not found."), HttpStatus.NOT_FOUND);
			}
			logger.info("Department identified.");
			if (updateDepartment.getDepartmentLabel() == null && updateDepartment.getEnrollmentYear() != null && departmentRepository.findByDepartmentLabelAndEnrollmentYearAndStatusLike(department.getDepartmentLabel(), updateDepartment.getEnrollmentYear(), 1) != null) {
				logger.info("---------------- Enrollment year already exist.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "Enrollment year already exist."), HttpStatus.NOT_ACCEPTABLE);
			}
			if ((updateDepartment.getDepartment_class() != null && updateDepartment.getSchoolYear() == null) || (updateDepartment.getDepartment_class() == null && updateDepartment.getSchoolYear() != null)) {
				logger.info("---------------- School year and class must be provided together.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "School year and class must be provided together."), HttpStatus.NOT_ACCEPTABLE);
			}
			if (updateDepartment.getDepartment_class() != null && !updateDepartment.getDepartment_class().equals(" ") && !updateDepartment.getDepartment_class().equals("")) {
				ClassEntity class_ = classRepository.findByClassLabelAndStatusLike(EClass.valueOf(updateDepartment.getDepartment_class()), 1);
				if (class_==null || class_.getStatus()!=1) {
					logger.info("---------------- Class not found.");
					return new ResponseEntity<RESTError>(new RESTError(4, "Class not found."), HttpStatus.NOT_FOUND);
				}
			}
			logger.info("Class identified.");
			if ((updateDepartment.getDepartment_class() != null && updateDepartment.getSchoolYear() != null) || updateDepartment.getDepartmentLabel() != null || updateDepartment.getEnrollmentYear() != null) {
				UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
				logger.info("Logged user identified.");
				departmentDao.modifyDepartment(loggedUser, department, updateDepartment);	
				logger.info("Department modified.");
			} else {
				logger.info("---------------- School year missing.");
				return new ResponseEntity<RESTError>(new RESTError(5, "School year missing."), HttpStatus.BAD_REQUEST);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(department, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

/*	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/change/{id}/class/{c_id}")
	public ResponseEntity<?> modifyClassLabel(@PathVariable String id, @PathVariable String c_id, Principal principal) {
		logger.info("################ /project/departments//change/{id}/class/{c_id}/modifyClassLabel started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		if (id == null) {
			logger.info("This is an info message: Department is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		if (c_id == null) {
			logger.info("This is an info message: New class of department is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		try {
			DepartmentEntity department = departmentRepository.findById(Integer.parseInt(id)).orElse(null);
			if (department==null || department.getStatus()!=1) {
				logger.info("This is an info message: Searched department not found.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			}
			ClassEntity class_ = classRepository.findById(Integer.parseInt(c_id)).orElse(null);
			if (class_==null || class_.getStatus()!=1) {
				logger.info("This is an info message: Searched class not found.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			}
			//***************************************
			//department.setClass_department(class_);
			//***************************************
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			department.setUpdatedById(loggedUser.getUser().getId());
			departmentRepository.save(department);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<DepartmentEntity>(department, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/change/{id}/label/{label}")
	public ResponseEntity<?> modifyDepartmentLabel(@PathVariable String id, @PathVariable String label, Principal principal) {
		logger.info("################ /project/departments//change/{id}/label/{label}/modifyDepartmentLabel started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		if (id == null) {
			logger.info("This is an info message: Department is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		if (label == null) {
			logger.info("This is an info message: New label of department is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		try {
			DepartmentEntity department = departmentRepository.findById(Integer.parseInt(id)).orElse(null);
			if (department==null || department.getStatus()!=1) {
				logger.info("This is an info message: Searched department not found.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			}
			department.setDepartmentLabel(label);
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			//AdminEntity user= adminRepository.getByUserAccount(loggedUser);
			//AdminEntity user= (AdminEntity) loggedUser.getUser();
			department.setUpdatedById(loggedUser.getUser().getId());
			departmentRepository.save(department);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<DepartmentEntity>(department, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
*/
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/subject/{s_id}/teacher/{t_id}/schoolyear/{school_year}")
	public ResponseEntity<?> addTeacherAndSubjectToDepartment(@PathVariable Integer id, @PathVariable Integer s_id, @PathVariable Integer t_id, @PathVariable String school_year, Principal principal) {
		logger.info("################ /project/department/{id}/subject/{s_id}/teacher/{t_id}/schoolyear/{school_year}/addTeacherAndSubjectToDepartment started.");
		logger.info("Logged user: " + principal.getName());
		logger.info("Data: " + id.toString() + "  " + s_id.toString() + "   " + t_id.toString() + "   " + school_year.toString());
		if (id == null || t_id == null || s_id == null || school_year == null) {
			logger.info("---------------- Some data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Some data is null."), HttpStatus.BAD_REQUEST);
	      }
		TeacherSubjectDepartmentEntity tsd = new TeacherSubjectDepartmentEntity();
		try {
			DepartmentEntity department = departmentRepository.findByIdAndStatusLike(id, 1);
			if (department == null || department.getStatus()!=1) {
				logger.info("---------------- Department not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Department not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Department identified.");
			ClassEntity clas = departmentClassRepository.getClasByDepartmentAndStatusLike(department, 1);
			if (department == null || department.getStatus()!=1) {
				logger.info("---------------- Class not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Class not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Class identified.");
			TeacherEntity teacher = teacherRepository.findByIdAndStatusLike(t_id, 1);
			if (teacher == null || teacher.getStatus()!=1) {
				logger.info("---------------- Teacher not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Teacher not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Teacher identified.");
			SubjectEntity subject = subjectRepository.findByIdAndStatusLike(s_id, 1);
			if (subject == null || subject.getStatus()!=1) {
				logger.info("---------------- Subject not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Subject not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Subject identified.");
			if (school_year != null && !school_year.equals(" ") && !school_year.equals("")) {
				UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
				logger.info("Logged user identified.");
				tsd = departmentDao.addTeacherAndSubjectToDepartment(loggedUser, teacher, department, clas, subject, school_year);
				logger.info("Teacher and subject added to department.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<TeacherSubjectDepartmentEntity>(tsd, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/remove/subject/{s_id}/teacher/{t_id}")
	public ResponseEntity<?> removeTeacherAndSubjectFromDepartment(@PathVariable Integer id, @PathVariable Integer s_id, @PathVariable Integer t_id, Principal principal) {
		logger.info("################ /project/department/{id}/remove/subject/{s_id}/teacher/{t_id}/removeTeacherAndSubjectFromDepartment started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null || t_id == null || s_id == null) {
			logger.info("---------------- Some data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Some data is null."), HttpStatus.BAD_REQUEST);
	      }
		TeacherSubjectDepartmentEntity tsd = new TeacherSubjectDepartmentEntity();
		try {
			DepartmentEntity department = departmentRepository.findByIdAndStatusLike(id, 1);
			if (department == null || department.getStatus()!=1) {
				logger.info("---------------- Department not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Department not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Department identified.");
			ClassEntity clas = departmentClassRepository.getClasByDepartmentAndStatusLike(department, 1);
			if (department == null || department.getStatus()!=1) {
				logger.info("---------------- Class not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Class not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Class identified.");
			TeacherEntity teacher = teacherRepository.findByIdAndStatusLike(t_id, 1);
			if (teacher == null || teacher.getStatus()!=1) {
				logger.info("---------------- Teacher not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Teacher not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Teacher identified.");
			SubjectEntity subject = subjectRepository.findByIdAndStatusLike(s_id, 1);
			if (subject == null || subject.getStatus()!=1) {
				logger.info("---------------- Subject not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Subject not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Subject identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			tsd = departmentDao.removeTeacherAndSubjectFromDepartment(loggedUser, teacher, department, clas, subject);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<TeacherSubjectDepartmentEntity>(tsd, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/primaryteacher/{t_id}/assignmentdate/{assignmentDate}")
	public ResponseEntity<?> addPrimaryTeacherToDepartment(@PathVariable Integer id, @PathVariable Integer t_id, @PathVariable String assignmentDate, Principal principal) {
		logger.info("################ /project/department/{id}/primaryteacher/{t_id}/assignmentdate/{assignmentDate}/addPrimaryTeacherToDepartment started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null || t_id == null || assignmentDate == null) {
			logger.info("---------------- Some data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Some data is null."), HttpStatus.BAD_REQUEST);
	      }
		PrimaryTeacherDepartmentEntity pt = new PrimaryTeacherDepartmentEntity();
		try {
			DepartmentEntity department = departmentRepository.findByIdAndStatusLike(id, 1);
			if (department == null) {
				logger.info("---------------- Department not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Department not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Department identified.");
			TeacherEntity teacher = teacherRepository.findByIdAndStatusLike(t_id, 1);
			if (teacher == null || teacher.getStatus()!=1) {
				logger.info("---------------- Teacher not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Teacher not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Teacher identified.");
			if (assignmentDate != null && !assignmentDate.equals(" ") && !assignmentDate.equals("")) {
				UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
				logger.info("Logged user identified.");
				pt = departmentDao.addPrimaryTeacherToDepartment(loggedUser, teacher, department, assignmentDate);
				logger.info("Primary teacher added to department.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<PrimaryTeacherDepartmentEntity>(pt, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/remove/primaryteacher/{t_id}")
	public ResponseEntity<?> removePrimaryTeacherToDepartment(@PathVariable Integer id, @PathVariable Integer t_id, Principal principal) {
		logger.info("################ /project/department/{id}/remove/primaryteacher/{t_id}/removePrimaryTeacherToDepartment started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null || t_id == null) {
			logger.info("---------------- Some data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Some data is null."), HttpStatus.BAD_REQUEST);
	      }
		PrimaryTeacherDepartmentEntity pt = new PrimaryTeacherDepartmentEntity();
		try {
			DepartmentEntity department = departmentRepository.findByIdAndStatusLike(id, 1);
			if (department == null || department.getStatus()!=1) {
				logger.info("---------------- Department not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Department not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Department identified.");
			TeacherEntity teacher = teacherRepository.findByIdAndStatusLike(t_id, 1);
			if (teacher == null || teacher.getStatus()!=1) {
				logger.info("---------------- Teacher not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Teacher not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Teacher identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			pt = departmentDao.removePrimaryTeacherFromDepartment(loggedUser, teacher, department);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<PrimaryTeacherDepartmentEntity>(pt, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/class/{c_id}/schoolyear/{schoolyear}")
	public ResponseEntity<?> addClassToDepartment(@PathVariable Integer id, @PathVariable Integer c_id, @PathVariable String schoolyear, Principal principal) {
		logger.info("################ /project/department/{id}/class/{c_id}/schoolyear/{schoolyear}/addClassToDepartment started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null || c_id == null || schoolyear == null) {
			logger.info("---------------- Some data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Some data is null."), HttpStatus.BAD_REQUEST);
	      }
		DepartmentClassEntity dce = new DepartmentClassEntity();
		try {
			DepartmentEntity department = departmentRepository.findByIdAndStatusLike(id, 1);
			if (department == null || department.getStatus()!=1) {
				logger.info("---------------- Department not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Department not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Department identified.");
			ClassEntity class_ = classRepository.findByIdAndStatusLike(c_id, 1);
			if (class_ == null || class_.getStatus()!=1) {
				logger.info("---------------- Class not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Class not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Class identified.");
			if (schoolyear != null && !schoolyear.equals(" ") && !schoolyear.equals("")) {
				UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
				logger.info("Logged user identified.");
				dce = departmentDao.addClassToDepartment(loggedUser, class_, department, schoolyear);
				logger.info("Class added to department.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<DepartmentClassEntity>(dce, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/remove/class/{c_id}")
	public ResponseEntity<?> removeClassFromDepartment(@PathVariable Integer id, @PathVariable Integer c_id, Principal principal) {
		logger.info("################ /project/department/{id}/remove/class/{c_id}/removeClassFromDepartment started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null || c_id == null) {
			logger.info("---------------- Some data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Some data is null."), HttpStatus.BAD_REQUEST);
	      }
		DepartmentClassEntity dce = new DepartmentClassEntity();
		try {
			DepartmentEntity department = departmentRepository.findByIdAndStatusLike(id, 1);
			if (department == null || department.getStatus()!=1) {
				logger.info("---------------- Department not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Department not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Department identified.");
			ClassEntity class_ = classRepository.findByIdAndStatusLike(c_id, 1);
			if (class_ == null || class_.getStatus()!=1) {
				logger.info("---------------- Class not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Class not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Class identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			dce = departmentDao.removeClassFromDepartment(loggedUser, class_, department);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<DepartmentClassEntity>(dce, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/student/{s_id}/transferdate/{transferdate}")
	public ResponseEntity<?> addStudentToDepartment(@PathVariable Integer id, @PathVariable Integer s_id, @PathVariable String transferdate, Principal principal) {
		logger.info("################ /project/department/{id}/student/{s_id}/transferdate/{transferdate}/addStudentToDepartment started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null || s_id == null || transferdate == null) {
			logger.info("---------------- Some data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Some data is null."), HttpStatus.BAD_REQUEST);
	      }
		StudentDepartmentEntity sde = new StudentDepartmentEntity();
		try {
			DepartmentEntity department = departmentRepository.findByIdAndStatusLike(id, 1);
			if (department == null || department.getStatus()!=1) {
				logger.info("---------------- Department not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Department not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Department identified.");
			StudentEntity student = studentRepository.findByIdAndStatusLike(s_id, 1);
			if (student == null || student.getStatus()!=1) {
				logger.info("---------------- Student not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Student not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Student identified.");
			if (transferdate != null && !transferdate.equals(" ") && !transferdate.equals("")) {
				UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
				logger.info("Logged user identified.");
				sde = departmentDao.addStudentToDepartment(loggedUser, student, department, transferdate);
				logger.info("Student added to department.");
			}
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/remove/student/{s_id}")
	public ResponseEntity<?> removeStudentFromDepartment(@PathVariable Integer id, @PathVariable Integer s_id, Principal principal) {
		logger.info("################ /project/department/{id}/remove/student/{s_id}/addStudentToDepartment started.");
		logger.info("Logged user: " + principal.getName());
		StudentDepartmentEntity sde = new StudentDepartmentEntity();
		try {
			DepartmentEntity department = departmentRepository.findByIdAndStatusLike(id, 1);
			if (department == null || department.getStatus()!=1) {
				logger.info("---------------- Department not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Department not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Department identified.");
			StudentEntity student = studentRepository.findByIdAndStatusLike(s_id, 1);
			if (student == null || student.getStatus()!=1) {
				logger.info("---------------- Student not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Student not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Student identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			sde = departmentDao.removeStudentFromDepartment(loggedUser, student, department);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/undelete/{id}")
	public ResponseEntity<?> unDelete(@PathVariable String id, Principal principal) {
		logger.info("################ /project/departments/delete started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null) {
			logger.info("---------------- Department is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Department is null."), HttpStatus.BAD_REQUEST);
	      }
		DepartmentEntity department = new DepartmentEntity();
		try {
			department = departmentRepository.findById(Integer.parseInt(id)).orElse(null);
			if (department==null || department.getStatus()!=0) {
				logger.info("----------------  Department not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Department not found."), HttpStatus.NOT_FOUND);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			departmentDao.undeleteDepartment(loggedUser, department);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<DepartmentEntity>(department, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/archive/{id}")
	public ResponseEntity<?> archive(@PathVariable String id, Principal principal) {
		logger.info("################ /project/departments/delete started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null) {
			logger.info("---------------- Department is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Department is null."), HttpStatus.BAD_REQUEST);
	      }
		DepartmentEntity department = new DepartmentEntity();
		try {
			department = departmentRepository.findById(Integer.parseInt(id)).orElse(null);
			if (department==null || department.getStatus()==-1) {
				logger.info("----------------  Department not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Department not found."), HttpStatus.NOT_FOUND);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			departmentDao.archiveDepartment(loggedUser, department);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<DepartmentEntity>(department, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable String id, Principal principal) {
		logger.info("################ /project/departments/delete started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null) {
			logger.info("---------------- Department is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Department is null."), HttpStatus.BAD_REQUEST);
	      }
		DepartmentEntity department = new DepartmentEntity();
		try {
			department = departmentRepository.findById(Integer.parseInt(id)).orElse(null);
			if (department==null || department.getStatus()!=1) {
				logger.info("----------------  Department not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Department not found."), HttpStatus.NOT_FOUND);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			departmentDao.deleteDepartment(loggedUser, department);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<DepartmentEntity>(department, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
