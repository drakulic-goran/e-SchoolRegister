package com.iktpreobuka.projekat_za_kraj.entities.dto;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.enumerations.EClass;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class DepartmentClassDto {

	@JsonView(Views.Student.class)
	//@Pattern(regexp = "^(IV|V?I{1,4})$", message="Student class is not valid, must be I, II, III, IV, V, VI, VII or VIII.")
	@NotNull (message = "Class label must be provided.")
	private EClass classLabel;
	@JsonView(Views.Student.class)
	//@Pattern(regexp = "^[a-zA-Z]$", message="Department label is not valid, can contain only one letter.")
	@NotNull (message = "Department label must be provided.")
	private String departmentLabel;
	/*@JsonView(Views.Student.class)
	@Enumerated(EnumType.STRING)
	@NotNull (message = "Semester must be provided.")
	private ESemester semester;*/
	@JsonView(Views.Admin.class)
	@Max(1)
    @Min(-1)
	private Integer status;
	@JsonView(Views.Admin.class)
	private Integer createdById;
    @JsonView(Views.Admin.class)
    @Column(name = "updated_by")
    private Integer updatedById;
	
	public DepartmentClassDto() {
		super();
	}

	public DepartmentClassDto(@Pattern(regexp = "^(IV|V?I{1,4})$", message="Student class is not valid, must be I, II, III, IV, V, VI, VII or VIII.") @NotNull(message = "Class label must be provided.") EClass classLabel,
			@Pattern(regexp = "^[a-zA-Z]$", message = "Department label is not valid, can contain only one letter.") @NotNull(message = "Department label must be provided.") String departmentLabel,
			//@NotNull(message = "Semester must be provided.") ESemester semester,
			@Max(1) @Min(-1) @NotNull(message = "Status must be provided.") Integer status,
			@NotNull(message = "Created by Id must be provided.") Integer createdById, Integer updatedById) {
		super();
		this.classLabel = classLabel;
		this.departmentLabel = departmentLabel;
		//this.semester = semester;
		this.status = status;
		this.createdById = createdById;
		this.updatedById = updatedById;
	}

	public DepartmentClassDto(@Pattern(regexp = "^(IV|V?I{1,4})$", message="Student class is not valid, must be I, II, III, IV, V, VI, VII or VIII.") @NotNull(message = "Class label must be provided.") EClass classLabel,
			@Pattern(regexp = "^[a-zA-Z]$", message = "Department label is not valid, can contain only one letter.") @NotNull(message = "Department label must be provided.") String departmentLabel/*,
			@NotNull(message = "Semester must be provided.") ESemester semester*/) {
		super();
		this.classLabel = classLabel;
		this.departmentLabel = departmentLabel;
		//this.semester = semester;
	}

	public EClass getClassLabel() {
		return classLabel;
	}

	public void setClassLabel(EClass classLabel) {
		this.classLabel = classLabel;
	}

	public String getDepartmentLabel() {
		return departmentLabel;
	}

	public void setDepartmentLabel(String departmentLabel) {
		this.departmentLabel = departmentLabel;
	}

	/*public ESemester getSemester() {
		return semester;
	}

	public void setSemester(ESemester semester) {
		this.semester = semester;
	}*/

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Integer createdById) {
		this.createdById = createdById;
	}

	public Integer getUpdatedById() {
		return updatedById;
	}

	public void setUpdatedById(Integer updatedById) {
		this.updatedById = updatedById;
	}

}
