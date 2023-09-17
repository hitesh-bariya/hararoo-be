package com.hararoobe.hararoo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@Table(name = "job_data")
@AllArgsConstructor
@NoArgsConstructor
public class JobData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long jobId;
	
	private String jobTitle;
	
	private String location;
	
	private String sector;
	
	private String subSector;
	
	private String industrialName;
	
	private String contractType;
	
	private String consultant;
	
	private String jobReferences;
	
	private String jobNature;
	
	private boolean status;
	
	@Column(columnDefinition = "TEXT")
	private String jobDescription;
	
}
