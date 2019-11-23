package com.iktpreobuka.projekat_za_kraj.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
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
import com.iktpreobuka.projekat_za_kraj.entities.GradeEntity;
import com.iktpreobuka.projekat_za_kraj.entities.ParentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserAccountEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.GradeDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.StudentSubjectGradesClassDTO;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectFinalGradesDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectGradesClassDTO;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectGradesDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.ESemester;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;
import com.iktpreobuka.projekat_za_kraj.models.EmailObject;
import com.iktpreobuka.projekat_za_kraj.repositories.GradeRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.ParentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.StudentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.SubjectRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherSubjectDepartmentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserAccountRepository;
import com.iktpreobuka.projekat_za_kraj.security.Views;
import com.iktpreobuka.projekat_za_kraj.services.EmailService;
import com.iktpreobuka.projekat_za_kraj.services.GradeDao;

@Controller
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value= "/project/grades")
public class GradeController {
	
	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private GradeRepository gradeRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private ParentRepository parentRepository;

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private GradeDao gradeDao;

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private TeacherSubjectDepartmentRepository teacherSubjectDepartmentRepository;


	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	private String createErrorMessage(BindingResult result) { 
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
		}

	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "")
	public ResponseEntity<?> getAll(Principal principal) {
		logger.info("################ /project/grades/getAll started.");
		logger.info("Logged user: " + principal.getName());
		try {
			Iterable<GradeEntity> grades= gradeRepository.findByStatusLike(1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<GradeEntity>>(grades, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/grades/{id}/getById started.");
		logger.info("Logged user: " + principal.getName());
		try {
			GradeEntity grade= gradeRepository.findByIdAndStatusLike(id, 1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<GradeEntity>(grade, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_TEACHER"})
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.GET, value = "/teacher")
	public ResponseEntity<?> getAllTeacher(Principal principal) {
		logger.info("################ /project/grades/teacher/getAllTeacher started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			TeacherEntity user = teacherRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			List<GradeEntity> grade = gradeRepository.findByTeacher(user);
			//List<GradeEntity> grade = gradeRepository.findByTeacherWithNullGrades(user);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<List<GradeEntity>>(grade, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_TEACHER"})
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.GET, value = "/teacher/{id}")
	public ResponseEntity<?> getByIdTeacher(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/grades/teacher/{id}/getByIdTeacher started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			TeacherEntity user = teacherRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			GradeEntity grade = gradeRepository.findByTeacherAndGradeId(user, id);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<GradeEntity>(grade, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_PARENT"})
	@JsonView(Views.Parent.class)
	@RequestMapping(method = RequestMethod.GET, value = "/parent")
	public ResponseEntity<?> getAllParent(Principal principal) {
		logger.info("################ /project/grades/parent/getAllParent started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			ParentEntity user = parentRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			//List<GradeEntity> grade = gradeRepository.findByParent(user);
			List<StudentSubjectGradesClassDTO> grade = gradeRepository.findGradesWithStudentAndClassAndSubjectByParent(user);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<List<StudentSubjectGradesClassDTO>>(grade, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_PARENT"})
	@JsonView(Views.Parent.class)
	@RequestMapping(method = RequestMethod.GET, value = "/parent/{id}")
	public ResponseEntity<?> getByIdParent(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/grades/parent/{id}/getByIdParent started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			ParentEntity user = parentRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			GradeEntity grade = gradeRepository.findByParentAndGradeId(user, id);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<GradeEntity>(grade, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_STUDENT"})
	@JsonView(Views.Student.class)
	@RequestMapping(method = RequestMethod.GET, value = "/student")
	public ResponseEntity<?> getAllStudent(Principal principal) {
		logger.info("################ /project/grades/student/getAllStudent started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			StudentEntity user = studentRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			//List<GradeEntity> grade = gradeRepository.findByStudentAndStatusLike(user, 1);
			List<SubjectGradesClassDTO> grade = gradeRepository.findGradesWithClassAndSubjectByStudent(user.getId());
			//logger.info(grade.get(0).getDepartment().getDepartmentLabel());
			//logger.info(grade.get(0).getClas().getClassLabel().toString());
			//logger.info(grade.get(0).getSubject().getSubjectName());
			//logger.info(grade.get(0).getGrade().getGradeValue().toString());
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<List<SubjectGradesClassDTO>>(grade, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_STUDENT"})
	@JsonView(Views.Student.class)
	@RequestMapping(method = RequestMethod.GET, value = "/student/{id}")
	public ResponseEntity<?> getByIdStudent(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/grades/student/{id}/getByIdStudent started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			StudentEntity user = studentRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			GradeEntity grade = gradeRepository.findByStudentAndIdAndStatusLike(user, id, 1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<GradeEntity>(grade, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_STUDENT"})
	@JsonView(Views.Student.class)
	@RequestMapping(method = RequestMethod.GET, value = "/student/semester/{semester}/groupedbysubject")
	public ResponseEntity<?> getAllStudentGroupedBySubjectForSemester(@PathVariable String semester, Principal principal) {
		logger.info("################ /project/grades/student/semester/{semester}/groupedbysubject/getAllStudentGroupedBySubjectForSemester started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			StudentEntity user = studentRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			List<SubjectGradesDto> grades= gradeRepository.findGradesWithSubjectBySemesterAndStudent(user.getId(), ESemester.valueOf(semester));
			Map<SubjectEntity, List<GradeEntity>> gradesBySubject = new HashMap<SubjectEntity, List<GradeEntity>>();
			for (SubjectGradesDto  entry : grades) {
				SubjectEntity subject = entry.getSubject();
			    GradeEntity grade = entry.getGrade();
			    List<GradeEntity> gradess = gradesBySubject.get(subject);
			    if (gradess == null) {
			        gradess = new ArrayList<GradeEntity>();
			        gradesBySubject.put(subject, gradess);
			    }
			    gradess.add(grade);
			}
			
			Map<String, List<GradeEntity>> gradesAndSubject = new TreeMap<String, List<GradeEntity>>();
			for (Map.Entry<SubjectEntity, List<GradeEntity>> entry : gradesBySubject.entrySet()) {
				gradesAndSubject.put(entry.getKey().getSubjectName(), entry.getValue());
			}			

			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(gradesAndSubject, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_STUDENT"})
	@JsonView(Views.Student.class)
	@RequestMapping(method = RequestMethod.GET, value = "/student/semester/{semester}/bysubject/{id}")
	public ResponseEntity<?> getByStudentAndSemsesterAndSubjectStudent(@PathVariable Integer id, @PathVariable String semester, Principal principal) {
		logger.info("################ /project/grades/student/semester/{semester}/bysubject/{id}/getByStudentAndSubjectStudent started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			StudentEntity user = studentRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			SubjectEntity subject = subjectRepository.findByIdAndStatusLike(id, 1);
			List<GradeEntity> grade = null;
			if (user!=null && subject!=null)
				grade = gradeRepository.findByStudentAndSemesterAndSubject(user, subject, ESemester.valueOf(semester));
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<List<GradeEntity>>(grade, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_STUDENT"})
	@JsonView(Views.Student.class)
	@RequestMapping(method = RequestMethod.GET, value = "/student/semester/{semester}/finalbysubject")
	public ResponseEntity<?> getAllStudentFinalBySubjectForSemester(@PathVariable String semester, Principal principal) {
		logger.info("################ /project/grades/student/semester/{semester}/finalbysubject/getAllStudentFinalBySubjectForSemester started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			StudentEntity user = studentRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			List<SubjectFinalGradesDto> grades= gradeRepository.findFinalGradesWithSubjectBySemesterAndStudent(user.getId(), ESemester.valueOf(semester));
			Map<SubjectEntity, List<Double>> gradesBySubject = new HashMap<SubjectEntity, List<Double>>();
			for (SubjectFinalGradesDto  entry : grades) {
				SubjectEntity subject = entry.getSubject();
				Double grade = entry.getGrade();
			    List<Double> gradess = gradesBySubject.get(subject);
			    if (gradess == null) {
			        gradess = new ArrayList<Double>();
			        gradesBySubject.put(subject, gradess);
			    }
			    gradess.add(grade);
			}
			
			Map<String, List<Double>> gradesAndSubject = new TreeMap<String, List<Double>>();
			for (Map.Entry<SubjectEntity, List<Double>> entry : gradesBySubject.entrySet()) {
				gradesAndSubject.put(entry.getKey().getSubjectName(), entry.getValue());
			}			

			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(gradesAndSubject, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_PARENT"})
	@JsonView(Views.Parent.class)
	@RequestMapping(method = RequestMethod.GET, value = "/parent/semester/{semester}/groupedbysubject")
	public ResponseEntity<?> getParentStudentsGroupedBySubjectForSemester(@PathVariable String semester, Principal principal) {
		logger.info("################ /project/grades/parent/semester/{semester}/groupedbysubject/getParentStudentsGroupedBySubjectForSemester started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			ParentEntity parent = parentRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			List<StudentEntity> students = studentRepository.findByParent(parent.getId());
			
			Map<StudentEntity, Map<String, List<GradeEntity>>> finalna = new HashMap<StudentEntity, Map<String, List<GradeEntity>>>();

			for (StudentEntity user : students) {
				Map<SubjectEntity, List<GradeEntity>> gradesBySubject = new HashMap<SubjectEntity, List<GradeEntity>>();
				List<SubjectGradesDto> grades= gradeRepository.findGradesWithSubjectBySemesterAndStudent(user.getId(), ESemester.valueOf(semester));
				for (SubjectGradesDto  entry : grades) {
					SubjectEntity subject = entry.getSubject();
				    GradeEntity grade = entry.getGrade();
				    List<GradeEntity> gradess = gradesBySubject.get(subject);
				    if (gradess == null) {
				        gradess = new ArrayList<GradeEntity>();
				        gradesBySubject.put(subject, gradess);
				    }
				    gradess.add(grade);
				}
				Map<String, List<GradeEntity>> gradesAndSubject = new TreeMap<String, List<GradeEntity>>();
				for (Map.Entry<SubjectEntity, List<GradeEntity>> entry : gradesBySubject.entrySet()) {
					gradesAndSubject.put(entry.getKey().getSubjectName(), entry.getValue());
				}			
				finalna.put(user, gradesAndSubject);
			}
			
			Map<String, Map<String, List<GradeEntity>>> zaispis = new TreeMap<String, Map<String, List<GradeEntity>>>();
			for (Map.Entry<StudentEntity, Map<String, List<GradeEntity>>> entry : finalna.entrySet()) {
				zaispis.put(entry.getKey().getFirstName() + " " + entry.getKey().getLastName(), entry.getValue());
			}			

			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(zaispis, HttpStatus.OK);

		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_PARENT"})
	@JsonView(Views.Parent.class)
	@RequestMapping(method = RequestMethod.GET, value = "/parent/semester/{semester}/bysubject/{id}")
	public ResponseEntity<?> getParentStudentsGroupedBySubjectForSemesterAndSubject(@PathVariable String semester, @PathVariable Integer id, Principal principal) {
		logger.info("################ /project/grades/parent/semester/{semester}/bysubject/{id}/getParentStudentsGroupedBySubjectForSemester started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			ParentEntity parent = parentRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			List<StudentEntity> students = studentRepository.findByParent(parent.getId());
			
			Map<StudentEntity, Map<String, List<GradeEntity>>> finalna = new HashMap<StudentEntity, Map<String, List<GradeEntity>>>();

			for (StudentEntity user : students) {
				Map<SubjectEntity, List<GradeEntity>> gradesBySubject = new HashMap<SubjectEntity, List<GradeEntity>>();
				List<SubjectGradesDto> grades= gradeRepository.findGradesWithSubjectBySemesterAndStudentAndSubject(user.getId(), id, ESemester.valueOf(semester));
				for (SubjectGradesDto  entry : grades) {
					SubjectEntity subject = entry.getSubject();
				    GradeEntity grade = entry.getGrade();
				    List<GradeEntity> gradess = gradesBySubject.get(subject);
				    if (gradess == null) {
				        gradess = new ArrayList<GradeEntity>();
				        gradesBySubject.put(subject, gradess);
				    }
				    gradess.add(grade);
				}
				Map<String, List<GradeEntity>> gradesAndSubject = new TreeMap<String, List<GradeEntity>>();
				for (Map.Entry<SubjectEntity, List<GradeEntity>> entry : gradesBySubject.entrySet()) {
					gradesAndSubject.put(entry.getKey().getSubjectName(), entry.getValue());
				}			
				finalna.put(user, gradesAndSubject);
			}
			
			Map<String, Map<String, List<GradeEntity>>> zaispis = new TreeMap<String, Map<String, List<GradeEntity>>>();
			for (Map.Entry<StudentEntity, Map<String, List<GradeEntity>>> entry : finalna.entrySet()) {
				zaispis.put(entry.getKey().getFirstName() + " " + entry.getKey().getLastName(), entry.getValue());
			}			

			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(zaispis, HttpStatus.OK);

		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_PARENT"})
	@JsonView(Views.Parent.class)
	@RequestMapping(method = RequestMethod.GET, value = "/parent/semester/{semester}/finalbysubject")
	public ResponseEntity<?> getParentStudentsFinalBySubjectForSemester(@PathVariable String semester, Principal principal) {
		logger.info("################ /project/grades/parent/semester/{semester}/finalbysubject/getParentStudentsFinalBySubjectForSemester started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			ParentEntity parent = parentRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			List<StudentEntity> students = studentRepository.findByParent(parent.getId());
			
			Map<StudentEntity, Map<String, List<Double>>> finalna = new HashMap<StudentEntity, Map<String, List<Double>>>();

			for (StudentEntity user : students) {
				Map<SubjectEntity, List<Double>> gradesBySubject = new HashMap<SubjectEntity, List<Double>>();
				List<SubjectFinalGradesDto> grades= gradeRepository.findFinalGradesWithSubjectBySemesterAndStudent(user.getId(), ESemester.valueOf(semester));
				for (SubjectFinalGradesDto  entry : grades) {
					SubjectEntity subject = entry.getSubject();
				    Double grade = entry.getGrade();
				    List<Double> gradess = gradesBySubject.get(subject);
				    if (gradess == null) {
				        gradess = new ArrayList<Double>();
				        gradesBySubject.put(subject, gradess);
				    }
				    gradess.add(grade);
				}
				Map<String, List<Double>> gradesAndSubject = new TreeMap<String, List<Double>>();
				for (Map.Entry<SubjectEntity, List<Double>> entry : gradesBySubject.entrySet()) {
					gradesAndSubject.put(entry.getKey().getSubjectName(), entry.getValue());
				}			
				finalna.put(user, gradesAndSubject);
			}
			
			Map<String, Map<String, List<Double>>> zaispis = new TreeMap<String, Map<String, List<Double>>>();
			for (Map.Entry<StudentEntity, Map<String, List<Double>>> entry : finalna.entrySet()) {
				zaispis.put(entry.getKey().getFirstName() + " " + entry.getKey().getLastName(), entry.getValue());
			}			

			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(zaispis, HttpStatus.OK);

		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_TEACHER"})
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.GET, value = "/primary-teacher/semester/{semester}/groupedbysubject")
	public ResponseEntity<?> getPrimaryTeacherStudentsGroupedBySubjectForSemester(@PathVariable String semester, Principal principal) {
		logger.info("################ /project/grades/primary-teacher/semester/{semester}/groupedbysubject/getPrimaryTeacherStudentsGroupedBySubjectForSemester started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			TeacherEntity teacher = teacherRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			List<StudentEntity> students = studentRepository.findByPrimaryTeacherId(teacher.getId());
			
			Map<StudentEntity, Map<String, List<GradeEntity>>> finalna = new HashMap<StudentEntity, Map<String, List<GradeEntity>>>();

			for (StudentEntity user : students) {
				Map<SubjectEntity, List<GradeEntity>> gradesBySubject = new HashMap<SubjectEntity, List<GradeEntity>>();
				List<SubjectGradesDto> grades= gradeRepository.findGradesWithSubjectBySemesterAndStudent(user.getId(), ESemester.valueOf(semester));
				for (SubjectGradesDto  entry : grades) {
					SubjectEntity subject = entry.getSubject();
				    GradeEntity grade = entry.getGrade();
				    List<GradeEntity> gradess = gradesBySubject.get(subject);
				    if (gradess == null) {
				        gradess = new ArrayList<GradeEntity>();
				        gradesBySubject.put(subject, gradess);
				    }
				    gradess.add(grade);
				}
				Map<String, List<GradeEntity>> gradesAndSubject = new TreeMap<String, List<GradeEntity>>();
				for (Map.Entry<SubjectEntity, List<GradeEntity>> entry : gradesBySubject.entrySet()) {
					gradesAndSubject.put(entry.getKey().getSubjectName(), entry.getValue());
				}			
				finalna.put(user, gradesAndSubject);
			}
			
			Map<String, Map<String, List<GradeEntity>>> zaispis = new TreeMap<String, Map<String, List<GradeEntity>>>();
			for (Map.Entry<StudentEntity, Map<String, List<GradeEntity>>> entry : finalna.entrySet()) {
				zaispis.put(entry.getKey().getFirstName() + " " + entry.getKey().getLastName(), entry.getValue());
			}			

			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(zaispis, HttpStatus.OK);

		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_TEACHER"})
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.GET, value = "/primary-teacher/semester/{semester}/bysubject/{id}")
	public ResponseEntity<?> getPrimaryTeacherStudentsGroupedBySubjectForSemesterAndSubject(@PathVariable String semester, @PathVariable Integer id, Principal principal) {
		logger.info("################ /project/grades/primary-teacher/semester/{semester}/bysubject/{id}/getPrimaryTeacherStudentsGroupedBySubjectForSemesterAndSubject started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			TeacherEntity teacher = teacherRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			List<StudentEntity> students = studentRepository.findByPrimaryTeacherId(teacher.getId());
			
			Map<StudentEntity, Map<String, List<GradeEntity>>> finalna = new HashMap<StudentEntity, Map<String, List<GradeEntity>>>();

			for (StudentEntity user : students) {
				Map<SubjectEntity, List<GradeEntity>> gradesBySubject = new HashMap<SubjectEntity, List<GradeEntity>>();
				List<SubjectGradesDto> grades= gradeRepository.findGradesWithSubjectBySemesterAndStudentAndSubject(user.getId(), id, ESemester.valueOf(semester));
				for (SubjectGradesDto  entry : grades) {
					SubjectEntity subject = entry.getSubject();
				    GradeEntity grade = entry.getGrade();
				    List<GradeEntity> gradess = gradesBySubject.get(subject);
				    if (gradess == null) {
				        gradess = new ArrayList<GradeEntity>();
				        gradesBySubject.put(subject, gradess);
				    }
				    gradess.add(grade);
				}
				Map<String, List<GradeEntity>> gradesAndSubject = new TreeMap<String, List<GradeEntity>>();
				for (Map.Entry<SubjectEntity, List<GradeEntity>> entry : gradesBySubject.entrySet()) {
					gradesAndSubject.put(entry.getKey().getSubjectName(), entry.getValue());
				}			
				finalna.put(user, gradesAndSubject);
			}
			
			Map<String, Map<String, List<GradeEntity>>> zaispis = new TreeMap<String, Map<String, List<GradeEntity>>>();
			for (Map.Entry<StudentEntity, Map<String, List<GradeEntity>>> entry : finalna.entrySet()) {
				zaispis.put(entry.getKey().getFirstName() + " " + entry.getKey().getLastName(), entry.getValue());
			}			

			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(zaispis, HttpStatus.OK);

		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_TEACHER"})
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.GET, value = "/primary-teacher/semester/{semester}/finalbysubject")
	public ResponseEntity<?> getPrimaryTeacherStudentsFinalBySubjectForSemester(@PathVariable String semester, Principal principal) {
		logger.info("################ /project/grades/primary-teacher/semester/{semester}/finalbysubject/getPrimaryTeacherStudentsFinalBySubjectForSemester started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			TeacherEntity teacher = teacherRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			List<StudentEntity> students = studentRepository.findByPrimaryTeacherId(teacher.getId());
			
			Map<StudentEntity, Map<String, List<Double>>> finalna = new HashMap<StudentEntity, Map<String, List<Double>>>();

			for (StudentEntity user : students) {
				Map<SubjectEntity, List<Double>> gradesBySubject = new HashMap<SubjectEntity, List<Double>>();
				List<SubjectFinalGradesDto> grades= gradeRepository.findFinalGradesWithSubjectBySemesterAndStudent(user.getId(), ESemester.valueOf(semester));
				for (SubjectFinalGradesDto  entry : grades) {
					SubjectEntity subject = entry.getSubject();
					Double grade = entry.getGrade();
				    List<Double> gradess = gradesBySubject.get(subject);
				    if (gradess == null) {
				        gradess = new ArrayList<Double>();
				        gradesBySubject.put(subject, gradess);
				    }
				    gradess.add(grade);
				}
				Map<String, List<Double>> gradesAndSubject = new TreeMap<String, List<Double>>();
				for (Map.Entry<SubjectEntity, List<Double>> entry : gradesBySubject.entrySet()) {
					gradesAndSubject.put(entry.getKey().getSubjectName(), entry.getValue());
				}			
				finalna.put(user, gradesAndSubject);
			}
			
			Map<String, Map<String, List<Double>>> zaispis = new TreeMap<String, Map<String, List<Double>>>();
			for (Map.Entry<StudentEntity, Map<String, List<Double>>> entry : finalna.entrySet()) {
				zaispis.put(entry.getKey().getFirstName() + " " + entry.getKey().getLastName(), entry.getValue());
			}			

			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(zaispis, HttpStatus.OK);

		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_TEACHER"})
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.GET, value = "/teacher/semester/{semester}/groupedbysubject")
	public ResponseEntity<?> getTeacherStudentsGroupedBySubjectForSemester(@PathVariable String semester, Principal principal) {
		logger.info("################ /project/grades/teacher/semester/{semester}/groupedbysubject/getTeacherStudentsGroupedBySubjectForSemester started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			TeacherEntity teacher = teacherRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			List<StudentEntity> students = studentRepository.findByTeachingTeacher(teacher.getId());
			
			Map<StudentEntity, Map<String, List<GradeEntity>>> finalna = new HashMap<StudentEntity, Map<String, List<GradeEntity>>>();

			for (StudentEntity user : students) {
				Map<SubjectEntity, List<GradeEntity>> gradesBySubject = new HashMap<SubjectEntity, List<GradeEntity>>();
				List<SubjectGradesDto> grades= gradeRepository.findGradesWithSubjectBySemesterAndStudentAndTeacher(user.getId(), teacher.getId(), ESemester.valueOf(semester));
				for (SubjectGradesDto  entry : grades) {
					SubjectEntity subject = entry.getSubject();
				    GradeEntity grade = entry.getGrade();
				    List<GradeEntity> gradess = gradesBySubject.get(subject);
				    if (gradess == null) {
				        gradess = new ArrayList<GradeEntity>();
				        gradesBySubject.put(subject, gradess);
				    }
				    gradess.add(grade);
				}
				Map<String, List<GradeEntity>> gradesAndSubject = new TreeMap<String, List<GradeEntity>>();
				for (Map.Entry<SubjectEntity, List<GradeEntity>> entry : gradesBySubject.entrySet()) {
					gradesAndSubject.put(entry.getKey().getSubjectName(), entry.getValue());
				}			
				finalna.put(user, gradesAndSubject);
			}
			
			Map<String, Map<String, List<GradeEntity>>> zaispis = new TreeMap<String, Map<String, List<GradeEntity>>>();
			for (Map.Entry<StudentEntity, Map<String, List<GradeEntity>>> entry : finalna.entrySet()) {
				zaispis.put(entry.getKey().getFirstName() + " " + entry.getKey().getLastName(), entry.getValue());
			}			

			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(zaispis, HttpStatus.OK);

		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_TEACHER"})
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.GET, value = "/teacher/semester/{semester}/bysubject/{id}")
	public ResponseEntity<?> getTeacherStudentsGroupedBySubjectForSemesterAndSubject(@PathVariable String semester, @PathVariable Integer id, Principal principal) {
		logger.info("################ /project/grades/teacher/semester/{semester}/bysubject/{id}/getTeacherStudentsGroupedBySubjectForSemesterAndSubject started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			TeacherEntity teacher = teacherRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			List<StudentEntity> students = studentRepository.findByTeachingTeacher(teacher.getId());
			
			Map<StudentEntity, Map<String, List<GradeEntity>>> finalna = new HashMap<StudentEntity, Map<String, List<GradeEntity>>>();

			for (StudentEntity user : students) {
				Map<SubjectEntity, List<GradeEntity>> gradesBySubject = new HashMap<SubjectEntity, List<GradeEntity>>();
				List<SubjectGradesDto> grades= gradeRepository.findGradesWithSubjectBySemesterAndStudentAndSubjectAndTeacher(user.getId(), teacher.getId(), id, ESemester.valueOf(semester));
				for (SubjectGradesDto  entry : grades) {
					SubjectEntity subject = entry.getSubject();
				    GradeEntity grade = entry.getGrade();
				    List<GradeEntity> gradess = gradesBySubject.get(subject);
				    if (gradess == null) {
				        gradess = new ArrayList<GradeEntity>();
				        gradesBySubject.put(subject, gradess);
				    }
				    gradess.add(grade);
				}
				Map<String, List<GradeEntity>> gradesAndSubject = new TreeMap<String, List<GradeEntity>>();
				for (Map.Entry<SubjectEntity, List<GradeEntity>> entry : gradesBySubject.entrySet()) {
					gradesAndSubject.put(entry.getKey().getSubjectName(), entry.getValue());
				}			
				finalna.put(user, gradesAndSubject);
			}
			
			Map<String, Map<String, List<GradeEntity>>> zaispis = new TreeMap<String, Map<String, List<GradeEntity>>>();
			for (Map.Entry<StudentEntity, Map<String, List<GradeEntity>>> entry : finalna.entrySet()) {
				zaispis.put(entry.getKey().getFirstName() + " " + entry.getKey().getLastName(), entry.getValue());
			}			

			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(zaispis, HttpStatus.OK);

		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/admin/semester/{semester}/groupedbysubject")
	public ResponseEntity<?> getAdminStudentsGroupedBySubjectForSemester(@PathVariable String semester, Principal principal) {
		logger.info("################ /project/grades/admin/semester/{semester}/groupedbysubject/getAdminStudentsGroupedBySubjectForSemester started.");
		logger.info("Logged user: " + principal.getName());
		try {
			logger.info("Logged user identified.");

			Iterable<StudentEntity> students = studentRepository.findByStatusLike(1);
			
			Map<StudentEntity, Map<String, List<GradeEntity>>> finalna = new HashMap<StudentEntity, Map<String, List<GradeEntity>>>();

			for (StudentEntity user : students) {
				Map<SubjectEntity, List<GradeEntity>> gradesBySubject = new HashMap<SubjectEntity, List<GradeEntity>>();
				List<SubjectGradesDto> grades= gradeRepository.findGradesWithSubjectBySemesterAndStudent(user.getId(), ESemester.valueOf(semester));
				for (SubjectGradesDto  entry : grades) {
					SubjectEntity subject = entry.getSubject();
				    GradeEntity grade = entry.getGrade();
				    List<GradeEntity> gradess = gradesBySubject.get(subject);
				    if (gradess == null) {
				        gradess = new ArrayList<GradeEntity>();
				        gradesBySubject.put(subject, gradess);
				    }
				    gradess.add(grade);
				}
				Map<String, List<GradeEntity>> gradesAndSubject = new TreeMap<String, List<GradeEntity>>();
				for (Map.Entry<SubjectEntity, List<GradeEntity>> entry : gradesBySubject.entrySet()) {
					gradesAndSubject.put(entry.getKey().getSubjectName(), entry.getValue());
				}			
				finalna.put(user, gradesAndSubject);
			}
			
			Map<String, Map<String, List<GradeEntity>>> zaispis = new TreeMap<String, Map<String, List<GradeEntity>>>();
			for (Map.Entry<StudentEntity, Map<String, List<GradeEntity>>> entry : finalna.entrySet()) {
				zaispis.put(entry.getKey().getFirstName() + " " + entry.getKey().getLastName(), entry.getValue());
			}			

			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(zaispis, HttpStatus.OK);

		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/admin/semester/{semester}/bysubject/{id}")
	public ResponseEntity<?> getAdminStudentsGroupedBySubjectForSemesterAndSubject(@PathVariable String semester, @PathVariable Integer id, Principal principal) {
		logger.info("################ /project/grades/admin/semester/{semester}/bysubject/{id}/getAdminStudentsGroupedBySubjectForSemesterAndSubject started.");
		logger.info("Logged user: " + principal.getName());
		try {
			logger.info("Logged user identified.");

			Iterable<StudentEntity> students = studentRepository.findByStatusLike(1);
			
			Map<StudentEntity, Map<String, List<GradeEntity>>> finalna = new HashMap<StudentEntity, Map<String, List<GradeEntity>>>();

			for (StudentEntity user : students) {
				Map<SubjectEntity, List<GradeEntity>> gradesBySubject = new HashMap<SubjectEntity, List<GradeEntity>>();
				List<SubjectGradesDto> grades= gradeRepository.findGradesWithSubjectBySemesterAndStudentAndSubject(user.getId(), id, ESemester.valueOf(semester));
				for (SubjectGradesDto  entry : grades) {
					SubjectEntity subject = entry.getSubject();
				    GradeEntity grade = entry.getGrade();
				    List<GradeEntity> gradess = gradesBySubject.get(subject);
				    if (gradess == null) {
				        gradess = new ArrayList<GradeEntity>();
				        gradesBySubject.put(subject, gradess);
				    }
				    gradess.add(grade);
				}
				Map<String, List<GradeEntity>> gradesAndSubject = new TreeMap<String, List<GradeEntity>>();
				for (Map.Entry<SubjectEntity, List<GradeEntity>> entry : gradesBySubject.entrySet()) {
					gradesAndSubject.put(entry.getKey().getSubjectName(), entry.getValue());
				}			
				finalna.put(user, gradesAndSubject);
			}
			
			Map<String, Map<String, List<GradeEntity>>> zaispis = new TreeMap<String, Map<String, List<GradeEntity>>>();
			for (Map.Entry<StudentEntity, Map<String, List<GradeEntity>>> entry : finalna.entrySet()) {
				zaispis.put(entry.getKey().getFirstName() + " " + entry.getKey().getLastName(), entry.getValue());
			}			

			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(zaispis, HttpStatus.OK);

		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/admin/semester/{semester}/bystudent/{id}")
	public ResponseEntity<?> getAdminStudentsGroupedByStudentForSemesterAndSubject(@PathVariable String semester, @PathVariable Integer id, Principal principal) {
		logger.info("################ /project/grades/admin/semester/{semester}/bystudent/{id}/getAdminStudentsGroupedBySubjectForSemesterAndSubject started.");
		logger.info("Logged user: " + principal.getName());
		try {
			logger.info("Logged user identified.");

			StudentEntity student = studentRepository.findByIdAndStatusLike(id, 1);
			
			Map<StudentEntity, Map<String, List<GradeEntity>>> finalna = new HashMap<StudentEntity, Map<String, List<GradeEntity>>>();

				Map<SubjectEntity, List<GradeEntity>> gradesBySubject = new HashMap<SubjectEntity, List<GradeEntity>>();
				List<SubjectGradesDto> grades= gradeRepository.findGradesWithSubjectBySemesterAndStudent(student.getId(), ESemester.valueOf(semester));
				for (SubjectGradesDto  entry : grades) {
					SubjectEntity subject = entry.getSubject();
				    GradeEntity grade = entry.getGrade();
				    List<GradeEntity> gradess = gradesBySubject.get(subject);
				    if (gradess == null) {
				        gradess = new ArrayList<GradeEntity>();
				        gradesBySubject.put(subject, gradess);
				    }
				    gradess.add(grade);
				}
				Map<String, List<GradeEntity>> gradesAndSubject = new TreeMap<String, List<GradeEntity>>();
				for (Map.Entry<SubjectEntity, List<GradeEntity>> entry : gradesBySubject.entrySet()) {
					gradesAndSubject.put(entry.getKey().getSubjectName(), entry.getValue());
				}			
				finalna.put(student, gradesAndSubject);
			
			Map<String, Map<String, List<GradeEntity>>> zaispis = new TreeMap<String, Map<String, List<GradeEntity>>>();
			for (Map.Entry<StudentEntity, Map<String, List<GradeEntity>>> entry : finalna.entrySet()) {
				zaispis.put(entry.getKey().getFirstName() + " " + entry.getKey().getLastName(), entry.getValue());
			}			

			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(zaispis, HttpStatus.OK);

		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_TEACHER")
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNew(@Valid @RequestBody GradeDto newGrade, Principal principal, BindingResult result) {
		logger.info("################ /project/grades/addNew started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newGrade == null) {
			logger.info("---------------- New grade is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "New grade is null."), HttpStatus.BAD_REQUEST);
	      }
		if (newGrade.getStudent() == null && newGrade.getSubject() == null && newGrade.getGradeValue() == null && newGrade.getSemester() == null) {
			logger.info("---------------- Some data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Some data is null"), HttpStatus.BAD_REQUEST);
		}
		GradeEntity grade = new GradeEntity();
		try {
			StudentEntity student = studentRepository.findByIdAndStatusLike(Integer.parseInt(newGrade.getStudent()), 1);
			if (student==null || student.getStatus()!=1) {
				logger.info("---------------- Student not found.");
				return new ResponseEntity<RESTError>(new RESTError(4, "Student not found."), HttpStatus.NOT_FOUND);
			}
			logger.info("Student: " + student.getId().toString());
			SubjectEntity subject = subjectRepository.findByIdAndStatusLike(Integer.parseInt(newGrade.getSubject()), 1);
			if (subject==null || subject.getStatus()!=1) {
				logger.info("---------------- Subject not found.");
				return new ResponseEntity<RESTError>(new RESTError(4, "Subject not found."), HttpStatus.NOT_FOUND);
			}
			logger.info("Subject: " + subject.getId().toString());
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			TeacherEntity teacher = teacherRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			if (teacher==null) {
				logger.info("---------------- Teacher not found.");
				return new ResponseEntity<RESTError>(new RESTError(4, "Teacher not found."), HttpStatus.NOT_FOUND);
			}
			logger.info("Teacher: " + teacher.getId().toString());
			TeacherSubjectDepartmentEntity teacherDepartments = teacherSubjectDepartmentRepository.getByTeachingTeacherAndTeachingSubjectAndTeachingDepartmentAndTeachingClass(teacher, subject, student);
			if (teacherDepartments==null) {
				logger.info("---------------- Not student teacher or not subject in class or not teacher subject.");
				return new ResponseEntity<RESTError>(new RESTError(3, "Not student teacher or not subject in class or not teacher subject."), HttpStatus.FORBIDDEN);
			}
			logger.info("Teacher subject department: " + teacherDepartments.getId().toString());
			grade = gradeDao.addNewGrade(teacher, student, teacherDepartments, newGrade);		
			if (grade != null) {
				for (ParentEntity p : student.getParents()) 
					if (p.getStatus() == 1) {
						EmailObject email = new EmailObject(p.getEmail(), "Nova ocena ucenika " + student.getFirstName() + " " + student.getLastName(), "Ucenik " + student.getFirstName() + " " + student.getLastName() + " je dobio ocenu " + grade.getGradeValue().toString() + " iz predmeta " + subject.getSubjectName() + " kod profesora " + teacher.getFirstName() + " " + teacher.getLastName() + ".");
						emailService.sendSimpleMessage(email);
					}
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<GradeEntity>(grade, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Secured({"ROLE_TEACHER", "ROLE_ADMIN"})
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> modify(@PathVariable String id, @Valid @RequestBody GradeDto newGrade, Principal principal, BindingResult result) {
		logger.info("################ /project/grades/{id}/modify started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (id == null) {
			logger.info("---------------- Grade is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Grade is null."), HttpStatus.BAD_REQUEST);
	      }
		if (newGrade == null) {
			logger.info("---------------- New grade is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "New grade is null."), HttpStatus.BAD_REQUEST);
	      }
		GradeEntity ge = new GradeEntity();
		try {
			GradeEntity grade = gradeRepository.findByIdAndStatusLike(Integer.parseInt(id), 1);
			if (grade==null) {
				logger.info("---------------- Grade not found.");
				return new ResponseEntity<RESTError>(new RESTError(4, "Grade not found."), HttpStatus.NOT_FOUND);
			}
			if (newGrade.getStudent() != null && newGrade.getSubject() != null && newGrade.getSemester() != null && newGrade.getGradeValue() != null) {
				StudentEntity student = studentRepository.findByIdAndStatusLike(Integer.parseInt(newGrade.getStudent()), 1);
				if (student==null || student.getStatus()!=1) {
					logger.info("---------------- Student not found.");
					return new ResponseEntity<RESTError>(new RESTError(4, "Student not found."), HttpStatus.NOT_FOUND);
				}
				logger.info("Student identified.");
				SubjectEntity subject = subjectRepository.findByIdAndStatusLike(Integer.parseInt(newGrade.getSubject()), 1);
				if (subject==null || subject.getStatus()!=1) {
					logger.info("---------------- Subject not found.");
					return new ResponseEntity<RESTError>(new RESTError(4, "Subject not found."), HttpStatus.NOT_FOUND);
				}
				logger.info("Subject identified.");
				UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
				logger.info("Logged user identified.");
				UserAccountEntity loggedUserAccount = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1);
				logger.info("Logged user Access role identified.");
				TeacherEntity teacher = new TeacherEntity();
				Date updateDate = new Date();
				if (loggedUserAccount.getAccessRole().equals(EUserRole.ROLE_ADMIN)) {
					teacher = grade.getTeacher_subject_department().getTeachingTeacher();
					updateDate.setYear(updateDate.getYear()-1);
				}
				else {
					teacher = teacherRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
					updateDate.setMinutes(updateDate.getMinutes()-30);
				}
				if (teacher==null) {
					logger.info("---------------- Teacher not found.");
					return new ResponseEntity<RESTError>(new RESTError(4, "Teacher not found."), HttpStatus.NOT_FOUND);
				}
				if (teacher != grade.getTeacher_subject_department().getTeachingTeacher() || !updateDate.before(grade.getGradeMadeDate())) {
					logger.info("---------------- Teacher doesn't match or time for change expired.");
					return new ResponseEntity<RESTError>(new RESTError(3, "Teacher doesn't match or time for change expired."), HttpStatus.FORBIDDEN);
				}
				logger.info("Teacher identified.");
				if (student == grade.getStudent() && /*grade.getTeacher_subject_department().getStatus() == 1 &&*/ teacher == grade.getTeacher_subject_department().getTeachingTeacher() && subject == grade.getTeacher_subject_department().getTeachingSubject() && grade.getSemester().equals(ESemester.valueOf(newGrade.getSemester())) && updateDate.before(grade.getGradeMadeDate()) && !newGrade.getGradeValue().equals(grade.getGradeValue()))				
					ge = gradeDao.modifyGrade(loggedUser, grade, newGrade.getGradeValue());
				else {
					logger.info("---------------- Semester, teacher, subject adn/or student doesn't match or time for change expired.");
					return new ResponseEntity<RESTError>(new RESTError(3, "Semester, teacher, subject adn/or student doesn't match or time for change expired."), HttpStatus.FORBIDDEN);
				}
				if (ge != null) {
					for (ParentEntity p : student.getParents()) 
						if (p.getStatus() == 1) {
							EmailObject email = new EmailObject(p.getEmail(), "Promena ocene ucenika " + student.getFirstName() + " " + student.getLastName(), "Uceniku " + student.getFirstName() + " " + student.getLastName() + " je promenjena ocena na " + grade.getGradeValue().toString() + " iz predmeta " + subject.getSubjectName() + " kod profesora " + teacher.getFirstName() + " " + teacher.getLastName() + ".");
							emailService.sendSimpleMessage(email);
						}
				}
				logger.info("---------------- Finished OK.");
			}
			return new ResponseEntity<>(ge, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings("deprecation")
	@Secured({"ROLE_TEACHER", "ROLE_ADMIN"})
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/change/{id}/value/{value}")
	public ResponseEntity<?> modifyGradeById(@PathVariable Integer id, @PathVariable Integer value, Principal principal) {
		logger.info("################ /project/grades/{id}/modifyGradeById started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null) {
			logger.info("---------------- Grade is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Grade is null."), HttpStatus.BAD_REQUEST);
	      }
		if (value == null || value < 1 || value > 5) {
			logger.info("---------------- Grade value is null or out of range.");
	        return new ResponseEntity<RESTError>(new RESTError(5, " Grade value is null or out of range."), HttpStatus.BAD_REQUEST);
	      }
		GradeEntity ge = new GradeEntity();
		try {
			GradeEntity grade = gradeRepository.findByIdAndStatusLike(id, 1);
			if (grade==null) {
				logger.info("---------------- Grade not found.");
				return new ResponseEntity<RESTError>(new RESTError(4, "Grade not found."), HttpStatus.NOT_FOUND);
			}			
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			UserAccountEntity loggedUserAccount = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user Access role identified.");
			TeacherEntity teacher = new TeacherEntity();
			Date updateDate = new Date();
			if (loggedUserAccount.getAccessRole().equals(EUserRole.ROLE_ADMIN)) {
				teacher = grade.getTeacher_subject_department().getTeachingTeacher();
				updateDate.setYear(updateDate.getYear()-1);
			}
			else {
				teacher = teacherRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
				updateDate.setMinutes(updateDate.getMinutes()-30);
			}
			if (teacher==null) {
				logger.info("---------------- Teacher not found.");
				return new ResponseEntity<RESTError>(new RESTError(4, "Teacher not found."), HttpStatus.NOT_FOUND);
			}
			if (teacher != grade.getTeacher_subject_department().getTeachingTeacher() || !updateDate.before(grade.getGradeMadeDate())) {
				logger.info("---------------- Teacher doesn't match or time for change expired.");
				return new ResponseEntity<RESTError>(new RESTError(3, "Teacher doesn't match or time for change expired."), HttpStatus.FORBIDDEN);
				//return new ResponseEntity<RESTError>(new RESTError(3, "Teacher doesn't match or time for change expired."), HttpStatus.FORBIDDEN);
			}
			logger.info("Teacher identified.");
			if (/*grade.getTeacher_subject_department().getStatus() == 1 &&*/ teacher == grade.getTeacher_subject_department().getTeachingTeacher() && updateDate.before(grade.getGradeMadeDate()) && !value.equals(grade.getGradeValue()))
				ge = gradeDao.modifyGrade(loggedUser, grade, value);
			else {
				logger.info("---------------- Teacher doesn't match, same grade or time for change expired.");
				return new ResponseEntity<RESTError>(new RESTError(3, "Teacher doesn't match or time for change expired."), HttpStatus.FORBIDDEN);
				//return new ResponseEntity<RESTError>(new RESTError(3, "Teacher doesn't match or time for change expired."), HttpStatus.FORBIDDEN);
			}
			if (ge != null) {
				for (ParentEntity p : grade.getStudent().getParents()) 
					if (p.getStatus() == 1) {
						EmailObject email = new EmailObject(p.getEmail(), "Promena ocene ucenika " + grade.getStudent().getFirstName() + " " + grade.getStudent().getLastName(), "Uceniku " + grade.getStudent().getFirstName() + " " + grade.getStudent().getLastName() + " je promenjena ocena na " + grade.getGradeValue().toString() + " iz predmeta " + grade.getTeacher_subject_department().getTeachingSubject().getSubjectName() + " kod profesora " + teacher.getFirstName() + " " + teacher.getLastName() + ".");
						emailService.sendSimpleMessage(email);
					}
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(ge, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings("deprecation")
	@Secured({"ROLE_TEACHER", "ROLE_ADMIN"})
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/undelete/{id}")
	public ResponseEntity<?> unDelete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/grades/undelete/{id}/unDelete started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null) {
			logger.info("---------------- Grade is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Grade is null."), HttpStatus.BAD_REQUEST);
	      }
		try {
			GradeEntity grade = gradeRepository.findByIdAndStatusLike(id, 0);
			if (grade==null || grade.getStatus()!=0) {
				logger.info("---------------- Grade not found.");
				return new ResponseEntity<RESTError>(new RESTError(4, "Grade not found."), HttpStatus.NOT_FOUND);
			}
			logger.info("Grade identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			UserAccountEntity loggedUserAccount = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user Access role identified.");
			TeacherEntity teacher = new TeacherEntity();
			Date updateDate = new Date();
			if (loggedUserAccount.getAccessRole().equals(EUserRole.ROLE_ADMIN)) {
				teacher = grade.getTeacher_subject_department().getTeachingTeacher();
				updateDate.setYear(updateDate.getYear()-1);
			}
			else {
				teacher = teacherRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
				updateDate.setMinutes(updateDate.getMinutes()-30);
			}
			if (teacher==null) {
				logger.info("---------------- Teacher not found.");
				return new ResponseEntity<RESTError>(new RESTError(4, "Teacher not found."), HttpStatus.NOT_FOUND);
			}
			if (grade.getTeacher_subject_department().getStatus() != 1 || teacher != grade.getTeacher_subject_department().getTeachingTeacher() || !updateDate.before(grade.getGradeMadeDate())) {
				logger.info("---------------- Teacher doesn't match or time for undelete expired.");
				return new ResponseEntity<RESTError>(new RESTError(3, "Teacher doesn't match or time for undelete expired."), HttpStatus.FORBIDDEN);
			}
			logger.info("Teacher identified.");
			gradeDao.setStatusActive(loggedUser, grade);
			for (ParentEntity p : grade.getStudent().getParents()) 
				if (p.getStatus() == 1) {
					EmailObject email = new EmailObject(p.getEmail(), "Aktiviranje obrisane ocene ucenika " + grade.getStudent().getFirstName() + " " + grade.getStudent().getLastName(), "Uceniku " + grade.getStudent().getFirstName() + " " + grade.getStudent().getLastName() + " je ponovo aktivirana ocena " + grade.getGradeValue().toString() + " iz predmeta " + grade.getTeacher_subject_department().getTeachingSubject().getSubjectName() + " kod profesora " + teacher.getFirstName() + " " + teacher.getLastName() + ".");
					emailService.sendSimpleMessage(email);
				}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<GradeEntity>(grade, HttpStatus.OK);
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
		logger.info("################ /project/grades/archive/{id}/archive started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null) {
			logger.info("---------------- Grade is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Grade is null."), HttpStatus.BAD_REQUEST);
	      }
		try {
			GradeEntity grade = gradeRepository.getById(id);
			if (grade==null || grade.getStatus()==-1) {
				logger.info("---------------- Grade not found.");
				return new ResponseEntity<RESTError>(new RESTError(4, "Grade not found."), HttpStatus.NOT_FOUND);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			gradeDao.setStatusArchived(loggedUser, grade);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<GradeEntity>(grade, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings("deprecation")
	@Secured({"ROLE_TEACHER", "ROLE_ADMIN"})
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/grades/{id}/delete started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null) {
			logger.info("---------------- Grade is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Grade is null."), HttpStatus.BAD_REQUEST);
	      }
		try {
			GradeEntity grade = gradeRepository.findByIdAndStatusLike(id, 1);
			if (grade==null || grade.getStatus()!=1) {
				logger.info("---------------- Grade not found.");
				return new ResponseEntity<RESTError>(new RESTError(4, "Grade not found."), HttpStatus.NOT_FOUND);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			UserAccountEntity loggedUserAccount = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user Access role identified.");
			TeacherEntity teacher = new TeacherEntity();
			Date updateDate = new Date();
			if (loggedUserAccount.getAccessRole().equals(EUserRole.ROLE_ADMIN)) {
				teacher = grade.getTeacher_subject_department().getTeachingTeacher();
				updateDate.setYear(updateDate.getYear()-1);
			}
			else {
				teacher = teacherRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
				updateDate.setMinutes(updateDate.getMinutes()-30);
			}
			if (teacher==null) {
				logger.info("---------------- Teacher not found.");
				return new ResponseEntity<RESTError>(new RESTError(4, "Teacher not found."), HttpStatus.NOT_FOUND);
			}
			if (/*grade.getTeacher_subject_department().getStatus() != 1 ||*/ teacher != grade.getTeacher_subject_department().getTeachingTeacher() || !updateDate.before(grade.getGradeMadeDate())) {
				logger.info("---------------- Teacher doesn't match or time for delete expired.");
				//return new ResponseEntity<RESTError>(new RESTError(3, "Teacher doesn't match or time for delete expired."), HttpStatus.FORBIDDEN);
				return new ResponseEntity<RESTError>(new RESTError(3, "Teacher doesn't match or time for delete expired."), HttpStatus.FORBIDDEN);
			}
			logger.info("Teacher identified.");
			gradeDao.setStatusDeleted(loggedUser, grade);
			for (ParentEntity p : grade.getStudent().getParents()) 
				if (p.getStatus() == 1) {
					EmailObject email = new EmailObject(p.getEmail(), "Brisanje ocene ucenika " + grade.getStudent().getFirstName() + " " + grade.getStudent().getLastName(), "Uceniku " + grade.getStudent().getFirstName() + " " + grade.getStudent().getLastName() + " je obrisana ocena " + grade.getGradeValue().toString() + " iz predmeta " + grade.getTeacher_subject_department().getTeachingSubject().getSubjectName() + " kod profesora " + teacher.getFirstName() + " " + teacher.getLastName() + ".");
					emailService.sendSimpleMessage(email);
				}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<GradeEntity>(grade, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
