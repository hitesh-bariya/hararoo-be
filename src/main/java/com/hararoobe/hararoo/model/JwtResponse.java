package com.hararoobe.hararoo.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
	
	private String jwt;
	
	private Long userId;
	
	private String userName;
	
	private String emailId;

	private List<String> role;
}
