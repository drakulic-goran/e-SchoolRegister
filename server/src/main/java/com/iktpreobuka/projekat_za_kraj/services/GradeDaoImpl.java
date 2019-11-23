package com.iktpreobuka.projekat_za_kraj.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.projekat_za_kraj.entities.GradeEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.GradeDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.ESemester;
import com.iktpreobuka.projekat_za_kraj.repositories.GradeRepository;

@Service
public class GradeDaoImpl implements GradeDao {
   
   @Autowired
   private GradeRepository gradeRepository;
   
   @Override
   public GradeEntity addNewGrade(TeacherEntity teacher, StudentEntity student, TeacherSubjectDepartmentEntity teacherDepartments, GradeDto newGrade) throws Exception {
	   if (teacher ==null || student==null || teacherDepartments==null || newGrade == null || newGrade.getSemester() == null || newGrade.getGradeValue() == null) {
		   throw new Exception("Some data is null.");
	   }
	   GradeEntity grade = new GradeEntity();
	   try {
			grade.setTeacher_subject_department(teacherDepartments);
			grade.setStudent(student);
			grade.setStatusActive();
			grade.setSemester(ESemester.valueOf(newGrade.getSemester()));
			grade.setGradeValue(newGrade.getGradeValue());
			grade.setGradeMadeDate(new Date());
			grade.setCreatedById(teacher.getId());
			gradeRepository.save(grade);
			return grade;
	   } catch (Exception e) {
			throw new Exception("addNewGrade save failed.");
	   }
   }
   
   @Override
   public GradeEntity modifyGrade(UserEntity loggedUser, GradeEntity grade, Integer gradeValue) throws Exception {
		if (grade ==null || gradeValue == null) {
			throw new Exception("Some data is null.");
		}
	   try {
		   grade.setGradeValue(gradeValue);
		   grade.setGradeMadeDate(new Date());
		   grade.setUpdatedById(loggedUser.getId());
		   gradeRepository.save(grade);
		   return grade;
	   } catch (Exception e) {
		   throw new Exception("modifyGrade save failed.");
	   }
   }
   
   @Override
   public void setStatusActive(UserEntity loggedUser, GradeEntity grade)  throws Exception {
		if (grade ==null) {
			throw new Exception("Some data is null.");
		}
	   try {
			grade.setStatusActive();
			grade.setUpdatedById(loggedUser.getId());
			gradeRepository.save(grade);
	   } catch (Exception e) {
		   throw new Exception("modifyGrade save failed.");
	   }
   }
   
   @Override
   public void setStatusArchived(UserEntity loggedUser, GradeEntity grade)  throws Exception {
		if (grade ==null) {
			throw new Exception("Some data is null.");
		}
	   try {
			grade.setStatusArchived();
			grade.setUpdatedById(loggedUser.getId());
			gradeRepository.save(grade);
	   } catch (Exception e) {
		   throw new Exception("modifyGrade save failed.");
	   }
   }
   
   @Override
   public void setStatusDeleted(UserEntity loggedUser, GradeEntity grade)  throws Exception {
		if (grade ==null) {
			throw new Exception("Some data is null.");
		}
	   try {
			grade.setStatusInactive();
			grade.setUpdatedById(loggedUser.getId());
			gradeRepository.save(grade);
	   } catch (Exception e) {
		   throw new Exception("modifyGrade save failed.");
	   }
   }

}