package com.iktpreobuka.projekat_za_kraj.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.projekat_za_kraj.entities.ClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.ClassSubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectDto;
import com.iktpreobuka.projekat_za_kraj.repositories.ClassRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.ClassSubjectRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.DepartmentClassRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.DepartmentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.SubjectRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherSubjectDepartmentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherSubjectRepository;

@Service
public class SubjectDaoImpl implements SubjectDao {

	
	@Autowired
	private ClassSubjectRepository classSubjectRepository;
	
	@Autowired
	private TeacherSubjectRepository teacherSubjectRepository;
	
	@Autowired
	private DepartmentClassRepository departmentClassRepository;
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private ClassRepository classRepository;
	
	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	private TeacherSubjectDepartmentRepository teacherSubjectDepartmentRepository;
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	
	@Override
	public SubjectEntity addNewSubject(UserEntity loggedUser, SubjectDto newSubject) throws Exception {
		if (newSubject.getSubjectName() == null || newSubject.getWeekClassesNumber() == null) {
			throw new Exception("Some data is null.");
		}
		SubjectEntity subject = new SubjectEntity();
		try {	
			subject.setSubjectName(newSubject.getSubjectName());
			subject.setWeekClassesNumber(newSubject.getWeekClassesNumber());
			subject.setStatusActive();
			subject.setCreatedById(loggedUser.getId());
			subjectRepository.save(subject);
			return subject;
		} catch (Exception e) {
			throw new Exception("addNewSubject failed on saving.");
		}
	}
	
	@Override
	public void modifySubject(UserEntity loggedUser, SubjectEntity subject, SubjectDto updateSubject) throws Exception {
		if (updateSubject.getSubjectName() == null && updateSubject.getWeekClassesNumber() == null) {
			logger.info("All data is null.");
			throw new Exception("All data is null.");
		}
		try {
			Integer i = 0;
			logger.info("modify");
			if (updateSubject.getSubjectName() != null && !updateSubject.getSubjectName().equals(" ") && !updateSubject.getSubjectName().equals("")) {
				subject.setSubjectName(updateSubject.getSubjectName());
				i++;
				logger.info("setSubjectName");
			}
			if (updateSubject.getWeekClassesNumber() != null ) {
				logger.info("1");
				subject.setWeekClassesNumber(updateSubject.getWeekClassesNumber());
				i++;
				logger.info("setWeekClassesNumber");
			}
			if (i>0) {
				logger.info("2");
				subject.setUpdatedById(loggedUser.getId());
				logger.info("3");
				subjectRepository.save(subject);
				logger.info("4");
			}
		} catch (Exception e) {
			throw new Exception("modifySubject failed on saving.");
		}
	}
	
	@Override
	public void deleteSubject(UserEntity loggedUser, SubjectEntity subject) throws Exception {
		try {		
			for (ClassSubjectEntity cs : subject.getClasses()) {
				if (cs.getStatus() == 1) {
					cs.setStatusInactive();
					cs.setUpdatedById(loggedUser.getId());
					classSubjectRepository.save(cs);
				}
			}
			for (TeacherSubjectEntity ts : subject.getTeachers()) {
				if (ts.getStatus() == 1) {
					ts.setStatusInactive();
					ts.setUpdatedById(loggedUser.getId());
					teacherSubjectRepository.save(ts);
				}
			}
			for (TeacherSubjectDepartmentEntity tsd : subject.getTeachers_departments()) {
				if (tsd.getStatus() == 1) {
					tsd.setStatusInactive();
					tsd.setUpdatedById(loggedUser.getId());
					teacherSubjectDepartmentRepository.save(tsd);
				}
			}
			subject.setStatusInactive();
			subject.setUpdatedById(loggedUser.getId());
			subjectRepository.save(subject);
		} catch (Exception e) {
			throw new Exception("deleteSubject failed on saving.");
		}
	}
	
	@Override
	public void undeleteSubject(UserEntity loggedUser, SubjectEntity subject) throws Exception {
		try {
			subject.setStatusActive();
			subject.setUpdatedById(loggedUser.getId());
			subjectRepository.save(subject);
		} catch (Exception e) {
			throw new Exception("undeleteSubject failed on saving.");
		}		
	}
	
	@Override
	public void archiveSubject(UserEntity loggedUser, SubjectEntity subject) throws Exception {
		try {
			for (ClassSubjectEntity cs : subject.getClasses()) {
				if (cs.getStatus() != -1) {
					cs.setStatusArchived();
					cs.setUpdatedById(loggedUser.getId());
					classSubjectRepository.save(cs);
				}
			}
			for (TeacherSubjectEntity ts : subject.getTeachers()) {
				if (ts.getStatus() != -1) {
					ts.setStatusArchived();
					ts.setUpdatedById(loggedUser.getId());
					teacherSubjectRepository.save(ts);
				}
			}
			for (TeacherSubjectDepartmentEntity tsd : subject.getTeachers_departments()) {
				if (tsd.getStatus() != -1) {
					tsd.setStatusArchived();
					tsd.setUpdatedById(loggedUser.getId());
					teacherSubjectDepartmentRepository.save(tsd);
				}
			}
			subject.setStatusArchived();;
			subject.setUpdatedById(loggedUser.getId());
			subjectRepository.save(subject);
		} catch (Exception e) {
			throw new Exception("archiveSubject failed on saving.");
		}		
	}
	
	@Override
	public List<ClassSubjectEntity> addClassToSubject(UserEntity loggedUser, List<String> classes, SubjectEntity subject, String learningProgram) throws Exception {
		try {
			List<ClassSubjectEntity> cse = new ArrayList<ClassSubjectEntity>();
			if (subject !=null && subject.getStatus() ==1 && !classes.isEmpty() && learningProgram !=null && !learningProgram.equals("") && !learningProgram.equals(" ")) {
				for (String t : classes) {
					if (t !=null && !t.equals("") && !t.equals(" ")) {
						ClassEntity class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(t), 1);
						if (class_==null)
							throw new Exception("Class not exist.");
						boolean contains = false;
						if (learningProgram != null && class_.getStatus() == 1 && subject.getStatus() == 1) {
							for (ClassSubjectEntity cs : subject.getClasses()) {
								if (cs.getClass_() == class_ && cs.getStatus() == 1) {
										contains = true;
								}
							}
						} else
							contains = true;
						if (!contains) {
							ClassSubjectEntity classSubject = new ClassSubjectEntity(class_, subject, learningProgram, loggedUser.getId());
							classSubjectRepository.save(classSubject);
							subject.getClasses().add(classSubject);
							subject.setUpdatedById(loggedUser.getId());
							subjectRepository.save(subject);
							cse.add(classSubject);
						}
					}
				}
			}
			return cse;
		} catch (Exception e) {
			throw new Exception("addClassToSubject failed on saving.");
		}
	}
		
	@Override
	public List<ClassSubjectEntity> removeClassFromSubject(UserEntity loggedUser, List<String> classes, SubjectEntity subject) throws Exception {
		try {
			List<ClassSubjectEntity> classSubject = new ArrayList<ClassSubjectEntity>();
			if (subject !=null && subject.getStatus() ==1 && !classes.isEmpty()) {
				for (String t : classes) {
					if (t !=null && !t.equals("") && !t.equals(" ")) {
						ClassEntity class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(t), 1);
						if (class_==null)
							throw new Exception("Class not exist.");
						if (class_.getStatus() == 1 && subject.getStatus() == 1) {
							for (ClassSubjectEntity cs : subject.getClasses()) {
								if (cs.getClass_() == class_ && cs.getStatus() == 1) {
									cs.setStatusInactive();
									cs.setUpdatedById(loggedUser.getId());
									classSubjectRepository.save(cs);
									classSubject.add(cs);
									for (TeacherSubjectDepartmentEntity tsd : cs.getSubject().getTeachers_departments()) {
										if (tsd.getTeachingSubject() == cs.getSubject() && tsd.getTeachingClass() == cs.getClas()) {
											tsd.setStatusInactive();
											tsd.setUpdatedById(loggedUser.getId());
											teacherSubjectDepartmentRepository.save(tsd);
										}
									}
								}
							}
						}
					}
				}
			}
			return classSubject;
		} catch (Exception e) {
			throw new Exception("removeSubjectFromClass failed on saving.");
		}
	}

	@Override
	public List<TeacherSubjectEntity> addTeachersToSubject(UserEntity loggedUser, SubjectEntity subject, List<String> teachers) throws Exception {
		try {
			List<TeacherSubjectEntity> tse = new ArrayList<TeacherSubjectEntity>();
			if (subject !=null && subject.getStatus() ==1 && !teachers.isEmpty()) {
				for (String t : teachers) {
					if (t !=null && !t.equals("") && !t.equals(" ")) {
						TeacherEntity teacher = teacherRepository.findByIdAndStatusLike(Integer.parseInt(t), 1);
						if (teacher==null)
							throw new Exception("Teacher not exist.");
						boolean contains = false;
						for (TeacherSubjectEntity ts : subject.getTeachers()) {
							if (ts.getTeacher() == teacher && ts.getStatus() == 1) {
								contains = true;
							}
						}
						if (!contains) {
							TeacherSubjectEntity teaching = new TeacherSubjectEntity(teacher, subject, new Date(), loggedUser.getId());
							teacherSubjectRepository.save(teaching);
							subject.getTeachers().add(teaching);
							subject.setUpdatedById(loggedUser.getId());
							subjectRepository.save(subject);
							tse.add(teaching);
						}
					}
				}
			}
			return tse;
		} catch (Exception e) {
			throw new Exception("addTeachersToSubject failed on saving.");
		}
	}
	
	@Override
	public List<TeacherSubjectEntity> removeTeachersFromSubject(UserEntity loggedUser, SubjectEntity subject, List<String> teachers) throws Exception {
		try {
			List<TeacherSubjectEntity> tse = new ArrayList<TeacherSubjectEntity>();
			if (subject !=null && subject.getStatus() ==1 && !teachers.isEmpty()) {
				for (String t : teachers) {
					if (t !=null && !t.equals("") && !t.equals(" ")) {
						TeacherEntity teacher = teacherRepository.findByIdAndStatusLike(Integer.parseInt(t), 1);
						if (teacher==null)
							throw new Exception("Teacher not exist.");
						for (TeacherSubjectEntity ts : subject.getTeachers()) {
							if (ts.getTeacher() == teacher && ts.getStatus() == 1) {
								ts.setStatusInactive();
								ts.setUpdatedById(loggedUser.getId());
								teacherSubjectRepository.save(ts);
								tse.add(ts);
								for (TeacherSubjectDepartmentEntity tsd : ts.getSubject().getTeachers_departments()) {
									if (tsd.getTeachingSubject() == ts.getSubject() && tsd.getTeachingTeacher() == ts.getTeacher()) {
										tsd.setStatusInactive();
										tsd.setUpdatedById(loggedUser.getId());
										teacherSubjectDepartmentRepository.save(tsd);
									}
								}
							}
						}
					}
				}
			}
			return tse;
		} catch (Exception e) {
			throw new Exception("removeTeachersFromSubject failed on saving.");
		}
	}

	@Override
	public TeacherSubjectDepartmentEntity addTeacherAndDepartmentToSubject(UserEntity loggedUser, SubjectEntity subject, String teachingDepartment, String teachingTeacher, String schoolYear) throws Exception {
		try {
			TeacherSubjectDepartmentEntity teaching = null;
			if (subject !=null && subject.getStatus() ==1 && teachingTeacher !=null && !teachingTeacher.equals("") && !teachingTeacher.equals(" ") && teachingDepartment !=null && !teachingDepartment.equals("") && !teachingDepartment.equals(" ") && schoolYear !=null && !schoolYear.equals("") && !schoolYear.equals(" ")) {
					TeacherEntity teacher = teacherRepository.findByIdAndStatusLike(Integer.parseInt(teachingTeacher), 1);
					if (teacher==null)
						throw new Exception("Teacher not exist.");
					DepartmentEntity department = departmentRepository.findByIdAndStatusLike(Integer.parseInt(teachingDepartment), 1);
					if (department==null)
						throw new Exception("Department not exist.");
					ClassEntity clas = departmentClassRepository.getClasByDepartmentAndStatusLike(department, 1);
					if (clas==null)
						throw new Exception("Class for selected department not exist.");
					boolean contains = false;
					for (TeacherSubjectDepartmentEntity tsd : department.getTeachers_subjects()) {
						if (tsd.getTeachingTeacher() == teacher && tsd.getTeachingSubject() == subject && tsd.getTeachingClass() == clas && tsd.getStatus() == 1) {
							contains = true;
						}
					}
					if (!contains) {
						contains = true;
						for (TeacherSubjectEntity ts : subject.getTeachers()) {
							if (ts.getTeacher() == teacher && ts.getStatus() == 1) {
								contains = false;
							}
						}
					}
					if (!contains) {
						contains = true;
						for (ClassSubjectEntity ds : subject.getClasses()) {
							if (ds.getStatus() == 1 && ds.getClas() == clas) {
										contains = false;
							}
						}
					}
					if (!contains) {
						teaching = new TeacherSubjectDepartmentEntity(department, clas, subject, teacher, schoolYear, loggedUser.getId());
						teacherSubjectDepartmentRepository.save(teaching);
						subject.getTeachers_departments().add(teaching);
						subject.setUpdatedById(loggedUser.getId());
						subjectRepository.save(subject);
					}
			}
			return teaching;
		} catch (Exception e) {
			throw new Exception("addTeacherAndDepartmentToSubject failed on saving.");
		}
	}
	
	@Override
	public TeacherSubjectDepartmentEntity removeTeacherAndDepartmentFromSubject(UserEntity loggedUser, SubjectEntity subject,	String teachingDepartment, String teachingTeacher) throws Exception {
		try {
			TeacherSubjectDepartmentEntity teaching = null;
			if (subject !=null && subject.getStatus() ==1 && teachingTeacher !=null && !teachingTeacher.equals("") && !teachingTeacher.equals(" ") && teachingDepartment !=null && !teachingDepartment.equals("") && !teachingDepartment.equals(" ")) {
					TeacherEntity teacher = teacherRepository.findByIdAndStatusLike(Integer.parseInt(teachingTeacher), 1);
					if (teacher==null)
						throw new Exception("Teacher not exist.");
					DepartmentEntity department = departmentRepository.findByIdAndStatusLike(Integer.parseInt(teachingDepartment), 1);
					if (department==null)
						throw new Exception("Department not exist.");
					ClassEntity clas = departmentClassRepository.getClasByDepartmentAndStatusLike(department, 1);
					if (clas==null)
						throw new Exception("Class for selected department not exist.");
					for (TeacherSubjectDepartmentEntity tsd : subject.getTeachers_departments()) {
						if (tsd.getTeachingTeacher() == teacher && tsd.getTeachingDepartment() == department && tsd.getTeachingClass() == clas && tsd.getStatus() == 1) {
							tsd.setStatusInactive();
							tsd.setUpdatedById(loggedUser.getId());
							teacherSubjectDepartmentRepository.save(tsd);
							teaching = tsd;
						}
					}
			}
			return teaching;
		} catch (Exception e) {
			throw new Exception("removeTeacherAndDepartmentFromSubject failed on saving.");
		}
	}
	
}
