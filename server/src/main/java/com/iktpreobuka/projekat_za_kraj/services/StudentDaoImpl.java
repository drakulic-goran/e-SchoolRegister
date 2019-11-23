package com.iktpreobuka.projekat_za_kraj.services;

import java.sql.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.ParentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.StudentDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EGender;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;
import com.iktpreobuka.projekat_za_kraj.repositories.StudentDepartmentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.StudentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserRepository;

@Service
public class StudentDaoImpl implements StudentDao {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private StudentDepartmentRepository studentDepartmentRepository;

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	/*@Override
	public StudentEntity findById(Integer id) throws Exception {
		try {
			return studentRepository.getById(id);
		} catch (Exception e) {
			throw new Exception("Get student by Id failed.");
		}
	}
	
	@Override
	public StudentEntity findByIdAndStatusLike(Integer id, Integer status) throws Exception {
		try {
			return studentRepository.findByIdAndStatusLike(id, status);
		} catch (Exception e) {
			throw new Exception("Get student by Id and Status failed.");
		}
	}

	@Override
	public Iterable<StudentEntity> findByStatusLike(Integer status) throws Exception {
		try {
			return studentRepository.findByStatusLike(status);
		} catch (Exception e) {
			throw new Exception("Get student by Status failed.");
		}		
	}*/
	
	@Override
	public UserEntity addNewStudent(UserEntity loggedUser, StudentDto newStudent) throws Exception {
		if (newStudent.getFirstName() == null || newStudent.getLastName() == null || newStudent.getEnrollmentDate() == null || newStudent.getGender() == null || newStudent.getjMBG() == null || newStudent.getSchoolIdentificationNumber() == null ) {
			throw new Exception("Some data is null.");
		}
		UserEntity temporaryUser = new TeacherEntity();
		try {
			temporaryUser = userRepository.findByJMBG(newStudent.getjMBG());
		} catch (Exception e1) {
			throw new Exception("Exist user check failed.");
		}
		if (temporaryUser != null && (!temporaryUser.getFirstName().equals(newStudent.getFirstName()) || !temporaryUser.getLastName().equals(newStudent.getLastName()) || !temporaryUser.getGender().toString().equals(newStudent.getGender()) || !temporaryUser.getjMBG().equals(newStudent.getjMBG()))) {
			throw new Exception("User exists, but import data not same as exist user data.");
		}
		StudentEntity user = new StudentEntity();
		try {
			if (temporaryUser == null) {
				try {
					user.setFirstName(newStudent.getFirstName());
					logger.info("setFirstName");
					user.setLastName(newStudent.getLastName());
					logger.info("setLastName");
					user.setjMBG(newStudent.getjMBG());
					logger.info("setjMBG");
					user.setGender(EGender.valueOf(newStudent.getGender()));
					logger.info("setGender");
					user.setEnrollmentDate(Date.valueOf(newStudent.getEnrollmentDate()));
					logger.info("setEnrollmentDate");
					user.setSchoolIdentificationNumber(newStudent.getSchoolIdentificationNumber());
					logger.info("setSchoolIdentificationNumber");
					user.setRole(EUserRole.ROLE_STUDENT);
					user.setStatusActive();
					user.setCreatedById(loggedUser.getId());
					studentRepository.save(user);
					temporaryUser = user;
				} catch (Exception e) {
					throw new Exception("addNewStudent save failed.");
				}
			} else {
				studentRepository.addAdminFromExistUser(newStudent.getEnrollmentDate(), newStudent.getSchoolIdentificationNumber(), temporaryUser.getId(), loggedUser.getId());
			}
			return temporaryUser;
		} catch (Exception e) {
			throw new Exception("addNewStudent save failed.");
		}
	}

	@Override
	public void modifyStudent(UserEntity loggedUser, StudentEntity student, StudentDto updateStudent) throws Exception {
		try {
			if (updateStudent.getFirstName() == null && updateStudent.getLastName() == null && updateStudent.getEnrollmentDate() == null && updateStudent.getGender() == null && updateStudent.getjMBG() != null && updateStudent.getSchoolIdentificationNumber() != null ) {
			     throw new Exception("All data is null.");
			}
		} catch (Exception e1) {
			throw new Exception("StudentDto check failed.");
		}
		try {
			Integer i = 0;
			if (updateStudent.getFirstName() != null && !updateStudent.getFirstName().equals(" ") && !updateStudent.getFirstName().equals("") && !updateStudent.getFirstName().equals(student.getFirstName())) {
				student.setFirstName(updateStudent.getFirstName());
				i++;
			}
			if (updateStudent.getLastName() != null && !updateStudent.getLastName().equals(student.getLastName()) && !updateStudent.getLastName().equals(" ") && !updateStudent.getLastName().equals("")) {
				student.setLastName(updateStudent.getLastName());
				i++;
			}
			if (updateStudent.getjMBG() != null && !updateStudent.getjMBG().equals(student.getjMBG()) && !updateStudent.getjMBG().equals(" ") && !updateStudent.getjMBG().equals("")) {
				student.setjMBG(updateStudent.getjMBG());
				i++;
			}
			if (updateStudent.getGender() != null && EGender.valueOf(updateStudent.getGender()) != student.getGender() && (EGender.valueOf(updateStudent.getGender()) == EGender.GENDER_FEMALE || EGender.valueOf(updateStudent.getGender()) == EGender.GENDER_MALE)) {
				student.setGender(EGender.valueOf(updateStudent.getGender()));
				i++;
			}
			if (updateStudent.getSchoolIdentificationNumber() != null && !updateStudent.getSchoolIdentificationNumber().equals(student.getSchoolIdentificationNumber()) && !updateStudent.getSchoolIdentificationNumber().equals(" ") && !updateStudent.getSchoolIdentificationNumber().equals("")) {
				student.setSchoolIdentificationNumber(updateStudent.getSchoolIdentificationNumber());
				i++;
			}
			if (updateStudent.getEnrollmentDate() != null && !Date.valueOf(updateStudent.getEnrollmentDate()).equals(student.getEnrollmentDate()) && !updateStudent.getEnrollmentDate().equals(" ") && !updateStudent.getEnrollmentDate().equals("")) {
				student.setEnrollmentDate(Date.valueOf(updateStudent.getEnrollmentDate()));
				i++;
			}
			if (i>0) {
				student.setUpdatedById(loggedUser.getId());
				studentRepository.save(student);
			}
		} catch (Exception e) {
			throw new Exception("ModifyStudent failed on saving.");
		}
	}

	@Override
	public void deleteStudent(UserEntity loggedUser, StudentEntity student) throws Exception {
		try {
			for (StudentDepartmentEntity sd : student.getDepartments()) {
				if (sd.getStatus() == 1) {
					sd.setStatusInactive();
					sd.setUpdatedById(loggedUser.getId());
					studentDepartmentRepository.save(sd);
				}
			}
			student.setStatusInactive();
			student.setUpdatedById(loggedUser.getId());
			studentRepository.save(student);
		} catch (Exception e) {
			throw new Exception("DeleteStudent failed on saving.");
		}
	}
	
	@Override
	public void undeleteStudent(UserEntity loggedUser, StudentEntity student) throws Exception {
		try {
			student.setStatusActive();
			student.setUpdatedById(loggedUser.getId());
			studentRepository.save(student);
		} catch (Exception e) {
			throw new Exception("UndeleteStudent failed on saving.");
		}		
	}
	
	@Override
	public void archiveStudent(UserEntity loggedUser, StudentEntity student) throws Exception {
		try {
			for (StudentDepartmentEntity sd : student.getDepartments()) {
				if (sd.getStatus() != 1) {
					sd.setStatusArchived();
					sd.setUpdatedById(loggedUser.getId());
					studentDepartmentRepository.save(sd);
				}
			}
			student.setStatusArchived();
			student.setUpdatedById(loggedUser.getId());
			studentRepository.save(student);
		} catch (Exception e) {
			throw new Exception("ArchiveDeletedStudent failed on saving.");
		}		
	}
	
	@Override
	public void addParentToStudent(UserEntity loggedUser, StudentEntity student, ParentEntity parent) throws Exception {
		try {
			student.getParents().add(parent);
			student.setUpdatedById(loggedUser.getId());
			studentRepository.save(student);
		} catch (Exception e) {
			throw new Exception("addParentToStudent failed on saving.");
		}
	}
	
	@Override
	public void removeParentFromStudent(UserEntity loggedUser, StudentEntity student, ParentEntity parent) throws Exception {
		try {
			student.getParents().remove(parent);
			student.setUpdatedById(loggedUser.getId());
			studentRepository.save(student);
		} catch (Exception e) {
			throw new Exception("removeParentFromStudent failed on saving.");
		}
	}
	
	@Override
	public StudentDepartmentEntity addDepartmentToStudent(UserEntity loggedUser, StudentEntity student, DepartmentEntity department, String transfer_date) throws Exception {
		try {
			StudentDepartmentEntity sde = null;
			if (department !=null && department.getStatus() ==1 && student !=null && student.getStatus() ==1 && transfer_date !=null && !transfer_date.equals("") && !transfer_date.equals(" ")) {
				boolean contains = false;
				for (StudentDepartmentEntity sd : student.getDepartments()) {
					if (sd.getStatus() == 1)
						if (sd.getDepartment() == department) {
							contains = true;
						} else {
							sd.setStatusInactive();
							sd.setUpdatedById(loggedUser.getId());
							studentDepartmentRepository.save(sd);
						}
				}
				if (!contains) {
					StudentDepartmentEntity studentDepartment = new StudentDepartmentEntity(student, department, Date.valueOf(transfer_date), loggedUser.getId());
					studentDepartmentRepository.save(studentDepartment);
					student.getDepartments().add(studentDepartment);
					student.setUpdatedById(loggedUser.getId());
					studentRepository.save(student);
					sde=studentDepartment;
				}
			}	
			return sde;
		} catch (Exception e) {
			throw new Exception("addDepartmentToStudent failed on saving.");
		}		
	}
	
	@Override
	public StudentDepartmentEntity removeDepartmentFromStudent(UserEntity loggedUser, StudentEntity student, DepartmentEntity department) throws Exception {
		try {
			StudentDepartmentEntity sde = null;
			if (department !=null && department.getStatus() ==1 && student !=null && student.getStatus() ==1) {
				for (StudentDepartmentEntity ds : student.getDepartments()) {
					if (ds.getStatus() == 1 && ds.getDepartment() == department) {
						ds.setStatusInactive();
						ds.setUpdatedById(loggedUser.getId());
						studentDepartmentRepository.save(ds);
						sde=ds;
					}
				}
			}
			return sde;
		} catch (Exception e) {
			throw new Exception("removeDepartmentFromStudent failed on saving.");
		}		
	}

	
}
