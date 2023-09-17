package com.hararoobe.hararoo.model;

import java.util.Set;

import lombok.Data;

@Data
public class SignupRequest {
	
	private String userName;
	
	private String emailId;
	
	private String password;
	
	private String contactNumber;
	
	private Set<String> role;

}
