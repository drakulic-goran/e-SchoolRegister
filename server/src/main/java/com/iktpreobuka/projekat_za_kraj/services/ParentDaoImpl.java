package com.iktpreobuka.projekat_za_kraj.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.projekat_za_kraj.entities.ParentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.ParentDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EGender;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;
import com.iktpreobuka.projekat_za_kraj.repositories.ParentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.StudentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserRepository;

@Service
public class ParentDaoImpl implements ParentDao {

	@Autowired
	private ParentRepository parentRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private StudentRepository studentRepository;

	/*@Override
	public ParentEntity findById(Integer id) throws Exception{
		try {
			return parentRepository.getById(id);
		} catch (Exception e) {
			throw new Exception("Get parent by Id failed.");
		}
	}
	
	@Override
	public ParentEntity findByIdAndStatusLike(Integer id, Integer status) throws Exception {
		try {
			return parentRepository.findByIdAndStatusLike(id, status);
		} catch (Exception e) {
			throw new Exception("Get parent by Id and Status failed.");
		}
	}

	@Override
	public Iterable<ParentEntity> findByStatusLike(Integer status) throws Exception {
		try {
			return parentRepository.findByStatusLike(status);
		} catch (Exception e) {
			throw new Exception("Get parent by Status failed.");
		}		
	}*/

	@Override
	public UserEntity addNewParent(UserEntity loggedUser, ParentDto newParent) throws Exception {
		if (newParent.getFirstName() == null || newParent.getLastName() == null || newParent.getGender() == null || newParent.getjMBG() == null || newParent.getEmail() == null) {
			throw new Exception("Some data is null.");
		}
		UserEntity temporaryUser = new TeacherEntity();
		try {
			temporaryUser = userRepository.findByJMBG(newParent.getjMBG());
		} catch (Exception e1) {
			throw new Exception("addNewParent Exist user check failed.");
		}
		if (temporaryUser != null && (!temporaryUser.getFirstName().equals(newParent.getFirstName()) || !temporaryUser.getLastName().equals(newParent.getLastName()) || !temporaryUser.getGender().toString().equals(newParent.getGender()) || !temporaryUser.getjMBG().equals(newParent.getjMBG()))) {
			throw new Exception("User exists, but import data not same as exist user data.");
		}
		ParentEntity user = new ParentEntity();
		try {
			if (temporaryUser == null) {
				try {
					user.setFirstName(newParent.getFirstName());
					user.setLastName(newParent.getLastName());
					user.setjMBG(newParent.getjMBG());
					user.setGender(EGender.valueOf(newParent.getGender()));
					user.setEmail(newParent.getEmail());
					user.setRole(EUserRole.ROLE_PARENT);
					user.setStatusActive();
					user.setCreatedById(loggedUser.getId());
					parentRepository.save(user);
					temporaryUser = user;
				} catch (Exception e) {
					throw new Exception("addNewParent save failed.");
				}
			} else {
				parentRepository.addAdminFromExistUser(newParent.getEmail(), temporaryUser.getId(), loggedUser.getId());
			}
			return temporaryUser;
		} catch (Exception e) {
			throw new Exception("addNewParent save failed.");
		}
	}


	@Override
	public void modifyParent(UserEntity loggedUser, ParentEntity parent, ParentDto updateParent) throws Exception {
		if (updateParent.getFirstName() == null && updateParent.getLastName() == null && updateParent.getGender() == null && updateParent.getjMBG() == null && updateParent.getEmail() == null) {
			throw new Exception("All data is null.");
		}
		try {
			Integer i = 0;
			if (updateParent.getFirstName() != null && !updateParent.getFirstName().equals(" ") && !updateParent.getFirstName().equals("") && !updateParent.getFirstName().equals(parent.getFirstName())) {
				parent.setFirstName(updateParent.getFirstName());
				i++;
			}
			if (updateParent.getLastName() != null && !updateParent.getLastName().equals(parent.getLastName()) && !updateParent.getLastName().equals(" ") && !updateParent.getLastName().equals("")) {
				parent.setLastName(updateParent.getLastName());
				i++;
			}
			if (updateParent.getjMBG() != null && !updateParent.getjMBG().equals(parent.getjMBG()) && !updateParent.getjMBG().equals(" ") && !updateParent.getjMBG().equals("") ) {
				parent.setjMBG(updateParent.getjMBG());
				i++;
			}
			if (updateParent.getGender() != null && EGender.valueOf(updateParent.getGender()) != parent.getGender() && (EGender.valueOf(updateParent.getGender()) == EGender.GENDER_FEMALE || EGender.valueOf(updateParent.getGender()) == EGender.GENDER_MALE)) {
				parent.setGender(EGender.valueOf(updateParent.getGender()));
				i++;
			}
			if (updateParent.getEmail() != null && !updateParent.getEmail().equals(parent.getEmail()) && !updateParent.getEmail().equals(" ") && !updateParent.getEmail().equals("")) {
				parent.setEmail(updateParent.getEmail());
				i++;
			}
			if (i>0) {
				parent.setUpdatedById(loggedUser.getId());
				parentRepository.save(parent);
			}
		} catch (Exception e) {
			throw new Exception("ModifyParent faild on saving.");
		}
	}

	@Override
	public void deleteParent(UserEntity loggedUser, ParentEntity parent) throws Exception {
		try {
			parent.setStatusInactive();
			parent.setUpdatedById(loggedUser.getId());
			parentRepository.save(parent);
		} catch (Exception e) {
			throw new Exception("DeleteParent failed on saving.");
		}
	}
	
	@Override
	public void undeleteParent(UserEntity loggedUser, ParentEntity parent) throws Exception {
		try {
			parent.setStatusActive();
			parent.setUpdatedById(loggedUser.getId());
			parentRepository.save(parent);
		} catch (Exception e) {
			throw new Exception("UndeleteParent failed on saving.");
		}		
	}
	
	@Override
	public void archiveParent(UserEntity loggedUser, ParentEntity parent) throws Exception {
		try {
			parent.setStatusArchived();
			parent.setUpdatedById(loggedUser.getId());
			parentRepository.save(parent);
		} catch (Exception e) {
			throw new Exception("ArchiveParent failed on saving.");
		}		
	}

	@Override
	public void addStudentToParent(UserEntity loggedUser, ParentEntity parent, StudentEntity student) throws Exception {
		try {
			student.getParents().add(parent);
			student.setUpdatedById(loggedUser.getId());
			studentRepository.save(student);
		} catch (Exception e) {
			throw new Exception("AddStudentToParent failed on saving.");
		}
	}
	
	@Override
	public void removeStudentFromParent(UserEntity loggedUser, ParentEntity parent, StudentEntity student) throws Exception {
		try {
			student.getParents().remove(parent);
			student.setUpdatedById(loggedUser.getId());
			studentRepository.save(student);
		} catch (Exception e) {
			throw new Exception("removeStudentFromParent failed on saving.");
		}
	}
	
}