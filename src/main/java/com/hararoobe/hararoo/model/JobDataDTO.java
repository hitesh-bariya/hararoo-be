package com.hararoobe.hararoo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDataDTO {

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
	
	private String jobDescription;
}
