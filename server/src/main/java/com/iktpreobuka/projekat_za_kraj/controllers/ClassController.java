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
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.ClassDepartmentDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.ClassDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectClassDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EClass;
import com.iktpreobuka.projekat_za_kraj.repositories.ClassRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.DepartmentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.SubjectRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserAccountRepository;
import com.iktpreobuka.projekat_za_kraj.security.Views;
import com.iktpreobuka.projekat_za_kraj.services.ClassDao;

@Controller
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value= "/project/class")
public class ClassController {
	
	@Autowired
	private ClassDao classDao;

	@Autowired
	private ClassRepository classRepository;
	
	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private UserAccountRepository userAccountRepository;

	/*@Autowired
	private AdminRepository adminRepository;*/

	@Autowired
	private SubjectRepository subjectRepository;

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	private String createErrorMessage(BindingResult result) { 
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
		}

	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll(Principal principal) {
		logger.info("################ /project/class/getAll started.");
		logger.info("Logged user: " + principal.getName());
		try {
			Iterable<ClassEntity> classes= classRepository.findByStatusLike(1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<ClassEntity>>(classes, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable String id, Principal principal) {
		logger.info("################ /project/class/{id}/getById started.");
		logger.info("Logged user: " + principal.getName());
		try {
			ClassEntity class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), 1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ClassEntity>(class_, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/deleted")
	public ResponseEntity<?> getAllDeleted(Principal principal) {
		logger.info("################ /project/class/getAll started.");
		logger.info("Logged user: " + principal.getName());
		try {
			Iterable<ClassEntity> classes= classRepository.findByStatusLike(0);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<ClassEntity>>(classes, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/deleted/{id}")
	public ResponseEntity<?> getByIdDeleted(@PathVariable String id, Principal principal) {
		logger.info("################ /project/class/{id}/getById started.");
		logger.info("Logged user: " + principal.getName());
		try {
			ClassEntity class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), 0);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ClassEntity>(class_, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived")
	public ResponseEntity<?> getAllArchived(Principal principal) {
		logger.info("################ /project/class/getAll started.");
		logger.info("Logged user: " + principal.getName());
		try {
			Iterable<ClassEntity> classes= classRepository.findByStatusLike(-1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<ClassEntity>>(classes, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/{id}")
	public ResponseEntity<?> getByIdArchived(@PathVariable String id, Principal principal) {
		logger.info("################ /project/class/{id}/getById started.");
		logger.info("Logged user: " + principal.getName());
		try {
			ClassEntity class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), -1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ClassEntity>(class_, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/departments")
	public ResponseEntity<?> getDepartments(Principal principal) {
		logger.info("################ /project/class/{id}/getById started.");
		logger.info("Logged user: " + principal.getName());
		try {
			Iterable<ClassEntity> classes= classRepository.findByStatusLike(1);
			List<ClassDepartmentDto> cd = new ArrayList<ClassDepartmentDto>();
			for(final ClassEntity c : classes) {
				for(final DepartmentClassEntity d : c.getDepartments()) {
					if (d.getStatus() == 1) {
						cd.add(new ClassDepartmentDto(d.getClas(), d.getDepartment()));
					}
				}
			}
			
			Map<ClassEntity, List<DepartmentEntity>> departmentsByClass = new HashMap<ClassEntity, List<DepartmentEntity>>();
			for (ClassDepartmentDto entry : cd) {
				DepartmentEntity department = entry.getDepartment();
			    ClassEntity clas = entry.getClas();
			    List<DepartmentEntity> subjectsss = departmentsByClass.get(clas);
			    if (subjectsss == null) {
			        subjectsss = new ArrayList<DepartmentEntity>();
			        departmentsByClass.put(clas, subjectsss);
			    }
			    subjectsss.add(department);
			}
			
			Map<String, List<DepartmentEntity>> departmentsAndClass = new TreeMap<String, List<DepartmentEntity>>();
			for (Map.Entry<ClassEntity, List<DepartmentEntity>> entry : departmentsByClass.entrySet()) {
				departmentsAndClass.put(entry.getKey().getClassLabel().toString(), entry.getValue());
			}			

			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(departmentsAndClass, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/departments")
	public ResponseEntity<?> getDepartmentsForClass(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/class/{id}/getById started.");
		logger.info("Logged user: " + principal.getName());
		try {
			ClassEntity clasa= classRepository.findByIdAndStatusLike(id, 1);
			List<ClassDepartmentDto> cd = new ArrayList<ClassDepartmentDto>();
			for(final DepartmentClassEntity d : clasa.getDepartments()) {
				if (d.getStatus() == 1) {
					cd.add(new ClassDepartmentDto(d.getClas(), d.getDepartment()));
				}
			}
			
			Map<ClassEntity, List<DepartmentEntity>> departmentsByClass = new HashMap<ClassEntity, List<DepartmentEntity>>();
			for (ClassDepartmentDto entry : cd) {
				DepartmentEntity department = entry.getDepartment();
			    ClassEntity clas = entry.getClas();
			    List<DepartmentEntity> subjectsss = departmentsByClass.get(clas);
			    if (subjectsss == null) {
			        subjectsss = new ArrayList<DepartmentEntity>();
			        departmentsByClass.put(clas, subjectsss);
			    }
			    subjectsss.add(department);
			}
			
			Map<String, List<DepartmentEntity>> departmentsAndClass = new TreeMap<String, List<DepartmentEntity>>();
			for (Map.Entry<ClassEntity, List<DepartmentEntity>> entry : departmentsByClass.entrySet()) {
				departmentsAndClass.put(entry.getKey().getClassLabel().toString(), entry.getValue());
			}			

			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(departmentsAndClass, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/subjects")
	public ResponseEntity<?> getSubjects(Principal principal) {
		logger.info("################ /project/class/subjects/getSubjects started.");
		logger.info("Logged user: " + principal.getName());
		try {
			Iterable<ClassEntity> classes= classRepository.findByStatusLike(1);
			List<SubjectClassDto> cd = new ArrayList<SubjectClassDto>();
			for(final ClassEntity c : classes) {
				for(final ClassSubjectEntity d : c.getSubjects()) {
					if (d.getStatus() == 1) {
						cd.add(new SubjectClassDto(d.getClas(), d.getSubject()));
					}
				}
			}
			
			Map<ClassEntity, List<SubjectEntity>> departmentsByClass = new HashMap<ClassEntity, List<SubjectEntity>>();
			for (SubjectClassDto entry : cd) {
				SubjectEntity department = entry.getSubject();
			    ClassEntity clas = entry.getClas();
			    List<SubjectEntity> subjectsss = departmentsByClass.get(clas);
			    if (subjectsss == null) {
			        subjectsss = new ArrayList<SubjectEntity>();
			        departmentsByClass.put(clas, subjectsss);
			    }
			    subjectsss.add(department);
			}
			
			Map<String, List<SubjectEntity>> departmentsAndClass = new TreeMap<String, List<SubjectEntity>>();
			for (Map.Entry<ClassEntity, List<SubjectEntity>> entry : departmentsByClass.entrySet()) {
				departmentsAndClass.put(entry.getKey().getClassLabel().toString(), entry.getValue());
			}			

			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(departmentsAndClass, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/subjects")
	public ResponseEntity<?> getClassWithSubjects(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/class/{id}/subjects/getClassWithSubjects started.");
		logger.info("Logged user: " + principal.getName());
		try {
			ClassEntity classes= classRepository.findByIdAndStatusLike(id, 1);
			List<SubjectClassDto> cd = new ArrayList<SubjectClassDto>();
				for(final ClassSubjectEntity d : classes.getSubjects()) {
					if (d.getStatus() == 1) {
						cd.add(new SubjectClassDto(d.getClas(), d.getSubject()));
					}
				}
			
			Map<ClassEntity, List<SubjectEntity>> departmentsByClass = new HashMap<ClassEntity, List<SubjectEntity>>();
			for (SubjectClassDto entry : cd) {
				SubjectEntity department = entry.getSubject();
			    ClassEntity clas = entry.getClas();
			    List<SubjectEntity> subjectsss = departmentsByClass.get(clas);
			    if (subjectsss == null) {
			        subjectsss = new ArrayList<SubjectEntity>();
			        departmentsByClass.put(clas, subjectsss);
			    }
			    subjectsss.add(department);
			}
			
			Map<String, List<SubjectEntity>> departmentsAndClass = new TreeMap<String, List<SubjectEntity>>();
			for (Map.Entry<ClassEntity, List<SubjectEntity>> entry : departmentsByClass.entrySet()) {
				departmentsAndClass.put(entry.getKey().getClassLabel().toString(), entry.getValue());
			}			

			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(departmentsAndClass, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNew(@Valid @RequestBody ClassDto newClass, Principal principal, BindingResult result) {
		logger.info("################ /project/class/addNew started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newClass == null || newClass.getClassLabel() == null || newClass.getDepartment() != null || newClass.getDepartments() != null || newClass.getSubject() != null || newClass.getSubjects() != null) {
			logger.info("---------------- Some data is null or wrong data.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Some data is null or wrong data."), HttpStatus.BAD_REQUEST);
	      }
		ClassEntity class_ = new ClassEntity();
		try {
			if (newClass.getClassLabel() != null && classRepository.findByClassLabelAndStatusLike(EClass.valueOf(newClass.getClassLabel()), 1) != null) {
				logger.info("---------------- Class label already exist.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "Class label already exist."), HttpStatus.NOT_ACCEPTABLE);
			}
			if (newClass.getClassLabel() != null && !newClass.getClassLabel().equals(" ") && !newClass.getClassLabel().equals("")) {
				UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
				logger.info("Logged user identified.");
				class_ = classDao.addNewClass(loggedUser, newClass);
				logger.info("Class created.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(class_, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> modify(@PathVariable String id, @Valid @RequestBody ClassDto updateClass, Principal principal, BindingResult result) {
		logger.info("################ /project/class/{id}/modify started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (id == null || updateClass == null || updateClass.getClassLabel() == null || updateClass.getDepartment() != null || updateClass.getDepartments() != null || updateClass.getSubject() != null || updateClass.getSubjects() != null) {
			logger.info("---------------- Some data is null or wrong data.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Some data is null or wrong data."), HttpStatus.BAD_REQUEST);
	      }
		ClassEntity class_ = new ClassEntity();
		try {
			class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), 1);
			if (class_==null) { 
				logger.info("---------------- Class not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Class not found."), HttpStatus.NOT_FOUND);
			}
			logger.info("Class identified.");
			if (updateClass.getClassLabel() != null && EClass.valueOf(updateClass.getClassLabel()).equals(class_.getClassLabel()) ) {
				updateClass.setClassLabel(null);
			}
			if (updateClass.getClassLabel() != null && classRepository.findByClassLabelAndStatusLike(EClass.valueOf(updateClass.getClassLabel()), 1) != null) {
				logger.info("---------------- Class label already exist.");
		        return new ResponseEntity<RESTError>(new RESTError(2, "Class label already exist."), HttpStatus.NOT_ACCEPTABLE);
			}
			if (updateClass.getClassLabel() != null && !updateClass.getClassLabel().equals(" ") && !updateClass.getClassLabel().equals("")) {
				UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
				logger.info("Logged user identified.");
				classDao.modifyClass(loggedUser, class_, updateClass);
				logger.info("Class modified.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(class_, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/add-subject/{s_id}/learningprogram/{name}")
	public ResponseEntity<?> addSubjectToClass(@PathVariable String id, @PathVariable String s_id, @PathVariable String name, Principal principal) {
		logger.info("################ /project/class/{id}/subject/{s_id}/learningprogram/{name}/addSubjectToClass started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null || s_id == null || name == null) {
			logger.info("---------------- Some data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Some data is null."), HttpStatus.BAD_REQUEST);
	      }
		ClassSubjectEntity cse = new ClassSubjectEntity();
		try {
			ClassEntity class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), 1);
			if (class_==null) {
				logger.info("---------------- Class not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Class not found."), HttpStatus.NOT_FOUND);
			} 
			SubjectEntity subject = subjectRepository.findById(Integer.parseInt(s_id)).orElse(null);
			if (subject==null) {
				logger.info("---------------- Subject not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Subject not found."), HttpStatus.NOT_FOUND);
			}
			if (name != null && !name.equals(" ") && !name.equals("")) {
				UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
				logger.info("Logged user identified.");
				cse = classDao.addSubjectToClass(loggedUser, class_, subject, name);
				logger.info("Subject added to class.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ClassSubjectEntity>(cse, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/remove-subject/{s_id}")
	public ResponseEntity<?> removeSubjectFromClass(@PathVariable String id, @PathVariable String s_id, Principal principal) {
		logger.info("################ /project/class/{id}/remove-subject/{s_id}/removeSubjectFromClass started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null || s_id == null) {
			logger.info("---------------- Some data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Some data is null."), HttpStatus.BAD_REQUEST);
	      }
		ClassSubjectEntity cse = new ClassSubjectEntity();
		try {
			ClassEntity class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), 1);
			if (class_==null) {
				logger.info("---------------- Class not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Class not found."), HttpStatus.NOT_FOUND);
			} 
			SubjectEntity subject = subjectRepository.findById(Integer.parseInt(s_id)).orElse(null);
			if (subject==null) {
				logger.info("---------------- Subject not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Subject not found."), HttpStatus.NOT_FOUND);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			cse = classDao.removeSubjectFromClass(loggedUser, class_, subject);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ClassSubjectEntity>(cse, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/add-department/{d_id}/schoolyear/{schoolyear}")
	public ResponseEntity<?> addDepartmentToClass(@PathVariable String id, @PathVariable String d_id, @PathVariable String schoolyear, Principal principal) {
		logger.info("################ /project/class/{id}/add-department/{d_id}/schoolyear/{schoolyear}/addDepartmentToClass started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null || d_id == null || schoolyear == null) {
			logger.info("---------------- Some data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Some data is null."), HttpStatus.BAD_REQUEST);
	      }
		DepartmentClassEntity dce = new DepartmentClassEntity();
		try {
			ClassEntity class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), 1);
			if (class_==null) {
				logger.info("---------------- Class not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Class not found."), HttpStatus.NOT_FOUND);
			} 
			DepartmentEntity department = departmentRepository.findByIdAndStatusLike(Integer.parseInt(d_id), 1);
			if (department==null) {
				logger.info("---------------- Department not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Department not found."), HttpStatus.NOT_FOUND);
			}
			if (schoolyear != null && !schoolyear.equals(" ") && !schoolyear.equals("")) {
				UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
				logger.info("Logged user identified.");
				dce = classDao.addDepartmentToClass(loggedUser, class_, department, schoolyear);
				logger.info("Department added to class.");
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/remove-department/{d_id}")
	public ResponseEntity<?> removeDepartmentToClass(@PathVariable String id, @PathVariable String d_id, Principal principal) {
		logger.info("################ /project/class/{id}/remove-department/{d_id}/removeDepartmentToClass started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null || d_id == null) {
			logger.info("---------------- Some data is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Some data is null."), HttpStatus.BAD_REQUEST);
	      }
		DepartmentClassEntity dc = new DepartmentClassEntity();
		try {
			ClassEntity class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), 1);
			if (class_==null) {
				logger.info("---------------- Class not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Class not found."), HttpStatus.NOT_FOUND);
			} 
			DepartmentEntity department = departmentRepository.findByIdAndStatusLike(Integer.parseInt(d_id), 1);
			if (department==null) {
				logger.info("---------------- Departmnet not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Departmnet not found."), HttpStatus.NOT_FOUND);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			dc = classDao.removeDepartmentFromClass(loggedUser, class_, department);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<DepartmentClassEntity>(dc, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/arhive/{id}")
	public ResponseEntity<?> arhive(@PathVariable String id, Principal principal) {
		logger.info("################ /project/class/arhive/{id}/arhive started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null) {
			logger.info("---------------- Class is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Class is null."), HttpStatus.BAD_REQUEST);
	      }
		ClassEntity class_ = new ClassEntity();
		try {
			class_ = classRepository.getById(Integer.parseInt(id));
			if (class_==null || class_.getStatus()==-1) {
				logger.info("---------------- Class not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Class not found."), HttpStatus.NOT_FOUND);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			classDao.archiveClass(loggedUser, class_);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ClassEntity>(class_, HttpStatus.OK);
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
		logger.info("################ /project/class/undelete/{id}/unDelete started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null) {
			logger.info("---------------- Class is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Class is null."), HttpStatus.BAD_REQUEST);
	      }
		ClassEntity class_ = new ClassEntity();
		try {
			class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), 0);
			if (class_==null || class_.getStatus()!=0) {
				logger.info("---------------- Class not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Class not found."), HttpStatus.NOT_FOUND);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			classDao.undeleteClass(loggedUser, class_);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ClassEntity>(class_, HttpStatus.OK);
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
		logger.info("################ /project/class/delete started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null) {
			logger.info("---------------- Class is null.");
	        return new ResponseEntity<RESTError>(new RESTError(5, "Class is null."), HttpStatus.BAD_REQUEST);
	      }
		ClassEntity class_ = new ClassEntity();
		try {
			class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), 1);
			if (class_==null || class_.getStatus()!=1) {
				logger.info("---------------- Class not found.");
		        return new ResponseEntity<RESTError>(new RESTError(4, "Class not found."), HttpStatus.NOT_FOUND);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			classDao.deleteClass(loggedUser, class_);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ClassEntity>(class_, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



}
