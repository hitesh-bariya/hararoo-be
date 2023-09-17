package com.hararoobe.hararoo.model;

import lombok.Data;

@Data
public class JobApplyDTO {
	
	private Long jobId;
	
	private String userName;
	
	private String emailId;
	
	private String contactNumber;
	
	private String password;

}
