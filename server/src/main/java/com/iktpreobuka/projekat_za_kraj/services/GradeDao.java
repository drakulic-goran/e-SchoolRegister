package com.iktpreobuka.projekat_za_kraj.services;

import com.iktpreobuka.projekat_za_kraj.entities.GradeEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.GradeDto;

public interface GradeDao {
	
	//public List<GradeEntity> findGradeValueByStudentAndSubject(StudentEntity studentId, Integer subjectId);
	/* public GradeEntity addDateOfActivationToAccount(Integer id, String doa) throws Exception;
	public GradeEntity addAccKindToAccount(Integer id, String accKind) throws Exception; */

	public GradeEntity addNewGrade(TeacherEntity teacher, StudentEntity student, TeacherSubjectDepartmentEntity teacherDepartments, GradeDto newGrade) throws Exception;

	public GradeEntity modifyGrade(UserEntity loggedUser, GradeEntity grade, Integer gradeValue) throws Exception;

	public void setStatusActive(UserEntity loggedUser, GradeEntity grade)  throws Exception;
	
	public void setStatusArchived(UserEntity loggedUser, GradeEntity grade)  throws Exception;
	
	public void setStatusDeleted(UserEntity loggedUser, GradeEntity grade)  throws Exception;

}
