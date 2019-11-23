package com.iktpreobuka.projekat_za_kraj.services;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.projekat_za_kraj.entities.ClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.ClassSubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.PrimaryTeacherDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.DepartmentDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EClass;
import com.iktpreobuka.projekat_za_kraj.repositories.ClassRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.DepartmentClassRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.DepartmentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.PrimaryTeacherDepartmentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.StudentDepartmentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherSubjectDepartmentRepository;

@Service
public class DepartmentDaoImpl implements DepartmentDao {

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private ClassRepository classRepository;
	
	@Autowired
	private DepartmentClassRepository departmentClassRepository;
	
	@Autowired
	private StudentDepartmentRepository studentDepartmentRepository;

	@Autowired
	private PrimaryTeacherDepartmentRepository primaryTeacherDepartmentRepository;
	
	@Autowired
	private TeacherSubjectDepartmentRepository teacherSubjectDepartmentRepository;
	
	
	
	@Override
	public DepartmentEntity addNewDepartment(UserEntity loggedUser, DepartmentDto newDepartment) throws Exception {
		try {
			if (newDepartment.getDepartmentLabel() == null || newDepartment.getEnrollmentYear() == null || newDepartment.getDepartment_class() == null) {
			     throw new Exception("Some data is null.");
			}
		} catch (Exception e) {
			throw new Exception("DepartmentDto check failed.");
		}
		DepartmentEntity department = new DepartmentEntity();
		try {
			department.setDepartmentLabel(newDepartment.getDepartmentLabel());
			department.setEnrollmentYear(newDepartment.getEnrollmentYear());
			department.setStatusActive();
			department.setCreatedById(loggedUser.getId());
			departmentRepository.save(department);
			ClassEntity class_ = classRepository.findByClassLabelAndStatusLike(EClass.valueOf(newDepartment.getDepartment_class()), 1);
			DepartmentClassEntity departmentClass = new DepartmentClassEntity(class_, department, newDepartment.getSchoolYear(), loggedUser.getId());
			departmentClassRepository.save(departmentClass);
			department.getClasses().add(departmentClass);
			departmentRepository.save(department);
			return department;
		} catch (Exception e) {
			throw new Exception("addNewStudent save failed.");
		}
	}
	
	@Override
	public void modifyDepartment(UserEntity loggedUser, DepartmentEntity department, DepartmentDto updateDepartment) throws Exception {
		try {
			if (updateDepartment.getDepartmentLabel() == null && updateDepartment.getEnrollmentYear() == null && (updateDepartment.getSchoolYear() == null || updateDepartment.getDepartment_class() == null) ) {
			     throw new Exception("All data is null.");
			}
		} catch (Exception e) {
			throw new Exception("modifyClass DepartmentDto check failed.");
		}
		try {
			Integer i = 0;
			if (updateDepartment.getDepartmentLabel() != null && !updateDepartment.getDepartmentLabel().equals(" ") && !updateDepartment.getDepartmentLabel().equals("")) {
				department.setDepartmentLabel(updateDepartment.getDepartmentLabel());
				i++;
			}
			if (updateDepartment.getSchoolYear() != null && updateDepartment.getDepartment_class() != null && !updateDepartment.getDepartment_class().equals(" ") && !updateDepartment.getDepartment_class().equals("")) {
				ClassEntity class_ = classRepository.findByClassLabelAndStatusLike(EClass.valueOf(updateDepartment.getDepartment_class()), 1);
				boolean contains = false;
				if (class_!=null && class_.getStatus()==1 && department.getStatus() == 1) {
					for (DepartmentClassEntity ds : department.getClasses()) {
						if (ds.getStatus() == 1) {
							if (ds.getClas() == class_ || ds.getSchoolYear() == updateDepartment.getSchoolYear()) {
								contains = true;
							} else {
								ds.setStatusInactive();
								ds.setUpdatedById(loggedUser.getId());
								departmentClassRepository.save(ds);
							}
						}
					}
				} else
					contains = true;
				if (!contains) {
					DepartmentClassEntity departmentClass = new DepartmentClassEntity(class_, department, updateDepartment.getSchoolYear(), loggedUser.getId());
					departmentClassRepository.save(departmentClass);
					department.getClasses().add(departmentClass);
					i++;
				}
			}
			if (updateDepartment.getEnrollmentYear() != null && !updateDepartment.getEnrollmentYear().equals(" ") && !updateDepartment.getEnrollmentYear().equals("")) {
				department.setEnrollmentYear(updateDepartment.getEnrollmentYear());
				i++;
			}
			if (i>0) {
				department.setUpdatedById(loggedUser.getId());
				departmentRepository.save(department);
			}
		} catch (Exception e) {
			throw new Exception("modifyClass failed on saving.");
		}
	}
	
	@Override
	public void deleteDepartment(UserEntity loggedUser, DepartmentEntity department) throws Exception {
		try {		
			for (DepartmentClassEntity dc : department.getClasses()) {
				if (dc.getStatus() == 1) {
					dc.setStatusInactive();
					dc.setUpdatedById(loggedUser.getId());
					departmentClassRepository.save(dc);
				}
			}
			for (PrimaryTeacherDepartmentEntity ptd : department.getTeachers()) {
				if (ptd.getStatus() == 1) {
					ptd.setStatusInactive();
					ptd.setUpdatedById(loggedUser.getId());
					primaryTeacherDepartmentRepository.save(ptd);
				}
			}
			for (TeacherSubjectDepartmentEntity tsd : department.getTeachers_subjects()) {
				if (tsd.getStatus() == 1) {
					tsd.setStatusInactive();
					tsd.setUpdatedById(loggedUser.getId());
					teacherSubjectDepartmentRepository.save(tsd);
				}
			}
			for (StudentDepartmentEntity sd : department.getStudents()) {
				if (sd.getStatus() == 1) {
					sd.setStatusInactive();
					sd.setUpdatedById(loggedUser.getId());
					studentDepartmentRepository.save(sd);
				}
			}
			department.setStatusInactive();
			department.setUpdatedById(loggedUser.getId());
			departmentRepository.save(department);
		} catch (Exception e) {
			throw new Exception("deleteDepartment failed on saving.");
		}
	}
	
	@Override
	public void undeleteDepartment(UserEntity loggedUser, DepartmentEntity department) throws Exception {
		try {
			department.setStatusActive();
			department.setUpdatedById(loggedUser.getId());
			departmentRepository.save(department);
		} catch (Exception e) {
			throw new Exception("undeleteDepartment failed on saving.");
		}		
	}
	
	@Override
	public void archiveDepartment(UserEntity loggedUser, DepartmentEntity department) throws Exception {
		try {
			for (DepartmentClassEntity dc : department.getClasses()) {
				if (dc.getStatus() != -1) {
					dc.setStatusArchived();
					dc.setUpdatedById(loggedUser.getId());
					departmentClassRepository.save(dc);
				}
			}
			for (PrimaryTeacherDepartmentEntity ptd : department.getTeachers()) {
				if (ptd.getStatus() != -1) {
					ptd.setStatusArchived();
					ptd.setUpdatedById(loggedUser.getId());
					primaryTeacherDepartmentRepository.save(ptd);
				}
			}
			for (TeacherSubjectDepartmentEntity tsd : department.getTeachers_subjects()) {
				if (tsd.getStatus() != -1) {
					tsd.setStatusArchived();
					tsd.setUpdatedById(loggedUser.getId());
					teacherSubjectDepartmentRepository.save(tsd);
				}
			}
			for (StudentDepartmentEntity sd : department.getStudents()) {
				if (sd.getStatus() != -1) {
					sd.setStatusArchived();
					sd.setUpdatedById(loggedUser.getId());
					studentDepartmentRepository.save(sd);
				}
			}
			department.setStatusArchived();
			department.setUpdatedById(loggedUser.getId());
			departmentRepository.save(department);
		} catch (Exception e) {
			throw new Exception("ArchiveDeletedStudent failed on saving.");
		}		
	}
		
	@Override
	public StudentDepartmentEntity addStudentToDepartment(UserEntity loggedUser, StudentEntity student, DepartmentEntity department, String transfer_date)  throws Exception {
		try {
			StudentDepartmentEntity sde = null;
			if (department !=null && department.getStatus() ==1 && student !=null && student.getStatus() ==1 && transfer_date !=null && !transfer_date.equals("") && !transfer_date.equals(" ")) {
				boolean contains = false;
				for (StudentDepartmentEntity sd : department.getStudents()) {
					if (sd.getStatus() == 1 && sd.getStudent() == student) {
						contains = true;
					}
				}
				if (!contains) {
					for (StudentDepartmentEntity sd : student.getDepartments()) {
						if (sd.getStatus()==1) {
							sd.setStatusInactive();
							sd.setUpdatedById(loggedUser.getId());
							studentDepartmentRepository.save(sd);
						}
					}
					StudentDepartmentEntity studentDepartment = new StudentDepartmentEntity(student, department, Date.valueOf(transfer_date), loggedUser.getId());
					studentDepartmentRepository.save(studentDepartment);
					department.getStudents().add(studentDepartment);
					department.setUpdatedById(loggedUser.getId());
					departmentRepository.save(department);
					sde=studentDepartment;
				}
			}
			return sde;
		} catch (Exception e) {
			throw new Exception("addStudentToDepartment failed on saving.");
		}		
	}
	
	@Override
	public StudentDepartmentEntity removeStudentFromDepartment(UserEntity loggedUser, StudentEntity student, DepartmentEntity department) throws Exception {
		try {
			StudentDepartmentEntity sde = null;
			if (department !=null && department.getStatus() ==1 && student !=null && student.getStatus() ==1) {
				for (StudentDepartmentEntity ds : department.getStudents()) {
					if (ds.getStatus() == 1 && ds.getStudent() == student) {
						ds.setStatusInactive();
						ds.setUpdatedById(loggedUser.getId());
						studentDepartmentRepository.save(ds);
						sde = ds;
					}
				}
			}
			return sde;
		} catch (Exception e) {
			throw new Exception("removeStudentFromDepartment failed on saving.");
		}		
	}
	
	@Override
	public DepartmentClassEntity addClassToDepartment(UserEntity loggedUser, ClassEntity class_, DepartmentEntity department, String schoolYear)  throws Exception {
		try {
			DepartmentClassEntity dce = null;
			if (department !=null && department.getStatus() ==1 && class_ !=null && class_.getStatus() ==1 && schoolYear !=null && !schoolYear.equals("") && !schoolYear.equals(" ")) {
				boolean contains = false;
				for (DepartmentClassEntity ds : department.getClasses()) {
					if (ds.getStatus() == 1) {
						if (ds.getClas() == class_) {
							contains = true;
						} else {
							ds.setStatusInactive();
							ds.setUpdatedById(loggedUser.getId());
							departmentClassRepository.save(ds);
						}
					}
				}
				if (!contains) {
					for (TeacherSubjectDepartmentEntity tsd : department.getTeachers_subjects()) {
						if (tsd.getStatus()==1) {
							tsd.setStatusInactive();
							tsd.setUpdatedById(loggedUser.getId());
							teacherSubjectDepartmentRepository.save(tsd);
						}
					}
					DepartmentClassEntity departmentClass = new DepartmentClassEntity(class_, department, schoolYear, loggedUser.getId());
					departmentClassRepository.save(departmentClass);
					department.getClasses().add(departmentClass);
					department.setUpdatedById(loggedUser.getId());
					departmentRepository.save(department);
					dce = departmentClass;
				}
			}
			return dce;
		} catch (Exception e) {
			throw new Exception("addClassToDepartment failed on saving.");
		}
	}
	
	@Override
	public DepartmentClassEntity removeClassFromDepartment(UserEntity loggedUser, ClassEntity class_, DepartmentEntity department) throws Exception {
		try {
			DepartmentClassEntity dce = null;
			if (department !=null && department.getStatus() ==1 && class_ !=null && class_.getStatus() ==1) {
				for (DepartmentClassEntity ds : department.getClasses()) {
					if (ds.getStatus() == 1 && ds.getClas() == class_) {
							ds.setStatusInactive();
							ds.setUpdatedById(loggedUser.getId());
							departmentClassRepository.save(ds);
							dce = ds;
					}
				}
			}
			if (dce != null) {
				for (TeacherSubjectDepartmentEntity tsd : dce.getDepartment().getTeachers_subjects()) {
					if (tsd.getTeachingClass() == dce.getClas() && tsd.getTeachingDepartment() == dce.getDepartment()) {
						tsd.setStatusInactive();
						tsd.setUpdatedById(loggedUser.getId());
						teacherSubjectDepartmentRepository.save(tsd);
					}
				}
			}
			return dce;
		} catch (Exception e) {
			throw new Exception("removeClassFromDepartment failed on saving.");
		}
	}
	
	@Override
	public PrimaryTeacherDepartmentEntity addPrimaryTeacherToDepartment(UserEntity loggedUser, TeacherEntity teacher, DepartmentEntity department, String assignmentDate) throws Exception {
		try {
			PrimaryTeacherDepartmentEntity pt = null;
			if (teacher !=null && teacher.getStatus() ==1 && department !=null && department.getStatus() ==1 && assignmentDate !=null && !assignmentDate.equals("") && !assignmentDate.equals(" ")) {
				boolean contains = false;
				for (PrimaryTeacherDepartmentEntity ptd : department.getTeachers()) {
					if (ptd.getStatus() == 1) {
						if (ptd.getTeacher() == teacher) {
							contains = true;
						} else {
							ptd.setStatusInactive();
							ptd.setUpdatedById(loggedUser.getId());
							primaryTeacherDepartmentRepository.save(ptd);
						}
					}
				}
				if (!contains) {
					for (PrimaryTeacherDepartmentEntity te : teacher.getDepartments()) {
						if (te.getStatus()==1) {
							te.setStatusInactive();
							te.setUpdatedById(loggedUser.getId());
							primaryTeacherDepartmentRepository.save(te);
						}
					}
					PrimaryTeacherDepartmentEntity primaryTeacher = new PrimaryTeacherDepartmentEntity(teacher, department, Date.valueOf(assignmentDate), loggedUser.getId());
					primaryTeacherDepartmentRepository.save(primaryTeacher);
					department.getTeachers().add(primaryTeacher);
					department.setUpdatedById(loggedUser.getId());
					departmentRepository.save(department);
					pt = primaryTeacher;
				}
			}
			return pt;
		} catch (Exception e) {
			throw new Exception("addPrimaryTeacherToDepartment failed on saving.");
		}
	}
	
	@Override
	public PrimaryTeacherDepartmentEntity removePrimaryTeacherFromDepartment(UserEntity loggedUser, TeacherEntity teacher, DepartmentEntity department) throws Exception {
		try {
			PrimaryTeacherDepartmentEntity pt = null;
			if (teacher !=null && teacher.getStatus() == 1 && department !=null && department.getStatus() == 1) {
				for (PrimaryTeacherDepartmentEntity ptd : department.getTeachers()) {
					if (ptd.getStatus() == 1 && ptd.getTeacher() == teacher) {
							ptd.setStatusInactive();
							ptd.setUpdatedById(loggedUser.getId());
							primaryTeacherDepartmentRepository.save(ptd);
							pt = ptd;
					}
				}
			}
			return pt;
		} catch (Exception e) {
			throw new Exception("removePrimaryTeacherFromDepartment failed on saving.");
		}
	}


	@Override
	public TeacherSubjectDepartmentEntity addTeacherAndSubjectToDepartment(UserEntity loggedUser, TeacherEntity teacher, DepartmentEntity department, ClassEntity clas, SubjectEntity subject, String school_year) throws Exception {
		try {
			TeacherSubjectDepartmentEntity teaching = null;
			if (teacher !=null && teacher.getStatus() ==1 && department !=null && department.getStatus() ==1 && clas !=null && clas.getStatus() ==1 && subject !=null && subject.getStatus() ==1  && school_year !=null && !school_year.equals("") && !school_year.equals(" ")) {
				boolean contains = false;
				for (TeacherSubjectDepartmentEntity tsd : department.getTeachers_subjects()) {
					if (tsd.getTeachingTeacher() == teacher && tsd.getTeachingSubject() == subject && tsd.getTeachingClass() == clas && tsd.getStatus() == 1) {
						contains = true;
					}
				}
				if (!contains) {
					contains = true;
					for (TeacherSubjectEntity ts : teacher.getSubjects()) {
						if (ts.getSubject() == subject && ts.getStatus() == 1) {
							contains = false;
						}
					}
				}
				if (!contains) {
					contains = true;
					for (ClassSubjectEntity cs : clas.getSubjects()) {
						if (cs.getSubject() == subject && cs.getStatus() == 1) {
							contains = false;
						}
					}
				}
				if (!contains) {
					teaching = new TeacherSubjectDepartmentEntity(department, clas, subject, teacher, school_year, loggedUser.getId());
					teacherSubjectDepartmentRepository.save(teaching);
					department.getTeachers_subjects().add(teaching);
					department.setUpdatedById(loggedUser.getId());
					departmentRepository.save(department);
				}
			}
			return teaching;
		} catch (Exception e) {
			throw new Exception("addTeacherAndSubjectToDepartment failed on saving.");
		}
	}
	
	@Override
	public TeacherSubjectDepartmentEntity removeTeacherAndSubjectFromDepartment(UserEntity loggedUser, TeacherEntity teacher, DepartmentEntity department, ClassEntity clas, SubjectEntity subject) throws Exception {
		try {
			TeacherSubjectDepartmentEntity tsd1 = null;
			if (teacher !=null && teacher.getStatus() ==1 && department !=null && department.getStatus() ==1 && clas !=null && clas.getStatus() ==1 && subject !=null && subject.getStatus() ==1 ) {
				for (TeacherSubjectDepartmentEntity tsd : department.getTeachers_subjects()) {
					if (tsd.getTeachingTeacher() == teacher && tsd.getTeachingSubject() == subject && tsd.getTeachingClass() == clas && tsd.getStatus() == 1) {
						tsd.setStatusInactive();
						tsd.setUpdatedById(loggedUser.getId());
						teacherSubjectDepartmentRepository.save(tsd);
						tsd1=tsd;
					}
				}
			}
			return tsd1;
		} catch (Exception e) {
			throw new Exception("removeTeacherAndSubjectFromDepartment failed on saving.");
		}
	}
	
}
