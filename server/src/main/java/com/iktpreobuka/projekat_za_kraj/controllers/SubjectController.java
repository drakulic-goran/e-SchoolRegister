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
import com.iktpreobuka.projekat_za_kraj.entities.ClassSubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.StudentSubjectDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.StudentSubjectTeacherDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectDepartmentClassSchoolYearDTO;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectTeacherDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.TeacherSubjectDepartmentDTO;
import com.iktpreobuka.projekat_za_kraj.entities.dto.TrioStudentSubjecTeachertDto;
import com.iktpreobuka.projekat_za_kraj.repositories.ClassRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.DepartmentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.SubjectRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserAccountRepository;
import com.iktpreobuka.projekat_za_kraj.security.Views;
import com.iktpreobuka.projekat_za_kraj.services.SubjectDao;

@Controller
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value= "/project/subjects")
public class SubjectController {
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	@Autowired
	private ClassRepository classRepository;
	
	@Autowired
	private TeacherRepository teacherRepository;
	
	@Autowired
	private DepartmentRepository departmentRepository;
		
	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private SubjectDao subjectDao;
	
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	private String createErrorMessage(BindingResult result) { 
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
		}

	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll(Principal principal) {
		logger.info("################ /project/subjects/getAll started.");
		logger.info("Logged user: " + principal.getName());
		try {
			Iterable<SubjectEntity> subjects= subjectRepository.findByStatusLike(1);
			for (SubjectEntity s : subjects) {
				List<ClassSubjectEntity> tempClasses = new ArrayList<ClassSubjectEntity>();
				for (ClassSubjectEntity cse : s.getClasses()) {
					if (cse.getStatus()==1) {
						tempClasses.add(cse);
					}
				}
				s.setClasses(tempClasses);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<SubjectEntity>>(subjects, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/subjects/getAll started.");
		logger.info("Logged user: " + principal.getName());
		try {
			SubjectEntity subjects= subjectRepository.findByIdAndStatusLike(id, 1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<SubjectEntity>(subjects, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}	
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/tacher/{id}")
	public ResponseEntity<?> getByTeacherId(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/subjects/teacher/getAllForTeacher started.");
		logger.info("Logged user: " + principal.getName());
		try {
			Iterable<SubjectEntity> subjects= subjectRepository.findByTeacher(id);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<SubjectEntity>>(subjects, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/parent/{id}")
	public ResponseEntity<?> getByParentId(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/subjects/parent/getAllForParent started.");
		logger.info("Logged user: " + principal.getName());
		try {
			List<StudentSubjectDto> subjects= subjectRepository.findByParent(id);
			logger.info("Lista predmeta za decu.");

			Map<StudentEntity, List<SubjectEntity>> subjectsByStudent = new HashMap<StudentEntity, List<SubjectEntity>>();
			for (StudentSubjectDto entry : subjects) {
				SubjectEntity subject = entry.getSubject();
			    StudentEntity student = entry.getStudent();
			    List<SubjectEntity> subjectsss = subjectsByStudent.get(student);
			    if (subjectsss == null) {
			        subjectsss = new ArrayList<SubjectEntity>();
			        subjectsByStudent.put(student, subjectsss);
			    }
			    subjectsss.add(subject);
			}
			
			Map<String, List<SubjectEntity>> subjectsAndStudent = new TreeMap<String, List<SubjectEntity>>();
			for (Map.Entry<StudentEntity, List<SubjectEntity>> entry : subjectsByStudent.entrySet()) {
				subjectsAndStudent.put(entry.getKey().getFirstName() + " " + entry.getKey().getLastName(), entry.getValue());
			}			
			
			return new ResponseEntity<>(subjectsAndStudent, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/student/{id}")
	public ResponseEntity<?> getByStudentId(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/subjects/student/getAllForStudent started.");
		logger.info("Logged user: " + principal.getName());
		try {
			Iterable<SubjectEntity> subjects= subjectRepository.findByStudent(id);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<SubjectEntity>>(subjects, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/primaryteacher/{id}")
	public ResponseEntity<?> getByPrimaryTeacherId(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/subjects/teacher/getAllForTeacher started.");
		logger.info("Logged user: " + principal.getName());
		try {
			Iterable<SubjectEntity> subjects= subjectRepository.findByPrimaryTeacher(id);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<SubjectEntity>>(subjects, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/byclass/{id}")
	public ResponseEntity<?> getClassSubjects(@PathVariable String id, Principal principal) {
		logger.info("################ /project/class/subjectsbyclass/{id}/getClassSubjects started.");
		logger.info("Logged user: " + principal.getName());
		try {
			ClassEntity class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), 1);
			if (class_==null) { 
				logger.info("---------------- Searched class not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Searched class not found"), HttpStatus.NOT_FOUND);
			}
			Iterable<SubjectEntity> classes= classRepository.findSubjectsByClass(class_);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<SubjectEntity>>(classes, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_TEACHER"})
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.GET, value = "/teacher")
	public ResponseEntity<?> getAllForTeacher(Principal principal) {
		logger.info("################ /project/subjects/teacher/getAllForTeacher started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			Iterable<SubjectEntity> subjects= subjectRepository.findByTeacher(loggedUser.getId());
			//Iterable<TeacherSubjectDepartmentDTO> subjects= subjectRepository.findByTeacherWithDepartments(loggedUser.getId());
			for (SubjectEntity s : subjects) {
				List<ClassSubjectEntity> tempClasses = new ArrayList<ClassSubjectEntity>();
				for (ClassSubjectEntity cse : s.getClasses()) {
					if (cse.getStatus()==1) {
						tempClasses.add(cse);
					}
				}
				s.setClasses(tempClasses);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<SubjectEntity>>(subjects, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_TEACHER"})
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.GET, value = "/teacher/departments")
	public ResponseEntity<?> getAllDepartmentsForTeacher(Principal principal) {
		logger.info("################ /project/subjects/teacher/departments/getAllDepartmentsForTeacher started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			//Iterable<SubjectEntity> subjects= subjectRepository.findByTeacher(loggedUser.getId());
			Iterable<TeacherSubjectDepartmentDTO> subjects= subjectRepository.findByTeacherWithDepartments(loggedUser.getId());
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<TeacherSubjectDepartmentDTO>>(subjects, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/admin/departments")
	public ResponseEntity<?> getAllWithDepartments(Principal principal) {
		logger.info("################ /project/subjects/admin/departments/getAllWithDepartments started.");
		logger.info("Logged user: " + principal.getName());
		try {
			logger.info("Logged user identified.");
			//Iterable<SubjectEntity> subjects= subjectRepository.findByTeacher(loggedUser.getId());
			Iterable<TeacherSubjectDepartmentDTO> subjects= subjectRepository.findAllWithDepartmentsStatusLike(1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<TeacherSubjectDepartmentDTO>>(subjects, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_TEACHER"})
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.GET, value = "/teacher/students")
	public ResponseEntity<?> getAllForTeacherWithStudents(Principal principal) {
		logger.info("################ /project/subjects/teacher/students/getAllForTeacher started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			Iterable<SubjectDepartmentClassSchoolYearDTO> subjects= subjectRepository.findByTeacherWithStudentAndClassAndDepartment(loggedUser.getId());
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<SubjectDepartmentClassSchoolYearDTO>>(subjects, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/admin/students")
	public ResponseEntity<?> getAllWithStudents(Principal principal) {
		logger.info("################ /project/subjects/admin/students/getAllWithStudents started.");
		logger.info("Logged user: " + principal.getName());
		try {
			logger.info("Logged user identified.");
			Iterable<SubjectDepartmentClassSchoolYearDTO> subjects= subjectRepository.findAllWithStudentAndClassAndDepartmentStatusLike();
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<SubjectDepartmentClassSchoolYearDTO>>(subjects, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_TEACHER"})
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.GET, value = "/primaryteacher")
	public ResponseEntity<?> getAllForPrimaryTeacher(Principal principal) {
		logger.info("################ /project/subjects/teacher/getAllForTeacher started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			Iterable<SubjectEntity> subjects= subjectRepository.findByPrimaryTeacher(loggedUser.getId());
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<SubjectEntity>>(subjects, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_PARENT"})
	@JsonView(Views.Parent.class)
	@RequestMapping(method = RequestMethod.GET, value = "/parent")
	public ResponseEntity<?> getAllForParent(Principal principal) {
		logger.info("################ /project/subjects/parent/getAllForParent started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");	
			/*List<StudentSubjectDto> subjects= subjectRepository.findByParent(loggedUser.getId());
			logger.info("Lista predmeta za decu.");

			Map<StudentEntity, List<SubjectEntity>> subjectsByStudent = new HashMap<StudentEntity, List<SubjectEntity>>();
			for (StudentSubjectDto entry : subjects) {
				SubjectEntity subject = entry.getSubject();
			    StudentEntity student = entry.getStudent();
			    List<SubjectEntity> subjectsss = subjectsByStudent.get(student);
			    if (subjectsss == null) {
			        subjectsss = new ArrayList<SubjectEntity>();
			        subjectsByStudent.put(student, subjectsss);
			    }
			    subjectsss.add(subject);
			}
			
			Map<String, List<SubjectEntity>> subjectsAndStudent = new TreeMap<String, List<SubjectEntity>>();
			for (Map.Entry<StudentEntity, List<SubjectEntity>> entry : subjectsByStudent.entrySet()) {
				subjectsAndStudent.put(entry.getKey().getFirstName() + " " + entry.getKey().getLastName(), entry.getValue());
			}			*/
			
			Iterable<SubjectDepartmentClassSchoolYearDTO> subjectsAndStudent= subjectRepository.findByParentWithClassAndDepartment(loggedUser.getId());
			logger.info("Lista predmeta za decu.");

			
			return new ResponseEntity<>(subjectsAndStudent, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_PARENT"})
	@JsonView(Views.Parent.class)
	@RequestMapping(method = RequestMethod.GET, value = "/parent/with-teacher")
	public ResponseEntity<?> getAllWithTeacherForParent(Principal principal) {
		logger.info("################ /project/subjects/parent/with-teacher/getAllWithTeacherForParent started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			List<TrioStudentSubjecTeachertDto> subjects= subjectRepository.findStudentAndSubjectAndTeacherByStudent(loggedUser.getId());
			logger.info("Lista predmeta za decu.");
			List<StudentSubjectTeacherDto> subjectsAndTeachers = new ArrayList<StudentSubjectTeacherDto>();
			for(final TrioStudentSubjecTeachertDto t : subjects) {
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
				subjectsAndStudent.put(entry.getKey().getFirstName() + " " + entry.getKey().getLastName(), entry.getValue());
			}			
			
			return new ResponseEntity<>(subjectsAndStudent, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_STUDENT"})
	@JsonView(Views.Student.class)
	@RequestMapping(method = RequestMethod.GET, value = "/student")
	public ResponseEntity<?> getAllForStudent(Principal principal) {
		logger.info("################ /project/subjects/student/getAllForStudent started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			//Iterable<SubjectEntity> subjects= subjectRepository.findByStudent(loggedUser.getId());
			Iterable<TeacherSubjectDepartmentEntity> subjects= subjectRepository.findByStudentWithClassAndDepartment(loggedUser.getId());
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(subjects, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_STUDENT"})
	@JsonView(Views.Student.class)
	@RequestMapping(method = RequestMethod.GET, value = "/student/with-teacher")
	public ResponseEntity<?> getAllWithTeacherForStudent(Principal principal) {
		logger.info("################ /project/subjects/student/with-teacher/getAllWithTeacherForStudent started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			Iterable<SubjectTeacherDto> subjectsAndTeachers = subjectRepository.findSubjectAndTeacherByStudent(loggedUser.getId());
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(subjectsAndTeachers, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNew(@Valid @RequestBody SubjectDto newSubject, Principal principal, BindingResult result) {
		logger.info("################ /project/subjects/addNew started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors: " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newSubject == null) {
			logger.info("---------------- New subject is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "New subject is null."), HttpStatus.BAD_REQUEST);
	      }
		if (newSubject.getWeekClassesNumber() == null || newSubject.getWeekClassesNumber()<1 ) {
			logger.info("---------------- Week classes number must be 1 or more.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Week classes number must be 1 or more."), HttpStatus.BAD_REQUEST);
		}
		if (newSubject.getSubjectName() == null || newSubject.getSubjectName().equals("") || newSubject.getSubjectName().equals(" ")) {
			logger.info("---------------- Subject name is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Subject name is null."), HttpStatus.BAD_REQUEST);			
		}
		SubjectEntity subject = new SubjectEntity();
		try {
			if (newSubject.getSubjectName() != null && !newSubject.getSubjectName().equals("") && !newSubject.getSubjectName().equals(" ") && newSubject.getWeekClassesNumber() != null) {
				if (subjectRepository.findBySubjectName(newSubject.getSubjectName()) != null) {
					logger.info("---------------- Subect name already exist.");
			        return new ResponseEntity<RESTError>(new RESTError(5, "Subect name already exist."), HttpStatus.BAD_REQUEST);
				}
				UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
				logger.info("Logged user identified.");
				subject = subjectDao.addNewSubject(loggedUser, newSubject);
				logger.info("Subject created.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(subject, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> modify(@PathVariable String id, @Valid @RequestBody SubjectDto updateSubject, Principal principal, BindingResult result) {
		logger.info("################ /project/subjects/modify started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateSubject == null) {
			logger.info("---------------- New subject is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "New subject is null."), HttpStatus.BAD_REQUEST);
	      }
		if (updateSubject.getWeekClassesNumber() != null && updateSubject.getWeekClassesNumber()<1 ) {
			logger.info("---------------- Week classes number must be 1 or more.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Week classes number must be 1 or more."), HttpStatus.BAD_REQUEST);
		}
		try {
			SubjectEntity subject = subjectRepository.findById(Integer.parseInt(id)).orElse(null);
			if (subject==null || subject.getStatus()!=1) {
				logger.info("---------------- Subject not found.");
				return new ResponseEntity<RESTError>(new RESTError(4, "Subject not found."), HttpStatus.NOT_FOUND);
			}
			logger.info("Subject identified.");
			if (updateSubject.getSubjectName() != null && updateSubject.getSubjectName().equals(subject.getSubjectName()) ) {
				logger.info("---------------- Same subject name.");
			    return new ResponseEntity<RESTError>(new RESTError(5, "Same subject name."), HttpStatus.BAD_REQUEST);
			}
			if (subjectRepository.findBySubjectName(updateSubject.getSubjectName()) != null) {
				logger.info("---------------- Subject name already exist.");
			    return new ResponseEntity<RESTError>(new RESTError(5, "Subject name already exist."), HttpStatus.BAD_REQUEST);
			}
			if (updateSubject.getSubjectName() != null || updateSubject.getWeekClassesNumber() != null) {
				UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
				logger.info("Logged user identified.");
				subjectDao.modifySubject(loggedUser, subject, updateSubject);
				logger.info("Subject modified.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(subject, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/add-teachers")
	public ResponseEntity<?> addTeachersToSubject(@PathVariable Integer id, @Valid @RequestBody SubjectDto updateSubject, Principal principal, BindingResult result) {
		logger.info("################ /project/teacher/{id}/add-teachers/addTeachersToSubject started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateSubject == null) {
			logger.info("---------------- Data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Data is null."), HttpStatus.BAD_REQUEST);
	      }
		if (updateSubject.getSubjectName() != null || updateSubject.getWeekClassesNumber() != null) {
			logger.info("---------------- Update have non acceptable atrributes.");
	        return new ResponseEntity<RESTError>(new RESTError(2, "Update have non acceptable atrributes."), HttpStatus.NOT_ACCEPTABLE);
		}
		for (String t : updateSubject.getTeachers()) {
			if (t ==null || t.equals("") || t.equals(" ")) {
				logger.info("---------------- New teacher/s is null.");
		        return new ResponseEntity<RESTError>(new RESTError(5, "New teacher/s is null."), HttpStatus.BAD_REQUEST);
			}
		}
		List<TeacherSubjectEntity> ts = new ArrayList<TeacherSubjectEntity>();
		try {
			for (String t : updateSubject.getTeachers()) {
				if (teacherRepository.findByIdAndStatusLike(Integer.parseInt(t), 1) == null ) {
					logger.info("---------------- Teacher/s not found.");
			        return new ResponseEntity<RESTError>(new RESTError(4, "Teacher/s not found."), HttpStatus.NOT_FOUND);
				}
			}
			SubjectEntity subject = subjectRepository.findByIdAndStatusLike(id, 1);
			if (subject == null) {
				logger.info("---------------- Subject not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Subject not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Subject identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updateSubject.getTeachers() != null && updateSubject.getTeachers() != null) {
				ts = subjectDao.addTeachersToSubject(loggedUser, subject, updateSubject.getTeachers());
				logger.info("Teacher/s added.");
			} else {
				logger.info("---------------- Some data is null.");
		        return new ResponseEntity<RESTError>(new RESTError(5, "Some data is null."), HttpStatus.BAD_REQUEST);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(ts, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/add-class")
	public ResponseEntity<?> addClassesToSubject(@PathVariable Integer id, @Valid @RequestBody SubjectDto updateSubject, Principal principal, BindingResult result) {
		logger.info("################ /project/teacher/{id}/add-class/addClassesToSubject started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateSubject == null) {
			logger.info("---------------- Data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Data is null."), HttpStatus.BAD_REQUEST);
	      }
		if (updateSubject.getSubjectName() != null || updateSubject.getWeekClassesNumber() != null) {
			logger.info("---------------- Update have non acceptable atrributes.");
	        return new ResponseEntity<RESTError>(new RESTError(2, "Update have non acceptable atrributes."), HttpStatus.NOT_ACCEPTABLE);
		}
		for (String c : updateSubject.getClasses()) {
			if (c ==null || c.equals("") || c.equals(" ")) {
				logger.info("---------------- New class/es is null.");
		        return new ResponseEntity<RESTError>(new RESTError(5, "New class/es is null."), HttpStatus.BAD_REQUEST);
			}
		}
		if (updateSubject.getLearningProgram() ==null || updateSubject.getLearningProgram().equals("") || updateSubject.getLearningProgram().equals(" ")) {
			logger.info("---------------- New learning program is null.");
		       return new ResponseEntity<RESTError>(new RESTError(5, "New learning program is null."), HttpStatus.BAD_REQUEST);
		}
		List<ClassSubjectEntity> cs = new ArrayList<ClassSubjectEntity>();
		try {
			for (String t : updateSubject.getClasses()) {
				if (classRepository.findByIdAndStatusLike(Integer.parseInt(t), 1) == null ) {
					logger.info("---------------- Class/es not found.");
			        return new ResponseEntity<RESTError>(new RESTError(4, "Class/es not found."), HttpStatus.NOT_FOUND);
				}
			}
			SubjectEntity subject = subjectRepository.findByIdAndStatusLike(id, 1);
			if (subject == null) {
				logger.info("---------------- Subject not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Subject not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Subject identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updateSubject.getClasses() != null && updateSubject.getLearningProgram() != null && updateSubject.getClasses() != null) {
				cs = subjectDao.addClassToSubject(loggedUser, updateSubject.getClasses(), subject, updateSubject.getLearningProgram());
				logger.info("Class/es added.");
			} else {
				logger.info("---------------- Some data is null.");
		        return new ResponseEntity<RESTError>(new RESTError(5, "Some data is null."), HttpStatus.BAD_REQUEST);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(cs, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/add-teachers-with-departments")
	public ResponseEntity<?> addTeachersWithDepartmentsToSubject(@PathVariable Integer id, @Valid @RequestBody SubjectDto updateSubject, Principal principal, BindingResult result) {
		logger.info("################ /project/teacher/{id}/add-teachers-with-departments/addTeachersWithDepartmentsToSubject started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateSubject == null) {
			logger.info("---------------- Data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Data is null."), HttpStatus.BAD_REQUEST);
	      }
		if (updateSubject.getSubjectName() != null || updateSubject.getWeekClassesNumber() != null) {
			logger.info("---------------- Update have non acceptable atrributes.");
	        return new ResponseEntity<RESTError>(new RESTError(2, "Update have non acceptable atrributes."), HttpStatus.NOT_ACCEPTABLE);
		}
		if (updateSubject.getTeachingTeacher() ==null || updateSubject.getTeachingTeacher().equals("") || updateSubject.getTeachingTeacher().equals(" ")) {
			logger.info("---------------- New teacher is null.");
		    return new ResponseEntity<RESTError>(new RESTError(5, "New teacher is null."), HttpStatus.BAD_REQUEST);
		}
		if (updateSubject.getTeachingDepartment() ==null || updateSubject.getTeachingDepartment().equals("") || updateSubject.getTeachingDepartment().equals(" ")) {
			logger.info("---------------- New department is null.");
		    return new ResponseEntity<RESTError>(new RESTError(5, "New department is null."), HttpStatus.BAD_REQUEST);
		}
		if (updateSubject.getSchoolYear() ==null || updateSubject.getSchoolYear().equals("") || updateSubject.getSchoolYear().equals(" ")) {
			logger.info("---------------- New school year is null.");
		       return new ResponseEntity<RESTError>(new RESTError(5, "New school year is null."), HttpStatus.BAD_REQUEST);
		}
		TeacherSubjectDepartmentEntity tsd = new TeacherSubjectDepartmentEntity();
		try {
			if (teacherRepository.findByIdAndStatusLike(Integer.parseInt(updateSubject.getTeachingTeacher()), 1) == null ) {
				logger.info("---------------- Teacher not found.");
			    return new ResponseEntity<RESTError>(new RESTError(4, "Teacher not found."), HttpStatus.NOT_FOUND);
			}
			if (departmentRepository.findByIdAndStatusLike(Integer.parseInt(updateSubject.getTeachingDepartment()), 1) == null ) {
				logger.info("---------------- Department not found.");
			    return new ResponseEntity<RESTError>(new RESTError(4, "Department not found."), HttpStatus.NOT_FOUND);
			}	
			SubjectEntity subject = subjectRepository.findByIdAndStatusLike(id, 1);
			if (subject == null) {
				logger.info("---------------- Subject not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Subject not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Subject identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updateSubject.getTeachingDepartment()!= null && updateSubject.getTeachingTeacher()!= null && updateSubject.getSchoolYear() != null && updateSubject.getTeachingTeacher() != null && updateSubject.getTeachingDepartment() != null) {
				tsd = subjectDao.addTeacherAndDepartmentToSubject(loggedUser, subject, updateSubject.getTeachingDepartment(), updateSubject.getTeachingTeacher(), updateSubject.getSchoolYear());
				logger.info("Teachers/s in department/s added.");
			} else {
				logger.info("---------------- Some data is null.");
		        return new ResponseEntity<RESTError>(new RESTError(5, "Some data is null."), HttpStatus.BAD_REQUEST);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/remove-teachers")
	public ResponseEntity<?> removeTeachersFromSubject(@PathVariable Integer id, @Valid @RequestBody SubjectDto updateSubject, Principal principal, BindingResult result) {
		logger.info("################ /project/teacher/{id}/remove-teachers/removeTeachersFromSubject started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateSubject == null) {
			logger.info("---------------- Data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Data is null."), HttpStatus.BAD_REQUEST);
	      }
		if (updateSubject.getSubjectName() != null || updateSubject.getWeekClassesNumber() != null) {
			logger.info("---------------- Update have non acceptable atrributes.");
	        return new ResponseEntity<RESTError>(new RESTError(2, "Update have non acceptable atrributes."), HttpStatus.NOT_ACCEPTABLE);
		}
		for (String t : updateSubject.getTeachers()) {
			if (t ==null || t.equals("") || t.equals(" ")) {
				logger.info("---------------- Remove teacher/s is null.");
		        return new ResponseEntity<RESTError>(new RESTError(5, "Remove teacher/s is null."), HttpStatus.BAD_REQUEST);
			}
		}
		List<TeacherSubjectEntity> ts = new ArrayList<TeacherSubjectEntity>();
		try {
			for (String t : updateSubject.getTeachers()) {
				if (teacherRepository.findByIdAndStatusLike(Integer.parseInt(t), 1) == null ) {
					logger.info("---------------- Teacher/s not found.");
			        return new ResponseEntity<RESTError>(new RESTError(4, "Teacher/s not found."), HttpStatus.NOT_FOUND);
				}
			}
			SubjectEntity subject = subjectRepository.findByIdAndStatusLike(id, 1);
			if (subject == null) {
				logger.info("---------------- Subject not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Subject not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Subject identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updateSubject.getTeachers() != null) {
				ts = subjectDao.removeTeachersFromSubject(loggedUser, subject, updateSubject.getTeachers());
				logger.info("Teacher/s removed.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(ts, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/remove-class")
	public ResponseEntity<?> removeClassesFromSubject(@PathVariable Integer id, @Valid @RequestBody SubjectDto updateSubject, Principal principal, BindingResult result) {
		logger.info("################ /project/teacher/{id}/remove-class/removeClassesFromSubject started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateSubject == null) {
			logger.info("---------------- Data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Data is null."), HttpStatus.BAD_REQUEST);
	      }
		if (updateSubject.getSubjectName() != null || updateSubject.getWeekClassesNumber() != null) {
			logger.info("---------------- Update have non acceptable atrributes.");
	        return new ResponseEntity<RESTError>(new RESTError(2, "Update have non acceptable atrributes."), HttpStatus.NOT_ACCEPTABLE);
		}
		for (String c : updateSubject.getClasses()) {
			if (c ==null || c.equals("") || c.equals(" ")) {
				logger.info("---------------- Removing class/es is null.");
		        return new ResponseEntity<RESTError>(new RESTError(5, "New class/es is null."), HttpStatus.BAD_REQUEST);
			}
		}
		List<ClassSubjectEntity> cs = new ArrayList<ClassSubjectEntity>();
		try {
			for (String t : updateSubject.getClasses()) {
				if (classRepository.findByIdAndStatusLike(Integer.parseInt(t), 1) == null ) {
					logger.info("---------------- Class/es not found.");
			        return new ResponseEntity<RESTError>(new RESTError(4, "Class/es not found."), HttpStatus.NOT_FOUND);
				}
			}
			SubjectEntity subject = subjectRepository.findByIdAndStatusLike(id, 1);
			if (subject == null) {
				logger.info("---------------- Subject not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Subject not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Subject identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updateSubject.getClasses() != null) {
				cs = subjectDao.removeClassFromSubject(loggedUser, updateSubject.getClasses(), subject);
				logger.info("Class/es removed.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(cs, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/remove-teachers-with-departments")
	public ResponseEntity<?> removeTeachersWithDepartmentsFromSubject(@PathVariable Integer id, @Valid @RequestBody SubjectDto updateSubject, Principal principal, BindingResult result) {
		logger.info("################ /project/teacher/{id}/remove-teachers-with-departments/removeTeachersWithDepartmentsFromSubject started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateSubject == null) {
			logger.info("---------------- Data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Data is null."), HttpStatus.BAD_REQUEST);
	      }
		if (updateSubject.getSubjectName() != null || updateSubject.getWeekClassesNumber() != null) {
			logger.info("---------------- Update have non acceptable atrributes.");
	        return new ResponseEntity<RESTError>(new RESTError(2, "Update have non acceptable atrributes."), HttpStatus.NOT_ACCEPTABLE);
		}
		if (updateSubject.getTeachingTeacher() ==null || updateSubject.getTeachingTeacher().equals("") || updateSubject.getTeachingTeacher().equals(" ")) {
			logger.info("---------------- New teacher is null.");
		    return new ResponseEntity<RESTError>(new RESTError(5, "New teacher is null."), HttpStatus.BAD_REQUEST);
		}
		if (updateSubject.getTeachingDepartment() ==null || updateSubject.getTeachingDepartment().equals("") || updateSubject.getTeachingDepartment().equals(" ")) {
			logger.info("---------------- New department is null.");
		    return new ResponseEntity<RESTError>(new RESTError(5, "New department is null."), HttpStatus.BAD_REQUEST);
		}
		TeacherSubjectDepartmentEntity tsd = new TeacherSubjectDepartmentEntity();
		try {
			if (teacherRepository.findByIdAndStatusLike(Integer.parseInt(updateSubject.getTeachingTeacher()), 1) == null ) {
				logger.info("---------------- Teacher not found.");
			    return new ResponseEntity<RESTError>(new RESTError(4, "Teacher not found."), HttpStatus.NOT_FOUND);
			}
			if (departmentRepository.findByIdAndStatusLike(Integer.parseInt(updateSubject.getTeachingDepartment()), 1) == null ) {
				logger.info("---------------- Department not found.");
			    return new ResponseEntity<RESTError>(new RESTError(4, "Department not found."), HttpStatus.NOT_FOUND);
			}	
			SubjectEntity subject = subjectRepository.findByIdAndStatusLike(id, 1);
			if (subject == null) {
				logger.info("---------------- Subject not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Subject not found."), HttpStatus.NOT_FOUND);
		      }
			logger.info("Subject identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updateSubject.getTeachingDepartment()!= null && updateSubject.getTeachingTeacher()!= null) {
				tsd = subjectDao.removeTeacherAndDepartmentFromSubject(loggedUser, subject, updateSubject.getTeachingDepartment(), updateSubject.getTeachingTeacher());
				logger.info("Teachers/s in department/s removed.");
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
	@RequestMapping(method = RequestMethod.PUT, value = "/undelete/{id}")
	public ResponseEntity<?> unDelete(@PathVariable String id, Principal principal) {
		logger.info("################ /project/subjects/unDelete started.");
		logger.info("Logged user: " + principal.getName());
		try {
			SubjectEntity subject = subjectRepository.findByIdAndStatusLike(Integer.parseInt(id), 0);
			if (subject==null || subject.getStatus()!=0) {
				logger.info("Subject not found.");
				return new ResponseEntity<RESTError>(new RESTError(4, "Subject not found."), HttpStatus.NOT_FOUND);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			subjectDao.undeleteSubject(loggedUser, subject);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<SubjectEntity>(subject, HttpStatus.OK);
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
	public ResponseEntity<?> archive(@PathVariable String id, Principal principal) {
		logger.info("################ /project/subjects/archive started.");
		logger.info("Logged user: " + principal.getName());
		try {
			SubjectEntity subject = subjectRepository.findById(Integer.parseInt(id)).orElse(null);
			if (subject==null || subject.getStatus()==-1) {
				logger.info("Subject not found.");
				return new ResponseEntity<RESTError>(new RESTError(4, "Subject not found."), HttpStatus.NOT_FOUND);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			subjectDao.archiveSubject(loggedUser, subject);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<SubjectEntity>(subject, HttpStatus.OK);
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
	public ResponseEntity<?> delete(@PathVariable String id, Principal principal) {
		logger.info("################ /project/subjects/delete started.");
		logger.info("Logged user: " + principal.getName());
		try {
			SubjectEntity subject = subjectRepository.findByIdAndStatusLike(Integer.parseInt(id), 1);
			if (subject==null  || subject.getStatus()!=1) {
				logger.info("Subject not found.");
				return new ResponseEntity<RESTError>(new RESTError(4, "Subject not found."), HttpStatus.NOT_FOUND);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			subjectDao.deleteSubject(loggedUser, subject);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<SubjectEntity>(subject, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
